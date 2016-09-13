package org.osivia.services.tasks.portlet.model;

import java.util.Date;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.directory.v2.model.Person;
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
    /** Initiator. */
    private Person initiator;
    /** Date. */
    private Date date;
    /** Acknowledgeable indicator. */
    private boolean acknowledgeable;
    /** Closeable indicator. */
    private boolean closeable;
    /** Message. */
    private String message;


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
     * Getter for initiator.
     * 
     * @return the initiator
     */
    public Person getInitiator() {
        return initiator;
    }

    /**
     * Setter for initiator.
     * 
     * @param initiator the initiator to set
     */
    public void setInitiator(Person initiator) {
        this.initiator = initiator;
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

    /**
     * Getter for closeable.
     * 
     * @return the closeable
     */
    public boolean isCloseable() {
        return closeable;
    }

    /**
     * Setter for closeable.
     * 
     * @param closeable the closeable to set
     */
    public void setCloseable(boolean closeable) {
        this.closeable = closeable;
    }

    /**
     * Getter for message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for message.
     * 
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
