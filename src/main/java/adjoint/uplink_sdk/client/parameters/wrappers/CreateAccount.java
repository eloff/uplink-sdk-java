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
 * @author Adjoint Inc.
 */
public class CreateAccount extends TxTypeHeader {
  public final CreateAccountBody contents;

  public CreateAccount(String tag, CreateAccountBody contents) {
    super(tag, "CreateAccount");
    this.contents = contents;
  }

  public final class CreateAccountBody {
    public final String timezone;
    public final String pubKey;
    public final Map metdata;

    public CreateAccountBody(String timezone, String pubKey, Map metdata) {
      this.timezone = timezone;
      this.pubKey = pubKey;
      this.metdata = metdata;
    }
  }
}
