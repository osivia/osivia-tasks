package org.osivia.services.tasks.portlet.controller;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.tasks.portlet.model.Tasks;
import org.osivia.services.tasks.portlet.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Tasks portlet controller.
 * 
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletConfigAware
 * @see PortletContextAware
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("tasks")
public class TasksController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;

    /** Tasks service. */
    @Autowired
    private TasksService service;


    /**
     * Constructor.
     */
    public TasksController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * View render mapping.
     * 
     * @param request render request
     * @param response render response
     * @param tasks tasks model attribute
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("tasks") Tasks tasks) {
        request.setAttribute("tasksCount", tasks.getCount());
        
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
        request.setAttribute("discussionUrl", this.service.getDiscussionsUrl(portalControllerContext));


        return "view";
    }


    /**
     * Accept action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param tasks tasks model attribute
     * @param index task index request parameter
     * @throws PortletException
     */
    @ActionMapping("accept")
    public void accept(ActionRequest request, ActionResponse response, @ModelAttribute("tasks") Tasks tasks, @RequestParam String index)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        this.service.acceptTask(portalControllerContext, tasks, NumberUtils.toInt(index));
    }


    /**
     * Reject action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param tasks tasks model attribute
     * @param index task index request parameter
     * @throws PortletException
     */
    @ActionMapping("reject")
    public void reject(ActionRequest request, ActionResponse response, @ModelAttribute("tasks") Tasks tasks, @RequestParam String index)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        this.service.rejectTask(portalControllerContext, tasks, NumberUtils.toInt(index));
    }


    /**
     * Close action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param tasks tasks model attribute
     * @param index task index request parameter
     * @throws PortletException
     */
    @ActionMapping("close")
    public void close(ActionRequest request, ActionResponse response, @ModelAttribute("tasks") Tasks tasks, @RequestParam String index)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        this.service.closeTask(portalControllerContext, tasks, NumberUtils.toInt(index));
    }


    /**
     * Get tasks model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return tasks
     * @throws PortletException
     */
    @ModelAttribute("tasks")
    public Tasks getTasks(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
        
        return this.service.getTasks(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
