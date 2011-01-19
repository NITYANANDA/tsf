/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.activation.DataHandler;

import common.Book;
import common.MultipartsService;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

/**
 * JAX-RS AttachmentService root resource
 */
public class MultipartsServiceImpl implements MultipartsService {

    public MultipartBody echoAttachment(MultipartBody body) {
        return duplicateMultipartBody(body);
    }

    private MultipartBody duplicateMultipartBody(MultipartBody body) {
        Book jaxbBook = body.getAttachmentObject("book1", Book.class);
        Book jsonBook = body.getAttachmentObject("book2", Book.class);
        
        Attachment imageAtt = body.getAttachment("image");
        
        if ("JAXB".equals(jaxbBook.getName()) && 1L == jaxbBook.getId()
            && "JSON".equals(jsonBook.getName()) && 2L == jsonBook.getId()
            && imageAtt != null) {
            return createMultipartBody(jaxbBook, jsonBook, imageAtt.getDataHandler());
        }
        throw new RuntimeException("Received Book attachment is corrupted");
    }
    
    private MultipartBody createMultipartBody(Book jaxbBook, Book jsonBook, DataHandler imageHandler) {
        List<Attachment> atts = new LinkedList<Attachment>();
        atts.add(new Attachment("book1", "application/xml", jaxbBook));
        atts.add(new Attachment("book2", "application/json", jsonBook));
        try {
            atts.add(new Attachment("image", "application/octet-stream", imageHandler.getInputStream()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return new MultipartBody(atts, true);  

    }
}
