package abilliontrillionstars.movesthemind.casting.actions.spells;

import abilliontrillionstars.movesthemind.Movesthemind;
import abilliontrillionstars.movesthemind.casting.FakeplayerUtils;
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
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster;
import at.petrak.hexcasting.api.casting.mishaps.MishapEntityTooFarAway;
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName;
import at.petrak.hexcasting.api.misc.MediaConstants;
import carpet.helpers.EntityPlayerActionPack;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OpSetSprint implements SpellAction
{
    public static final Action INSTANCE = new OpSetSprint();

    @Override
    public int getArgc() { return 2; }

    @Override
    public @NotNull SpellAction.Result executeWithUserdata(@NotNull List<? extends Iota> args, @NotNull CastingEnvironment env, @NotNull CompoundTag tags)
    {
        ServerPlayer target = OperatorUtils.getPlayer(args, 0, getArgc());
        if(!env.isEntityInRange(target))
            JavaMishapThrower.throwMishap(new MishapEntityTooFarAway(target));
        Entity caster = env.getCastingEntity();
        if(!(caster instanceof ServerPlayer))
            JavaMishapThrower.throwMishap(new MishapBadCaster());
        if(!FakeplayerUtils.canBid((ServerPlayer) caster, target))
            JavaMishapThrower.throwMishap(new MishapOthersName(target));
        boolean doesSprint = OperatorUtils.getBool(args, 1, getArgc());
        return new SpellAction.Result(new OpSetSprint.Spell(target, doesSprint),
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
        private boolean doesSprint;
        public Spell(ServerPlayer target, boolean doesSprint) {this.target = target; this.doesSprint = doesSprint;}

        @Override
        public void cast(@NotNull CastingEnvironment env)
        {
            MinecraftServer server = target.getServer();
            CommandSourceStack sourceStack = server.createCommandSourceStack();
            server.getCommands().performPrefixedCommand(sourceStack, "player "+FakeplayerUtils.getUsernameString(target)+(doesSprint ? " sprint":" unsprint"));
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
