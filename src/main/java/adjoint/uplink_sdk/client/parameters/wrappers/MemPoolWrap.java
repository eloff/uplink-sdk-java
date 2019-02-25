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
import adjoint.uplink_sdk.client.parameters.wrappers.TxContents;
import java.util.List;

/**
 *
 * @author Adjoint Inc.
 */
public class MemPoolWrap extends Response {
	public final MemPoolContents contents;

	public MemPoolWrap(String tag, MemPoolContents contents){
		super(tag, "RPCResp");
		this.contents = contents;
	}

	class MemPoolContents {
		public final List<TxContents> transactions;
		public final int size;

		public MemPoolContents(List<TxContents> transactions, int size) {
			this.transactions = transactions;
			this.size = size;
		}
	}
}