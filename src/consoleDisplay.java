package org.citmb2.citmb;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;

public class consoleDisplay
{
    public static void showScreen(UUID playerID)
    {
        Bukkit.getPlayer(playerID).sendMessage(citmUDB.getPlayerOnlineData(playerID).cdData.bComponent);
    }

    public static void putConsole(UUID playerID, List<TextComponent> textCompList)
    {
        consoleData consoleData = citmUDB.getPlayerOnlineData(playerID).cdData;

        BaseComponent bComponent = new TextComponent("");
        String userMessage = consoleData.basicMessage;
        consoleData.basicMessage = "";

        for (int i = 0; i < 10; i++)
        {
            bComponent.addExtra("\n");
        }
        bComponent.addExtra(userMessage+"\n");
        for (int i = 0; i < textCompList.size(); i++)
        {
            bComponent.addExtra(textCompList.get(i));
        }

        consoleData.bComponent = bComponent;
        Bukkit.getPlayer(playerID).sendMessage(bComponent);
    }
}
