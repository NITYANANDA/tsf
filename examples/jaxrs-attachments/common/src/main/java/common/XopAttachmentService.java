/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package common;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.cxf.annotations.EndpointProperty;

/**
 * This interface describes a JAX-RS root resource capable of echoing the attachments
 */

@Path("/xop")
@EndpointProperty(key = org.apache.cxf.message.Message.MTOM_ENABLED, value = "true")
public interface XopAttachmentService {

    @POST
    @Consumes("multipart/related")
    @Produces("multipart/related;type=text/xml")
    public XopBean echoXopAttachment(XopBean xop);

}
