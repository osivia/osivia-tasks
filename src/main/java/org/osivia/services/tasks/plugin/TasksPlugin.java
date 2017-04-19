package org.osivia.services.tasks.plugin;

import java.util.Map;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.services.tasks.plugin.forms.SetNotificationMessageFormFilter;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;

/**
 * Tasks plugin.
 *
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
public class TasksPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "tasks.plugin";


    /** Set notification message form filter. */
    private final FormFilter setNotificationMessageFormFilter;


    /**
     * Constructor.
     */
    public TasksPlugin() {
        super();
        this.setNotificationMessageFormFilter = new SetNotificationMessageFormFilter();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPluginName() {
        return PLUGIN_NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(CustomizationContext context) {
        // Form filters
        Map<String, FormFilter> formFilters = this.getFormFilters(context);
        formFilters.put(SetNotificationMessageFormFilter.IDENTIFIER, this.setNotificationMessageFormFilter);
    }

}
