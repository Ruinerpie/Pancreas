package ruinerpie.pancreas.mixin;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;
import ruinerpie.pancreas.saved.*;
import ruinerpie.pancreas.stalk.*;
import ruinerpie.pancreas.theme.*;
import ruinerpie.pancreas.screens.*;
import ruinerpie.pancreas.screens.widgets.*;
import ruinerpie.pancreas.hud.*;
import ruinerpie.pancreas.hud.parts.*;
import ruinerpie.pancreas.tweaks.cheats.*;
import ruinerpie.pancreas.tweaks.extras.*;
import ruinerpie.pancreas.tweaks.misc.*;
import ruinerpie.pancreas.tweaks.utilities.*;

import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Matrix4fc;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelMixin {
    @Inject(method = "renderLevel", at = @At("RETURN"))
    private void onRenderLevel(GraphicsResourceAllocator graphicsResourceAllocator, DeltaTracker deltaTracker, boolean renderBlockOutline, CameraRenderState cameraRenderState, Matrix4fc matrix4fc, com.mojang.blaze3d.buffers.GpuBufferSlice gpuBufferSlice, Vector4f vector4f, boolean bl2, ChunkSectionsToRender chunkSectionsToRender, CallbackInfo info) {
        float tickDelta = deltaTracker.getGameTimeDeltaPartialTick(false);
        Render3D event = new Render3D(tickDelta);
        Pancreas.EVENT_BUS.post(event);
    }
}