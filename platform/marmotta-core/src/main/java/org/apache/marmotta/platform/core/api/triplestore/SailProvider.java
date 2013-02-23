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
package org.apache.marmotta.platform.core.api.triplestore;

/**
 * Common interface for all sail providers
 * <p/>
 * Author: Sebastian Schaffert (sschaffert@apache.org)
 */
public interface SailProvider {


    /**
     * Return the name of the provider. Used e.g. for displaying status information or logging.
     * @return
     */
    public String getName();


    /**
     * Return true if this sail provider is enabled in the configuration.
     * @return
     */
    public boolean isEnabled();
}
