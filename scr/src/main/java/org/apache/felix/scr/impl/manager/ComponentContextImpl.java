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
package org.apache.felix.scr.impl.manager;


import java.util.Arrays;
import java.util.Dictionary;

import org.apache.felix.scr.component.ExtComponentContext;
import org.apache.felix.scr.impl.helper.ReadOnlyDictionary;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentInstance;


/**
 * Implementation for the ComponentContext interface
 *
 */
public class ComponentContextImpl<S> implements ExtComponentContext {

    private final AbstractComponentManager<S> m_componentManager;
    
    private final EdgeInfo[] edgeInfos;
    
    private volatile ComponentInstance m_componentInstance;

    ComponentContextImpl( AbstractComponentManager<S> componentManager )
    {
        m_componentManager = componentManager;
        edgeInfos = new EdgeInfo[componentManager.getComponentMetadata().getDependencies().size()];
    }
    
    EdgeInfo getEdgeInfo(DependencyManager<S, ?> dm)
    {
        int index = dm.getIndex();
        if (edgeInfos[index] == null)
        {
            edgeInfos[index] = new EdgeInfo();
        }
        return edgeInfos[index];
    }

    void clearEdgeInfos()
    {
        Arrays.fill( edgeInfos, null );
    }

    protected AbstractComponentManager<S> getComponentManager()
    {
        return m_componentManager;
    }

    public final Dictionary<String, Object> getProperties()
    {
        // 112.12.3.5 The Dictionary is read-only and cannot be modified
        Dictionary<String, Object> ctxProperties = m_componentManager.getProperties();
        return new ReadOnlyDictionary<String, Object>( ctxProperties );
    }


    public Object locateService( String name )
    {
        DependencyManager<S, ?> dm = m_componentManager.getDependencyManager( name );
        return ( dm != null ) ? dm.getService() : null;
    }


    public Object locateService( String name, ServiceReference ref )
    {
        DependencyManager<S, ?> dm = m_componentManager.getDependencyManager( name );
        return ( dm != null ) ? dm.getService( ref ) : null;
    }


    public Object[] locateServices( String name )
    {
        DependencyManager dm = m_componentManager.getDependencyManager( name );
        return ( dm != null ) ? dm.getServices() : null;
    }


    public BundleContext getBundleContext()
    {
        return m_componentManager.getActivator().getBundleContext();
    }


    public Bundle getUsingBundle()
    {
        return null;
    }


    public ComponentInstance getComponentInstance()
    {
        return m_componentInstance;
    }


    public void enableComponent( String name )
    {
        m_componentManager.getActivator().enableComponent( name );
    }


    public void disableComponent( String name )
    {
        m_componentManager.getActivator().disableComponent( name );
    }


    public ServiceReference<S> getServiceReference()
    {
        return m_componentManager.getServiceReference();
    }


    //---------- Speculative MutableProperties interface ------------------------------

    public void setServiceProperties(Dictionary properties)
    {
        getComponentManager().setServiceProperties(properties );
    }
    
    //---------- ComponentInstance interface support ------------------------------

    Object getImplementationObject()
    {
        return getComponentManager().getInstance();
    }
    
    void newComponentInstance()
    {
        m_componentInstance = new ComponentInstanceImpl(this);
    }
    
    void clearComponentInstance(){
        m_componentInstance = null;
    }

    private static class ComponentInstanceImpl implements ComponentInstance
    {
        private final ComponentContextImpl m_componentContext;

        private ComponentInstanceImpl(ComponentContextImpl m_componentContext)
        {
            this.m_componentContext = m_componentContext;
        }


        public Object getInstance()
        {
            return m_componentContext.getImplementationObject();
        }


        public void dispose()
        {
            m_componentContext.getComponentManager().dispose();
        }

    }
}
