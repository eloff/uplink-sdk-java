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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adjoint Inc.
 */
public class TransactionsWrap extends Response {
  public final List<TxContents> contents;
  
  public TransactionsWrap(String tag, List<TxContents> contents){
    super(tag, "RPCResp");
    this.contents = contents;
  }
}

class TxContents{
  private TxHeader header;
  private String fromPub;
  private String from;
  private String signature;

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
   * @return the fromPub
   */
  public String getFromPub() {
    return fromPub;
  }

  /**
   * @param fromPub the fromPub to set
   */
  public void setFromPub(String fromPub) {
    this.fromPub = fromPub;
  }

  /**
   * @return the from
   */
  public String getFrom() {
    return from;
  }

  /**
   * @param from the from to set
   */
  public void setFrom(String from) {
    this.from = from;
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
}
