package abilliontrillionstars.movesthemind.casting.actions.spells;

import abilliontrillionstars.movesthemind.casting.JavaMishapThrower;
import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapEntityTooFarAway;
import at.petrak.hexcasting.api.misc.MediaConstants;
import carpet.helpers.EntityPlayerActionPack;
import com.mojang.brigadier.Command;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OpWalkAMileInTheseLouisVuittons implements SpellAction
{
    public static final Action INSTANCE = new OpWalkAMileInTheseLouisVuittons();

    @Override
    public int getArgc() { return 2; }

    @Override
    public @NotNull SpellAction.Result executeWithUserdata(@NotNull List<? extends Iota> args, @NotNull CastingEnvironment env, @NotNull CompoundTag tags)
    {
        ServerPlayer target = OperatorUtils.getPlayer(args, 0, getArgc());
        try { env.assertEntityInRange(target); }
        catch(MishapEntityTooFarAway e)
        {
            JavaMishapThrower.throwMishap(new MishapEntityTooFarAway(target));
        }
        boolean doesWalk = OperatorUtils.getBool(args, 1, getArgc());
        return new SpellAction.Result(new OpWalkAMileInTheseLouisVuittons.Spell(target, doesWalk),
                MediaConstants.DUST_UNIT,
                List.of(ParticleSpray.burst(target.position().add(0.0, target.getEyeHeight() / 2.0, 0.0), 1.0, 10)),
                1);
    }

    @Override
    public boolean hasCastingSound(@NotNull CastingEnvironment castingEnvironment) { return true; }
    @Override
    public boolean awardsCastingStat(@NotNull CastingEnvironment castingEnvironment) { return true; }
    @Override
    public @NotNull SpellAction.Result execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment)
    {
        throw new IllegalStateException();
    }

    private class Spell implements RenderedSpell
    {
        private ServerPlayer target;
        private boolean doesWalk;
        public Spell(ServerPlayer target, boolean doesWalk) {this.target = target; this.doesWalk = doesWalk;}

        @Override
        public void cast(@NotNull CastingEnvironment env)
        {
            EntityPlayerActionPack actor = new EntityPlayerActionPack(this.target);
            actor.setForward(this.doesWalk ? 1F:0F);
        }

        @Override
        public @Nullable CastingImage cast(@NotNull CastingEnvironment env, @NotNull CastingImage castingImage)
        {
            cast(env);
            return castingImage;
        }
    }


    @Override
    public @NotNull OperationResult operate(@NotNull CastingEnvironment castingEnvironment, @NotNull CastingImage castingImage, @NotNull SpellContinuation spellContinuation)
    {
        return SpellAction.DefaultImpls.operate(this, castingEnvironment, castingImage, spellContinuation);
    }
}