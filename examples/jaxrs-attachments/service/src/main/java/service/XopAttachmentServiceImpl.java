/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package service;

import common.XopAttachmentService;
import common.XopBean;

/**
 * JAX-RS AttachmentService root resource
 */
public class XopAttachmentServiceImpl implements XopAttachmentService {

    public XopBean echoXopAttachment(XopBean xop) {
        return xop;
    }

}
