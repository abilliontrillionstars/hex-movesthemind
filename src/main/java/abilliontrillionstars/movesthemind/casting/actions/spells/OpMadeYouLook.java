package abilliontrillionstars.movesthemind.casting.actions.spells;

import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpMadeYouLook implements ConstMediaAction
{
    public static final Action INSTANCE = new OpMadeYouLook();

    @Override
    public int getArgc() { return 1; }
    @Override
    public long getMediaCost() { return 0; }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment)
    {
        return List.of();
    }

    @Override
    public @NotNull CostMediaActionResult executeWithOpCount(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment)
    {
        return null;
    }

    @Override
    public @NotNull OperationResult operate(@NotNull CastingEnvironment castingEnvironment, @NotNull CastingImage castingImage, @NotNull SpellContinuation spellContinuation)
    {
        return null;
    }
}
