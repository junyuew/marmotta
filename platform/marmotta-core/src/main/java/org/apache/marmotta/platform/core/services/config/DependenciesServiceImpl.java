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
package org.apache.marmotta.platform.core.services.config;

import org.apache.marmotta.platform.core.api.config.DependenciesService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides meta information about the current dependencies
 * 
 * @author Sergio Fernández
 * 
 */
@ApplicationScoped
public class DependenciesServiceImpl implements DependenciesService {

    @Inject
    private Logger log;

    private List<Map<String, String>> dependencies;

    public DependenciesServiceImpl() {
        this.dependencies = new ArrayList<Map<String, String>>();
    }

    @PostConstruct
    public void populate() {
        InputStream deps;

        // first try to get webapp dependencies
        deps = Thread.currentThread().getContextClassLoader().getResourceAsStream(DependenciesService.PATH);
        if (deps == null) {
            // fallback to the core dependencies only
            log.warn("Dependencies not found at the webapp, so falling back to core dependencies...");
            deps = DependenciesServiceImpl.class.getResourceAsStream(DependenciesService.PATH);
        }
        if (deps == null) {
            log.error("No suitable '{}' file found", DependenciesService.PATH);
        } else {
            DataInputStream in = new DataInputStream(deps);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            try {

                // first two line generated by maven are not actually dependencies,
                // anyway this code could be really buggy
                br.readLine();
                br.readLine();

                while ((line = br.readLine()) != null) {
                    line = StringUtils.stripToNull(line);
                    if (line != null) {
                        Map<String, String> dep = new HashMap<String, String>();
                        String[] split = StringUtils.split(line, ":");
                        if (split.length >= 4) {
                            dep.put(DependenciesService.GROUP, split[0]);
                            dep.put(DependenciesService.ARTIFACT, split[1]);
                            dep.put(DependenciesService.VERSION, split[3]);
                            this.dependencies.add(dep);
                            log.debug("Recovered dependency " + split[0] + ":" + split[2] + ":" + split[2]);
                        }
                    }
                }

                br.close();

            } catch (IOException e) {
                log.error("Error reading dependencies: " + e.getMessage());
            } catch (NullPointerException e) {
                log.error("Error reading dependencies: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Map<String, String>> getArtifacs() {
        return Collections.unmodifiableList(this.dependencies);
    }

    @Override
    public List<Map<String, String>> getArtifacs(String groupId) {
        List<Map<String, String>> deps = new ArrayList<Map<String, String>>();
        for (Map<String, String> dep : this.dependencies) {
            if (groupId.equals(dep.get(DependenciesService.GROUP))) {
                deps.add(Collections.unmodifiableMap(dep));
            }
        }
        return Collections.unmodifiableList(deps);
    }

    @Override
    public String getVersion(String groupId, String artifactId) {
        for (Map<String, String> dep : this.dependencies) {
            if (groupId.equals(dep.get(DependenciesService.GROUP)) &&
                    artifactId.equals(dep.get(DependenciesService.ARTIFACT))) return dep.get(DependenciesService.VERSION);
        }
        return null;
    }


}
