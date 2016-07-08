package org.osivia.services.tasks.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Task action types enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum TaskActionType {

    /** Accept. */
    ACCEPT("actionIdYes"),
    /** Reject. */
    REJECT("actionIdNo");


    /** Action reference variable name. */
    private final String actionReference;
    /** Internationalization key. */
    private final String key;


    /**
     * Constructor.
     * 
     * @param actionReference action reference variable name
     */
    private TaskActionType(String actionReference) {
        this.actionReference = actionReference;
        this.key = "TASK_ACTION_TYPE_" + StringUtils.upperCase(this.name());
    }


    /**
     * Getter for actionReference.
     * 
     * @return the actionReference
     */
    public String getActionReference() {
        return actionReference;
    }

    /**
     * Getter for key.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }
    
}
