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

/**
 *
 * @author Adjoint Inc.
 */
public enum TxTypeEnum {
    CREATE_CONTRACT(1000, "CreateContract"),
    CREATE_ACCOUNT(1006, "CreateAccount"),
    CREATE_ASSET(1003, "CreateAsset"),
    TRANSFER_ASSET(1004, "Transfer"),
    REVOKE_ACCOUNT(1007, "RevokeAccount"),
    BIND_ASSET(1005, "BindAsset"),
    CALL_CONTRACT(1002, "Call"),
    SYNC_LOCAL(1001, "SyncLocal"),
    TX_CONTRACT(0, "TxContract"),
    TX_ASSET(0, "TxAsset"),
    TX_ACCOUNT(0, "TxAccount");

    private final Integer code;
    private final String name;

    private TxTypeEnum(Integer code, String name) {
        this.code =  code;
        this.name = name;
    };

    public Integer getValue() {
      return this.code;
    };

    public String getName(){
      return this.name;
    };

}