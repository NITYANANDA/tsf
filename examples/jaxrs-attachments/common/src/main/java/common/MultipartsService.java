/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package common;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

/**
 * This interface describes a JAX-RS root resource capable of echoing the attachments
 */

@Path("/multipart")
public interface MultipartsService {

    @POST
    @Consumes("multipart/mixed")
    @Produces("multipart/mixed")
    public MultipartBody echoAttachment(MultipartBody body);

}
