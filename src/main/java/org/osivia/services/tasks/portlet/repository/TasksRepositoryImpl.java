package org.osivia.services.tasks.portlet.repository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.html.HTMLConstants;
import org.osivia.services.tasks.portlet.model.Task;
import org.osivia.services.tasks.portlet.model.TaskActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.services.tag.INuxeoTagService;

/**
 * Tasks repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see TasksRepository
 */
@Repository
public class TasksRepositoryImpl implements TasksRepository {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Person service. */
    @Autowired
    private PersonService personService;

    /** Forms service. */
    @Autowired
    private IFormsService formsService;

    /** Tag service. */
    @Autowired
    private INuxeoTagService tagService;


    /**
     * Constructor.
     */
    public TasksRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getTasks(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Principal
        Principal principal = portalControllerContext.getRequest().getUserPrincipal();
        
        // Tasks
        List<Task> tasks;
        
        if (principal == null) {
            tasks = new ArrayList<>(0);
        } else {
            // User name
            String user = principal.getName();

            // Nuxeo command
            INuxeoCommand command = this.applicationContext.getBean(GetTasksCommand.class, user);

            // Nuxeo documents
            Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);
            
            // Tasks
            tasks = new ArrayList<>(documents.size());
            for (Document document : documents.list()) {
                // Task variables
                PropertyMap taskVariables = document.getProperties().getMap("nt:task_variables");

                // Task
                Task task = this.applicationContext.getBean(Task.class);
                task.setDocument(document);
                task.setDisplay(this.getTaskDisplay(portalControllerContext, document));
                task.setDate(document.getDate("dc:created"));
                task.setAcknowledgeable(BooleanUtils.isTrue(taskVariables.getBoolean("acquitable")));

                tasks.add(task);
            }
        }

        return tasks;
    }


    /**
     * Get task display.
     * 
     * @param portalControllerContext portal controller context
     * @param task task Nuxeo document
     * @return display
     */
    private String getTaskDisplay(PortalControllerContext portalControllerContext, Document task) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Procedure instance properties
        PropertyMap instanceProperties = task.getProperties().getMap("nt:pi");

        // Task variables
        PropertyMap taskVariables = task.getProperties().getMap("nt:task_variables");

        // Global variables
        PropertyMap globalVariables = instanceProperties.getMap("pi:globalVariablesValues");


        // Display
        String display = taskVariables.getString("stringMsg");


        // Initiator
        if (StringUtils.contains(display, "${initiator}")) {
            String initiatorName = task.getString("nt:initiator");
            Person initiator = this.personService.getPerson(initiatorName);

            // Container
            Element container;

            if (initiator == null) {
                container = DOM4JUtils.generateElement(HTMLConstants.SPAN, null, initiatorName, "glyphicons glyphicons-user", null);
            } else {
                container = DOM4JUtils.generateElement(HTMLConstants.SPAN, null, null);
                
                // Avatar
                Element avatar = DOM4JUtils.generateElement(HTMLConstants.IMG, "avatar", null);
                DOM4JUtils.addAttribute(avatar, HTMLConstants.SRC, initiator.getAvatar().getUrl());
                DOM4JUtils.addAttribute(avatar, HTMLConstants.ALT, StringUtils.EMPTY);
                container.add(avatar);
                
                // URL
                String url = this.tagService.getUserProfileLink(nuxeoController, initiatorName, initiator.getDisplayName()).getUrl();
                
                // Display name
                Element displayName = DOM4JUtils.generateLinkElement(url, null, null, "no-ajax-link", initiator.getDisplayName());
                container.add(displayName);
            }

            display = StringUtils.replace(display, "${initiator}", DOM4JUtils.write(container));
        }


        // Target document
        if (StringUtils.contains(display, "${document}")) {
            // Target path
            String path = globalVariables.getString("documentPath");
            // Target document context
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
            // Target document
            Document target = documentContext.getDoc();

            // URL
            String url = nuxeoController.getLink(target).getUrl();
            
            // Link
            Element link = DOM4JUtils.generateLinkElement(url, null, null, "no-ajax-link", target.getTitle());
            
            display = StringUtils.replace(display, "${document}", DOM4JUtils.write(link));
        }

        return display;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTask(PortalControllerContext portalControllerContext, String path, TaskActionType actionType) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Principal
        Principal principal = portalControllerContext.getRequest().getUserPrincipal();

        // User name
        String user = principal.getName();

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetTasksCommand.class, user, path);

        // Nuxeo documents
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        // Task document
        Document task = documents.get(0);

        // Task variables
        PropertyMap taskVariables = task.getProperties().getMap("nt:task_variables");

        // Action identifier
        String actionId = taskVariables.getString(actionType.getActionReference());
        
        try {
            this.formsService.proceed(portalControllerContext, task, actionId, null);
        } catch (PortalException e) {
            throw new PortletException(e);
        }
    }

}
