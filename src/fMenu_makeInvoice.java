package org.citmb2.citmb;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;

public class fMenu_makeInvoice
{
    public static void scanBox(UUID playerID)
    {
        Player player = getPlayer(playerID);
        World world = player.getWorld();
        citmInvoiceData invoiceData = citmUDB.getInvoiceEditData(playerID);

        Location loc = new Location(world, invoiceData.SignAxisX, invoiceData.SignAxisY, invoiceData.SignAxisZ);
        Block signBlock = loc.getBlock();

        Block connectedBlock = shareFanc.getAttachedBlock(signBlock);
        Chest chest = (Chest)connectedBlock.getState();
        Material itemType = null;
        for(ItemStack item : chest.getInventory())
        {
            if (item != null)
            {
                itemType = item.getType();
                break;
            }
        }

        // if there is no items
        if (itemType == null)
        {
            //player.sendMessage("チェストが空です。");
            citmUDB.addUserMessage(playerID, "チェストが空です。");
            Bukkit.getPlayer(playerID).performCommand("citm-uie fMenu.fMenuEnt");
            return;
        }

        invoiceData.itemID = itemType.name();
        citmUDB.putInvoiceEditData(playerID, invoiceData);
        Bukkit.getPlayer(playerID).performCommand("citm-uie fMenu.fMenuEnt");
    }
    public static void putAmount(UUID playerID, String itemAmountString)
    {
        if (!citmUDB.isMakingInvoiceMode(playerID))
        {
            return;
        }

        int itemAmount = 0;
        try {itemAmount = Integer.parseInt(itemAmountString);}
        catch (NumberFormatException ignored) {}

        citmUDB.getInvoiceEditData(playerID).itemAmount = itemAmount;
        Bukkit.getPlayer(playerID).performCommand("citm-uie fMenu.fMenuEnt");
    }
    public static void putItemName(UUID playerID, String nameInput)
    {
        if (!citmUDB.isMakingInvoiceMode(playerID))
        {
            return;
        }
        citmInvoiceData invoiceData = citmUDB.getInvoiceEditData(playerID);

        // 15文字以内にする
        if (nameInput.length() > 15)
        {
            nameInput = nameInput.substring(0,15);
        }

        invoiceData.itemName = nameInput;
        citmUDB.putInvoiceEditData(playerID, invoiceData);

        Bukkit.getPlayer(playerID).performCommand("citm-uie fMenu.fMenuEnt");
    }

    public static void putPrice(UUID playerID, String itemPriceString)
    {
        if (!citmUDB.isMakingInvoiceMode(playerID))
        {
            return;
        }

        int itemPrice = 0;

        try {itemPrice = Integer.parseInt(itemPriceString);}
        catch (NumberFormatException ignored) {}

        citmUDB.getInvoiceEditData(playerID).itemPrice = itemPrice;

        Bukkit.getPlayer(playerID).performCommand("citm-uie fMenu.fMenuEnt");
    }

