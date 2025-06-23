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
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation;
import at.petrak.hexcasting.api.misc.MediaConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OpCreateFakeplayer implements SpellAction
{
    public static final Action INSTANCE = new OpCreateFakeplayer();

    @Override
    public int getArgc() { return 1; }

    @Override
    public @NotNull SpellAction.Result executeWithUserdata(@NotNull List<? extends Iota> args, @NotNull CastingEnvironment env, @NotNull CompoundTag tags)
    {
        Vec3 pos = OperatorUtils.getVec3(args, 0, getArgc());
        try { env.assertVecInRange(pos); }
        catch(MishapBadLocation e)
        {
            JavaMishapThrower.throwMishap(new MishapBadLocation(pos, "too_far"));
        }
        return new SpellAction.Result(new OpCreateFakeplayer.Spell(pos),
                MediaConstants.CRYSTAL_UNIT,
                List.of(ParticleSpray.burst(pos, 1.0, 10)),
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
        private Vec3 pos;
        public Spell(Vec3 pos) {this.pos = pos;}

        @Override
        public void cast(@NotNull CastingEnvironment env)
        {
            MinecraftServer server = env.getWorld().getServer();
            CommandSourceStack sourceStack = server.createCommandSourceStack();
            String compName = env.getCaster().getName().toString();
            String username = compName.substring(compName.indexOf('{')+1, compName.length()-1);
            // TODO: make this pad the name to 16 characters to distinguish from any real player
            if(username.contains("_bot")) return; // no grey-goo! bad fakeplayer! bad!
            server.getCommands().performPrefixedCommand(sourceStack, "player "+username+"_bot spawn at "+pos.x+" "+pos.y+" "+pos.z);
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
