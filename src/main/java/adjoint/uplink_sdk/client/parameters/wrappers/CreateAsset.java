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
public class CreateAsset extends TxTypeHeader {
  private String tag;
  private CreateAssetBody contents;

  public CreateAsset(String tag, CreateAssetBody contents){
    super(tag, "CreateAsset");
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
  public CreateAssetBody getContents() {
    return contents;
  }

  /**
   * @param contents the contents to set
   */
  public void setContents(CreateAssetBody contents) {
    this.contents = contents;
  }
  private class CreateAssetBody{
      private String assetName;
      private AssetType assetType;
      private String reference;
      private Long supply;

    /**
     * @return the assetName
     */
    public String getAssetName() {
      return assetName;
    }

    /**
     * @param assetName the assetName to set
     */
    public void setAssetName(String assetName) {
      this.assetName = assetName;
    }

    /**
     * @return the assetType
     */
    public AssetType getAssetType() {
      return assetType;
    }

    /**
     * @param assetType the assetType to set
     */
    public void setAssetType(AssetType assetType) {
      this.assetType = assetType;
    }

    /**
     * @return the reference
     */
    public String getReference() {
      return reference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference(String reference) {
      this.reference = reference;
    }

    /**
     * @return the supply
     */
    public Long getSupply() {
      return supply;
    }

    /**
     * @param supply the supply to set
     */
    public void setSupply(Long supply) {
      this.supply = supply;
    }
  }
  private class AssetType{
    private String type;
    private int precision;

    /**
     * @return the type
     */
    public String getType() {
      return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
      this.type = type;
    }

    /**
     * @return the precision
     */
    public int getPrecision() {
      return precision;
    }

    /**
     * @param precision the precision to set
     */
    public void setPrecision(int precision) {
      this.precision = precision;
    }
  }
}
