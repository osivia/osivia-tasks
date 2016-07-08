package org.osivia.services.tasks.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.tasks.portlet.model.Tasks;

/**
 * Tasks service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface TasksService {

    /**
     * Get tasks.
     * 
     * @param portalControllerContext portal controller context
     * @return tasks
     * @throws PortletException
     */
    Tasks getTasks(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Accept task.
     * 
     * @param portalControllerContext portal controller context
     * @param path task document path
     * @throws PortletException
     */
    void acceptTask(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Reject task.
     * 
     * @param portalControllerContext portal controller context
     * @param path task document path
     * @throws PortletException
     */
    void rejectTask(PortalControllerContext portalControllerContext, String path) throws PortletException;

}
