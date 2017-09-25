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

import java.util.Map;

/**
 *
 * @author Adjoint Inc.
 */
public class CreateAccount extends TxTypeHeader {
  private String tag;
  private CreateAccountBody contents;

  public CreateAccount(String tag, CreateAccountBody contents){
    super(tag, "CreateAccount");
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
  public CreateAccountBody getContents() {
    return contents;
  }

  /**
   * @param contents the contents to set
   */
  public void setContents(CreateAccountBody contents) {
    this.contents = contents;
  }
  private class CreateAccountBody{
      private String timezone;
      private String pubKey;
      private Map metdata;

    /**
     * @return the timezone
     */
    public String getTimezone() {
      return timezone;
    }

    /**
     * @param timezone the timezone to set
     */
    public void setTimezone(String timezone) {
      this.timezone = timezone;
    }

    /**
     * @return the pubKey
     */
    public String getPubKey() {
      return pubKey;
    }

    /**
     * @param pubKey the pubKey to set
     */
    public void setPubKey(String pubKey) {
      this.pubKey = pubKey;
    }

    /**
     * @return the metdata
     */
    public Map getMetdata() {
      return metdata;
    }

    /**
     * @param metdata the metdata to set
     */
    public void setMetdata(Map metdata) {
      this.metdata = metdata;
    }
  }
}
