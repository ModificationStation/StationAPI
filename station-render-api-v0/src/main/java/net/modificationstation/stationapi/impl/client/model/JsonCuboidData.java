package net.modificationstation.stationapi.impl.client.model;

import com.google.common.primitives.Doubles;
import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.block.Direction;

import java.util.*;

import static net.modificationstation.stationapi.api.block.Direction.DOWN;
import static net.modificationstation.stationapi.api.block.Direction.EAST;
import static net.modificationstation.stationapi.api.block.Direction.NORTH;
import static net.modificationstation.stationapi.api.block.Direction.SOUTH;
import static net.modificationstation.stationapi.api.block.Direction.UP;
import static net.modificationstation.stationapi.api.block.Direction.WEST;

@RequiredArgsConstructor
public class JsonCuboidData {

    public final double[] from;
    public final double[] to;
//    public final JsonFacesData faces;
    public final EnumMap<Direction, JsonFaceData> faces;

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

        faces.get(DOWN).rotation += 90;
        faces.get(UP).rotation += 90;

        faces.get(DOWN).rotation = (faces.get(DOWN).rotation / 90) % 4;
        faces.get(UP).rotation = (faces.get(UP).rotation / 90) % 4;
        faces.get(EAST).rotation = (faces.get(EAST).rotation / 90) % 4;
        faces.get(WEST).rotation = (faces.get(WEST).rotation / 90) % 4;
        faces.get(NORTH).rotation = (faces.get(NORTH).rotation / 90) % 4;
        faces.get(SOUTH).rotation = (faces.get(SOUTH).rotation / 90) % 4;

        double
                tmp,
                startU1 = faces.get(DOWN).localUVs[0],
                startV1 = faces.get(DOWN).localUVs[1],
                endU1 = faces.get(DOWN).localUVs[2],
                endV1 = faces.get(DOWN).localUVs[3];
        if (faces.get(DOWN).rotation % 2 == 0) {
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
        switch (faces.get(DOWN).rotation) {
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
        faces.get(DOWN).localUVs = new double[] {startU1, startV1, endU1, endV1, startU2, startV2, endU2, endV2};

        startU1 = faces.get(UP).localUVs[0];
        startV1 = faces.get(UP).localUVs[1];
        endU1 = faces.get(UP).localUVs[2];
        endV1 = faces.get(UP).localUVs[3];
        endU2 = endU1;
        startU2 = startU1;
        startV2 = startV1;
        endV2 = endV1;
        switch (faces.get(UP).rotation) {
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
        faces.get(UP).localUVs = new double[] {startU1, startV1, endU1, endV1, startU2, startV2, endU2, endV2};
    }
}
