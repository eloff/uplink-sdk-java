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

import java.util.List;

/**
 *
 * @author Adjoint Inc.
 */

public class CallContract extends TxTypeHeader {
  private String tag;
  private CallBody contents;

  public CallContract(String tag, CallBody contents){
    super(tag, "Call");
    this.contents = contents;
  }

  /**
   * @return the tag
   */
  public String getTag() {
    return tag;
  }

  /**
   * @param tag the tag to set
   */
  public void setTag(String tag) {
    this.tag = tag;
  }

  /**
   * @return the contents
   */
  public CallBody getContents() {
    return contents;
  }

  /**
   * @param contents the contents to set
   */
  public void setContents(CallBody contents) {
    this.contents = contents;
  }
  private class CallBody{
      private String address;
      private String method;
      private List args;

    /**
     * @return the address
     */
    public String getAddress() {
      return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
      this.address = address;
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
     * @return the args
     */
    public List getArgs() {
      return args;
    }

    /**
     * @param args the args to set
     */
    public void setArgs(List args) {
      this.args = args;
    }
  }
}
