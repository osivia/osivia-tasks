package org.osivia.services.tasks.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
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

        return container;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptTask(PortalControllerContext portalControllerContext, String path) throws PortletException {
        this.repository.updateTask(portalControllerContext, path, TaskActionType.ACCEPT);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void rejectTask(PortalControllerContext portalControllerContext, String path) throws PortletException {
        this.repository.updateTask(portalControllerContext, path, TaskActionType.REJECT);
    }

}
