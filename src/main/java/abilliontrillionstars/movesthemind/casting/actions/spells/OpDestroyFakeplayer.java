package abilliontrillionstars.movesthemind.casting.actions.spells;

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
import at.petrak.hexcasting.api.casting.mishaps.*;
import at.petrak.hexcasting.api.misc.MediaConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OpDestroyFakeplayer implements SpellAction
{
    public static final Action INSTANCE = new OpDestroyFakeplayer();

    @Override
    public int getArgc() { return 1; }

    @Override
    public @NotNull SpellAction.Result executeWithUserdata(@NotNull List<? extends Iota> args, @NotNull CastingEnvironment env, @NotNull CompoundTag tags)
    {
        ServerPlayer player = OperatorUtils.getPlayer(args, 0, getArgc());
        if(!env.isEntityInRange(player))
            JavaMishapThrower.throwMishap(new MishapEntityTooFarAway(player));
        Entity caster = env.getCastingEntity();
        if(!(caster instanceof ServerPlayer))
            JavaMishapThrower.throwMishap(new MishapBadCaster());
        if(!FakeplayerUtils.canBid((ServerPlayer) caster, player))
            JavaMishapThrower.throwMishap(new MishapOthersName(player));

        if(caster.getStringUUID().equals(player.getStringUUID()) && caster.getClass() == ServerPlayer.class)
        {
            // easter egg joke advancement! I love modding.
            MinecraftServer server = env.getWorld().getServer();
            CommandSourceStack sourceStack = server.createCommandSourceStack().withSuppressedOutput();
            server.getCommands().performPrefixedCommand(sourceStack, "advancement grant "+FakeplayerUtils.getUsernameString((ServerPlayer) caster)+" only minecraft:movesthemind/try_banish_self");

            JavaMishapThrower.throwMishap(new MishapOthersName((ServerPlayer) caster));
        }
        return new SpellAction.Result(new OpDestroyFakeplayer.Spell(player),
                MediaConstants.DUST_UNIT * 5,
                List.of(ParticleSpray.burst(player.position().add(0.0, player.getEyeHeight() / 2.0, 0.0), 1.0, 10)),
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
        private final ServerPlayer player;
        public Spell(ServerPlayer player) {this.player = player;}

        @Override
        public void cast(@NotNull CastingEnvironment env)
        {
            MinecraftServer server = env.getWorld().getServer();
            CommandSourceStack sourceStack = server.createCommandSourceStack();
            server.getCommands().performPrefixedCommand(sourceStack, "player "+FakeplayerUtils.getUsernameString(player)+" kill");
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
