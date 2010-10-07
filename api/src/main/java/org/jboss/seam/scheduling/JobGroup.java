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
 * Represents a job group
 * </p>
 * 
 * @author <a href="mailto:jordanorc@gmail.com>Jordano Celestrini</a>
 */
public class JobGroup {

    private String name;

    public JobGroup() {
        this(null);
    }

    public JobGroup(String name) {
        setName(name);
    }

    /**
     * @return the nome of job group
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Set the name of this <code>JobGroup</code>.
     * </p>
     *
     * @param name if <code>null</code>, JobConfiguration.DEFAULT_GROUP_NAME will be used.
     *
     * @exception IllegalArgumentException
     *              if name is empty.
     */
    public void setName(String name) {
        if (name != null && name.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "Group name cannot be empty.");
        }

        if (name == null) {
            name = JobConfiguration.DEFAULT_GROUP_NAME;
        }

        this.name = name;
    }

    @Override
    public String toString() {
        return "jobGroup[" + "name=" + name + ']';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JobGroup other = (JobGroup) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    /**
     * <p>
     * Validates whether the properties of the <code>JobGroup</code> are valid
     * </p>
     *
     * @throws IllegalStateException
     *           if a required property (such as name) is not
     *           set.
     */
    public void validate() {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "Group name cannot be empty.");
        }
    }
}
