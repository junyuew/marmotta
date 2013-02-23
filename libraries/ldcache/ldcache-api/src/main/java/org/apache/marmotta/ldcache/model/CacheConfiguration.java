/**
 * Copyright (C) 2013 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.marmotta.ldcache.model;

import org.apache.marmotta.ldclient.model.ClientConfiguration;

/**
 * Configuration options for LDCache instances. Can be subclassed by backends as needed.
 * <p/>
 * Author: Sebastian Schaffert (sschaffert@apache.org)
 */
public class CacheConfiguration {

    /** the LDClient configuration to use for configuring the Linked Data Client */
    private ClientConfiguration clientConfiguration;


    private long defaultExpiry = 86400L;

    public CacheConfiguration() {
        this(new ClientConfiguration());
    }

    public CacheConfiguration(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    public ClientConfiguration getClientConfiguration() {
        return clientConfiguration;
    }

    public void setClientConfiguration(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    public long getDefaultExpiry() {
        return defaultExpiry;
    }

    public void setDefaultExpiry(long defaultExpiry) {
        this.defaultExpiry = defaultExpiry;
    }
}
