package abilliontrillionstars.movesthemind.casting;

import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class FakeplayerUtils
{
    public static boolean canBid(ServerPlayer caster, ServerPlayer target)
    {
        String username = caster.getName().toString();
        username = username.substring(username.indexOf('{')+1, username.length()-1);
        String targetname = target.getName().toString();
        targetname = targetname.substring(targetname.indexOf('{')+1, targetname.length()-1);
        // let the owner of a fakeplayer control it TODO needs edited when adding polyamory
        return getFakeName(username).equals(targetname)
                // let a fakeplayer control itself
                || getFakeName(targetname).equals(targetname);
    }

    public static String getFakeName(String username)
    {
        // this should return the same name when used with a fakeplayer's name
        while(username.length() < 16)
            username = username.concat("_"); // pad the string to 16 characters
        username = username.substring(0,12).concat("_bot");
        return username;
    }

    public static String getUsernameString(ServerPlayer player)
    {
        String compName = player.getName().toString();
        compName = compName.substring(compName.indexOf('{')+1, compName.length()-1);
        return compName;
    }
}
