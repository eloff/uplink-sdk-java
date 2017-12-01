/*
 * Copyright 2017 Adjoint Inc..
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
package adjoint.uplink_sdk.client.header;

import adjoint.uplink_sdk.client.Base58convert;
import adjoint.uplink_sdk.client.WriteBinary;
import adjoint.uplink_sdk.client.TxTypeEnum;

import java.io.*;

/**
 * @author Adjoint Inc.
 */

public class CreateContractHeader implements WriteBinary {

  public final String script;
  public final String owner;
  public final String address;
  public final long timestamp;

  public CreateContractHeader(String script, String owner, String address, long timestamp) {
    this.script = script;
    this.address = address;
    this.owner = owner;
    this.timestamp = timestamp;
  }

  public void writeBinary(DataOutputStream out) throws IOException {
    TxTypeEnum.CREATE_CONTRACT.writeBinary(out);
    out.write(Base58convert.decode(address));
    out.writeShort(script.length());
    out.writeBytes(script);
  }

}
