package org.osivia.services.tasks.plugin.forms;

import java.util.HashMap;
import java.util.Map;

import org.osivia.services.tasks.portlet.repository.TasksRepository;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * Set notification message form filter.
 * 
 * @author Cédric Krommenhoek
 * @see FormFilter
 */
public class SetNotificationMessageFormFilter implements FormFilter {

    /** Form filter identifier. */
    public static final String IDENTIFIER = "SET_NOTIFICATION_MESSAGE";

    /** Form filter label internationalization key. */
    private static final String LABEL_INTERNATIONALIZATION_KEY = "SET_NOTIFICATION_MESSAGE_FORM_FILTER_LABEL";
    /** Form filter description internationalization key. */
    private static final String DESCRIPTION_INTERNATIONALIZATION_KEY = "SET_NOTIFICATION_MESSAGE_FORM_FILTER_DESCRIPTION";

    /** Message parameter. */
    private static final String MESSAGE_PARAMETER = "message";


    /**
     * Constructor.
     */
    public SetNotificationMessageFormFilter() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return IDENTIFIER;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabelKey() {
        return LABEL_INTERNATIONALIZATION_KEY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_INTERNATIONALIZATION_KEY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, FormFilterParameterType> getParameters() {
        Map<String, FormFilterParameterType> parameters = new HashMap<>(1);
        parameters.put(MESSAGE_PARAMETER, FormFilterParameterType.TEXT);
        return parameters;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasChildren() {
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
        // Message content
        String message = context.getParamValue(executor, MESSAGE_PARAMETER);
        context.getVariables().put(TasksRepository.MESSAGE_PROPERTY, message);
    }

}
