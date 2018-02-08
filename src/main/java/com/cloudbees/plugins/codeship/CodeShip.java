package com.cloudbees.plugins.codeship;

import com.cloudbees.plugins.codeship.model.Service;
import com.cloudbees.plugins.codeship.model.Services;
import com.cloudbees.plugins.codeship.model.Steps;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Convert a CodeShip pipeline (<code>codeship-steps.yaml</code> and al.) into a Jenkins CPS Pipeline script.
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class CodeShip {

    public static String translate(String services_yaml, String steps_yaml) throws IOException {


        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final Services services = mapper.readValue(services_yaml, Services.class);

        final Steps steps = mapper.readValue(steps_yaml, Steps.class);

        final StringWriter out = new StringWriter();
        final PrintWriter p = new PrintWriter(out);
        p.println("node('docker') {");
        for (Map.Entry<String, Service> e : services.entrySet()) {
            final String name = e.getKey();
            final Service service = e.getValue();

            // NOTE alternatively, we could produce a PodTemplate here
            if (service.image != null) {
                p.println("   def "+name+"_image = docker.image('"+service.image+"')");
            } else {
                p.println("   def "+name+"_image = docker.build('"+service.build.image+"')");
                // TODO pass Dockerfile parameter as args. @CodeShip: is this Just dockerfile or context path ?
            }
        }

        p.println();
        List<String> sorted = sortServicesByDependency(services);
        for (String name : sorted) {
            final Service service = services.get(name);
            p.print("    def "+name+" = "+name+"_image.run(");
            if (service.depends_on != null) {
                p.print("args='");
                for (String dependency : service.depends_on) {
                    p.print(" --link " + dependency);
                }
                p.print("'");
            }
            p.println(")");
        }

        p.println("}");
        return out.toString();
    }


    private static List<String> sortServicesByDependency(Services services) {

        List<String> ordered = new ArrayList<>();
        Map<String, Service> remaining = new HashMap<>(services);

        do {
            Map<String, Service> all = new HashMap<>(remaining);

            NEXT: for (Map.Entry<String, Service> entry : all.entrySet()) {
                final Service service = entry.getValue();
                if (service.depends_on == null) {
                    ordered.add(entry.getKey());
                    remaining.remove(entry.getKey());
                } else {
                    for (String d : service.depends_on) {
                        if (!ordered.contains(d)) {
                            continue NEXT;
                        }
                    }
                    ordered.add(entry.getKey());
                    remaining.remove(entry.getKey());
                }
            }
        } while (!remaining.isEmpty());

        return ordered;
    }
}
