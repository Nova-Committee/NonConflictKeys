package committee.nova.nckey.api;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyModifier;

import java.util.Set;

public interface ILookup {
    Set<KeyMapping> nckey$lookupActives(InputConstants.Key key);

    Set<KeyMapping> nckey$getBindings(InputConstants.Key key, KeyModifier keyModifier);
}
