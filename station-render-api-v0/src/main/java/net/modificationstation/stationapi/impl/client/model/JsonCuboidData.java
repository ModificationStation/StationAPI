package net.modificationstation.stationapi.impl.client.model;

import com.google.common.primitives.Doubles;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonCuboidData {

    public final double[] from;
    public final double[] to;
    public final JsonFacesData faces;

    public void postprocess() {
        Doubles.reverse(from);
        Doubles.reverse(to);
        double fromTmp = from[2];
        from[2] = 16 - to[2];
        to[2] = 16 - fromTmp;
        from[0] /= 16;
        from[1] /= 16;
        from[2] /= 16;
        to[0] /= 16;
        to[1] /= 16;
        to[2] /= 16;
        double tmp;

        // Rotating down UVs
        tmp = faces.down.uv[0];
        faces.down.uv[0] = faces.down.uv[1];
        faces.down.uv[1] = tmp;
        tmp = faces.down.uv[2];
        faces.down.uv[2] = faces.down.uv[3];
        faces.down.uv[3] = tmp;
        faces.down.uv[0] = 16 - faces.down.uv[0];
        faces.down.uv[1] = 16 - faces.down.uv[1];
        faces.down.uv[2] = 16 - faces.down.uv[2];
        faces.down.uv[3] = 16 - faces.down.uv[3];

        // Rotating up UVs
        tmp = faces.up.uv[0];
        faces.up.uv[0] = faces.up.uv[1];
        faces.up.uv[1] = tmp;
        tmp = faces.up.uv[2];
        faces.up.uv[2] = faces.up.uv[3];
        faces.up.uv[3] = tmp;
        faces.up.uv[0] = 16 - faces.up.uv[0];
        faces.up.uv[1] = 16 - faces.up.uv[1];
        faces.up.uv[2] = 16 - faces.up.uv[2];
        faces.up.uv[3] = 16 - faces.up.uv[3];
    }
}
