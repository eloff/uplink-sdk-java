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
package adjoint.uplink_sdk.client;

/**
 *
 * @author Adjoint Inc.
 */
public class Response {
    private static String tag;

  /**
   * @return the tag
   */
  public static String getTag() {
    return tag;
  }

  /**
   * @param aTag the tag to set
   */
  public static void setTag(String aTag) {
    tag = aTag;
  }
    private String type;

    public Response(String tag, String type){
      this.tag = tag;
      this.type = type;
    }

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
}
