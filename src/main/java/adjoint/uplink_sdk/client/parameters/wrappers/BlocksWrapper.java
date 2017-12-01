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

import adjoint.uplink_sdk.client.Response;

import java.util.List;

public class BlocksWrapper extends Response{
  public final List<Contents> contents;

  public BlocksWrapper(String tag, List<Contents> contents){
    super(tag,"RPCResp");
    this.contents = contents;
  }

  class Contents{
    public final BlocksHeader header;
    public final List<Transactions> transactions;
    public final Integer index;
    public final String signature;
    public final String addr;


    Contents(BlocksHeader header, List<Transactions> transactions, Integer index, String signature, String addr) {
      this.header = header;
      this.transactions = transactions;
      this.index = index;
      this.signature = signature;
      this.addr = addr;
    }
  }

  class BlocksHeader {
    public final String origin;
    public final String merkleRoot;
    public final Integer timestamp;
    public final String prevBlock;


    BlocksHeader(String origin, String merkleRoot, Integer timestamp, String prevBlock) {
      this.origin = origin;
      this.merkleRoot = merkleRoot;
      this.timestamp = timestamp;
      this.prevBlock = prevBlock;
    }
  }

  class Transactions {
    public final TxHeader header;
    public final String origin;
    public final String signature;
    public final Integer timestamp;

    Transactions(TxHeader header, String origin, String signature, Integer timestamp) {
      this.header = header;
      this.origin = origin;
      this.signature = signature;
      this.timestamp = timestamp;
    }
  }
}

