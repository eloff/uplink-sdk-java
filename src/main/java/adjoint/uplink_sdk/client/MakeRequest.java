/*
 * Copyright 2017 Adjoint Inc.
 *
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
package adjoint.uplink_sdk.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author Adjoint Inc.
 */

public class MakeRequest {
  private Client client;

  public MakeRequest(){
    this.client = Client.create();
  }
  String Call(String url, String params){
    String output = "";

    try {
      WebResource webResource = client.resource(url);

      ClientResponse response = webResource.accept("application/json")
              .post(ClientResponse.class, params);

      if (response.getStatus() != 200) {
              System.out.println(response);
              throw new RuntimeException("Failed : HTTP error code : "
                              + response.getStatus());
      }
      output = response.getEntity(String.class);

    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return output;
  }
}
