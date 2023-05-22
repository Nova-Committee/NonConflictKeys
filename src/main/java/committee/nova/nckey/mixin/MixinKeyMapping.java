package committee.nova.nckey.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.nckey.api.IKeyBindingMap;
import committee.nova.nckey.api.IKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyBindingMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyMapping.class)
public class MixinKeyMapping implements IKeyMapping {
    @Shadow
    @Final
    private static KeyBindingMap MAP;

    @Shadow
    private int clickCount;

    @Inject(method = "click", at = @At("HEAD"), cancellable = true)
    private static void inject$onTick(InputConstants.Key key, CallbackInfo ci) {
        ci.cancel();
        ((IKeyBindingMap) MAP).lookupActives(key).forEach(k -> ((IKeyMapping) k).press());
    }

    @Override
    public void press() {
        clickCount++;
    }
}
