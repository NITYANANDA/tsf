/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package server;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/*
 * Class that can be used (instead of XML-based configuration) to inform the JAX-RS 
 * runtime about the resources and providers it is supposed to deploy.  See the 
 * ApplicationServer class for more information.  
 */
@ApplicationPath("/personservice")
public class OAuthApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>();
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> classes = new HashSet<Object>();
        return classes;
    }
}
