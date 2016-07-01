/*
 * Copyright (C) 2016 summers.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package net.saga.github.notification.logger.vo;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Arrays.stream;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.http.Header;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

/**
 *
 * @author summers
 */
public class GitHubResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private MultivaluedMap<String, String> headers;
    private String body;

    public void loadHeaders(Header[] pHeaders) {
        this.headers = new MultivaluedMapImpl<>();
        stream(pHeaders).forEach((header) -> headers.add(header.getName(), header.getValue()));
        
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "GitHubResponse{\n" + "\theaders=" + headers.entrySet().stream().map(e -> String.format("[%s:%s],", e.getKey(), e.getValue())).reduce("", String::concat) + ",\n\tbody=" + body + "\n}";
    }
    
    public int getStatus() {
        return Integer.parseInt(ofNullable(headers.getFirst("Status")).orElse("500").split(" ")[0]);
    }
    
    public int getRateLimitLimit() {
        return Integer.parseInt(ofNullable(headers.getFirst("X-RateLimit-Limit")).orElse("60"));
    }
   
    public int getRateLimitRemaining() {
        return Integer.parseInt(ofNullable(headers.getFirst("X-RateLimit-Remaining")).orElse("60"));
    }
    
    public Optional<URL> getNext() {
        
        String linkHeader = headers.getFirst("Link");
        if (linkHeader != null) {
            String[] links = linkHeader.split(",");
            for (String link : links) {
                String[] parts = link.split(";");
                if (parts[1].trim().equalsIgnoreCase("rel=\"next\"")) {
                    try {
                        return Optional.of(new URL(parts[0].replace("<", "").replace(">", "")));
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(GitHubResponse.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
                    
        }
        
        return Optional.empty();
    }

    public String getLastModified() {
        return ofNullable(headers.getFirst("Last-Modified")).orElse("");
    }
    
    public String getETag() {
        try {
            return ofNullable(headers.getFirst("ETag").replace("\"", "")).orElse("");
        } catch (NullPointerException ignore)  {
            return "";
        }
    }
    
}
