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
package org.jboss.seam.scheduling.job;

import java.lang.annotation.Annotation;
import java.util.Map;
import javax.enterprise.inject.spi.BeanManager;
import org.jboss.seam.scheduling.Task;

/**
 *
 * @author <a href="mailto:jordanorc@gmail.com>Jordano Celestrini</a>
 */
public class EventJob implements Task {

    public static final String BEAN_MANAGER = "BEAN_MANAGER";
    public static final String EVENT = "EVENT";
    public static final String QUALIFIERS = "QUALIFIERS";

    @Override
    public void execute(Map<String, Object> parameters) {
        BeanManager manager = (BeanManager) parameters.get(BEAN_MANAGER);
        Object event = parameters.get(EVENT);
        Annotation[] annotations = (Annotation[]) parameters.get(QUALIFIERS);
        manager.fireEvent(event, annotations);
    }
}
