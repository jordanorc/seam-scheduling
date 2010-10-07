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

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * <p>
 * Represents a cron expression
 * </p>
 *
 * @author <a href="mailto:jordanorc@gmail.com>Jordano Celestrini</a>
 */
public class CronExpression {

    protected static final int SECOND = 0;
    protected static final int MINUTE = 1;
    protected static final int HOUR = 2;
    protected static final int DAY_OF_MONTH = 3;
    protected static final int MONTH = 4;
    protected static final int DAY_OF_WEEK = 5;
    protected static final int YEAR = 6;
    protected static final int ALL_SPEC_INT = 99; // '*'
    protected static final int NO_SPEC_INT = 98; // '?'
    protected static final Integer ALL_SPEC = new Integer(ALL_SPEC_INT);
    protected static final Integer NO_SPEC = new Integer(NO_SPEC_INT);
    protected static final Map monthMap = new HashMap(20);
    protected static final Map dayMap = new HashMap(60);
    protected transient TreeSet seconds;
    protected transient TreeSet minutes;
    protected transient TreeSet hours;
    protected transient TreeSet daysOfMonth;
    protected transient TreeSet months;
    protected transient TreeSet daysOfWeek;
    protected transient TreeSet years;
    private boolean expressionParsed = false;

    static {
        monthMap.put("JAN", new Integer(0));
        monthMap.put("FEB", new Integer(1));
        monthMap.put("MAR", new Integer(2));
        monthMap.put("APR", new Integer(3));
        monthMap.put("MAY", new Integer(4));
        monthMap.put("JUN", new Integer(5));
        monthMap.put("JUL", new Integer(6));
        monthMap.put("AUG", new Integer(7));
        monthMap.put("SEP", new Integer(8));
        monthMap.put("OCT", new Integer(9));
        monthMap.put("NOV", new Integer(10));
        monthMap.put("DEC", new Integer(11));

        dayMap.put("SUN", new Integer(1));
        dayMap.put("MON", new Integer(2));
        dayMap.put("TUE", new Integer(3));
        dayMap.put("WED", new Integer(4));
        dayMap.put("THU", new Integer(5));
        dayMap.put("FRI", new Integer(6));
        dayMap.put("SAT", new Integer(7));
    }
    private String cronExpression = null;

    /**
     * Constructs a new <CODE>CronExpression</CODE> based on the specified
     * parameter.
     *
     * @param cronExpression String representation of the cron expression the
     *                       new object should represent
     * @throws java.text.ParseException
     *         if the string expression cannot be parsed into a valid
     *         <CODE>CronExpression</CODE>
     */
    public CronExpression(String cronExpression) {
        if (cronExpression == null) {
            throw new IllegalArgumentException("cronExpression cannot be null");
        }

        this.cronExpression = cronExpression.toUpperCase();

        //TODO: Needs validate cron expression
        //buildExpression(this.cronExpression);
    }
}
