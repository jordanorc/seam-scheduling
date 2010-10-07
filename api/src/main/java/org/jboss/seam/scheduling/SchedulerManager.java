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
import java.util.Map;
import org.jboss.seam.scheduling.event.Event;
import org.jboss.seam.scheduling.exception.JobGroupNotFoundException;
import org.jboss.seam.scheduling.exception.JobNotFoundException;
import org.jboss.seam.scheduling.exception.SchedulerException;

/**
 * <p>
 * Common base class for control job scheduling, i.e. for registering jobs,
 * pause jobs, remove jobs, etc.
 * </p>
 *
 * @author <a href="mailto:jordanorc@gmail.com>Jordano Celestrini</a>
 */
public interface SchedulerManager {

    /**
     * <p>Schedule a new job</p>
     * @param job job that will be scheduled
     * @param parameters parameters to parse when scheduling job
     */
    void schedule(Job job, Map<String, Object> parameters);

    /**
     * <p>Schedule a new job based on Event</p>
     * @param event Event that will be fired
     * @param annotation qualifiers for event
     */
    void schedule(Event event, Annotation... annotation);

    /**
     * <p>Pause all jobs</p>
     */
    void pauseAll();

    /**
     * <p>Pause a specific job</p>
     * @param job indicates the job
     *
     * @throws SchedulerException
     */
    void pauseJob(Job job) throws SchedulerException;

    /**
     * <p>Pause all jobs from a group</p>
     * @param group indicates the group to pause
     *
     * @throws SchedulerException
     */
    void pauseJobGroup(JobGroup jobGroup) throws SchedulerException;

    /**
     * <p>Resume all paused jobs</p>
     * 
     * @throws SchedulerException
     */
    void resumeAll() throws SchedulerException;

    /**
     * <p>Resume a specific job</p>
     * @param job indicates the job to be resumed
     *
     * @throws JobNotFoundException exception fired case job not found
     * @throws SchedulerException
     */
    void resumeJob(Job job) throws SchedulerException;

    /**
     * <p>Resume all jobs from a group</p>
     * @param jobGroup indicates the job group
     * 
     * @throws JobGroupNotFoundException
     * @throws SchedulerException
     */
    void resumeJobGroup(JobGroup jobGroup) throws SchedulerException;

    /**
     * <p>Start a job</p>
     * @param job indicates the job to be started
     * 
     * @throws SchedulerException
     */
    void startJob(Job job) throws SchedulerException;

    /**
     * <p>Delete a job</p>
     * @param job indicates the job to be deleted
     * 
     * @throws JobNotFoundException exception fired case job not found
     * @throws SchedulerException
     */
    void deleteJob(Job job) throws SchedulerException;

    /**
     * <p>Find a job with specified name</p>
     * 
     * @param name job name
     * 
     * @return
     */
    Job getJob(String name) throws JobNotFoundException;

    /**
     * <p>Find a job with specified name</p>
     * 
     * @param name job name
     * @param jobGroup job group name
     * 
     * @return
     */
    Job getJob(String name, String jobGroup) throws JobNotFoundException;
}
