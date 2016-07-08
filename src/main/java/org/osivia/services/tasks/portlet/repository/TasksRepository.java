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

    /**
     * Get tasks.
     * 
     * @param portalControllerContext portal controller context
     * @return tasks
     * @throws PortletException
     */
    List<Task> getTasks(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Update task.
     * 
     * @param portalControllerContext portal controller context
     * @param path task document path
     * @param actionType action type
     * @throws PortletException
     */
    void updateTask(PortalControllerContext portalControllerContext, String path, TaskActionType actionType) throws PortletException;

}
