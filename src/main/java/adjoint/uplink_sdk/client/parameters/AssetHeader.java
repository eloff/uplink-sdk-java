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
public class AssetHeader{
  private String assetName;
  private Integer supply;
  private AssetType assetType;
  private String reference;
  private String issuer;

  public AssetHeader(String Name, Integer Supply, AssetType assetType, String reference, String issuer) {
    this.assetName = Name;
    this.supply = Supply;
    this.assetType = assetType;
    this.reference = reference;
  }

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
   * @return the supply
   */
  public Integer getSupply() {
    return supply;
  }

  /**
   * @param supply the supply to set
   */
  public void setSupply(Integer supply) {
    this.supply = supply;
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
   * @return the issuer
   */
  public String getIssuer() {
    return issuer;
  }

  /**
   * @param issuer the issuer to set
   */
  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }
}