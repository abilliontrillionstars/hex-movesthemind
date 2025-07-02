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
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation;
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName;
import at.petrak.hexcasting.api.misc.MediaConstants;
import carpet.patches.EntityPlayerMPFake;
import net.fabricmc.loader.api.FabricLoader;
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
        // ambit check
        if(!env.isVecInAmbit(pos))
            JavaMishapThrower.throwMishap(new MishapBadLocation(pos, "too_far"));
        Entity caster = env.getCastingEntity();
        // shouldn't be cast playerless
        if(!(caster instanceof ServerPlayer))
            JavaMishapThrower.throwMishap(new MishapBadCaster());
        // no grey-goo! bad fakeplayer! bad!
        if(caster instanceof EntityPlayerMPFake)
            JavaMishapThrower.throwMishap(new MishapBadCaster());

        String username = FakeplayerUtils.getFakeName(FakeplayerUtils.getUsernameString((ServerPlayer) caster));
        MinecraftServer server = env.getWorld().getServer();
        // fail early if the player exists already
        ServerPlayer player = server.getPlayerList().getPlayerByName(username);
        if(player != null)
            JavaMishapThrower.throwMishap(new MishapOthersName(player));

        return new SpellAction.Result(new OpCreateFakeplayer.Spell(pos,username),
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
            String dim = env.getWorld().dimension().toString();
            dim = dim.substring(dim.lastIndexOf(' ')+1, dim.indexOf(']'));
            String gamemode;
            if(env.getCaster().gameMode.isCreative())
                gamemode = "creative";
            else gamemode = "survival";
            server.getCommands().performPrefixedCommand(sourceStack, "player " + name + " spawn at " + pos.x + " " + pos.y + " " + pos.z
                    + " facing 0 0 in "+dim+" in "+gamemode);
            // set the origin of a player if it's installed (now suppressed from logs)
            if(FabricLoader.getInstance().isModLoaded("origins"))
                server.getCommands().performPrefixedCommand(sourceStack.withSuppressedOutput(), "origin set "+name+" origins:origin origins:human");
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
