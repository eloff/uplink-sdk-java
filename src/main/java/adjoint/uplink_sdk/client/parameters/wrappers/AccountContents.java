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

import java.util.Map;

class AccountContents {
    public final String publicKey;
    public final String address;
    public final String timezone;
    public final Map<String, Object> metadata;

    AccountContents(String publicKey, String address, String timezone, Map<String, Object> metadata) {
      this.publicKey = publicKey;
      this.address = address;
      this.timezone = timezone;
      this.metadata = metadata;
    }

    @Override
    public String toString() {
	  return publicKey + address + timezone;
	}
  }