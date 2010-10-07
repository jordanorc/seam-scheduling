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
package org.jboss.seam.scheduling.quartz;

import java.lang.annotation.Annotation;
import org.jboss.seam.scheduling.JobGroup;
import org.jboss.seam.scheduling.SchedulerManager;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.seam.scheduling.Job;
import org.jboss.seam.scheduling.JobConfiguration;
import org.jboss.seam.scheduling.event.Event;
import org.jboss.seam.scheduling.exception.JobNotFoundException;
import org.quartz.CronExpression;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.jboss.seam.scheduling.exception.SchedulerException;
import org.jboss.seam.scheduling.job.EventJob;
import org.quartz.CronTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Implementation of SchedulerManager API for Quartz</p>
 *
 * @author <a href="mailto:jordanorc@gmail.com>Jordano Celestrini</a>
 */
@Named("org.jboss.seam.scheduling.quartz.QuartzShedulerManager")
@ApplicationScoped
@Default
public class QuartzShedulerManager implements SchedulerManager {

    public static final String TASK = "task";
    public static final String TASK_PARAM = "job";
    public static final String BEAN_MANAGER = "bean_manager";
    private Logger log = LoggerFactory.getLogger(QuartzShedulerManager.class);
    private Scheduler scheduler;
    @Inject
    private BeanManager manager;
    //save jobs
    private Map<String, Job> jobs = new HashMap<String, Job>();

    public QuartzShedulerManager() {
        try {
            this.scheduler = StdSchedulerFactory.getDefaultScheduler();
            this.scheduler.start();
        } catch (org.quartz.SchedulerException ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    public void schedule(Job job, Map<String, Object> parameters) throws SchedulerException {
        String key = JobWrapper.getJobNameKey(job.getName(), job.getGroup().getName());

        //verify if job exists
        if (jobs.containsKey(key)) {
            throw new SchedulerException("Job " + job.toString() + " alread exists");
        }

        //validate job
        job.validate();

        //create job detail
        JobDetail jobDetail = new JobDetail(job.getName(), job.getGroup().getName(), QuartzJob.class);
        jobDetail.getJobDataMap().put(TASK_PARAM, parameters);
        jobDetail.getJobDataMap().put(TASK, job.getTask());

        //criate expression to validate it
        CronExpression cronExpression = null;
        try {
            cronExpression = new CronExpression(job.getCronExpression());
        } catch (ParseException ex) {
            log.error(ex.getMessage());
            throw new SchedulerException("Invalid cron expression for job " + job.toString());
        }

        try {
            this.scheduler.addJob(jobDetail, true);
        } catch (org.quartz.SchedulerException ex) {
            log.error(ex.getMessage());
            throw new SchedulerException("Problem adding job " + job.toString());
        }

        //verify if job will start automatically
        if (job.isAutoStart()) {
            startJob(job);
        }

        //add job into job list
        jobs.put(key, job);

        log.info("Scheduler for {} initialised", job.toString());
    }

    @Override
    public void schedule(Event event, Annotation... annotation) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(EventJob.BEAN_MANAGER, this.manager);
        parameters.put(EventJob.EVENT, event);
        parameters.put(EventJob.QUALIFIERS, annotation);

        schedule(event.getJob(), parameters);
    }

    @Override
    public void pauseAll() {
        try {
            scheduler.pauseAll();
        } catch (org.quartz.SchedulerException ex) {
            //TODO: create message
            throw new SchedulerException("");
        }
    }

    @Override
    public void pauseJob(Job job) throws SchedulerException {
        job.validate();

        //verify if job exists
        if (!jobs.containsValue(job)) {
            throw new JobNotFoundException("Job not found");
        }

        try {
            //pause job
            scheduler.pauseJob(job.getName(), job.getGroup().getName());
        } catch (org.quartz.SchedulerException ex) {
            //TODO: create message
            throw new SchedulerException("");
        }
    }

    @Override
    public void pauseJobGroup(JobGroup jobGroup) throws SchedulerException {
        jobGroup.validate();

        try {
            scheduler.pauseJobGroup(jobGroup.getName());
        } catch (org.quartz.SchedulerException ex) {
            //TODO: create message
            throw new SchedulerException("");
        }
    }

    @Override
    public void resumeAll() throws SchedulerException {
        try {
            scheduler.resumeAll();
        } catch (org.quartz.SchedulerException ex) {
            //TODO: create message
            throw new SchedulerException("");
        }
    }

    @Override
    public void resumeJob(Job job) throws SchedulerException {
        job.validate();

        //verify if job exists
        if (!jobs.containsValue(job)) {
            throw new JobNotFoundException("Job not found");
        }

        try {
            scheduler.resumeJob(job.getName(), job.getGroup().getName());
        } catch (org.quartz.SchedulerException ex) {
            //TODO: create message
            throw new SchedulerException("");
        }
    }

    @Override
    public void resumeJobGroup(JobGroup jobGroup) throws SchedulerException {
        jobGroup.validate();

        try {
            scheduler.resumeJobGroup(jobGroup.getName());
        } catch (org.quartz.SchedulerException ex) {
            throw new SchedulerException("");
        }
    }

    @Override
    public void startJob(Job job) throws SchedulerException {
        job.validate();

        JobDetail jobDetail = null;
        try {
            jobDetail = this.scheduler.getJobDetail(job.getName(), job.getGroup().getName());
        } catch (org.quartz.SchedulerException ex) {
            //Logger.getLogger(ShedulerManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new org.jboss.seam.scheduling.exception.SchedulerException("Job not found to start");
        }

        //cria a expression
        CronExpression cronExpression = null;
        try {
            cronExpression = new CronExpression(job.getCronExpression());
        } catch (ParseException ex) {
            //Logger.getLogger(ShedulerManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Define a Trigger that will fire "now"
        CronTrigger trigger = new CronTrigger();
        trigger.setCronExpression(cronExpression);
        trigger.setName("[" + "trigger" + "]" + job.getName());
        trigger.setJobGroup(job.getGroup().getName());
        trigger.setJobName(job.getName());
        try {
            this.scheduler.scheduleJob(trigger);
        } catch (org.quartz.SchedulerException ex) {
            //Logger.getLogger(ShedulerManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new org.jboss.seam.scheduling.exception.SchedulerException("Problem starting job: " + ex.getMessage());
        }
    }

    @Override
    public void deleteJob(Job job) throws SchedulerException {
        job.validate();

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Job getJob(String name, String group) throws JobNotFoundException {

        if (name == null) {
            throw new SchedulerException("You need to inform job name");
        }

        if (group == null) {
            group = JobConfiguration.DEFAULT_GROUP_NAME;
        }

        String key = JobWrapper.getJobNameKey(name, group);

        if (jobs.containsKey(key)) {
            return jobs.get(key);
        } else {
            throw new JobNotFoundException();
        }
    }

    public Job getJob(String name) throws JobNotFoundException {
        return getJob(name, null);
    }
}

class JobWrapper {

    public String key;

    JobWrapper(Job job) {
        key = getJobNameKey(job.getName(), job.getGroup().getName());
    }

    static String getJobNameKey(String jobName, String groupName) {
        return groupName + "_$x$x$_" + jobName;
    }

    public boolean equals(Object obj) {
        if (obj instanceof JobWrapper) {
            JobWrapper jw = (JobWrapper) obj;
            if (jw.key.equals(this.key)) {
                return true;
            }
        }

        return false;
    }

    public int hashCode() {
        return key.hashCode();
    }
}
