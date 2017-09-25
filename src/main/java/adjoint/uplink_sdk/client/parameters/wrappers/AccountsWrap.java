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
package adjoint.uplink_sdk.client.parameters.wrappers;

import adjoint.uplink_sdk.client.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Adjoint Inc.
 */
public class AccountsWrap extends Response {
  List<AccountContents> contents = new ArrayList<AccountContents>();
  
  public AccountsWrap(String tag, List<AccountContents> contents){
    super(tag, "RPCResp");
    this.contents = contents;
  }

  class AccountContents{
    private String publicKey;
    private String address;
    private Integer nodeKey;
    private String timezone;
    private Map<String, Object> metadata = new HashMap<String,Object>();

    @Override
    public String toString(){
      return getNodeKey() + getPublicKey() + getAddress() + getTimezone();
    }

    /**
     * @return the publicKey
     */
    public String getPublicKey() {
      return publicKey;
    }

    /**
     * @param publicKey the publicKey to set
     */
    public void setPublicKey(String publicKey) {
      this.publicKey = publicKey;
    }

    /**
     * @return the address
     */
    public String getAddress() {
      return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
      this.address = address;
    }

    /**
     * @return the nodeKey
     */
    public Integer getNodeKey() {
      return nodeKey;
    }

    /**
     * @param nodeKey the nodeKey to set
     */
    public void setNodeKey(Integer nodeKey) {
      this.nodeKey = nodeKey;
    }

    /**
     * @return the timezone
     */
    public String getTimezone() {
      return timezone;
    }

    /**
     * @param timezone the timezone to set
     */
    public void setTimezone(String timezone) {
      this.timezone = timezone;
    }

    /**
     * @return the metadata
     */
    public Map<String, Object> getMetadata() {
      return metadata;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(Map<String, Object> metadata) {
      this.metadata = metadata;
    }
  }
}
