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
 *
 * @author Adjoint Inc.
 */

public class RevokeAccount extends TxTypeHeader {
  private String tag;
  private RevokeBody contents;

  public RevokeAccount(String tag, RevokeBody contents){
    super(tag, "RevokeAccount");
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
  public RevokeBody getContents() {
    return contents;
  }

  /**
   * @param contents the contents to set
   */
  public void setContents(RevokeBody contents) {
    this.contents = contents;
  }
  private class RevokeBody{
      private String address;

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
  }
}
