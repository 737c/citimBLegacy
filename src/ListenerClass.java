package org.citmb2.citmb;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class ListenerClass implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        e.setJoinMessage("プレイヤーがログインしました: " + e.getPlayer().getName());
        citmUDB.playerLoginInit(e.getPlayer().getUniqueId());

        fMenu.drawMainMenu(e.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        //e.getPlayer().getWorld().sendMessage("プレイヤーがログアウトしました: " + e.getPlayer().getName());
        citmUDB.playerLogoutSave(e.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onSwapItem(PlayerSwapHandItemsEvent e)
    {
        e.setCancelled(true);
        fMenu.pressFKey(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerClickSign(PlayerInteractEvent event)
    {
        /*
        Player player = event.getPlayer();
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            Block block =  event.getClickedBlock();
            if (block == null)
            {
                return;
            }

            Location blockLoc = block.getLocation();
            if (player.getInventory().getItemInMainHand().getType()  == Material.AIR)
            {
                Material blockType = event.getClickedBlock().getType();
                if(shareFanc.isBTypeSign(blockType))
                {
                    int[] signXYZ = new int[3];
                    signXYZ[0] = blockLoc.getBlockX();
                    signXYZ[1] = blockLoc.getBlockY();
                    signXYZ[2] = blockLoc.getBlockZ();

                    cShop.listener_interact(player.getUniqueId(), signXYZ);
                    event.setCancelled(true);
                }
            }
        }
        */



    }
}
