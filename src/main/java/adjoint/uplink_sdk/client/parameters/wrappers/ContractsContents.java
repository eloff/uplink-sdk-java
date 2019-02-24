package adjoint.uplink_sdk.client.parameters.wrappers;

import java.util.List;
import java.util.Map;

class ContractContents {
	public final String address;
	public final String script;
	public final String state;
	public final String owner;
	public final Map<String, Object> storage;
	public final Map<String, Object> localStorage;
	public final List<String> methods;
	public final List<String> localStorageVars;
	public final long timestamp;

	
	ContractContents(String address, String script, String state, String owner, Map<String, Object> storage, Map<String, Object> localStorage, List<String> methods, List<String> localStorageVars, long timestamp) {
		this.address = address;
		this.script = script;
		this.state = state;
		this.owner = owner;
		this.storage = storage;
		this.localStorage = localStorage;
		this.methods = methods;
		this.localStorageVars = localStorageVars;
		this.timestamp = timestamp;
    }
  }