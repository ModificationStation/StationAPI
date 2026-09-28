package net.modificationstation.stationapi.impl.network.stream;

import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 * A basic OutputStream wrapper that can be easily reset and reused for tracking written data sizes.
 */
public class TrackingOutputStream extends DataOutputStream {

    public TrackingOutputStream() {
        super(null);
    }

    public TrackingOutputStream(OutputStream out) {
        super(out);
    }

    public void reset(OutputStream out) {
        written = 0;
        this.out = out;
    }
}
