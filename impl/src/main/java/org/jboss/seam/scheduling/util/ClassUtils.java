/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.seam.scheduling.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * A collection of class management utility methods.
 *
 * @author <a href="mailto:jordanorc@gmail.com>Jordano Celestrini</a>
 */
public class ClassUtils {

    /**
     * Return the context classloader.
     * 
     * @return the current classloader.
     */
    public static ClassLoader getCurrentClassLoader() {
        ClassLoader loader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {

            public ClassLoader run() {
                try {
                    return Thread.currentThread().getContextClassLoader();

                } catch (Exception e) {
                    return null;
                }
            }
        });

        if (loader == null) {
            loader = ClassUtils.class.getClassLoader();
        }

        return loader;
    }
}
