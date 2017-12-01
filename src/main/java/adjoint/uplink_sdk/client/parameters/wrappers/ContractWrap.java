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


/**
 * @author Adjoint Inc.
 */
public class ContractWrap extends Response {
  public final ContractContent contents;

  public ContractWrap(String tag, ContractContent contents) {
    super(tag, "RPCResp");
    this.contents = contents;
  }


  class ContractContent {
    public final Integer timestamp;
    public final String script;
    public final Storage storage;

    ContractContent(Integer timestamp, String script, Storage storage) {
      this.timestamp = timestamp;
      this.script = script;
      this.storage = storage;
    }
  }

  class Storage {
    // nothing
  }
}
