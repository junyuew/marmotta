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
package org.apache.marmotta.platform.core.webservices.resource;

import org.apache.marmotta.platform.core.api.config.ConfigurationService;
import org.apache.marmotta.platform.core.services.templating.TemplatingHelper;
import org.apache.marmotta.commons.http.ContentType;
import org.openrdf.model.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper methods shared accross the difference resource web services
 * 
 * @author Sergio Fernández
 *
 */
public class ResourceWebServiceHelper {
    
    public static void addHeader(Response response, String name, String value) {
        response.getMetadata().add(name, value);
    }
    
    public static String appendTypes(List<String> datamimes, String mime) {
        StringBuilder sb = new StringBuilder();
        sb.append(appendContentTypes(mime));
        sb.append(appendMetaTypes(datamimes));
        return sb.toString();
    }   
    
    public static String appendMetaTypes(List<String> datamimes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < datamimes.size(); i++) {
            sb.append(datamimes.get(i));
            sb.append(";rel=meta");
            if (i < datamimes.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
    
    public static String appendContentTypes(String mime) {
        if (mime != null) {
            return mime + ";rel=content";
        } else {
            return "";
        }
    }
    
    public static String buildContentLink(URI resource, String uuid, String mime, ConfigurationService configurationService) {
        // test if there is content
        StringBuffer b = new StringBuffer();
        if (mime != null) {
            b.append("<");
            // b.append(configurationService.getBaseUri() + "content/" + mime +
            // appendix);
            b.append(buildResourceLink(resource, ConfigurationService.CONTENT_PATH, mime, configurationService));
            b.append(">");
            b.append(";type=");
            b.append(mime);
            b.append(";rel=content");
        }
        return b.toString();
    }     
    
    public static String buildMetaLinks(URI resource, String uuid, List<String> datamimes, ConfigurationService configurationService) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < datamimes.size(); i++) {
            b.append("<");
            // b.append(configurationService.getBaseUri() + "meta/" +
            // datamimes.get(i) + appendix);
            b.append(buildResourceLink(resource, ConfigurationService.META_PATH, datamimes.get(i), configurationService));
            b.append(">");
            b.append(";type=");
            b.append(datamimes.get(i));
            b.append(";rel=meta");
            if (i < datamimes.size() - 1) {
                b.append(",");
            }
        }
        return b.toString();
    }
    
    public static String buildResourceLink(URI resource, ContentType cType, ConfigurationService configurationService) {
        return buildResourceLink(resource, cType.getParameter("rel"),
                cType.getMime(), configurationService);
    }

    public static String buildResourceLink(URI resource, String rel, String mime, ConfigurationService configurationService) {
        final String src = configurationService.getServerUri(), base = configurationService
                .getBaseUri();

        if (src.equals(base)
                && resource.stringValue().startsWith(base + ConfigurationService.RESOURCE_PATH + "/")) {
            final String uuid;
            uuid = resource.stringValue().substring(
                    (base + "resource/").length());
            return String.format("%s%s/%s/%s", base, rel, mime, uuid);

        } else {
            try {
                return String.format("%s%s/%s?uri=%s", src, rel, mime,
                        URLEncoder.encode(resource.stringValue(), ResourceWebService.CHARSET));
            } catch (UnsupportedEncodingException e) {
                return String.format("%s%s/%s?uri=%s", src, rel, mime,
                        resource.stringValue());
            }
        }
    }
    
    public static Response buildErrorPage(String uri, String base, Status status, String message) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("uri", uri);
        data.put("baseUri", base);
        data.put("message", message);
        try {
            data.put("encoded_uri", URLEncoder.encode(uri, "UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            data.put("encoded_uri", uri);
        }
        try {
            return Response.status(status)
                    .entity(TemplatingHelper.processTemplate("404.ftl", data))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Not Found").build();
        }
    } 

}
