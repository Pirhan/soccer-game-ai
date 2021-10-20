package pison.soccergame.utils;

import math.geom2d.Vector2D;

public class VectorUtils {
    private static final double MinNorm = 0.000001;

    public static Vector2D scale(Vector2D v, double max) {
        if (v.norm() <= max)
            return v.clone();
        else {
            Vector2D scaled = v.normalize();
            scaled = scaled.times(max);
            return scaled;
        }
    }

    public static double distance(Vector2D node, Vector2D position) {
        return node.minus(position).norm();
    }

    public static Vector2D normalize(Vector2D v) {
        if (v.norm() < MinNorm)
            return new Vector2D(0, 0);
        return v.normalize();

    }
}
