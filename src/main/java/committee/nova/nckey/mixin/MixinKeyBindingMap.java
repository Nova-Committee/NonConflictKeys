package committee.nova.nckey.mixin;

import com.google.common.collect.Sets;
import committee.nova.nckey.api.IKeyBindingMap;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.client.settings.KeyModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Mixin(KeyBindingMap.class)
public class MixinKeyBindingMap implements IKeyBindingMap {
    @Shadow(remap = false)
    @Final
    private static EnumMap<KeyModifier, Map<InputMappings.Input, Collection<KeyBinding>>> map;

    @Override
    public Set<KeyBinding> lookupActives(InputMappings.Input key) {
        final KeyModifier activeModifier = KeyModifier.getActiveModifier();
        if (!activeModifier.matches(key)) {
            final Set<KeyBinding> bindings = getBindings(key, activeModifier);
            if (!bindings.isEmpty()) return bindings;
        }
        return getBindings(key, KeyModifier.NONE);
    }

    @Override
    public Set<KeyBinding> getBindings(InputMappings.Input key, KeyModifier keyModifier) {
        final Collection<KeyBinding> bindings = map.get(keyModifier).get(key);
        final Set<KeyBinding> toReturn = Sets.newHashSet();
        if (bindings == null) return toReturn;
        for (KeyBinding binding : bindings)
            if (binding.isActiveAndMatches(key))
                toReturn.add(binding);
        return toReturn;
    }
}
