package committee.nova.nckey.mixin;

import committee.nova.nckey.api.IKeyBinding;
import committee.nova.nckey.api.IKeyBindingMap;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyBindingMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding implements IKeyBinding {
    @Shadow
    @Final
    private static KeyBindingMap HASH;

    @Shadow
    private int pressTime;

    @Inject(method = "onTick", at = @At("HEAD"), cancellable = true)
    private static void inject$onTick(int keyCode, CallbackInfo ci) {
        ci.cancel();
        if (keyCode != 0) ((IKeyBindingMap) HASH).lookupActives(keyCode).forEach(k -> ((IKeyBinding) k).press());
    }

    @Override
    public void press() {
        pressTime++;
    }
}
