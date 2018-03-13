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
 * @author Adjoint Inc.
 */

public enum ResponseEnum {
  RPC_RESP("RPCResp"),
  RPC_RESP_OK("RPCRespOK"),
  RPC_TX_OK("RPCTransactionOK"),
  RPC_RESP_ERROR("RPCRespError");


  public final String name;

  ResponseEnum(String name) {
    this.name = name;
  }

}