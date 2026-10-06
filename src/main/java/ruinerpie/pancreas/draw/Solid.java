package ruinerpie.pancreas.draw;

import ruinerpie.pancreas.kit.Facing;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

public class Solid {
    public static final Solid INSTANCE = new Solid();

    public void box(AABB box, Color sideColor, Color lineColor, Shape shapeMode, int exclude) {
        if (box == null) return;
        box(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, sideColor, lineColor, shapeMode, exclude);
    }

    public void box(BlockPos pos, Color sideColor, Color lineColor, Shape shapeMode, int exclude) {
        if (pos == null) return;
        box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, sideColor, lineColor, shapeMode, exclude);
    }

    public void box(double x1, double y1, double z1, double x2, double y2, double z2, Color sideColor, Color lineColor, Shape shapeMode, int exclude) {
        if (shapeMode == null) return;
        double minX = Math.min(x1, x2);
        double minY = Math.min(y1, y2);
        double minZ = Math.min(z1, z2);
        double maxX = Math.max(x1, x2);
        double maxY = Math.max(y1, y2);
        double maxZ = Math.max(z1, z2);

        Vec3 cam = getCameraPos();
        double rx1 = minX - cam.x;
        double ry1 = minY - cam.y;
        double rz1 = minZ - cam.z;
        double rx2 = maxX - cam.x;
        double ry2 = maxY - cam.y;
        double rz2 = maxZ - cam.z;

        if (shapeMode.sides() && sideColor != null && sideColor.a > 0) {
            drawBoxSides(rx1, ry1, rz1, rx2, ry2, rz2, sideColor, exclude);
        }

        if (shapeMode.lines() && lineColor != null && lineColor.a > 0) {
            drawBoxLines(rx1, ry1, rz1, rx2, ry2, rz2, lineColor, exclude);
        }
    }

    public void line(double x1, double y1, double z1, double x2, double y2, double z2, Color color) {
        if (color == null || color.a <= 0) return;
        Vec3 cam = getCameraPos();
        double rx1 = x1 - cam.x;
        double ry1 = y1 - cam.y;
        double rz1 = z1 - cam.z;
        double rx2 = x2 - cam.x;
        double ry2 = y2 - cam.y;
        double rz2 = z2 - cam.z;

        drawRawLine(rx1, ry1, rz1, rx2, ry2, rz2, color);
    }

    public void sideHorizontal(double x1, double y, double z1, double x2, double z2, Color sideColor, Color lineColor, Shape shapeMode) {
        box(x1, y, z1, x2, y, z2, sideColor, lineColor, shapeMode, Facing.NORTH | Facing.SOUTH | Facing.WEST | Facing.EAST);
    }

    public void sideVertical(double x1, double y1, double z1, double x2, double y2, double z2, Color sideColor, Color lineColor, Shape shapeMode) {
        box(x1, y1, z1, x2, y2, z2, sideColor, lineColor, shapeMode, Facing.UP | Facing.DOWN);
    }

    private Vec3 getCameraPos() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameRenderer != null && mc.gameRenderer.getMainCamera() != null) {
            return mc.gameRenderer.getMainCamera().position();
        }
        if (mc.player != null) {
            return mc.player.position();
        }
        return Vec3.ZERO;
    }

    private void drawBoxSides(double x1, double y1, double z1, double x2, double y2, double z2, Color c, int exclude) {
        try {
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

            if ((exclude & Facing.DOWN) == 0) {
                vertex(builder, x1, y1, z1, c);
                vertex(builder, x2, y1, z1, c);
                vertex(builder, x2, y1, z2, c);
                vertex(builder, x1, y1, z2, c);
            }
            if ((exclude & Facing.UP) == 0) {
                vertex(builder, x1, y2, z2, c);
                vertex(builder, x2, y2, z2, c);
                vertex(builder, x2, y2, z1, c);
                vertex(builder, x1, y2, z1, c);
            }
            if ((exclude & Facing.NORTH) == 0) {
                vertex(builder, x1, y1, z1, c);
                vertex(builder, x1, y2, z1, c);
                vertex(builder, x2, y2, z1, c);
                vertex(builder, x2, y1, z1, c);
            }
            if ((exclude & Facing.SOUTH) == 0) {
                vertex(builder, x2, y1, z2, c);
                vertex(builder, x2, y2, z2, c);
                vertex(builder, x1, y2, z2, c);
                vertex(builder, x1, y1, z2, c);
            }
            if ((exclude & Facing.WEST) == 0) {
                vertex(builder, x1, y1, z2, c);
                vertex(builder, x1, y2, z2, c);
                vertex(builder, x1, y2, z1, c);
                vertex(builder, x1, y1, z1, c);
            }
            if ((exclude & Facing.EAST) == 0) {
                vertex(builder, x2, y1, z1, c);
                vertex(builder, x2, y2, z1, c);
                vertex(builder, x2, y2, z2, c);
                vertex(builder, x2, y1, z2, c);
            }

            var meshData = builder.buildOrThrow();
            if (meshData != null) {
                try {
                    java.lang.reflect.Method draw = Class.forName("com.mojang.blaze3d.vertex.BufferUploader").getMethod("drawWithShader", meshData.getClass());
                    draw.invoke(null, meshData);
                } catch (Exception e) {
                    meshData.close();
                }
            }
        } catch (Exception ignored) {}
    }

    private void drawBoxLines(double x1, double y1, double z1, double x2, double y2, double z2, Color c, int exclude) {
        try {
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder builder = tesselator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

            if ((exclude & (Facing.DOWN | Facing.NORTH)) == 0) lineVertices(builder, x1, y1, z1, x2, y1, z1, c);
            if ((exclude & (Facing.DOWN | Facing.EAST)) == 0) lineVertices(builder, x2, y1, z1, x2, y1, z2, c);
            if ((exclude & (Facing.DOWN | Facing.SOUTH)) == 0) lineVertices(builder, x2, y1, z2, x1, y1, z2, c);
            if ((exclude & (Facing.DOWN | Facing.WEST)) == 0) lineVertices(builder, x1, y1, z2, x1, y1, z1, c);

            if ((exclude & (Facing.UP | Facing.NORTH)) == 0) lineVertices(builder, x1, y2, z1, x2, y2, z1, c);
            if ((exclude & (Facing.UP | Facing.EAST)) == 0) lineVertices(builder, x2, y2, z1, x2, y2, z2, c);
            if ((exclude & (Facing.UP | Facing.SOUTH)) == 0) lineVertices(builder, x2, y2, z2, x1, y2, z2, c);
            if ((exclude & (Facing.UP | Facing.WEST)) == 0) lineVertices(builder, x1, y2, z2, x1, y2, z1, c);

            if ((exclude & (Facing.NORTH | Facing.WEST)) == 0) lineVertices(builder, x1, y1, z1, x1, y2, z1, c);
            if ((exclude & (Facing.NORTH | Facing.EAST)) == 0) lineVertices(builder, x2, y1, z1, x2, y2, z1, c);
            if ((exclude & (Facing.SOUTH | Facing.EAST)) == 0) lineVertices(builder, x2, y1, z2, x2, y2, z2, c);
            if ((exclude & (Facing.SOUTH | Facing.WEST)) == 0) lineVertices(builder, x1, y1, z2, x1, y2, z2, c);

            var meshData = builder.buildOrThrow();
            if (meshData != null) {
                try {
                    java.lang.reflect.Method draw = Class.forName("com.mojang.blaze3d.vertex.BufferUploader").getMethod("drawWithShader", meshData.getClass());
                    draw.invoke(null, meshData);
                } catch (Exception e) {
                    meshData.close();
                }
            }
        } catch (Exception ignored) {}
    }

    private void drawRawLine(double x1, double y1, double z1, double x2, double y2, double z2, Color c) {
        try {
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder builder = tesselator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
            lineVertices(builder, x1, y1, z1, x2, y2, z2, c);
            var meshData = builder.buildOrThrow();
            if (meshData != null) {
                try {
                    java.lang.reflect.Method draw = Class.forName("com.mojang.blaze3d.vertex.BufferUploader").getMethod("drawWithShader", meshData.getClass());
                    draw.invoke(null, meshData);
                } catch (Exception e) {
                    meshData.close();
                }
            }
        } catch (Exception ignored) {}
    }

    private void lineVertices(BufferBuilder builder, double x1, double y1, double z1, double x2, double y2, double z2, Color c) {
        vertex(builder, x1, y1, z1, c);
        vertex(builder, x2, y2, z2, c);
    }

    private void vertex(BufferBuilder builder, double x, double y, double z, Color c) {
        builder.addVertex((float) x, (float) y, (float) z).setColor(c.r, c.g, c.b, c.a);
    }
}
