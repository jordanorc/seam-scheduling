/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.seam.scheduling.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Provides functions to locate URLs
 * 
 * @author <a href="mailto:jordanorc@gmail.com>Jordano Celestrini</a>
 */
public class ClasspathUrlFinder {

    /**
     * Find the classpath URLs for a specific classpath resource.  The classpath URL is extracted
     * from loader.getResources() using the baseResource.
     *
     * @param baseResource
     * @return
     */
    public static URL[] findResourceBases(String baseResource, ClassLoader loader) {
        ArrayList<URL> list = new ArrayList<URL>();
        try {
            Enumeration<URL> urls = loader.getResources(baseResource);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                list.add(findResourceBase(url, baseResource));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list.toArray(new URL[list.size()]);
    }

    private static URL findResourceBase(URL url, String baseResource) {
        String urlString = url.toString();
        int idx = urlString.lastIndexOf(baseResource);
        urlString = urlString.substring(0, idx);
        URL deployUrl = null;
        try {
            deployUrl = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return deployUrl;
    }

    public static URL[] findResources(String resource, ClassLoader loader) {
        ArrayList<URL> list = new ArrayList<URL>();
        try {
            Enumeration<URL> urls = loader.getResources(resource);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                list.add(url);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list.toArray(new URL[list.size()]);
    }
}
