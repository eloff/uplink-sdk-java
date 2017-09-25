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
import java.util.List;

/**
 *
 * @author Adjoint Inc.
 */
public class PeersWrap extends Response{
  private  List<Peers> contents = new ArrayList<Peers>();;

  public PeersWrap(String tag, List<Peers> contents){
    super(tag, "RPCResp");
    this.contents = contents;
  }

  /**
   * @return the contents
   */
  public List<Peers> getContents() {
    return contents;
  }

  /**
   * @param contents the contents to set
   */
  public void setContents(List<Peers> contents) {
    this.contents = contents;
  }
  class Peers{
    private String tag; // Peer
    private PeersBody contents;

    /**
     * @return the tag
     */
    public String getTag() {
      return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
      this.tag = tag;
    }

    /**
     * @return the contents
     */
    public PeersBody getContents() {
      return contents;
    }

    /**
     * @param contents the contents to set
     */
    public void setContents(PeersBody contents) {
      this.contents = contents;
    }
  }
  class PeersBody{
    private String peer_acc_addr;
    private String peer_pid;

    /**
     * @return the peer_acc_addr
     */
    public String getPeer_acc_addr() {
      return peer_acc_addr;
    }

    /**
     * @param peer_acc_addr the peer_acc_addr to set
     */
    public void setPeer_acc_addr(String peer_acc_addr) {
      this.peer_acc_addr = peer_acc_addr;
    }

    /**
     * @return the peer_pid
     */
    public String getPeer_pid() {
      return peer_pid;
    }

    /**
     * @param peer_pid the peer_pid to set
     */
    public void setPeer_pid(String peer_pid) {
      this.peer_pid = peer_pid;
    }
  }
}

//
//[{
//  "tag": "Peer",
//  "contents": {
//    "peer_acc_addr": "F8XUEiSUDKRhG6Srfft7zB4n8KWEZ6G3YnRg3F5MetVU",
//    "peer_pid": "127.0.1.1:8001:0"
//  }
//}]