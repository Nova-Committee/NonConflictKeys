package committee.nova.nckey.mixin;

import com.google.common.collect.Sets;
import committee.nova.nckey.api.IKeyBindingMap;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.IntHashMap;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.client.settings.KeyModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Set;

@Mixin(KeyBindingMap.class)
public abstract class MixinKeyBindingMap implements IKeyBindingMap {
    @Shadow(remap = false)
    @Final
    private static EnumMap<KeyModifier, IntHashMap<Collection<KeyBinding>>> map;

    @Override
    public Set<KeyBinding> lookupActives(int keyCode) {
        final KeyModifier activeModifier = KeyModifier.getActiveModifier();
        if (!activeModifier.matches(keyCode)) {
            final Set<KeyBinding> bindings = getBindings(keyCode, activeModifier);
            if (!bindings.isEmpty()) return bindings;
        }
        return getBindings(keyCode, KeyModifier.NONE);
    }

    @Override
    public Set<KeyBinding> getBindings(int keyCode, KeyModifier keyModifier) {
        final Collection<KeyBinding> bindings = map.get(keyModifier).lookup(keyCode);
        final Set<KeyBinding> toReturn = Sets.newHashSet();
        if (bindings == null) return toReturn;
        for (KeyBinding binding : bindings)
            if (binding.isActiveAndMatches(keyCode))
                toReturn.add(binding);
        return toReturn;
    }
}
