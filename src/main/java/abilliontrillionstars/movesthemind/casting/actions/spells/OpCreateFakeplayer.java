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
import at.petrak.hexcasting.fabric.FabricHexConfig;
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
        Entity caster = env.getCastingEntity();
        if(!(caster instanceof ServerPlayer))
            JavaMishapThrower.throwMishap(new MishapBadCaster());

        String username = caster.getName().toString();
        username = username.substring(username.indexOf('{')+1, username.length()-1);
        while(username.length() < 16)
            username = username.concat("_"); // pad the string to 16 characters

        if(username.substring(12,16).equals("_bot"))
            JavaMishapThrower.throwMishap(new MishapBadCaster()); // no grey-goo! bad fakeplayer! bad!

        username = username.substring(12,16).concat("_bot");


        return new SpellAction.Result(new OpCreateFakeplayer.Spell(pos, username),
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
        private String name;
        public Spell(Vec3 pos, String name) {this.pos = pos; this.name = name;}

        @Override
        public void cast(@NotNull CastingEnvironment env)
        {
            MinecraftServer server = env.getWorld().getServer();
            CommandSourceStack sourceStack = server.createCommandSourceStack();

            server.getCommands().performPrefixedCommand(sourceStack, "player "+name+" spawn at "+pos.x+" "+pos.y+" "+pos.z);
            server.getCommands().performPrefixedCommand(sourceStack, "gamemode "+name+" survival");
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
