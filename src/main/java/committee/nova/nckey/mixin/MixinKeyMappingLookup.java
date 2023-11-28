package committee.nova.nckey.mixin;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.nckey.api.ILookup;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyMappingLookup;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Mixin(KeyMappingLookup.class)
public class MixinKeyMappingLookup implements ILookup {
    @Shadow(remap = false)
    @Final
    private static EnumMap<KeyModifier, Map<InputConstants.Key, Collection<KeyMapping>>> map;

    @Override
    public Set<KeyMapping> nckey$lookupActives(InputConstants.Key key) {
        final KeyModifier activeModifier = KeyModifier.getActiveModifier();
        if (!activeModifier.matches(key)) {
            final Set<KeyMapping> bindings = nckey$getBindings(key, activeModifier);
            if (!bindings.isEmpty()) return bindings;
        }
        return nckey$getBindings(key, KeyModifier.NONE);
    }

    @Override
    public Set<KeyMapping> nckey$getBindings(InputConstants.Key key, KeyModifier keyModifier) {
        final Collection<KeyMapping> bindings = map.get(keyModifier).get(key);
        final Set<KeyMapping> toReturn = Sets.newHashSet();
        if (bindings == null) return toReturn;
        for (KeyMapping binding : bindings)
            if (binding.isActiveAndMatches(key))
                toReturn.add(binding);
        return toReturn;
    }
}
