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
     * @param tasks tasks
     * @param index task index
     * @throws PortletException
     */
    void acceptTask(PortalControllerContext portalControllerContext, Tasks tasks, int index) throws PortletException;


    /**
     * Reject task.
     * 
     * @param portalControllerContext portal controller context
     * @param tasks tasks
     * @param index task index
     * @throws PortletException
     */
    void rejectTask(PortalControllerContext portalControllerContext, Tasks tasks, int index) throws PortletException;


    /**
     * Close task.
     * 
     * @param portalControllerContext portal controller context
     * @param tasks tasks
     * @param index task index
     * @throws PortletException
     */
    void closeTask(PortalControllerContext portalControllerContext, Tasks tasks, int index) throws PortletException;


    /**
     * Gets the discussions url.
     *
     * @param portalControllerContext the portal controller context
     * @return the discussions url
     * @throws PortletException the portlet exception
     */
    String getDiscussionsUrl(PortalControllerContext portalControllerContext) ;

}
