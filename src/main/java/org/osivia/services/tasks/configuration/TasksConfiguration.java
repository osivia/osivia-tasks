package org.osivia.services.tasks.configuration;

import org.osivia.directory.v2.service.preferences.UserPreferencesService;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.tasks.ITasksService;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoServiceFactory;
import fr.toutatice.portail.cms.nuxeo.api.services.tag.INuxeoTagService;

/**
 * Tasks portlet configuration.
 * 
 * @author Cédric Krommenhoek
 */
@Configuration
@ComponentScan(basePackages = "org.osivia.services.tasks.portlet")
public class TasksConfiguration {

    /**
     * Constructor.
     */
    public TasksConfiguration() {
        super();
    }


    /**
     * Get view resolver.
     *
     * @return view resolver
     */
    @Bean
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setCache(true);
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }


    /**
     * Get message source.
     *
     * @return message source
     */
    @Bean(name = "messageSource")
    public ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("Resource");
        return messageSource;
    }


    /**
     * Get person service.
     *
     * @return person service
     */
    @Bean
    public PersonService getPersonService() {
        return DirServiceFactory.getService(PersonService.class);
    }


    /**
     * Get forms service.
     * 
     * @return forms service
     */
    @Bean
    public IFormsService getFormsService() {
        return NuxeoServiceFactory.getFormsService();
    }


    /**
     * Get tasks service.
     * 
     * @return tasks service
     */
    @Bean
    public ITasksService getTasksService() {
        return Locator.findMBean(ITasksService.class, ITasksService.MBEAN_NAME);
    }


    /**
     * Get tag service.
     * 
     * @return tag service
     */
    @Bean
    public INuxeoTagService getTagService() {
        return NuxeoServiceFactory.getTagService();
    }


    /**
     * Get bundle factory.
     *
     * @return bundle factory
     */
    @Bean
    public IBundleFactory getBundleFactory() {
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        return internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }

    
    /**
     * Get portal URL factory.
     *
     * @return portal URL factory
     */
    @Bean
    public IPortalUrlFactory getPortalUrlFactory() {
        return Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
    }

    
    /**
     * Get user preferences service.
     *
     * @return user preferences service
     */
    @Bean
    public UserPreferencesService getUserPreferencesService() {
        return DirServiceFactory.getService(UserPreferencesService.class);
    }

    
}
