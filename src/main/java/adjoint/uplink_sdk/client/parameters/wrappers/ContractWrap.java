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
public class ContractWrap extends Response {
  private ContractContent contents;

  public ContractWrap(String tag, ContractContent contents){
    super(tag, "RPCResp");
    this.contents = contents;
  }

  /**
   * @return the contents
   */
  public ContractContent getContents() {
    return contents;
  }

  /**
   * @param contents the contents to set
   */
  public void setContents(ContractContent contents) {
    this.contents = contents;
  }

  class ContractContent{
    private Integer timestamp;
    private String script;
    private Storage storage;

    /**
     * @return the timestamp
     */
    public Integer getTimestamp() {
      return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Integer timestamp) {
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
    public Storage getStorage() {
      return storage;
    }

    /**
     * @param storage the storage to set
     */
    public void setStorage(Storage storage) {
      this.storage = storage;
    }
  }

  class Storage{
    // nothing
  }
}
