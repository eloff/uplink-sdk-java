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

import adjoint.uplink_sdk.client.TxTypeEnum;
import adjoint.uplink_sdk.client.UplinkSDK;
import adjoint.uplink_sdk.client.WriteBinary;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Adjoint Inc.
 */
public class CreateAccountHeader implements WriteBinary {
  public final String pubKey; //should be in hex
  public final String timezone;
  public final Map<String, String> metadata;

  public CreateAccountHeader(String pubKey, String timezone, Map<String, String> meta) {
    this.pubKey = pubKey;
    this.timezone = timezone;
    this.metadata = meta;
  }


  public void writeBinary(DataOutputStream out) throws IOException {
    TxTypeEnum.CREATE_ACCOUNT.writeBinary(out);
    out.writeShort(pubKey.length());
    out.writeBytes(pubKey);
    out.writeShort(timezone.length());
    out.writeBytes(timezone);

    if (!metadata.isEmpty()) {
      out.writeShort(metadata.size());
      metadata.forEach((k, v) -> {
        try {
          out.writeShort(k.length());
          out.writeBytes(k);
          out.writeShort(v.length());
          out.writeBytes(v);
        } catch (IOException ex) {
          Logger.getLogger(UplinkSDK.class.getName()).log(Level.SEVERE, null, ex);
        }
      });
    } else {
      out.writeShort(0);
    }
  }
}

