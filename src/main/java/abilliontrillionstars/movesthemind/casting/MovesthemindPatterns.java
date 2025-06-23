package abilliontrillionstars.movesthemind.casting;

import abilliontrillionstars.movesthemind.Movesthemind;
import abilliontrillionstars.movesthemind.casting.actions.spells.*;
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

    public static final HexPattern CREATE = make("edeaqqwwawwqq", HexDir.NORTH_WEST, "create", OpCreateFakeplayer.INSTANCE);
    public static final HexPattern DESTROY = make("edeaqqwadawqq", HexDir.NORTH_WEST, "destroy", OpDestroyFakeplayer.INSTANCE);

    public static final HexPattern STOP_ALL = make("edeaqqwqqwqq", HexDir.NORTH_WEST, "move/stopall", OpStopAll.INSTANCE);
    public static final HexPattern LOOK = make("edeaqwa", HexDir.NORTH_WEST, "move/look", OpMadeYouLook.INSTANCE);
    public static final HexPattern WALK = make("edeqd", HexDir.NORTH_WEST, "move/walk", OpWalkAMileInTheseLouisVuittons.INSTANCE);
    public static final HexPattern STRAFE = make("edewa", HexDir.NORTH_WEST, "move/strafe", OpStrafeInAforementionedLouisVuittons.INSTANCE);
    public static final HexPattern JUMP = make("edeqda", HexDir.NORTH_WEST, "move/jump", OpJumpButWithYourFeet.INSTANCE);
    public static final HexPattern SNEAK = make("edeade", HexDir.NORTH_WEST, "move/sneak", OpSetSneak.INSTANCE);
    public static final HexPattern SPRINT = make("edeaqad", HexDir.NORTH_WEST, "move/sprint", OpSetSprint.INSTANCE);

    public static final HexPattern USE = make("edeaqwaaq", HexDir.NORTH_WEST, "move/use", OpUseItem.INSTANCE);
    public static final HexPattern ATTACK = make("edeaqedde", HexDir.NORTH_WEST, "move/attack", OpAttack.INSTANCE);

    public static final HexPattern HOTBAR = make("edeawq", HexDir.NORTH_WEST, "move/hotbar", OpSelectHotbar.INSTANCE);
    public static final HexPattern SWAP_HANDS = make("edeawqa", HexDir.NORTH_WEST, "move/swap_hands", OpSwapHands.INSTANCE);
    public static final HexPattern DROP = make("edeaqe", HexDir.NORTH_WEST, "move/drop", OpDropAndRoll.INSTANCE);


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