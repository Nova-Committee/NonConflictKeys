package io.github.vulpesstella.nckeys.mixin;

import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * In 1.6.4 {@code KeyBinding.hash} can only hold a single binding per key code,
 * so vanilla {@code setKeyBindState}/{@code onTick} only see one of them.
 * {@code KeyBinding.keybindArray} always contains every binding, so we scan it
 * and compare against each binding's <em>current</em> {@code keyCode}.
 * <p>
 * Scanning is important: the controls GUI and {@code options.txt} assign
 * {@code keyCode} directly (and {@code KeyBinding} has no setter to hook),
 * so any map built once in the constructor would go stale as soon as the
 * player rebinds a key.
 */
@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding {

    @Inject(method = "setKeyBindState", at = @At("HEAD"), cancellable = true)
    private static void nckeys$setKeyBindState(int keyCode, boolean pressed, CallbackInfo ci) {
        ci.cancel();
        for (Object element : KeyBinding.keybindArray) {
            KeyBinding binding = (KeyBinding) element;
            if (binding.keyCode == keyCode) {
                binding.pressed = pressed;
            }
        }
    }

    @Inject(method = "onTick", at = @At("HEAD"), cancellable = true)
    private static void nckeys$onTick(int keyCode, CallbackInfo ci) {
        ci.cancel();
        for (Object element : KeyBinding.keybindArray) {
            KeyBinding binding = (KeyBinding) element;
            if (binding.keyCode == keyCode) {
                ++binding.pressTime;
            }
        }
    }
}
