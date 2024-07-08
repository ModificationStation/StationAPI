package net.modificationstation.stationapi.impl.block;

import net.modificationstation.stationapi.api.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class Line {
    public Vec3d[] points;

    Line(Vec3d a, Vec3d b) {
        points = new Vec3d[]{a, b};
    }

    public void expand(double d, Vec3d center) {
        points[0] = movePointAwayFromCenter(points[0], center, d);
        points[1] = movePointAwayFromCenter(points[1], center, d);
    }

    private static Vec3d movePointAwayFromCenter(Vec3d point, Vec3d center, double distance) {
        Vec3d direction = point.subtract(center).normalize();
        Vec3d displacement = direction.multiply(distance);
        return point.add(displacement);
    }

    public boolean coincidesWith(Line other) {
        return (this.points[0].equals(other.points[0]) && this.points[1].equals(other.points[1])) ||
                (this.points[0].equals(other.points[1]) && this.points[1].equals(other.points[0]));
    }

    public boolean partiallyCoincidesWith(Line other) {
        return this.containsPoint(other.points[0]) && this.containsPoint(other.points[1]) ||
                other.containsPoint(this.points[0]) && other.containsPoint(this.points[1]);
    }

    private boolean containsPoint(Vec3d point) {
        double minX = Math.min(this.points[0].x, this.points[1].x);
        double maxX = Math.max(this.points[0].x, this.points[1].x);
        double minY = Math.min(this.points[0].y, this.points[1].y);
        double maxY = Math.max(this.points[0].y, this.points[1].y);
        double minZ = Math.min(this.points[0].z, this.points[1].z);
        double maxZ = Math.max(this.points[0].z, this.points[1].z);

        return (point.x >= minX && point.x <= maxX) &&
                (point.y >= minY && point.y <= maxY) &&
                (point.z >= minZ && point.z <= maxZ);
    }

    public Line[] getNonOverlappingParts(Line other) {
        List<Line> nonOverlappingParts = new ArrayList<>();

        Vec3d start = this.points[0];
        Vec3d end = this.points[1];

        if (this.containsPoint(other.points[0]) && this.containsPoint(other.points[1])) {
            // Other line is completely inside this line, split this line into two parts
            if (!start.equals(other.points[0])) {
                nonOverlappingParts.add(new Line(start, other.points[0]));
            }
            if (!end.equals(other.points[1])) {
                nonOverlappingParts.add(new Line(other.points[1], end));
            }
        } else if (this.containsPoint(other.points[0])) {
            // Other line overlaps at the start of this line
            nonOverlappingParts.add(new Line(start, other.points[0]));
            nonOverlappingParts.add(new Line(other.points[0], end));
        } else if (this.containsPoint(other.points[1])) {
            // Other line overlaps at the end of this line
            nonOverlappingParts.add(new Line(start, other.points[1]));
            nonOverlappingParts.add(new Line(other.points[1], end));
        } else {
            // This line is completely inside other line
            if (!other.points[0].equals(start)) {
                nonOverlappingParts.add(new Line(other.points[0], start));
            }
            if (!other.points[1].equals(end)) {
                nonOverlappingParts.add(new Line(end, other.points[1]));
            }
        }

        return nonOverlappingParts.toArray(new Line[0]);
    }
}