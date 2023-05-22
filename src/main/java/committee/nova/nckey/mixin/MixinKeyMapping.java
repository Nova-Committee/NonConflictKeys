package committee.nova.nckey.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.nckey.api.IKeyMapping;
import committee.nova.nckey.api.ILookup;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyMappingLookup;
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
    private static KeyMappingLookup MAP;

    @Shadow
    private int clickCount;

    @Inject(method = "click", at = @At("HEAD"), cancellable = true)
    private static void inject$onTick(InputConstants.Key key, CallbackInfo ci) {
        ci.cancel();
        ((ILookup) MAP).lookupActives(key).forEach(k -> ((IKeyMapping) k).press());
    }

    @Override
    public void press() {
        clickCount++;
    }
}
