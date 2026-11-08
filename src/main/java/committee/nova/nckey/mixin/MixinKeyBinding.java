package committee.nova.nckey.mixin;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.nckey.api.IKeyBinding;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

/**
 * 26.3 port (Mojang mappings — Yarn ends at 1.21.11, see PORTING.md).
 *
 * <p>Note: vanilla 26.x {@code KeyMapping} already keeps its own
 * {@code MAP} multimap and {@code click()}/{@code set()} already fire
 * every binding sharing a key. This mixin faithfully ports the original
 * 1.19.x logic (cancel + fire all from our own map) so the mod's
 * behaviour is explicit even if vanilla changes again. Original code
 * by Tapio / Nova-Committee, AGPL-3.0-only.
 */
@Mixin(KeyMapping.class)
public class MixinKeyBinding implements IKeyBinding {
    @Shadow
    private int clickCount;

    @Shadow
    protected InputConstants.Key key;

    @Shadow
    @Final
    private static Map<String, KeyMapping> ALL;

    private static final Multimap<InputConstants.Key, KeyMapping> newMap = ArrayListMultimap.create();

    @Inject(method = "<init>(Ljava/lang/String;Lcom/mojang/blaze3d/platform/InputConstants$Type;ILnet/minecraft/client/KeyMapping$Category;)V", at = @At("TAIL"))
    private void inject$init(String name, InputConstants.Type type, int value, KeyMapping.Category category, CallbackInfo ci) {
        newMap.put(key, (KeyMapping) (Object) this);
    }

    @Inject(method = "click", at = @At("HEAD"), cancellable = true)
    private static void inject$click(InputConstants.Key key, CallbackInfo ci) {
        ci.cancel();
        newMap.get(key).forEach(k -> ((IKeyBinding) k).press());
    }

    @Inject(method = "set", at = @At("HEAD"), cancellable = true)
    private static void inject$set(InputConstants.Key key, boolean state, CallbackInfo ci) {
        ci.cancel();
        newMap.get(key).forEach(k -> k.setDown(state));
    }

    @Inject(method = "resetMapping", at = @At("HEAD"))
    private static void inject$resetMapping(CallbackInfo ci) {
        newMap.clear();
        ALL.values().forEach(k -> newMap.put(((AccessorKeyBinding) k).getBoundKey(), k));
    }

    @Override
    public void press() {
        clickCount++;
    }
}
