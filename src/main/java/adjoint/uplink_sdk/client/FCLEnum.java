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
public enum FCLEnum {
    VINT(0, "VInt", "int"),
    VFLOAT(2, "VFloat", "float"),
    VBOOL(4, "VBool", "bool"),
    VADDRESS(5, "VAddress", "address"),
    VACCOUNT(6, "VAccount", "account"),
    VASSET(7, "VAsset", "asset"),
    VCONTRACT(8, "VContract", "contract"),
    VMSG(9, "VMsg", "msg"),
    VSIG(10, "VSig", "sig"),
    VVOID(11, "VVoid", "void"),
    VDATETIME(12, "VDateTime", "datetime"),
    VTIMEDELTA(13,"VTimeDelta", "timedelta"),
    VUNDEFINED(15, "VUndefined","undefined");

    private final Integer vCode;
    private final String vName;
    private final String human;

    private FCLEnum(Integer vCode, String vName, String human) {
        this.vCode =  vCode;
        this.vName = vName;
        this.human = human;
    };

    public Integer getValue() {
      return this.vCode;
    };

    public String getName(){
      return this.vName;
    };
    public String getHuman(){
      return this.human;
    }
}
