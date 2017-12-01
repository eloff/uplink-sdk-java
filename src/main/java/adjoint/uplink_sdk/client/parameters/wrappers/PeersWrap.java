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

import java.util.List;

/**
 * @author Adjoint Inc.
 */
public class PeersWrap extends Response {
  public final List<Peers> contents;

  public PeersWrap(String tag, List<Peers> contents) {
    super(tag, "RPCResp");
    this.contents = contents;
  }

  class Peers {
    public final String tag; // Peer
    public final PeersBody contents;

    Peers(String tag, PeersBody contents) {
      this.tag = tag;
      this.contents = contents;
    }
  }

  class PeersBody {
    public final String peer_acc_addr;
    public final String peer_pid;


    PeersBody(String peer_acc_addr, String peer_pid) {
      this.peer_acc_addr = peer_acc_addr;
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