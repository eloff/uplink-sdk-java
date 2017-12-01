package adjoint.uplink_sdk.client;

import java.io.DataOutputStream;
import java.io.IOException;

@FunctionalInterface
public interface WriteBinary {
  void writeBinary(DataOutputStream out) throws IOException;
}


