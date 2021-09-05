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

        faces.down.rotation += 90;
        faces.up.rotation += 90;

        faces.down.rotation = (faces.down.rotation / 90) % 4;
        faces.up.rotation = (faces.up.rotation / 90) % 4;
        faces.east.rotation = (faces.east.rotation / 90) % 4;
        faces.west.rotation = (faces.west.rotation / 90) % 4;
        faces.north.rotation = (faces.north.rotation / 90) % 4;
        faces.south.rotation = (faces.south.rotation / 90) % 4;

        double
                tmp,
                startU1 = faces.down.localUVs[0],
                startV1 = faces.down.localUVs[1],
                endU1 = faces.down.localUVs[2],
                endV1 = faces.down.localUVs[3];
        if (faces.down.rotation % 2 == 0) {
            tmp = startV1;
            startV1 = endV1;
            endV1 = tmp;
        } else {
            tmp = startU1;
            startU1 = endU1;
            endU1 = tmp;
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (faces.down.rotation) {
            case 1:
                startV2 = startV1;
                endV2 = endV1;
                endU2 = startU1;
                startU2 = endU1;
                startV1 = endV1;
                endV1 = startV2;
                break;
            case 2:
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
                break;
            case 3:
                endU2 = endU1;
                startU2 = startU1;
                startU1 = endU1;
                endU1 = startU2;
                startV2 = endV1;
                endV2 = startV1;
                break;
        }
        faces.down.localUVs = new double[] {startU1, startV1, endU1, endV1, startU2, startV2, endU2, endV2};

        startU1 = faces.up.localUVs[0];
        startV1 = faces.up.localUVs[1];
        endU1 = faces.up.localUVs[2];
        endV1 = faces.up.localUVs[3];
        endU2 = endU1;
        startU2 = startU1;
        startV2 = startV1;
        endV2 = endV1;
        switch (faces.up.rotation) {
            case 1:
                endU2 = endU1;
                startU2 = startU1;
                startU1 = endU1;
                endU1 = startU2;
                startV2 = endV1;
                endV2 = startV1;
                break;
            case 2:
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
                break;
            case 3:
                startV2 = startV1;
                endV2 = endV1;
                endU2 = startU1;
                startU2 = endU1;
                startV1 = endV1;
                endV1 = startV2;
                break;
        }
        faces.up.localUVs = new double[] {startU1, startV1, endU1, endV1, startU2, startV2, endU2, endV2};
    }
}
