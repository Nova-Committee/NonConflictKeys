package committee.nova.nckey.api;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.Set;

public interface IKeyBindingMap {
    Set<KeyBinding> lookupActives(InputMappings.Input key);

    Set<KeyBinding> getBindings(InputMappings.Input key, KeyModifier keyModifier);
}
