/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package client;

import java.awt.Image;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.mail.util.ByteArrayDataSource;

import common.XopAttachmentService;
import common.XopBean;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;

/**
 * Example showing the interaction between HTTP-centric and proxy based RESTful clients and JAX-RS server
 * providing multiple services (PersonService and SearchService)
 */
public final class RESTClient {

    private static final String PORT_PROPERTY = "http.port";
    private static final int DEFAULT_PORT_VALUE = 8080;

    private static final String HTTP_PORT;
    static {
        Properties props = new Properties();
        try {
            props.load(RESTClient.class.getResourceAsStream("/client.properties"));
        } catch (Exception ex) {
            throw new RuntimeException("client.properties resource is not available");
        }
        HTTP_PORT = props.getProperty(PORT_PROPERTY);
    }

    int port;

    public RESTClient() {
        this(getPort());
    }

    public RESTClient(int port) {
        this.port = port;
    }

    public void useXopAttachmentServiceWithWebClient() throws Exception {

        final String serviceURI = "http://localhost:" + port + "/services/attachments/xop";
        
        JAXRSClientFactoryBean factoryBean = new JAXRSClientFactoryBean();
        factoryBean.setAddress(serviceURI);
        factoryBean.setProperties(Collections.singletonMap(org.apache.cxf.message.Message.MTOM_ENABLED, 
                                                    (Object)"true"));
        WebClient client = factoryBean.createWebClient();
        client.type("multipart/related").accept("multipart/related");
        
        XopBean xop = createXopBean();
        
        System.out.println();
        System.out.println("Posting a XOP attachment with a WebClient");
        
        XopBean xopResponse = client.post(xop, XopBean.class);
        
        verifyXopResponse(xop, xopResponse);
    }
    
    public void useXopAttachmentServiceWithProxy() throws Exception {

        final String serviceURI = "http://localhost:" + port + "/services/attachments";
        
        XopAttachmentService proxy = JAXRSClientFactory.create(serviceURI, 
                                                               XopAttachmentService.class);
        
        XopBean xop = createXopBean();
        
        System.out.println();
        System.out.println("Posting a XOP attachment with a proxy");
                
        XopBean xopResponse = proxy.echoXopAttachment(xop);
        
        verifyXopResponse(xop, xopResponse);
    }
    
    private void verifyXopResponse(XopBean xopOriginal, XopBean xopResponse) {
        if (!Arrays.equals(xopResponse.getBytes(), xopOriginal.getBytes())) {
            throw new RuntimeException("Received XOP attachment is corrupted");
        }
        System.out.println();
        System.out.println("XOP attachment has been successfully received");
    }
    
    private XopBean createXopBean() throws Exception  {
        XopBean xop = new XopBean();
        xop.setName("xopName");
        
        InputStream is = getClass().getResourceAsStream("/java.jpg");
        byte[] data = IOUtils.readBytesFromStream(is);
        
        // Pass java.jpg as an array of bytes
        xop.setBytes(data);
        
        // Wrap java.jpg as a DataHandler
        xop.setDatahandler(new DataHandler(
                             new ByteArrayDataSource(data, "application/octet-stream")));
        
        if (Boolean.getBoolean("java.awt.headless")) {
            System.out.println("Running headless. Ignoring an Image property.");
        } else {
            xop.setImage(getImage("/java.jpg"));
        }
        
        return xop;
    }
    
    private Image getImage(String name) throws Exception {
        return ImageIO.read(getClass().getResource(name));
    }

    public static void main(String[] args) throws Exception {

        RESTClient client = new RESTClient();

        // uses CXF JAX-RS WebClient
        client.useXopAttachmentServiceWithWebClient();
        
        // uses CXF JAX-RS Proxy
        client.useXopAttachmentServiceWithProxy();
    }

    private static int getPort() {
        try {
            return Integer.valueOf(HTTP_PORT);
        } catch (NumberFormatException ex) {
            // ignore
        }
        return DEFAULT_PORT_VALUE;
    }
}
