package org.citmb2.citmb;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class vMoney
{
    public static boolean transferMoney(String fromUserName, String toUserName, int amount)
    {
        if (checkBalance(fromUserName) < amount)
        {
            return false;
        }

        modifyMoney_admin(fromUserName, amount*-1);
        modifyMoney_admin(toUserName, amount);
        return true;
    }

    private static void modifyMoney_admin(String userName, int amount)
    {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(userName);
        if (offlinePlayer.isOnline())
        {
            citmUDB.onlinePlayerData.get(offlinePlayer.getUniqueId()).usData.money += amount;
        }
        else
        {
            citmUserSave userSaveData = citmSystem.getUserSaveData(offlinePlayer.getUniqueId());
            if (userSaveData == null)
            {
                return;
            }
            userSaveData.money += amount;
            citmSystem.setUserSaveData(offlinePlayer.getUniqueId(), userSaveData);
        }
    }
    public static int checkBalance(String userName)
    {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(userName);
        if (offlinePlayer.isOnline())
        {
            return citmUDB.onlinePlayerData.get(offlinePlayer.getUniqueId()).usData.money;
        }
        else
        {
            return checkBalance_InColdWallet_admin(userName);
        }
    }

    private static int checkBalance_InColdWallet_admin(String userName)
    {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(userName);

        citmUserSave userSaveData = citmSystem.getUserSaveData(offlinePlayer.getUniqueId());
        if (userSaveData == null)
        {
            return 0;
        }

        return userSaveData.money;
    }


}
