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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * @author Adjoint Inc.
 */

  public class AssetWrap extends Response{
    private List<AssetContents> contents = new ArrayList<AssetContents>();

    public AssetWrap(String tag, List<AssetContents> contents){
      super(tag,"RPCResp");
      this.contents = contents;
    }

  /**
   * @return the contents
   */
  public List<AssetContents> getContents() {
    return contents;
  }

  /**
   * @param contents the contents to set
   */
  public void setContents(List<AssetContents> contents) {
    this.contents = contents;
  }

    class AssetContents {
     private String address;
     private Asset asset;

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
     * @return the asset
     */
    public Asset getAsset() {
      return asset;
    }

    /**
     * @param asset the asset to set
     */
    public void setAsset(Asset asset) {
      this.asset = asset;
    }
   }
    class Asset{
     private Integer issuedOn;
     private String name;
     private String reference;
     private Integer supply;
     private String issuer;
     private AssetType assetType;
     private Map<String, Object> holdings = new HashMap<String,Object>();

    /**
     * @return the issuedOn
     */
    public Integer getIssuedOn() {
      return issuedOn;
    }

    /**
     * @param issuedOn the issuedOn to set
     */
    public void setIssuedOn(Integer issuedOn) {
      this.issuedOn = issuedOn;
    }

    /**
     * @return the name
     */
    public String getName() {
      return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
      this.name = name;
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
     * @return the holdings
     */
    public Map<String, Object> getHoldings() {
      return holdings;
    }

    /**
     * @param holdings the holdings to set
     */
    public void setHoldings(Map<String, Object> holdings) {
      this.holdings = holdings;
    }
    }

    class AssetType{
      private String type;
      private transient Integer precision;
    }
 }