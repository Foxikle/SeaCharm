package dev.foxikle.seacharm.utils;

import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;

public class Util {
    public static Matrix4f composeMatrixFromTransformation(Transformation t) {
        Matrix4f matrix4f = new Matrix4f();

        matrix4f.translation(t.getTranslation());
        matrix4f.rotate(t.getLeftRotation());
        matrix4f.scale(t.getScale());
        matrix4f.rotate(t.getRightRotation());

        return matrix4f;
    }

    public static Vector yawToVector(double yaw) {
        yaw = Math.abs(yaw);
        yaw %= 360;

        double rads = Math.toRadians(yaw);

        return new Vector(-Math.sin(rads), 0, Math.cos(rads));
    }
}
