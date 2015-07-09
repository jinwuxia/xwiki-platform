/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
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
package org.xwiki.cache.oscache.internal;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.xwiki.cache.CacheException;
import org.xwiki.cache.CacheFactory;
import org.xwiki.cache.config.CacheConfiguration;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.component.phase.Initializable;
import org.xwiki.component.phase.InitializationException;
import org.xwiki.environment.Environment;

/**
 * Base implementation for OSCache support.
 * 
 * @version $Id$
 * @since 1.9M2
 */
public abstract class AbstractOSCacheCacheFactory implements CacheFactory, Initializable
{
    /**
     * The logger to log.
     */
    @Inject
    private Logger logger;

    /**
     * Used to lookup the container.
     */
    @Inject
    private ComponentManager componentManager;

    /**
     * Optional Environment used to access configuration files.
     */
    private Environment environment;

    /**
     * @return the default configuration identifier used to load cache configuration file
     */
    protected abstract String getDefaultPropsId();

    @Override
    public void initialize() throws InitializationException
    {
        // Container
        // Note that the reason we lazy load the container is because we want to be able to use the Cache in
        // environments where there's no container.

        try {
            this.environment = this.componentManager.getInstance(Environment.class);
        } catch (ComponentLookupException e) {
            this.logger.debug("Can't find any Environment", e);
        }   
    }
    
    @Override
    public <T> org.xwiki.cache.Cache<T> newCache(CacheConfiguration configuration) throws CacheException
    {
        this.logger.debug("Start OSCache initialisation");

        OSCacheCache<T> cache = new OSCacheCache<T>();
        cache.initialize(new OSCacheCacheConfiguration(this.environment, configuration, getDefaultPropsId()));

        this.logger.debug("End OSCache initialisation");

        return cache;
    }
}
