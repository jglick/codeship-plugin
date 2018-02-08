package com.cloudbees.plugins.codeship.model;

import java.util.List;

/**
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class Service {

    public String image;
    public DockerBuild build;
    public List<String> depends_on;

}
