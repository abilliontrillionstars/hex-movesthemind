package abilliontrillionstars.movesthemind.casting.actions.spells;

import abilliontrillionstars.movesthemind.Movesthemind;
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
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity;
import at.petrak.hexcasting.api.casting.mishaps.MishapEntityTooFarAway;
import at.petrak.hexcasting.api.misc.MediaConstants;
import carpet.helpers.EntityPlayerActionPack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static org.spongepowered.asm.mixin.injection.selectors.ElementNode.listOf;

public class OpMadeYouLook implements SpellAction
{
    public static final Action INSTANCE = new OpMadeYouLook();

    @Override
    public int getArgc() { return 2; }

    @Override
    public @NotNull Result executeWithUserdata(@NotNull List<? extends Iota> args, @NotNull CastingEnvironment env, @NotNull CompoundTag tags)
    {
        ServerPlayer target = OperatorUtils.getPlayer(args, 0, getArgc());
        try { env.assertEntityInRange(target); }
        catch(MishapEntityTooFarAway e)
        {
            JavaMishapThrower.throwMishap(new MishapEntityTooFarAway(target));
        }
        Vec3 dir = OperatorUtils.getVec3(args, 1, getArgc());
        return new Result(new Spell(target, dir),
                MediaConstants.DUST_UNIT / 10,
                List.of(ParticleSpray.burst(target.position().add(0.0, target.getEyeHeight() / 2.0, 0.0), 1.0, 10)),
                1);
    }

    @Override
    public boolean hasCastingSound(@NotNull CastingEnvironment castingEnvironment) { return true; }
    @Override
    public boolean awardsCastingStat(@NotNull CastingEnvironment castingEnvironment) { return true; }


    @Override
    public @NotNull Result execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment)
    {
        throw new IllegalStateException();
    }

    private class Spell implements RenderedSpell
    {
        private ServerPlayer target;
        private Vec3 dir;
        public Spell(ServerPlayer target, Vec3 dir) {this.target = target; this.dir = dir;}

        @Override
        public void cast(@NotNull CastingEnvironment env)
        {
            EntityPlayerActionPack actor = new EntityPlayerActionPack(this.target);
            dir = dir.normalize();
            float pitch = (float) Math.toDegrees(asin(-dir.y));
            float yaw = (float) Math.toDegrees(atan2(-dir.x, dir.z));
            actor.look(yaw, pitch);
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
