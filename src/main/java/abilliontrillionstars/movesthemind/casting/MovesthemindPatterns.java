package abilliontrillionstars.movesthemind.casting;

import abilliontrillionstars.movesthemind.Movesthemind;
import abilliontrillionstars.movesthemind.casting.actions.spells.OpMadeYouLook;
import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.castables.OperationAction;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class MovesthemindPatterns
{
    private static final Map<ResourceLocation, ActionRegistryEntry> PATTERNS = new LinkedHashMap<>();

    public static void register() {
        for (Map.Entry<ResourceLocation, ActionRegistryEntry> entry : PATTERNS.entrySet())
            Registry.register(HexActions.REGISTRY, entry.getKey(), entry.getValue());
    }

    public static final HexPattern LOOK = make("qaqqqwa", HexDir.NORTH_EAST, "move/look", OpMadeYouLook.INSTANCE);


    private static HexPattern make(String signature, HexDir dir, String name, Action act ) {
        PATTERNS.put(
                new ResourceLocation(Movesthemind.MOD_ID, name),
                new ActionRegistryEntry(HexPattern.fromAngles(signature, dir), act)
        );
        return HexPattern.fromAngles(signature, dir);
    }
    private static HexPattern make(String signature, HexDir dir, String name) {
        HexPattern pattern = HexPattern.fromAngles(signature, dir);
        PATTERNS.put(
                new ResourceLocation(Movesthemind.MOD_ID, name),
                new ActionRegistryEntry(pattern, new OperationAction(pattern))
        );
        return pattern;
    }
}