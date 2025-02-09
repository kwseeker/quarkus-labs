/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.kwseeker.quarkus;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class HelloResource {

	@Inject
	HelloService service;
		 
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getHello() {
        return service.getHello();
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{name}")
    public String getHello(@PathParam("name") String name) {
    	return service.getHello(name);
    }
    
}