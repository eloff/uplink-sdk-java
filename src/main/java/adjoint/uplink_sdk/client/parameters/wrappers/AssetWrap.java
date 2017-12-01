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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * @author Adjoint Inc.
 */

public class AssetWrap extends Response {
  public final List<AssetContents> contents;

  public AssetWrap(String tag, List<AssetContents> contents) {
    super(tag, "RPCResp");
    this.contents = contents;
  }

  class AssetContents {
    public final String address;
    public final Asset asset;

    AssetContents(String address, Asset asset) {
      this.address = address;
      this.asset = asset;
    }
  }

  class Asset {
    public final Integer issuedOn;
    public final String name;
    public final String reference;
    public final Integer supply;
    public final String issuer;
    public final AssetType assetType;
    public final Map<String, Object> holdings = new HashMap<String, Object>();

    Asset(Integer issuedOn, String name, String reference, Integer supply, String issuer, AssetType assetType) {
      this.issuedOn = issuedOn;
      this.name = name;
      this.reference = reference;
      this.supply = supply;
      this.issuer = issuer;
      this.assetType = assetType;
    }
  }

  class AssetType {
    public final String type;
    public final Integer precision;

    AssetType(String type, Integer precision) {
      this.type = type;
      this.precision = precision;
    }
  }
}