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
package org.jboss.seam.scheduling.qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 * A qualifier annotation for use observing events that fires every minute
 *
 * @author <a href="mailto:jordanorc@gmail.com>Jordano Celestrini</a>
 */
@Scheduled(expression="0 0/1 * * * ?", autoStart = true)
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
public @interface EveryMinute {

}
