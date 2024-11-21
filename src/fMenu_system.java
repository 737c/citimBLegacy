package org.citmb2.citmb;

import org.bukkit.Bukkit;

import java.util.UUID;

import static java.lang.Math.abs;

public class fMenu_system
{
    public static boolean isSignInteractFrequent(UUID playerID)
    {
        int currentTick = Bukkit.getServer().getCurrentTick();
        int lastTimeTick = citmUDB.getlastTimeInteractedSign(playerID);
        if (abs(currentTick - lastTimeTick) < 20)
        {
            return true;
        }
        citmUDB.setlastTimeInteractedSign(playerID, currentTick);
        return false;
    }
}
