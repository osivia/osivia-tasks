package org.osivia.services.tasks.portlet.repository;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


public class RetrieveActiviteByIdCommand implements INuxeoCommand {

    /** schemas */
    private static final String schemas = "*";

    /** id */
    private final String id;



    public RetrieveActiviteByIdCommand(String id) {
        this.id = id;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        final OperationRequest request = nuxeoSession.newRequest("Document.QueryES");

        request.set(Constants.HEADER_NX_SCHEMAS, schemas);
        request.set("query", "SELECT * FROM UsageNum WHERE ecm:uuid = '" + id + "' ");
        return request.execute();
    }

    @Override
    public String getId() {
        return "RetrieveActiviteCommand/" + id;
    }


}
