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
package adjoint.uplink_sdk.client.parameters;

/**
 *
 * @author Adjoint Inc.
 */
public class TransferHeader{
  private String assetAddr;
  private String toAddr;
  private Integer balance;

  public TransferHeader(String assetAddr, String toAddr, Integer balance ){
    this.assetAddr = assetAddr;
    this.toAddr = toAddr;
    this.balance = balance;
  }

  /**
   * @return the assetAddr
   */
  public String getAssetAddr() {
    return assetAddr;
  }

  /**
   * @param assetAddr the assetAddr to set
   */
  public void setAssetAddr(String assetAddr) {
    this.assetAddr = assetAddr;
  }

  /**
   * @return the toAddr
   */
  public String getToAddr() {
    return toAddr;
  }

  /**
   * @param toAddr the toAddr to set
   */
  public void setToAddr(String toAddr) {
    this.toAddr = toAddr;
  }

  /**
   * @return the balance
   */
  public Integer getBalance() {
    return balance;
  }

  /**
   * @param balance the balance to set
   */
  public void setBalance(Integer balance) {
    this.balance = balance;
  }
}
