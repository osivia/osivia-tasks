package org.osivia.services.tasks.portlet.repository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.PortletException;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.services.tasks.portlet.model.Task;
import org.osivia.services.tasks.portlet.model.TaskActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;

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

    /** Forms service. */
    @Autowired
    private IFormsService formsService;

    /** Person service. */
    @Autowired
    private PersonService personService;


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

                // Task initiator
                Person initiator = this.personService.getPerson(document.getString("nt:initiator"));
                
                // Task
                Task task = this.applicationContext.getBean(Task.class);
                task.setDocument(document);
                task.setDisplay(this.getTaskDisplay(portalControllerContext, document));
                task.setInitiator(initiator);
                task.setDate(document.getDate("dc:created"));
                task.setAcknowledgeable(BooleanUtils.isTrue(taskVariables.getBoolean("acquitable")));
                task.setCloseable(BooleanUtils.isTrue(taskVariables.getBoolean("closable")));

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
     * @return task display
     * @throws PortletException
     */
    private String getTaskDisplay(PortalControllerContext portalControllerContext, Document task) throws PortletException {
        // Procedure instance properties
        PropertyMap instanceProperties = task.getProperties().getMap("nt:pi");

        // Global variables
        PropertyMap globalVariables = instanceProperties.getMap("pi:globalVariablesValues");

        // Task variables
        PropertyMap taskVariables = task.getProperties().getMap("nt:task_variables");

        // Expression
        String expression = taskVariables.getString("stringMsg");

        // Variables
        Map<String, String> variables = new HashMap<>(globalVariables.size() + taskVariables.size());
        for (Entry<String, Object> entry : globalVariables.getMap().entrySet()) {
            variables.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        for (Entry<String, Object> entry : taskVariables.getMap().entrySet()) {
            variables.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        variables.put("procedureInitiator", instanceProperties.getString("pi:procedureInitiator"));
        variables.put("taskInitiator", task.getString("nt:initiator"));


        // Tranformed expression
        String transformedExpression;
        try {
            transformedExpression = this.formsService.transform(portalControllerContext, expression, variables);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return transformedExpression;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getHelp(PortalControllerContext portalControllerContext) throws PortletException {
        // Location
        String location = System.getProperty(HELP_LOCATION_PROPERTY);

        // Help content
        String help;

        if (StringUtils.isBlank(location)) {
            help = null;
        } else {
            // Nuxeo controller
            NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

            // Path
            String path;
            if (location.startsWith("/")) {
                path = location;
            } else {
                path = NuxeoController.webIdToFetchPath(location);
            }

            try {
                // Document context
                NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);

                // Document
                Document document = documentContext.getDoc();

                // Transformation
                help = nuxeoController.transformHTMLContent(StringUtils.trimToEmpty(document.getString("note:note")));
            } catch (NuxeoException e) {
                if (e.getErrorCode() == NuxeoException.ERROR_NOTFOUND) {
                    help = null;
                } else {
                    throw e;
                }
            }
        }

        return help;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String updateTask(PortalControllerContext portalControllerContext, Task task, TaskActionType actionType) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Principal
        Principal principal = portalControllerContext.getRequest().getUserPrincipal();

        // User name
        String user = principal.getName();

        // Nuxeo command
        String path = task.getDocument().getPath();
        INuxeoCommand command = this.applicationContext.getBean(GetTasksCommand.class, user, path);

        // Nuxeo documents
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        // Task document
        Document document = documents.get(0);

        // Task variables
        PropertyMap taskVariables = document.getProperties().getMap("nt:task_variables");

        // Action identifier
        String actionId = taskVariables.getString(actionType.getActionReference());
        
        // Task message
        String message;

        try {
            Map<String, String> updatedVariables = this.formsService.proceed(portalControllerContext, document, actionId, null);
            message = updatedVariables.get(MESSAGE_PROPERTY);
        } catch (PortalException e) {
            throw new PortletException(e);
        } catch (FormFilterException e) {
            throw new PortletException(e);
        }

        return message;
    }

}
