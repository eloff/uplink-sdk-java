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
package adjoint.uplink_sdk.client;

import java.io.*;

/**
 * @author Adjoint Inc.
 */
public enum TxTypeEnum implements WriteBinary {
  CREATE_CONTRACT(1000, "CreateContract"),
  SYNC_LOCAL(1001, "SyncLocal"),
  CALL_CONTRACT(1002, "Call"),
  CREATE_ASSET(1003, "CreateAsset"),
  TRANSFER_ASSET(1004, "Transfer"),
  CIRCULATE_ASSET(1005, "Circulate"),
  BIND_ASSET(1006, "BindAsset"),
  REVOKE_ASSET(1007, "RevokeAsset"),
  CREATE_ACCOUNT(1008, "CreateAccount"),
  REVOKE_ACCOUNT(1009, "RevokeAccount"),
  TX_CONTRACT(0, "TxContract"),
  TX_ASSET(0, "TxAsset"),
  TX_ACCOUNT(0, "TxAccount");

  public final Integer value;
  public final String name;

  TxTypeEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public void writeBinary(DataOutputStream out) throws IOException {
    out.writeShort(value);
  }

}