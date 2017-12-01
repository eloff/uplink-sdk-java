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

import adjoint.uplink_sdk.client.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
//import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Adjoint Inc.
 */
public class CallContractHeader implements WriteBinary {
  public final String address;
  public final String method;
  public final List<Map<String, String>> args;

  public CallContractHeader(String contractAddr, String method, List<Map<String, String>> args) {
    this.address = contractAddr;
    this.method = method;
    this.args = args;
  }


  public void writeBinary(DataOutputStream out) throws IOException {
    TxTypeEnum.CALL_CONTRACT.writeBinary(out);
    out.write(Base58convert.decode(address));
    out.writeLong(method.length());
    out.writeBytes(method);
    out.writeLong(args.size());

    args.forEach((obj) -> {
      String type = obj.get("tag");

      try {
        if (FCLEnum.VACCOUNT.name.equals(type)) {
          byte[] vB = Base58convert.decode((obj.get("contents")));
          out.writeByte(FCLEnum.VACCOUNT.value);
          out.write(vB);
        }
        if (FCLEnum.VASSET.name.equals(type)) {
          byte[] vB = Base58convert.decode(obj.get("contents"));
          out.writeByte(FCLEnum.VASSET.value);
          out.write(vB);
        }
        if (FCLEnum.VCONTRACT.name.equals(type)) {
          byte[] vB = Base58convert.decode(obj.get("contents"));
          out.writeByte(FCLEnum.VCONTRACT.value);
          out.write(vB);

        }
        if (FCLEnum.VINT.name.equals(type)) {
          long vB = Long.valueOf(String.valueOf(obj.get("contents")));
          out.writeByte(FCLEnum.VINT.value);
          out.writeLong(vB);
        }
        if (FCLEnum.VFLOAT.name.equals(type)) {

          double vB = Double.parseDouble((String.valueOf(obj.get("contents"))));
          out.writeByte(FCLEnum.VFLOAT.value);
          out.writeDouble(vB);

        }
        if (FCLEnum.VBOOL.name.equals(type)) {
          boolean vB = Boolean.parseBoolean(String.valueOf(obj.get("contents")));
          out.writeByte(FCLEnum.VBOOL.value);
          out.writeBoolean(vB);
        }
        if (FCLEnum.VMSG.name.equals(type)) {
          int vlen = obj.get("contents").length();
          out.writeByte(FCLEnum.VMSG.value);
          out.writeShort(vlen);
          out.writeBytes(obj.get("contents"));
        }
        if (FCLEnum.VDATETIME.name.equals(type)) {
          OffsetDateTime datetime = OffsetDateTime.parse(obj.get("contents"));
          int year = datetime.getYear();
          int month = datetime.getMonthValue();
          int day = datetime.getDayOfMonth();
          int hour = datetime.getHour();
          int minute = datetime.getMinute();
          int second = datetime.getSecond();

          ZoneOffset tz = datetime.getOffset();
          int tzoffset = tz.getTotalSeconds();

          DayOfWeek dayofweek = datetime.getDayOfWeek();
          int dayNum = dayofweek.getValue();

          out.writeByte(FCLEnum.VDATETIME.value);
          out.writeLong(year);
          out.writeLong(month);
          out.writeLong(day);
          out.writeLong(hour);
          out.writeLong(minute);
          out.writeLong(second);
          out.writeLong(tzoffset);
          out.writeLong(dayNum);

        }
      } catch (IOException ex) {
        Logger.getLogger(UplinkSDK.class.getName()).log(Level.SEVERE, null, ex);
      }

    });

  }
}
