/**
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.scheduling.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A singleton instance of the properties read from the /META-INF/schedule.properties
 * file on the classpath.
 * @author Peter Royle
 */
public class SchedulePropertiesManager {

    /**
     * The path on the classpath at which the properties file cotaining named schedules is expected to be found.
     */
    public static final String SCHEDULE_PROPERTIES_PATH = "META-INF/scheduler.properties";
    private Map<String, String> scheduleProperties = null;
    private static SchedulePropertiesManager instance = null;
    private Logger log = LoggerFactory.getLogger(SchedulePropertiesManager.class);

    /**
     * Return a singleton instance of this class, creating the instance if necessary.
     * @return the singleton instance of this class.
     */
    public static SchedulePropertiesManager instance() {
        if (instance == null) {
            instance = new SchedulePropertiesManager();
        }

        return instance;
    }

    /**
     * Creates a new instance of SchedulePropertiesManager, reading the named schedules
     * properties file if found.
     */
    public SchedulePropertiesManager() {
        scheduleProperties = new HashMap<String, String>();

        URL[] urls = ClasspathUrlFinder.findResources(SCHEDULE_PROPERTIES_PATH,
                ClassUtils.getCurrentClassLoader());

        if (urls.length > 0) {
            for (URL url : urls) {
                InputStream stream;
                try {
                    stream = url.openStream();
                    //save properties
                    scheduleProperties.putAll(PropertiesUtils.toMap(PropertiesUtils.loadProperties(stream)));
                } catch (IOException ex) {
                    log.error("Error loading properties file for named schedules at " + url.toString(), ex);
                }
            }
        } else {
            log.warn("No named schedule configurations found (" + SCHEDULE_PROPERTIES_PATH
                    + " not found on classpath).");
        }
    }

    /**
     *
     * @return the scheduleProperties.
     */
    public Map<String, String> getScheduleProperties() {
        return scheduleProperties;
    }
}
