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
package adjoint.uplink_sdk.client.header;

import adjoint.uplink_sdk.client.Base58convert;
import adjoint.uplink_sdk.client.TxTypeEnum;
import adjoint.uplink_sdk.client.WriteBinary;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Adjoint Inc.
 */
public class CreateAssetHeader implements WriteBinary {
  public final String assetName;
  public final String assetAddr;
  public final Integer supply;
  public final AssetType assetType;
  public final String reference;
  public final String issuer;

  public CreateAssetHeader(String Name, String assetAddr, Integer Supply, AssetType assetType, String reference, String issuer) {
    this.assetName = Name;
    this.assetAddr = assetAddr;
    this.supply = Supply;
    this.assetType = assetType;
    this.reference = reference;
    this.issuer = issuer;
  }


  public void writeBinary(DataOutputStream out) throws IOException {
    TxTypeEnum.CREATE_ASSET.writeBinary(out);
    out.write(Base58convert.decode(assetAddr));
    out.writeShort(assetName.length());
    out.writeBytes(assetName);
    out.writeLong(supply);
    out.writeShort(1);
    out.writeShort(reference.length());
    out.writeBytes(reference);
    out.writeShort(assetType.tag.length());
    out.writeBytes(assetType.tag);
    if ("Fractional".equals(assetType.tag)) {
      out.writeLong(assetType.contents);
    }

  }
}