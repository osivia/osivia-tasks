package org.osivia.services.tasks.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Tasks java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Refreshable
public class Tasks {

    /** Tasks. */
    private List<Task> tasks;


    /**
     * Constructor.
     */
    public Tasks() {
        super();
    }


    /**
     * Getter for tasks.
     * 
     * @return the tasks
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Setter for tasks.
     * 
     * @param tasks the tasks to set
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

}
