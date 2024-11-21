package org.citmb2.citmb;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class admin_newGame
{
    public static void newGame(UUID playerID, String fullReset)
    {
        Player player = Bukkit.getPlayer(playerID);
        if (fullReset == "eraseEveryPri")
        {

        }

        player.setOp(false);
        player.setBedSpawnLocation(Bukkit.getServer().getWorld("citmworld").getSpawnLocation());
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();

        citmUDB.setPlayerMoneyAmount(playerID, 0);
        citmUDB.getPlayerOnlineData(playerID).usData.PRISqAll = 100;
        citmUDB.getPlayerOnlineData(playerID).usData.PRISqUsed = 0;

        player.setHealth(0.0);
    }
}
