/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.scr.impl.helper;

import org.osgi.service.component.ComponentContext;

/**
 * This object describes a constructor for a component.
 * The name ConstructorMethod has been chosen to avoid a clash with the 
 * existing Constructor class.
 */
public interface ConstructorMethod {

	/**
	 * Create a new instance
	 * @param componentClass The implementation class of the component
	 * @param componentContext The component context
	 * @param logger A logger 
	 * @return The instance
	 */
	<T> T newInstance(Class<T> componentClass,
    		           ComponentContext componentContext,
                       SimpleLogger logger )
    throws Exception;
    
    public ConstructorMethod DEFAULT = new ConstructorMethod() {
		
		@Override
		public <T> T newInstance(Class<T> componentClass, ComponentContext componentContext, SimpleLogger logger) 
				throws Exception
		{
            // 112.4.4 The class must be public and have a public constructor without arguments so component instances
            // may be created by the SCR with the newInstance method on Class
            return componentClass.newInstance();
		}
	};

}
