/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.seam.scheduling.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author celestrini
 */
public class PropertiesUtils {

    /**
     * Read Properties from an InputStream.
     *
     * @param inputStream InputStream to read Properties from
     * @return Properties as read from inputStream
     */
    public static Properties loadProperties(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Error loading properties from stream", e);
        }

        return properties;
    }

    /**
     * Make a Map&lt;String, String&gt; from the supplied Properties,
     * copying all the keys and values.
     *
     * @param sourceProperties Properties representing properties key-value map.
     * @return a Map&lt;String, String&gt; representation of the source
     *          Properties.
     */
    public static Map<String, String> toMap(Properties sourceProperties) {
        if (sourceProperties == null) {
            return null;
        }
        Map<String, String> configMap = new HashMap<String, String>();
        Iterator<?> iter = sourceProperties.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            configMap.put(key, sourceProperties.getProperty(key));
        }
        return configMap;
    }
}
