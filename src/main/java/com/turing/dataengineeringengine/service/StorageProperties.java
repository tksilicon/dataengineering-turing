package com.turing.dataengineeringengine.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
	
	String systemLocation = System.getProperty("user.dir");
    private String location = systemLocation + "/src/main/resources/data1";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
