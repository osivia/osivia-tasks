package org.osivia.services.tasks.portlet.model;

import java.util.Date;

import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Task java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Task {

    /** Document. */
    private Document document;
    /** Display. */
    private String display;
    /** Date. */
    private Date date;
    /** Acknowledgeable indicator. */
    private boolean acknowledgeable;


    /**
     * Constructor.
     */
    public Task() {
        super();
    }


    /**
     * Getter for document.
     * 
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Setter for document.
     * 
     * @param document the document to set
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * Getter for display.
     * 
     * @return the display
     */
    public String getDisplay() {
        return display;
    }

    /**
     * Setter for display.
     * 
     * @param display the display to set
     */
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     * Getter for date.
     * 
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for date.
     * 
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for acknowledgeable.
     * 
     * @return the acknowledgeable
     */
    public boolean isAcknowledgeable() {
        return acknowledgeable;
    }

    /**
     * Setter for acknowledgeable.
     * 
     * @param acknowledgeable the acknowledgeable to set
     */
    public void setAcknowledgeable(boolean acknowledgeable) {
        this.acknowledgeable = acknowledgeable;
    }

}
