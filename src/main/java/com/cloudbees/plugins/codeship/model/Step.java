package com.cloudbees.plugins.codeship.model;

/**
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class Step {


    private String name;
    private String service;
    private String command;

    public void setName(String name) {
        this.name = name;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
