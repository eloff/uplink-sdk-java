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
package adjoint.uplink_sdk.client.parameters.wrappers;

/**
 * @author Adjoint Inc.
 */

public class TransferAsset extends TxTypeHeader {
  public final TransferBody contents;

  public TransferAsset(String tag, TransferBody contents) {
    super(tag, "Transfer");
    this.contents = contents;
  }

  public class TransferBody {
    public final String assetAddr;
    public final String toAddr;
    public final long balance;

    public TransferBody(String assetAddr, String toAddr, long balance) {
      this.assetAddr = assetAddr;
      this.toAddr = toAddr;
      this.balance = balance;
    }
  }
}
