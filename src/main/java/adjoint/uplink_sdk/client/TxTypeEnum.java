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
  CREATE_CONTRACT(0, 0, "CreateContract"),
  SYNC_LOCAL(0, 1, "SyncLocal"),
  CALL_CONTRACT(0, 2, "Call"),
  CREATE_ASSET(1, 0, "CreateAsset"),
  TRANSFER_ASSET(1, 1, "Transfer"),
  CIRCULATE_ASSET(1, 2, "Circulate"),
  BIND_ASSET(1, 3, "BindAsset"),
  REVOKE_ASSET(1, 4, "RevokeAsset"),
  CREATE_ACCOUNT(2, 0, "CreateAccount"),
  REVOKE_ACCOUNT(2, 1, "RevokeAccount"),
  // We only care about the names of TxContract, TxAsset and TxAccount
  TX_CONTRACT(0, -1, "TxContract"),
  TX_ASSET(1, -1, "TxAsset"),
  TX_ACCOUNT(2, -1, "TxAccount");

  public final Integer fstFlag;
  public final Integer sndFlag;
  public final String name;

  TxTypeEnum(Integer fstFlag, Integer sndFlag, String name) {
    this.fstFlag = fstFlag;
    this.sndFlag = sndFlag;
    this.name = name;
  }

  public void writeBinary(DataOutputStream out) throws IOException {
    out.writeShort(fstFlag);
    out.writeShort(sndFlag);
  }

}