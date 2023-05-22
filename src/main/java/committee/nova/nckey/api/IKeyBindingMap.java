package committee.nova.nckey.api;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.Set;

public interface IKeyBindingMap {
    Set<KeyBinding> lookupActives(int keyCode);

    Set<KeyBinding> getBindings(int keyCode, KeyModifier keyModifier);
}
