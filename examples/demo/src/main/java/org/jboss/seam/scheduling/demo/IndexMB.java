/*
 * Copyright 2010, Celestrini, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.scheduling.demo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.seam.scheduling.Job;
import org.jboss.seam.scheduling.SchedulerManager;
import org.jboss.seam.scheduling.Task;
import org.jboss.seam.scheduling.qualifier.Scheduled;
import org.jboss.seam.scheduling.event.Event;
import org.jboss.seam.scheduling.exception.JobNotFoundException;
import org.jboss.seam.scheduling.qualifier.EverySecond;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author <a href="mailto:jordanorc@gmail.com>Jordano Celestrini</a>
 */
@Named
@ApplicationScoped
public class IndexMB {

    @Inject
    private SchedulerManager scheduleManager;
    private static final Logger log = LoggerFactory.getLogger(IndexMB.class);
    private Map<String, String> logMap;
    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    private boolean initialized;
    //job name
    private String jobName;

    /** Creates a new instance of IndexMB */
    public IndexMB() {
        logMap = new HashMap<String, String>();
    }

    private String getFormattedDate() {
        SimpleDateFormat sdf =
                new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance(); // today
        return sdf.format(calendar.getTime());
    }

    public void everySecondJob(@Observes @EverySecond Event e) throws org.quartz.SchedulerException {
        StringBuilder info = logMap.containsKey("everySecond") ? new StringBuilder(logMap.get("everySecond")) : new StringBuilder();
        info.append(getFormattedDate()).append("   Running action\n");
        log.info("Running job {}", "everySecond");

        if (!initialized) {
            initCustomJob();
            initialized = true;
        }

        logMap.put("everySecond", info.toString());
    }

    public void jobTest(@Observes @Scheduled(jobName = "jobTeste", expression = "0/20 * * * * ?", autoStart = true) Event e) {
        StringBuilder info = logMap.containsKey("jobTeste") ? new StringBuilder(logMap.get("jobTeste")) : new StringBuilder();
        info.append(getFormattedDate()).append("   Running action\n");
        log.info("Running job {}", "jobTeste");
        logMap.put("jobTeste", info.toString());
    }

    public void initCustomJob() {
        Job job = new Job("otherJob", true, "0/10 * * * * ?");
        job.setTask(new Task() {

            @Override
            public void execute(Map<String, Object> parameters) {
                System.out.println("Executing second custom Job");
            }
        });

        scheduleManager.schedule(job, null);
    }

    public void pause() {
        StringBuilder info = logMap.containsKey(jobName) ? new StringBuilder(logMap.get(jobName)) : new StringBuilder();
        info.append(getFormattedDate()).append("   Pausing job\n");
        Job job = null;
        try {
            job = scheduleManager.getJob(jobName);
            log.info("Pausing job {}", jobName);
            scheduleManager.pauseJob(job);
        } catch (JobNotFoundException ex) {
            info.append(getFormattedDate()).append(" Error pausing job: ").append(ex.getMessage()).append("\n");
        }

        logMap.put(jobName, info.toString());
    }

    public void resume() {
        StringBuilder info = logMap.containsKey(jobName) ? new StringBuilder(logMap.get(jobName)) : new StringBuilder();
        info.append(getFormattedDate()).append("   Resuming job\n");
        Job job = null;
        try {
            job = scheduleManager.getJob(jobName);
            log.info("Resuming job {}", jobName);
            scheduleManager.resumeJob(job);
        } catch (JobNotFoundException ex) {
            info.append(getFormattedDate()).append(" Error resuming job: ").append(ex.getMessage()).append("\n");
        }

        logMap.put(jobName, info.toString());
    }

    public void delete() throws org.quartz.SchedulerException {
        StringBuilder info = logMap.containsKey(jobName) ? new StringBuilder(logMap.get(jobName)) : new StringBuilder();
        info.append(getFormattedDate()).append("   Deleting job\n");
        Job job = null;
        try {
            job = scheduleManager.getJob(jobName);
            log.info("Deleting job {}", jobName);
            scheduleManager.deleteJob(job);
        } catch (JobNotFoundException ex) {
            info.append(getFormattedDate()).append(" Error deleting job: ").append(ex.getMessage()).append("\n");
        }

        logMap.put(jobName, info.toString());
    }

    /**
     * @return the log
     */
    public Map<String, String> getLog() {
        Map retorno = new HashMap(logMap);
        logMap = new HashMap();
        return retorno;
    }

    /**
     * @param jobName the jobName to set
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
