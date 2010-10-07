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
package org.jboss.seam.scheduling;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import org.jboss.seam.scheduling.qualifier.Scheduled;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import org.jboss.seam.scheduling.event.Event;
import org.jboss.seam.scheduling.job.EventJob;
import org.jboss.seam.scheduling.util.SchedulePropertiesManager;

/**
 *
 * @author <a href="mailto:jordanorc@gmail.com>Jordano Celestrini</a>
 */
@ApplicationScoped
public class SchedulerExtension implements Extension {

    public static final String MANAGER = "manager";
    public static final String ACTION = "action";
    private Map<Scheduled, Set<Annotation>> schedulesFound = new HashMap<Scheduled, Set<Annotation>>();
    private static final Logger log = Logger.getLogger(SchedulerExtension.class.getName());

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager manager) {

        //find default SchedulerManager
        SchedulerManager schedulerManager = getContextualInstance(manager, SchedulerManager.class);

//        Map<String, String> scheduleProperties = SchedulePropertiesManager.instance().getScheduleProperties();
//
//        for (Entry<String, String> entry : scheduleProperties.entrySet()) {
//            //System.out.println(entry.getKey() + " = " + entry.getValue());
//        }

        for (Entry<Scheduled, Set<Annotation>> entry : schedulesFound.entrySet()) {
            Job job = createJob(entry.getKey());
            schedulerManager.schedule(new Event(job), entry.getValue().toArray(new Annotation[entry.getValue().size()]));
        }
    }

    public <T, X> void processAnnotatedType(@Observes ProcessObserverMethod<T, X> pat, BeanManager beanManager) {
        ObserverMethod observerMethod = pat.getObserverMethod();

        for (Object bindingObj : observerMethod.getObservedQualifiers()) {
            Scheduled schedBinding = null;

            Annotation binding = (Annotation) bindingObj;

            if (binding instanceof Scheduled) {
                schedBinding = (Scheduled) binding;
            } else {
                // check for a @Scheduled meta-annotation
                Scheduled scheduled = binding.annotationType().getAnnotation(Scheduled.class);
                if (scheduled != null) {
                    schedBinding = scheduled;
                }
            }
            if (schedBinding != null) {
                Set<Annotation> annotations = new HashSet<Annotation>();
                annotations.add(binding);
                schedulesFound.put(schedBinding, annotations);
            }
        }
    }

    private Job createJob(Scheduled annotation) {
        String expression = annotation.expression();
        String name = annotation.jobName().trim().equals("") ? null : annotation.jobName();
        JobGroup group = annotation.jobGroup().trim().equals("") ? null : new JobGroup(annotation.jobGroup());
        boolean autoStart = annotation.autoStart();
        Task task = new EventJob();

        Job job = new Job(name, group, autoStart, expression);
        job.setTask(task);

        return job;
    }

    /**
     * Get a single CDI managed instance of a specific class. Return only the
     * first result if multiple beans are available.
     * <p>
     * <b>NOTE:</b> Using this method should be avoided at all costs.
     *
     * @param manager The bean manager with which to perform the lookup.
     * @param type The class for which to return an instance.
     * @return The managed instance, or null if none could be provided.
     */
    @SuppressWarnings("unchecked")
    public <T> T getContextualInstance(final BeanManager manager, final Class<T> type) {
        T result = null;
        Bean<T> bean = (Bean<T>) manager.resolve(manager.getBeans(type));
        if (bean != null) {
            CreationalContext<T> context = manager.createCreationalContext(bean);
            if (context != null) {
                result = (T) manager.getReference(bean, type, context);
            }
        }
        return result;
    }
}
