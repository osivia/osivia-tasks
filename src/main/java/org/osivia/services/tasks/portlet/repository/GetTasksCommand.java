package org.osivia.services.tasks.portlet.repository;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Get tasks command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetTasksCommand implements INuxeoCommand {

    /** User UID. */
    private final String user;
    /** Task path. */
    private final String path;


    /**
     * Constructor.
     *
     * @param user user UID
     */
    public GetTasksCommand(String user) {
        this(user, null);
    }


    /**
     * Constructor.
     *
     * @param user user UID
     * @param path task path
     */
    public GetTasksCommand(String user, String path) {
        super();
        this.user = user;
        this.path = path;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Query
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM Document ");
        query.append("WHERE ecm:primaryType = 'TaskDoc' ");
        query.append("AND nt:task_variables.notifiable = 'true' ");
        query.append("AND nt:actors = '").append(this.user).append("' ");
        if (StringUtils.isNotEmpty(this.path)) {
            query.append("AND ecm:path = '").append(this.path).append("' ");
        }

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, task");
        request.set("query", query.toString());

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append("/");
        builder.append(this.user);
        if (StringUtils.isNotEmpty(this.path)) {
            builder.append("/");
            builder.append(this.path);
        }
        return builder.toString();
    }

}
