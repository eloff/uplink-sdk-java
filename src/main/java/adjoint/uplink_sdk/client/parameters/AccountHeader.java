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

import java.util.Map;

/**
 *
 * @author Adjoint Inc.
 */
public class AccountHeader{
  private String pubKey; //should be in hex
  private String timezone;
  private Map<String, String> metadata;

  public AccountHeader(String pubKey, String timezone, Map<String, String> meta) {
    this.pubKey = pubKey;
    this.timezone = timezone;
    this.metadata = meta;
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
   * @return the metadata
   */
  public Map<String, String> getMetadata() {
    return metadata;
  }

  /**
   * @param metadata the metadata to set
   */
  public void setMetadata(Map<String, String> metadata) {
    this.metadata = metadata;
  }
}

