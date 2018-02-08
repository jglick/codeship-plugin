package com.cloudbees.plugins.codeship;

import com.cloudbees.plugins.codeship.model.ExternalBuild;
import com.cloudbees.plugins.codeship.model.ExternalService;
import com.cloudbees.plugins.codeship.model.ExternalServices;
import com.cloudbees.plugins.codeship.model.VersionedServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Convert a CodeShip pipeline (<code>codeship-steps.yaml</code> and al.) into a Jenkins CPS Pipeline script.
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class CodeShip {

    public static String translate(String services_yaml, String steps_yaml) throws IOException, InvocationTargetException, IllegalAccessException {

        Yaml parser = new Yaml();
        Map<String,?> map = (Map) parser.load(services_yaml);

        // see https://documentation.codeship.com/pro/builds-and-configuration/services/#services-file-setup--configuration



        // reproduce logic from https://github.com/codeship/jet/blob/master/service/unmarshal_external_services.go#unmarshalExternalServices
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final VersionedServices versionedServices = mapper.readValue(services_yaml, VersionedServices.class);

        ExternalServices services;

        if (versionedServices.version != null) {
            services = versionedServices.services;
        } else {
            services = mapper.readValue(services_yaml, ExternalServices.class);
        }


        // Write equivalent Jenkins pipeline

        final StringWriter out = new StringWriter();
        final PrintWriter p = new PrintWriter(out);

        // NOTE alternatively, we could produce a PodTemplate here
        p.println("node('docker') {");
        for (Map.Entry<String, ExternalService> e : services.entrySet()) {
            final String name = e.getKey();
            final ExternalService service = e.getValue();

            p.println("    def "+name+"_image = " + service.getImageForPipeline());
        }

        p.println();
        List<String> sorted = sortServicesByDependency(services);
        for (String name : sorted) {
            final ExternalService service = services.get(name);
            p.print("    def "+name+" = "+name+"_image.run(");
            if (service.links != null) {
                p.print("args='");
                for (String dependency : service.links) {
                    p.print(" --link " + dependency);
                }
                p.print("'");
            }
            p.println(")");
        }

        p.println("}");
        return out.toString();
    }


    private static List<String> sortServicesByDependency(ExternalServices services) {

        List<String> ordered = new ArrayList<>();
        Map<String, ExternalService> remaining = new HashMap<>(services);

        do {
            Map<String, ExternalService> all = new HashMap<>(remaining);

            NEXT: for (Map.Entry<String, ExternalService> entry : all.entrySet()) {
                final ExternalService service = entry.getValue();
                if (service.links == null) {
                    ordered.add(entry.getKey());
                    remaining.remove(entry.getKey());
                } else {
                    for (String d : service.links) {
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
