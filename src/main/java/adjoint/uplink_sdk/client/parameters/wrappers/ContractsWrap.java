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
import java.util.List;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Adjoint Inc.
 */
public class ContractsWrap extends Response {
  private List<ContractContents> contents = new ArrayList<ContractContents>();

  public ContractsWrap(String tag, List<ContractContents> contents){
    super(tag, "RPCResp");
    this.contents = contents;
  }

  /**
   * @return the contents
   */
  public List<ContractContents> getContents() {
    return contents;
  }

  /**
   * @param contents the contents to set
   */
  public void setContents(List<ContractContents> contents) {
    this.contents = contents;
  }

  class ContractContents{
    private Contract contract;
    private String address;

    /**
     * @return the contract
     */
    public Contract getContract() {
      return contract;
    }

    /**
     * @param contract the contract to set
     */
    public void setContract(Contract contract) {
      this.contract = contract;
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
  }
  class Contract {
    private int timestamp;
    private String script;
    private Map<String, Object> storage = new HashMap<String,Object>();
    private List<String> methods = new ArrayList<String>();

    /**
     * @return the timestamp
     */
    public int getTimestamp() {
      return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(int timestamp) {
      this.timestamp = timestamp;
    }

    /**
     * @return the script
     */
    public String getScript() {
      return script;
    }

    /**
     * @param script the script to set
     */
    public void setScript(String script) {
      this.script = script;
    }

    /**
     * @return the storage
     */
    public Map<String, Object> getStorage() {
      return storage;
    }

    /**
     * @param storage the storage to set
     */
    public void setStorage(Map<String, Object> storage) {
      this.storage = storage;
    }

    /**
     * @return the methods
     */
    public List<String> getMethods() {
      return methods;
    }

    /**
     * @param methods the methods to set
     */
    public void setMethods(List<String> methods) {
      this.methods = methods;
    }
  }
}
