package org.osivia.services.tasks.portlet.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.model.preferences.UserPreferences;
import org.osivia.directory.v2.service.preferences.UserPreferencesService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.tasks.CustomTask;
import org.osivia.portal.api.tasks.ITasksService;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.constants.InternalConstants;
import org.osivia.services.tasks.portlet.model.Task;
import org.osivia.services.tasks.portlet.model.TaskActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.discussions.DiscussionHelper;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.services.TaskDirective;

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

    /** Tasks service. */
    @Autowired
    private ITasksService tasksService;

    /** Forms service. */
    @Autowired
    private IFormsService formsService;

    /** Person service. */
    @Autowired
    private PersonService personService;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    @Autowired
    private UserPreferencesService userPreferencesService;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

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
        // Task documents
        List<EcmDocument> documents;


        try {
            documents = this.tasksService.getTasks(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        // Tasks
        List<Task> tasks = new ArrayList<>(documents.size());

        for (EcmDocument ecmDocument : documents) {
            if (ecmDocument instanceof Document) {
                // Nuxeo document
                Document document = (Document) ecmDocument;

                if (document.getType().equals("TaskDoc")) {

                    // Task display
                    String display = this.getTaskDisplay(portalControllerContext, document);

                    if (StringUtils.isNotBlank(display)) {
                        // Task variables
                        PropertyMap taskVariables = document.getProperties().getMap("nt:task_variables");

                        // Task initiator
                        Person initiator = this.personService.getPerson(document.getString("nt:initiator"));

                        // Task
                        Task task = this.applicationContext.getBean(Task.class);
                        task.setDocument(document);
                        task.setDisplay(display);
                        task.setInitiator(initiator);
                        task.setDate(document.getDate("dc:created"));
                        task.setAcknowledgeable(BooleanUtils.isTrue(taskVariables.getBoolean("acquitable")));
                        task.setCloseable(BooleanUtils.isTrue(taskVariables.getBoolean("closable")));

                        tasks.add(task);
                    }
                }
            }

            if (ecmDocument instanceof CustomTask) {
                // Nuxeo document
                CustomTask customTask = (CustomTask) ecmDocument;
                Document document = (Document) customTask.getInnerDocument();


                // Non task document
                Task task = this.applicationContext.getBean(Task.class);
                task.setDocument(document);

                try {


                    // Internationalization bundle
                    Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

                    String subject = customTask.getProperties().get("discussionTitle");

                    if (subject.length() > 0)
                        subject = "(" + subject + ")";


                    String url = DiscussionHelper.getDiscussionUrlById(portalControllerContext, document.getProperties().getString("ttc:webid"));
                    task.setDisplay(" <a href=\"" + url + "\">" + bundle.getString("TASK_DISCUSSIONS_NEW_MESSAGE") + "</a> "+ subject);

                    tasks.add(task);
                } catch (Exception e) {
                    throw new PortletException(e);
                }
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
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Task variables
        PropertyMap taskVariables = task.getProperties().getMap("nt:task_variables");
        // Task directive identifier
        String directiveId = task.getString("nt:directive");


        // Expression
        String expression = null;

        if (BooleanUtils.isTrue(taskVariables.getBoolean("notifiable"))) {
            expression = taskVariables.getString("stringMsg");
        } else if (StringUtils.isNotEmpty(directiveId)) {
            // Task directive
            TaskDirective directive = TaskDirective.fromId(directiveId);

            if (directive != null) {
                String key = TASK_DIRECTIVE_KEY_PREFIX + StringUtils.upperCase(directive.toString());
                expression = bundle.getString(key);
            }
        }


        // Task display
        String display;

        if (StringUtils.isNotBlank(expression)) {
            // Tranformed expression
            String transformedExpression;
            try {
                transformedExpression = this.formsService.transform(portalControllerContext, expression, task);
            } catch (PortalException e) {
                throw new PortletException(e);
            }

            // Replace line separators
            display = StringUtils.replace(transformedExpression, System.lineSeparator(), "<br>");
        } else {
            display = null;
        }

        return display;
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
                Document document = documentContext.getDocument();

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
        // Task path
        String path = task.getDocument().getPath();

        // Task ECM document
        EcmDocument ecmDocument;
        try {
            ecmDocument = this.tasksService.getTask(portalControllerContext, path);
        } catch (PortalException e) {
            throw new PortletException(e);
        }


        // Task message
        String message;

        if ((ecmDocument != null) && (ecmDocument instanceof Document)) {
            // Task document
            Document document = (Document) ecmDocument;

            // Task variables
            PropertyMap taskVariables = document.getProperties().getMap("nt:task_variables");

            // Action identifier
            String actionId = taskVariables.getString(actionType.getActionReference());

            try {
                Map<String, String> updatedVariables = this.formsService.proceed(portalControllerContext, document, actionId, null);
                message = updatedVariables.get(MESSAGE_PROPERTY);
            } catch (PortalException e) {
                throw new PortletException(e);
            } catch (FormFilterException e) {
                throw new PortletException(e);
            }
        } else {
            message = null;
        }

        return message;
    }

}
