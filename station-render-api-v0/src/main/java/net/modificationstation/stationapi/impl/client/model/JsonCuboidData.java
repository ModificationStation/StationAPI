package net.modificationstation.stationapi.impl.client.model;

import com.google.common.primitives.Doubles;
import lombok.Getter;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.*;

import static net.modificationstation.stationapi.api.util.math.Direction.DOWN;
import static net.modificationstation.stationapi.api.util.math.Direction.UP;

public class JsonCuboidData {

    public final double[] from = Null.get();
    public final double[] to = Null.get();
    public final EnumMap<Direction, JsonFaceData> faces = Null.get();
    @SuppressWarnings("FieldMayBeFinal")
    @Getter
    private boolean shade = true;

    public void postprocess() {
        Collections.reverse(Doubles.asList(from));
        Collections.reverse(Doubles.asList(to));
        double fromTmp = from[2];
        from[2] = 16 - to[2];
        to[2] = 16 - fromTmp;

        faces.forEach((direction, face) -> {
            if (face.localUVs == null)
                face.localUVs = getRotatedMatrix(direction);
        });

        from[0] /= 16;
        from[1] /= 16;
        from[2] /= 16;
        to[0] /= 16;
        to[1] /= 16;
        to[2] /= 16;

        faces.forEach((direction, face) -> {
            if (direction == DOWN || direction == UP)
                face.rotation += 90;
            face.rotation = (face.rotation / 90) % 4;
            double
                    tmp,
                    startU1 = face.localUVs[0],
                    startV1 = face.localUVs[1],
                    endU1 = face.localUVs[2],
                    endV1 = face.localUVs[3];
            switch (direction) {
                case DOWN:
                    if (face.rotation % 2 == 0) {
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
                    switch (face.rotation) {
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
                    face.localUVs = new double[] {startU1, startV1, endU1, endV1, startU2, startV2, endU2, endV2};
                    break;
                case UP:
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
                    face.localUVs = new double[] {startU1, startV1, endU1, endV1, startU2, startV2, endU2, endV2};
                    break;
            }
        });
    }

    private double[] getRotatedMatrix(Direction direction) {
        switch(direction) {
            case DOWN:
                return new double[]{this.from[0], 16 - this.to[2], this.to[0], 16 - this.from[2]};
            case UP:
                return new double[]{this.from[0], this.from[2], this.to[0], this.to[2]};
            case NORTH:
            default:
                return new double[]{16 - this.to[0], 16 - this.to[1], 16 - this.from[0], 16 - this.from[1]};
            case SOUTH:
                return new double[]{this.from[0], 16 - this.to[1], this.to[0], 16 - this.from[1]};
            case WEST:
                return new double[]{this.from[2], 16 - this.to[1], this.to[2], 16 - this.from[1]};
            case EAST:
                return new double[]{16 - this.to[2], 16 - this.to[1], 16 - this.from[2], 16 - this.from[1]};
        }
    }
}
