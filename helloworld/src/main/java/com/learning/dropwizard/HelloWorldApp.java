package com.learning.dropwizard;

import com.learning.dropwizard.health.TemplateHealthCheck;
import com.learning.dropwizard.resource.HelloWorldResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import java.io.File;

public class HelloWorldApp extends Application<HelloWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        if(null == args || args.length == 0) {
            String configFile = HelloWorldApp.class.getClassLoader().getResource("helloworld.yml").getFile();
            args = new String[]{"server", configFile};
        }
        new HelloWorldApp().run(args);
    }

    @Override
    public void run(HelloWorldConfiguration configuration, Environment environment) throws Exception {
        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);

        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
    }

    @Override
    public String getName() {
        return "hello-world";
    }
}
