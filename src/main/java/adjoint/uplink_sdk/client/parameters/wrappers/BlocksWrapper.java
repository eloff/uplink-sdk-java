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
import adjoint.uplink_sdk.client.parameters.wrappers.TxHeader;
import java.util.ArrayList;
import java.util.List;

public class BlocksWrapper extends Response{
  private List<Contents> contents = new ArrayList<Contents>();

  public BlocksWrapper(String tag, List<Contents> contents){
    super(tag,"RPCResp");
    this.contents = contents;
  }

  /**
   * @return the contents
   */
  public List<Contents> getContents() {
    return contents;
  }

  /**
   * @param contents the contents to set
   */
  public void setContents(List<Contents> contents) {
    this.contents = contents;
  }

  class Contents{
    private BlocksHeader header;
    private List<Transactions> transactions =  new ArrayList<Transactions>();
    private Integer index;
    private String signature;
    private String addr;

    /**
     * @return the header
     */
    public BlocksHeader getHeader() {
      return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(BlocksHeader header) {
      this.header = header;
    }

    /**
     * @return the transactions
     */
    public List<Transactions> getTransactions() {
      return transactions;
    }

    /**
     * @param transactions the transactions to set
     */
    public void setTransactions(List<Transactions> transactions) {
      this.transactions = transactions;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
      return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
      this.index = index;
    }

    /**
     * @return the signature
     */
    public String getSignature() {
      return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
      this.signature = signature;
    }

    /**
     * @return the addr
     */
    public String getAddr() {
      return addr;
    }

    /**
     * @param addr the addr to set
     */
    public void setAddr(String addr) {
      this.addr = addr;
    }
  }

  class BlocksHeader {
    private String origin;
    private String merkleRoot;
    private Integer timestamp;
    private String prevBlock;

    /**
     * @return the origin
     */
    public String getOrigin() {
      return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(String origin) {
      this.origin = origin;
    }

    /**
     * @return the merkleRoot
     */
    public String getMerkleRoot() {
      return merkleRoot;
    }

    /**
     * @param merkleRoot the merkleRoot to set
     */
    public void setMerkleRoot(String merkleRoot) {
      this.merkleRoot = merkleRoot;
    }

    /**
     * @return the timestamp
     */
    public Integer getTimestamp() {
      return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Integer timestamp) {
      this.timestamp = timestamp;
    }

    /**
     * @return the prevBlock
     */
    public String getPrevBlock() {
      return prevBlock;
    }

    /**
     * @param prevBlock the prevBlock to set
     */
    public void setPrevBlock(String prevBlock) {
      this.prevBlock = prevBlock;
    }
  }

  class Transactions {
    private TxHeader header;
    private String origin;
    private String signature;
    private Integer timestamp;

    /**
     * @return the header
     */
    public TxHeader getHeader() {
      return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(TxHeader header) {
      this.header = header;
    }

    /**
     * @return the origin
     */
    public String getOrigin() {
      return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(String origin) {
      this.origin = origin;
    }

    /**
     * @return the signature
     */
    public String getSignature() {
      return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
      this.signature = signature;
    }

    /**
     * @return the timestamp
     */
    public Integer getTimestamp() {
      return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Integer timestamp) {
      this.timestamp = timestamp;
    }
  }
}

