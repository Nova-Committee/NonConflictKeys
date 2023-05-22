package committee.nova.nckey.api;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.Set;

public interface ILookup {
    Set<KeyMapping> lookupActives(InputConstants.Key key);

    Set<KeyMapping> getBindings(InputConstants.Key key, KeyModifier keyModifier);
}
