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
public class AssetType {
  private String tag;
  private short contents;
  public AssetType(String assetType, short precision){
    this.tag = assetType;
    this.contents = precision;
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
  public short getContents() {
    return contents;
  }

  /**
   * @param contents the contents to set
   */
  public void setContents(short contents) {
    this.contents = contents;
  }
}
