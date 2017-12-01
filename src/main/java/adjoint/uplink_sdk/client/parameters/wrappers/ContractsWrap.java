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

import java.util.List;
import java.util.Map;


/**
 * @author Adjoint Inc.
 */
public class ContractsWrap extends Response {
  public final List<ContractContents> contents;

  public ContractsWrap(String tag, List<ContractContents> contents) {
    super(tag, "RPCResp");
    this.contents = contents;
  }


  class ContractContents {
    public final Contract contract;
    public final String address;

    ContractContents(Contract contract, String address) {
      this.contract = contract;
      this.address = address;
    }
  }

  class Contract {
    public final int timestamp;
    public final String script;
    public final Map<String, Object> storage;
    public final List<String> methods;

    Contract(int timestamp, String script, Map<String, Object> storage, List<String> methods) {
      this.timestamp = timestamp;
      this.script = script;
      this.storage = storage;
      this.methods = methods;
    }
  }
}
