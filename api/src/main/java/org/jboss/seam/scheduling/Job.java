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

/**
 * <p>
 * Represents a job to be scheduled
 * </p>
 *
 * @author <a href="mailto:jordanorc@gmail.com>Jordano Celestrini</a>
 */
public class Job {

    private String name;
    private JobGroup group;
    private boolean autoStart;
    private String cronExpression;
    private Task task;

    public Job(String name, JobGroup group, boolean autoStart, String cronExpression) {
        setName(name);
        setGroup(group);
        setAutoStart(autoStart);
        setCronExpression(cronExpression);
    }

    public Job(String name, boolean autoStart, String cronExpression) {
        this(name, null, autoStart, cronExpression);
    }

    public Job(String cronExpression) {
        this(null, null, true, cronExpression);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Set the name of this <code>Job</code>.
     * </p>
     *
     * @param name if <code>null</code>, a radom name will be generated
     *
     * @exception IllegalArgumentException
     *              if name is empty.
     */
    public void setName(String name) {
        if (name != null && name.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "Job name cannot be empty.");
        }

        if (name == null) {
            name = JobConfiguration.generateJobName();
        }

        this.name = name;
    }

    /**
     * @return the group
     */
    public JobGroup getGroup() {
        return group;
    }

    /**
     * <p>
     * Set the <code>JobGroup</code>. of this <code>Job</code>.
     * </p>
     *
     * @param group if <code>null</code>, a new <code>JobGroup</code>. will be used.
     */
    protected void setGroup(JobGroup group) {
        if (group == null) {
            group = new JobGroup();
        }

        this.group = group;
    }

    /**
     * @return the autoStart
     */
    public boolean isAutoStart() {
        return autoStart;
    }

    /**
     * @param autoStart the autoStart to set
     */
    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    /**
     * @return the cronExpression
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * <p>
     * Set the cron expression for this <code>Job</code>.
     * </p>
     *
     * @exception IllegalArgumentException
     *              if cron expression is null or empty.
     */
    public void setCronExpression(String cronExpression) {
        if (cronExpression == null || cronExpression.trim().length() == 0) {
            throw new IllegalArgumentException("Cron expression cannot be empty.");
        }

        this.cronExpression = cronExpression;
    }

    /**
     * <p>
     * Validates whether the properties of the <code>Job</code> are valid
     * </p>
     *
     * @throws IllegalStateException
     *           if a required property (such as Name, Group, Cron Expression) is not
     *           set.
     */
    public void validate() {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "Job name cannot be empty.");
        }
        if (group == null) {
            throw new IllegalArgumentException(
                    "Group cannot be null.");
        }
        if (cronExpression == null || cronExpression.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "Cron expression cannot be empty.");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Job other = (Job) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.group != other.group && (this.group == null || !this.group.equals(other.group))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 11 * hash + (this.group != null ? this.group.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Job[" + "name=" + name + "; group=" + group + ']';
    }

    /**
     * @return the task
     */
    public Task getTask() {
        return task;
    }

    /**
     * @param task the task to set
     */
    public void setTask(Task task) {
        this.task = task;
    }
}
