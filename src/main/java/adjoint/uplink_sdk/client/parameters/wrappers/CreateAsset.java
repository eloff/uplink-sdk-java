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
 * @author Adjoint Inc.
 */
public class CreateAsset extends TxTypeHeader {
  public final CreateAssetBody contents;

  public CreateAsset(String tag, CreateAssetBody contents) {
    super(tag, "CreateAsset");
    this.contents = contents;
  }

  public class CreateAssetBody {
    public final String assetName;
    public final AssetType assetType;
    public final String reference;
    public final Long supply;


    public CreateAssetBody(String assetName, AssetType assetType, String reference, Long supply) {
      this.assetName = assetName;
      this.assetType = assetType;
      this.reference = reference;
      this.supply = supply;
    }
  }

  public class AssetType {
    public final String type;
    public final int precision;

    public AssetType(String type, int precision) {
      this.type = type;
      this.precision = precision;
    }
  }
}
