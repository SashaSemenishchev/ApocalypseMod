package me.mrfunny.apocalypsemodfabric.mixin;

import me.mrfunny.apocalypsemodfabric.client.ApocalypseModClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {
    @Shadow protected abstract void renderSpyglassOverlay(float scale);

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getLastFrameDuration()F"))
    public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo callbackInfo) {
        if(ApocalypseModClient.scopeMode == 1){
            this.renderSpyglassOverlay(1.125F);
        }
    }
}