    public static void show(UUID playerID)
    {
        Player player = Bukkit.getPlayer(playerID);

        citmInvoiceData invoiceData = citmUDB.getInvoiceEditData(playerID);

        String itemNameString = invoiceData.itemName;

        String itemIDString = invoiceData.itemID;


        if (itemNameString.equals(""))
        {
            itemNameString = "設定されていません";
        }
        if (itemIDString.equals(""))
        {
            itemIDString = "スキャンしてください。";
        }
        else
        {
            itemIDString = invoiceData.itemID;
        }


        if (invoiceData.itemAmount <= 0)
        {
            invoiceData.itemAmount = 1;
        }

        String itemSellPrice = String.valueOf(invoiceData.itemPrice);
        if (invoiceData.itemPrice <= 0)
        {
            itemSellPrice = "価格を入力してください。";
            invoiceData.itemPrice = 1;
        }


        List<TextComponent> tComponentList = new ArrayList<>();

        if (!invoiceData.sellShop)
        {
            tComponentList.add(fMenu.tComponent("販売 ",ChatColor.AQUA));
        }
        else
        {
            tComponentList.add(fMenu.tComponent("売却 ",ChatColor.RED));
        }

        tComponentList.add(new TextComponent("[新規ショップ] ===========\n"));
        tComponentList.add(new TextComponent("アイテム名: "+itemNameString+" "));
        tComponentList.add(fMenu.tComponentGen_stringInput("[入力]\n","/citm-uie fMenu_makeInvoice.putItemName "+invoiceData.itemName, ChatColor.AQUA));
        tComponentList.add(new TextComponent("販売アイテム: "+itemIDString+" "));
        tComponentList.add(fMenu.tComponent_link("[スキャン]\n","/citm-uie fMenu_makeInvoice.scanBox"));

        if (!invoiceData.sellShop)
        {
            tComponentList.add(new TextComponent("\n"));
        }
        else
        {
            tComponentList.add(fMenu.tComponent("上限売却数: "+invoiceData.itemMaxAmount+"個 "));
            tComponentList.add(fMenu.tComponentGen_stringInput("[入力]\n", "/citm-uie fMenu_makeInvoice.putMaxAmount "+invoiceData.itemMaxAmount, ChatColor.AQUA));
        }


        tComponentList.add(new TextComponent("個数: "+invoiceData.itemAmount+"個 "));
        tComponentList.add(fMenu.tComponentGen_stringInput("[入力]\n","/citm-uie fMenu_makeInvoice.putAmount "+invoiceData.itemAmount, ChatColor.AQUA));
        tComponentList.add(new TextComponent("価格: "+itemSellPrice+" 円 "));
        tComponentList.add(fMenu.tComponentGen_stringInput("[入力]\n","/citm-uie fMenu_makeInvoice.putPrice "+invoiceData.itemPrice, ChatColor.AQUA));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(fMenu.tComponent_link("[登録]\n","/citm-uie fMenu_makeInvoice.register", ChatColor.RED));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(fMenu.tComponent_link("<<戻る","/citm-uie shareFanc.fMenu_makeInvoice_quit", ChatColor.AQUA));

        BaseComponent bComponent = fMenu.bComponentToChatScreen(tComponentList, playerID);
        player.sendMessage(bComponent);
    }
    public static void putMaxAmount(UUID playerID, String maxAmountString)
    {
        if (!citmUDB.isMakingInvoiceMode(playerID))
        {
            return;
        }

        int maxAmount = 0;
        try {maxAmount = Integer.parseInt(maxAmountString);}
        catch (NumberFormatException ignored) {}

        citmUDB.getInvoiceEditData(playerID).itemMaxAmount = maxAmount;

        Bukkit.getPlayer(playerID).performCommand("citm-uie fMenu.fMenuEnt");

    }
    public static void register(UUID playerID)
    {
        //invoiceData.itemID = itemType.getKey().asString();
        Player player = Bukkit.getPlayer(playerID);

        citmInvoiceData invoiceData = citmUDB.getInvoiceEditData(playerID);

        int itemPrice = invoiceData.itemPrice;
        String itemName = invoiceData.itemName;
        int itemAmount = invoiceData.itemAmount;
        int itemMaxAmount = invoiceData.itemMaxAmount;
        String userName = player.getName();

        // if adminshop
        if (player.isOp())
        {
            userName = "#Official";
        }

        //check the itemID
        if (invoiceData.itemID.equals(""))
        {
            // decline
            return;
        }
        if (invoiceData.itemPrice  <= 0)
        {
            // decline
            return;
        }
        if (invoiceData.itemAmount  <= 0)
        {
            // decline
            return;
        }

        List pubID = adaptor.findPUB(invoiceData.SignAxisX, invoiceData.SignAxisZ,1,1);
        if (pubID.size() < 1)
        {
            Bukkit.getPlayer(playerID).sendMessage("看板がエリア外にあります。");
            return;
        }

        // fix unspicified info

        if (itemName.equals(""))
        {
            ItemStack item = new ItemStack(Material.getMaterial(invoiceData.itemID));
            itemName = item.getItemMeta().getLocalizedName();

            //item.getType().getId();
        }
        if (itemAmount <= 0)
        {
            itemAmount = 0;
        }

        Location loc = new Location(Bukkit.getPlayer(playerID).getWorld(), invoiceData.SignAxisX, invoiceData.SignAxisY, invoiceData.SignAxisZ);
        Sign sign = (Sign)loc.getBlock().getState();

        if (!invoiceData.sellShop)
        {
            sign.setLine(0,itemName);
            sign.setLine(1,""+itemAmount);
            sign.setLine(2,"B:"+itemPrice);
            sign.setLine(3,userName);
        }
        else
        {
            sign.setLine(0,itemName);
            sign.setLine(1,itemAmount+"x"+itemMaxAmount);
            sign.setLine(2,"S:"+itemPrice);
            sign.setLine(3,userName);
        }


        sign.update();
        //loc.getBlock().setBlockData(sign.getBlockData());


        citmUDB.resetInvoice(playerID);
        Bukkit.getPlayer(playerID).performCommand("citm-uie fMenu.fMenuEnt");
    }
    public static void newInvoice(UUID playerID, String sellShopString,String axisX, String axisY, String axisZ)
    {
        boolean sellShopState;
        if (sellShopString.equalsIgnoreCase("sell"))
        {
            sellShopState = true;
        }
        else
        {
            sellShopState = false;
        }

        Player player = getPlayer(playerID);
        World world = player.getWorld();

        int blockAxisX;
        int blockAxisY;
        int blockAxisZ;
        try
        {
            blockAxisX = Integer.parseInt(axisX);
            blockAxisY = Integer.parseInt(axisY);
            blockAxisZ = Integer.parseInt(axisZ);
        }
        catch (NumberFormatException e)
        {
            return;
        }
        Location loc = new Location(world, blockAxisX, blockAxisY, blockAxisZ);
        Block signBlock = loc.getBlock();

        Block connectedBlock = shareFanc.getAttachedBlock(signBlock);
        if (!connectedBlock.getType().equals(Material.CHEST))
        {
            player.sendMessage("看板にチェストについていません。");
            return;
        }

        // check if is this in PUB
        List pubID = adaptor.findPUB(blockAxisX, blockAxisZ,1,1);
        if (pubID.size() < 1)
        {
            player.sendMessage("看板がエリア外にあります。");
            return;
        }

        citmUDB.initInvoiceMode(playerID, sellShopState,blockAxisX, blockAxisY, blockAxisZ);

        Bukkit.getPlayer(playerID).performCommand("citm-uie fMenu_makeInvoice.show");
    }

}
