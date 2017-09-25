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
public class TransactionParams {
  private String method;
  private Transactions params;

  public TransactionParams(Transactions params){
    this.method = "Transaction";
    this.params = params;
  }

  /**
   * @return the method
   */
  public String getMethod() {
    return method;
  }

  /**
   * @param method the method to set
   */
  public void setMethod(String method) {
    this.method = method;
  }

  /**
   * @return the params
   */
  public Transactions getParams() {
    return params;
  }

  /**
   * @param params the params to set
   */
  public void setParams(Transactions params) {
    this.params = params;
  }
}
