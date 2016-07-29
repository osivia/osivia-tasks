package org.osivia.services.tasks.portlet.model;

/**
 * Task action types enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum TaskActionType {

    /** Accept. */
    ACCEPT("actionIdYes"),
    /** Reject. */
    REJECT("actionIdNo"),
    /** Close. */
    CLOSE("actionIdClosable");


    /** Action reference variable name. */
    private final String actionReference;


    /**
     * Constructor.
     * 
     * @param actionReference action reference variable name
     */
    private TaskActionType(String actionReference) {
        this.actionReference = actionReference;
    }


    /**
     * Getter for actionReference.
     * 
     * @return the actionReference
     */
    public String getActionReference() {
        return actionReference;
    }

}
