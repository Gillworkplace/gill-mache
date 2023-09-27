package com.gill.mache.config;

import java.io.File;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.PathResource;

import cn.hutool.core.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.support.StandardServletEnvironment;

/**
 * CustomConfig
 *
 * @author gill
 * @version 2023/06/26 17:47
 **/
@Slf4j
@Configuration
public class ExternConfig {

    @Autowired
    private StandardServletEnvironment environment;

    @PostConstruct
    public void init() throws BeansException {
        String appRoot = ObjUtil.defaultIfBlank(System.getenv("APP_ROOT"), "./");
        log.debug("APP_ROOT: {}", appRoot);
        File configDir = new File(appRoot + "/config");
        if (!configDir.exists() || !configDir.isDirectory()) {
            log.debug("not find external config directory");
            return;
        }
        File[] configs = configDir.listFiles((dir, name) -> name.endsWith("yaml") || name.endsWith("yml"));
        if (configs == null) {
            log.debug("not find external config files");
            return;
        }
        log.debug("loading external config...");
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        for (File config : configs) {
            yaml.setResources(new PathResource(config.getPath()));
            Properties properties = yaml.getObject();
            if (properties != null) {
                String configFileName = getConfigFileName(config);
                MutablePropertySources propertySources = environment.getPropertySources();
                propertySources.addLast(new PropertiesPropertySource(configFileName, properties));
            }
        }
        log.debug("finished loading external config");
    }

    private String getConfigFileName(File file) {
        return file.getName().replaceAll("\\.[^.]*$", "");
    }
}

