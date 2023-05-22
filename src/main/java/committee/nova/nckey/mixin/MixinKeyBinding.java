package committee.nova.nckey.mixin;

import committee.nova.nckey.api.IKeyBinding;
import committee.nova.nckey.api.IKeyBindingMap;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyBindingMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public class MixinKeyBinding implements IKeyBinding {
    @Shadow
    private int pressTime;

    @Shadow
    @Final
    private static KeyBindingMap HASH;

    @Inject(method = "onTick", at = @At("HEAD"), cancellable = true)
    private static void inject$onTick(InputMappings.Input key, CallbackInfo ci) {
        ci.cancel();
        ((IKeyBindingMap) HASH).lookupActives(key).forEach(k -> ((IKeyBinding) k).press());
    }

    @Override
    public void press() {
        pressTime++;
    }
}
