package com.test;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;


public class MyApplication  extends ResourceConfig {
    public MyApplication() {
        packages("com.test");
        register(JacksonFeature.class);
    }
}