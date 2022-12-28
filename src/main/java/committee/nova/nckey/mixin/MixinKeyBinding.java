package committee.nova.nckey.mixin;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import committee.nova.nckey.api.IKeyBinding;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(KeyBinding.class)
public class MixinKeyBinding implements IKeyBinding {
    @Shadow
    private int timesPressed;

    @Shadow
    private InputUtil.Key boundKey;
    @Shadow
    @Final
    private static Map<String, KeyBinding> KEYS_BY_ID;
    private static final Multimap<InputUtil.Key, KeyBinding> newMap = ArrayListMultimap.create();

    @Inject(method = "<init>(Ljava/lang/String;Lnet/minecraft/client/util/InputUtil$Type;ILjava/lang/String;)V", at = @At("TAIL"))
    private void inject$init(String translationKey, InputUtil.Type type, int code, String category, CallbackInfo ci) {
        newMap.put(boundKey, (KeyBinding) (Object) this);
    }

    @Inject(method = "onKeyPressed", at = @At("HEAD"), cancellable = true)
    private static void inject$onKeyPressed(InputUtil.Key key, CallbackInfo ci) {
        ci.cancel();
        newMap.get(key).forEach(k -> ((IKeyBinding) k).press());
    }

    @Inject(method = "setKeyPressed", at = @At("HEAD"), cancellable = true)
    private static void inject$setKeyPressed(InputUtil.Key key, boolean pressed$, CallbackInfo ci) {
        ci.cancel();
        newMap.get(key).forEach(k -> k.setPressed(pressed$));
    }

    @Inject(method = "updateKeysByCode", at = @At("HEAD"))
    private static void inject$updateKeysByCode(CallbackInfo ci) {
        newMap.clear();
        KEYS_BY_ID.values().forEach(k -> newMap.put(((AccessorKeyBinding) k).getBoundKey(), k));
    }

    @Override
    public void press() {
        timesPressed++;
    }
}
