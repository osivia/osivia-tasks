package org.osivia.services.tasks.portlet.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.tasks.ITasksService;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.tasks.portlet.model.Task;
import org.osivia.services.tasks.portlet.model.TaskActionType;
import org.osivia.services.tasks.portlet.model.Tasks;
import org.osivia.services.tasks.portlet.repository.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Tasks service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see TasksService
 */
@Service
public class TasksServiceImpl implements TasksService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Tasks repository. */
    @Autowired
    private TasksRepository repository;

    /** Tasks service. */
    @Autowired
    private ITasksService tasksService;
    
    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;


    /**
     * Constructor.
     */
    public TasksServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Tasks getTasks(PortalControllerContext portalControllerContext) throws PortletException {
        // Tasks container
        Tasks container = this.applicationContext.getBean(Tasks.class);

        // Tasks
        List<Task> tasks = this.repository.getTasks(portalControllerContext);
        container.setTasks(tasks);

        // Count
        container.setCount(tasks.size());

        // Help content
        String help = this.repository.getHelp(portalControllerContext);
        container.setHelp(help);

        return container;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptTask(PortalControllerContext portalControllerContext, Tasks tasks, int index) throws PortletException {
        this.updateTask(portalControllerContext, tasks, index, TaskActionType.ACCEPT);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void rejectTask(PortalControllerContext portalControllerContext, Tasks tasks, int index) throws PortletException {
        this.updateTask(portalControllerContext, tasks, index, TaskActionType.REJECT);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void closeTask(PortalControllerContext portalControllerContext, Tasks tasks, int index) throws PortletException {
        this.updateTask(portalControllerContext, tasks, index, TaskActionType.CLOSE);
    }


    /**
     * Update task.
     * 
     * @param portalControllerContext portal controller context
     * @param tasks tasks
     * @param index task index
     * @param actionType action type
     * @throws PortletException
     */
    private void updateTask(PortalControllerContext portalControllerContext, Tasks tasks, int index, TaskActionType actionType) throws PortletException {
        // Task
        Task task = tasks.getTasks().get(index);

        String message = this.repository.updateTask(portalControllerContext, task, actionType);

        // Reset tasks count
        try {
            this.tasksService.resetTasksCount(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        // Update model
        if (StringUtils.isBlank(message)) {
            tasks.getTasks().remove(index);
        } else {
            task.setMessage(message);
        }
        tasks.setCount(tasks.getCount() - 1);
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDiscussionsUrl(PortalControllerContext portalControllerContext) {

        Map<String, String> properties = new HashMap<>();

        properties.put("osivia.hideTitle", "1");
        Map<String, String> params = new HashMap<>();

        // URL
        String url;
        try {
            url = portalUrlFactory.getStartPortletInNewPage(portalControllerContext, "discussion", "Discussion",
                    "index-cloud-ens-discussion-instance", properties, params);
        } catch (PortalException e) {
            url = null;
        }
        
        return url;

    }


}
