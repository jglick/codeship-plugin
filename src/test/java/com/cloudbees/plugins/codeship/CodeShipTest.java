package com.cloudbees.plugins.codeship;

import org.junit.Test;

import java.io.IOException;

/**
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class CodeShipTest {


    @Test
    public void should_parse_codeship_yaml() throws Exception {
        String pipeline = CodeShip.translate(
                "demo:\n" +
                "  build:\n" +
                "    image: myapp\n" +
                "    dockerfile: Dockerfile\n" +
                "  depends_on:\n" +
                "    - redis\n" +
                "    - postgres\n" +
                "redis:\n" +
                "  image: healthcheck/redis:alpine\n" +
                "postgres:\n" +
                "  image: healthcheck/postgres:alpine",

                "- name: ruby\n" +
                        "  service: demo\n" +
                        "  command: bundle exec ruby check.rb");

        System.out.println(pipeline);
    }

}