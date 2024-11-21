package org.citmb2.citmb;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class itemExchanger
{
    public static void itemExchangeList(UUID PlayerID)
    {
        Player player = Bukkit.getPlayer(PlayerID);

        BaseComponent bComponent;
        List<TextComponent> tComponentList = new ArrayList<>();

        TextComponent tComponent[] = new TextComponent[10];
        tComponentList.add(new TextComponent("[アイテム交換] =====================\n"));

        tComponentList.add(fMenu.tComponent_link("丸石x4 ", "/citm-uie itemExchanger.itemExchange COBBLESTONE"));
        tComponentList.add(fMenu.tComponent("(原木x1 と交換)\n",ChatColor.GRAY));

        tComponentList.add(fMenu.tComponent_link("土x4 ", "/citm-uie itemExchanger.itemExchange DIRT"));
        tComponentList.add(fMenu.tComponent("(原木x1 と交換)\n",ChatColor.GRAY));

        tComponentList.add(fMenu.tComponent_link("鉄鉱石 ", "/citm-uie itemExchanger.itemExchange IRON_ORE"));
        tComponentList.add(fMenu.tComponent("(原木x1 と交換)\n",ChatColor.GRAY));

        tComponentList.add(fMenu.tComponent_link("糸 ", "/citm-uie itemExchanger.itemExchange STRING"));
        tComponentList.add(fMenu.tComponent("(小麦x1 と交換)\n",ChatColor.GRAY));

        tComponentList.add(fMenu.tComponent_link("イカスミ ", "/citm-uie itemExchanger.itemExchange INK_SAC"));
        tComponentList.add(fMenu.tComponent("(木炭x1 と交換)\n",ChatColor.GRAY));

        tComponentList.add(fMenu.tComponent_link("骨 ", "/citm-uie itemExchanger.itemExchange BONE" ));
        tComponentList.add(fMenu.tComponent("(小麦x3 と交換)\n", ChatColor.GRAY));

        tComponentList.add(fMenu.tComponent("\n"));
        tComponentList.add(fMenu.tComponent("\n"));
        tComponentList.add(fMenu.tComponent_link("<< 戻る", "/citm-uie fMenu.fMenuEnt"));

        bComponent = fMenu.bComponentToChatScreen( tComponentList);
        player.sendMessage(bComponent);
    }
    public static void itemExchange(UUID PlayerID, String toItem)
    {
        Player player = Bukkit.getPlayer(PlayerID);
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (toItem.equalsIgnoreCase("BONE"))
        {
            itemExchange_core(PlayerID, Material.WHEAT.name(), Material.BONE.name(), 3,1);
        }
        if (toItem.equalsIgnoreCase("STRING"))
        {
            itemExchange_core(PlayerID, Material.WHEAT.name(), Material.STRING.name(), 1,1);
        }
        if (toItem.equalsIgnoreCase("INK_SAC"))
        {
            itemExchange_core(PlayerID,Material.CHARCOAL.name(), Material.INK_SAC.name(), 1,1);
        }
        if (toItem.equalsIgnoreCase("COBBLESTONE"))
        {
            itemExchange_core(PlayerID, Material.OAK_LOG.name(), Material.COBBLESTONE.name(), 1,4);
        }
        if (toItem.equalsIgnoreCase("DIRT"))
        {
            itemExchange_core(PlayerID, Material.OAK_LOG.name(), Material.DIRT.name(), 1,4);
        }
        if (toItem.equalsIgnoreCase("IRON_ORE"))
        {
            itemExchange_core(PlayerID, Material.OAK_LOG.name(), Material.IRON_ORE.name(), 1,1);
        }

    }
    public static void itemExchange_core(UUID playerID, String fromItemID, String toItemID, int fromAmount, int toAmount)
    {
        Player player = Bukkit.getPlayer(playerID);

        ItemStack fromItem = new ItemStack(Material.getMaterial(fromItemID));
        ItemStack itemStackInHand = player.getInventory().getItemInMainHand();
        if (fromItem.getType().equals(itemStackInHand.getType()) || (isAnyLog(fromItem.getType())&&isAnyLog(itemStackInHand.getType())))
        {
            if (fromAmount <= itemStackInHand.getAmount())
            {
                // take item from player
                ItemStack itemStackTake = new ItemStack(itemStackInHand.getType());
                itemStackTake.setAmount(fromAmount);
                player.getInventory().remove(itemStackTake);

                //give player
                ItemStack itemStackGive = new ItemStack(Material.getMaterial(toItemID));
                itemStackGive.setAmount(toAmount);
                player.getInventory().addItem(itemStackGive);
            }
        }
    }

    public static boolean isAnyLog(Material material)
    {
        if ((material == Material.OAK_LOG) ||
                (material == Material.ACACIA_LOG) ||
                (material == Material.BIRCH_LOG)||
                (material == Material.JUNGLE_LOG)||
                (material == Material.DARK_OAK_LOG)||
                (material == Material.SPRUCE_LOG))
        {
            return true;
        }
        return false;
    }

}
