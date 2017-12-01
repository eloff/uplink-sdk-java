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

import java.util.Map;

/**
 * @author Adjoint Inc.
 */
public class AccountWrap extends Response {
  public final AccountContent contents;

  public AccountWrap(String tag, AccountContent contents) {
    super(tag, "RPCResp");
    this.contents = contents;
  }

  class AccountContent {
    public final String publicKey;
    public final String address;
    public final Integer nodeKey;
    public final String timezone;
    public final Map<String, Object> metadata;

    AccountContent(String publicKey, String address, Integer nodeKey, String timezone, Map<String, Object> metadata) {
      this.publicKey = publicKey;
      this.address = address;
      this.nodeKey = nodeKey;
      this.timezone = timezone;
      this.metadata = metadata;
    }

    @Override
    public String toString() {
      return nodeKey + publicKey + address + timezone;
    }

  }
}
