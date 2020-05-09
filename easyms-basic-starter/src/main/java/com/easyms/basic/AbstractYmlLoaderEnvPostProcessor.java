package com.easyms.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractYmlLoaderEnvPostProcessor implements EnvironmentPostProcessor {

    private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            loadYml(environment, getYmlFileName());
        } catch (IOException e) {
            throw new IllegalStateException("Exception while loading yml " + getYmlFileName(), e);
        }
    }

    private void loadYml(ConfigurableEnvironment environment, String ymlFilePath) throws IOException {
        List<PropertySource<?>> propertySources = loadYml(ymlFilePath, environment);
        propertySources.forEach((prop) -> environment.getPropertySources().addLast(prop));
    }

    protected abstract String getYmlFileName() ;

    List<PropertySource<?>> loadYml(String path, ConfigurableEnvironment env) throws IOException {
        Resource resource = new ClassPathResource(path);
        if(! resource.exists()) {
            throw new IllegalStateException("Resource " + path + " was not found");
        }

        List<PropertySource<?>> resources = this.loader.load(path, resource);

        return resources.stream()
                .map(this::buildPropertySourceWithProfile)
                .filter(propertySourceWithProfile -> matchAnyActiveProfiles(propertySourceWithProfile, env))
                .sorted((props1, props2) -> bool2int(props2.hasAnyProfile()) - bool2int(props1.hasAnyProfile()))
                .map(PropertySourceWithProfile::getPropertySource)
                .collect(Collectors.toList());


    }

    private int bool2int(boolean bool) {
        return bool ? 1 : 0;
    }

    private PropertySourceWithProfile buildPropertySourceWithProfile(PropertySource<?> propertySource) {
        return new PropertySourceWithProfile(propertySource, (String) propertySource.getProperty("spring.profiles"));
    }

    private boolean matchAnyActiveProfiles(PropertySourceWithProfile propertySourceWithProfile, ConfigurableEnvironment env) {
        if(! propertySourceWithProfile.hasAnyProfile()) {
            return true;
        } else {
            List<String> propertySourceProfiles = Arrays.asList(propertySourceWithProfile.getProfiles().split(","));
            Set<String> activeProfiles = new HashSet<>(Arrays.asList(env.getActiveProfiles()));
            return propertySourceProfiles.stream().map(String::trim).anyMatch(activeProfiles::contains);
        }
    }

    @AllArgsConstructor
    @Getter
    static class PropertySourceWithProfile {
        private PropertySource<?> propertySource;
        private String profiles;
        boolean hasAnyProfile() {
            return profiles != null;
        }
    }
}
