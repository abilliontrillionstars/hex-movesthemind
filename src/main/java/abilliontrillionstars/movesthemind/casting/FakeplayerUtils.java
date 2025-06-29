package abilliontrillionstars.movesthemind.casting;

import net.minecraft.server.level.ServerPlayer;

public class FakeplayerUtils
{
    public static boolean canBid(ServerPlayer caster, ServerPlayer target)
    {
        String username = getUsernameString(caster);
        String targetname = getUsernameString((target));
        // let the owner of a fakeplayer control it TODO needs edited when adding polyamory
        if(getFakeName(username).equals(targetname))
            return true;
        // let a fakeplayer control itself
        if(caster.getStringUUID().equals(target.getStringUUID()))
            return true;
        return false;
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
