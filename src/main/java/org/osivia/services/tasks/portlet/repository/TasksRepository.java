package org.osivia.services.tasks.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.tasks.portlet.model.Task;
import org.osivia.services.tasks.portlet.model.TaskActionType;

/**
 * Tasks repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface TasksRepository {

    /** Notification message property. */
    String MESSAGE_PROPERTY = "tasksNotificationMessage";
    /** Help location property name. */
    String HELP_LOCATION_PROPERTY = "tasks.help.location";
    /** Task directive internalization key prefix. */
    String TASK_DIRECTIVE_KEY_PREFIX = "TASK_DIRECTIVE_";


    /**
     * Get tasks.
     * 
     * @param portalControllerContext portal controller context
     * @return tasks
     * @throws PortletException
     */
    List<Task> getTasks(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get help content.
     * 
     * @param portalControllerContext portal controller context
     * @return help content
     * @throws PortletException
     */
    String getHelp(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Update task.
     * 
     * @param portalControllerContext portal controller context
     * @param task task
     * @param actionType action type
     * @return task message
     * @throws PortletException
     */
    String updateTask(PortalControllerContext portalControllerContext, Task task, TaskActionType actionType) throws PortletException;

}
