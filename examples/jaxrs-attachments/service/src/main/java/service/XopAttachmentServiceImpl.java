/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package service;

import common.XopAttachmentService;
import common.XopBean;

/**
 * JAX-RS XopAttachmentService root resource
 */
public class XopAttachmentServiceImpl implements XopAttachmentService {

    /**
     * {@inheritDoc}
     */
    public XopBean echoXopAttachment(XopBean xop) {
        return xop;
    }

}
