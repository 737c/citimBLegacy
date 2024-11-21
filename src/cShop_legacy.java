package org.citmb2.citmb;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.abs;
import static org.bukkit.Bukkit.getPlayer;

public class cShop_legacy
{
    public static void user_interactShop(UUID PlayerID, int[] signAxisXYZ)
    {
        int currentTick = Bukkit.getServer().getCurrentTick();
        int lastTimeTick = citmUDB.getlastTimeInteractedSign(PlayerID);
        if (abs(currentTick - lastTimeTick) < 20)
        {
            return;
        }
        citmUDB.setlastTimeInteractedSign(PlayerID, currentTick);

        Bukkit.getServer().getConsoleSender().sendMessage("awdawdadwad");

        String[] signData = citmSignShop_signPhase(shareFanc.getSignTexts(PlayerID, signAxisXYZ[0], signAxisXYZ[1], signAxisXYZ[2]));
        if (signData == null){
            Bukkit.getServer().getConsoleSender().sendMessage("fuck you\n");
            return;}
        int[] chestAxisXYZ = shareFanc.getAttachedBlock(PlayerID,signAxisXYZ);
        String sellItemKeyID = cShop_getSellItemKeyID(PlayerID, chestAxisXYZ);
        int itemAmountInChest = shareFanc.checkBox_countItem(PlayerID, sellItemKeyID, chestAxisXYZ);

        boolean sellState = true;
        if (signData[4].equalsIgnoreCase(""))
        {
            sellState = false;
        }

        String itemTitle = signData[0];
        int bulkAmount = Integer.parseInt(signData[1]);
        int sellPrice = Integer.parseInt(signData[2]);
        String sellerName = signData[3];
        if (sellerName.equalsIgnoreCase("#Official"))
        {
            //ellerName = "citm公式";
        }

        int maxBuyAmount = 0;
        if (sellState)
        {
            maxBuyAmount = Integer.parseInt(signData[4]);
        }


        int buyAmount = 1;
        int current_bulkAmountInChest = itemAmountInChest / bulkAmount;
        int left_bulkAmountInChest = current_bulkAmountInChest - buyAmount;

        citmBuyInvoice buyInvoiceData = citmUDB.getCitmBuyInvoice(PlayerID);
        buyInvoiceData.sellUser = sellerName;
        buyInvoiceData.itemTitle = itemTitle;
        buyInvoiceData.itemNameKey = sellItemKeyID;
        buyInvoiceData.sellBulkAmount = bulkAmount;
        buyInvoiceData.itemPrice = sellPrice;
        buyInvoiceData.itemAmountInChest = itemAmountInChest;
        buyInvoiceData.buyBulkAmount = buyAmount;
        buyInvoiceData.sign_axisXYZ = signAxisXYZ;
        buyInvoiceData.chest_axisXYZ = chestAxisXYZ;
        buyInvoiceData.sellShop = false;
        if (sellState)
        {
            buyInvoiceData.maxBuyAmount = maxBuyAmount;
            buyInvoiceData.sellShop = true;
        }



        Bukkit.getServer().getConsoleSender().sendMessage("drhgdbfd");

        citmUDB.setCitmBuyInvoiceData(PlayerID, buyInvoiceData);
        displayBuyInvoice(PlayerID);
    }
    public static String cShop_getSellItemKeyID(UUID PlayerID, int[] chestAxisXYZ)
    {
        Player player = getPlayer(PlayerID);
        World world = player.getWorld();

        Location cLoc = new Location(world, chestAxisXYZ[0], chestAxisXYZ[1], chestAxisXYZ[2]);
        Chest chest = (Chest)cLoc.getBlock().getState();

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
            return null;
        }

        return itemType.name();
    }

    public static void displayBuyInvoice_sell(UUID PlayerID)
    {
        Player player = getPlayer(PlayerID);
        citmBuyInvoice ibData = citmUDB.getCitmBuyInvoice(PlayerID);

        String sellerName = ibData.sellUser;
        if (sellerName.equalsIgnoreCase("#Official"))
        {
            sellerName = "citm公式";
        }
        int selfMoneyAmount = citmUDB.getPlayerMoney(PlayerID);


        int maxBulkBuyAmount = ibData.maxBuyAmount/ ibData.sellBulkAmount;
        int current_bulkAmountInChest = ibData.itemAmountInChest / ibData.sellBulkAmount;
        int left_AvailableBuyBulkAmount = maxBulkBuyAmount - current_bulkAmountInChest;

        ChatColor color_maxSellAmount = ChatColor.WHITE;
        //指定量を上回るか計算
        if (left_AvailableBuyBulkAmount < ibData.buyBulkAmount)
        {
            color_maxSellAmount = ChatColor.RED;
        }

        int hasAmount = getPlayerInventoryItemAmount(PlayerID, ibData.itemNameKey);
        int bulkHasAmount = hasAmount/ ibData.sellBulkAmount;

        ChatColor sellAmount_textColor = ChatColor.WHITE;
        if (bulkHasAmount < ibData.buyBulkAmount)
        {
            sellAmount_textColor = ChatColor.RED;
        }

        int totalSellMoney = ibData.itemPrice * ibData.buyBulkAmount;

        List<TextComponent> tComponentList = new ArrayList<>();

        tComponentList.add(fMenu.tComponentGen_color("取引先ユーザー: "+sellerName+"\n", ChatColor.GRAY));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(fMenu.tComponentGen_color("取引アイテム: "+ibData.itemNameKey+"\n", ChatColor.GRAY));
        tComponentList.add(fMenu.tComponentGen_color("購入バルク: "+ibData.sellBulkAmount+"\n", ChatColor.GRAY));

        tComponentList.add(new TextComponent("[売却確認] ========================\n"));
        tComponentList.add(new TextComponent("品名: "+ibData.itemTitle+"\n"));
        tComponentList.add(new TextComponent("価格: "+ibData.itemPrice+"円\n"));
        tComponentList.add(new TextComponent("\n"));

        tComponentList.add(fMenu.tComponentGen_color("売却上限: "+current_bulkAmountInChest+"/ "+ibData.maxBuyAmount+"個\n", color_maxSellAmount));

        tComponentList.add(fMenu.tComponent("売却個数: "+ibData.buyBulkAmount+"個 ",sellAmount_textColor));
        tComponentList.add(fMenu.tComponentGen_color("x"+ibData.sellBulkAmount, ChatColor.GRAY));
        tComponentList.add(new TextComponent("/ "+bulkHasAmount+"個 :所持個数 "));
        tComponentList.add(fMenu.tComponentGen_stringInput("[変更]\n","/citm-uie cShop.cShop_putAmount "+ibData.buyBulkAmount, ChatColor.AQUA));//color_sufficientAmountMoney

        tComponentList.add(fMenu.tComponentGen_color("合計: "+totalSellMoney+"円/ "+selfMoneyAmount+"円 :所持金\n", ChatColor.AQUA));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("売却しますか？\n"));
        tComponentList.add(fMenu.tComponentGen("[売却]","/citm-uie cShop.cShop_sell", ChatColor.AQUA));
        tComponentList.add(new TextComponent(" "));
        tComponentList.add(fMenu.tComponentGen("[キャンセル]","/citm-uie fMenu.fMenuEnt", ChatColor.RED));

        BaseComponent bComponent = fMenu.bComponentToChatScreen(tComponentList, PlayerID);
        player.sendMessage(bComponent);
    }
    public static int getPlayerInventoryItemAmount(UUID PlayerID, String itemNameKey)
    {
        Player player = getPlayer(PlayerID);

        Inventory inventory = player.getInventory();
        Material material = Material.getMaterial(itemNameKey);
        int itemAmount = 0;
        for(ItemStack itemStack : inventory.getContents())
        {
            if (itemStack == null) {continue;}
            if (itemStack.getType() == material)
            {
                itemAmount = itemAmount + itemStack.getAmount();
            }
        }
        return itemAmount;
    }

    public static void displayBuyInvoice(UUID PlayerID)
    {
        Player player = getPlayer(PlayerID);
        citmBuyInvoice ibData = citmUDB.getCitmBuyInvoice(PlayerID);

        // if the bInvoiceDB isn't initialized
        if (ibData.sellUser == null)
        {
            return;
        }
        if (ibData.sellShop)
        {
            displayBuyInvoice_sell(PlayerID);
            return;
        }

        String sellerName = ibData.sellUser;
        if (sellerName == "#Official")
        {
            sellerName = "citm公式";
        }

        //Bukkit.getServer().getConsoleSender().sendMessage("ertey5rgb");

        int current_bulkAmountInChest = ibData.itemAmountInChest / ibData.sellBulkAmount;
        int left_bulkAmountInChest = current_bulkAmountInChest - ibData.buyBulkAmount;


        int buySumMoneyAmount =  ibData.itemPrice * ibData.buyBulkAmount;
        int selfMoneyAmount = citmUDB.getPlayerMoney(PlayerID);


        ChatColor color_left_bulkAmountInChest = ChatColor.WHITE;
        if (!ibData.sellShop)
        {
            if (left_bulkAmountInChest < 0)
            {
                color_left_bulkAmountInChest = ChatColor.RED;
            }
        }
        else
        {
            //すでに買ったアイテムの量
            current_bulkAmountInChest = ibData.itemAmountInChest / ibData.sellBulkAmount;
            //ユーザーが指定した数を追加
            left_bulkAmountInChest = current_bulkAmountInChest + ibData.buyBulkAmount;
            //指定量を上回るか計算

            if (ibData.maxBuyAmount < left_bulkAmountInChest)
            {
                color_left_bulkAmountInChest = ChatColor.RED;
            }
        }


        ChatColor color_sufficientAmountMoney = ChatColor.WHITE;
        if (!ibData.sellShop)
        if (selfMoneyAmount < buySumMoneyAmount)
        {
            color_sufficientAmountMoney = ChatColor.RED;
        }
        //Bukkit.getServer().getConsoleSender().sendMessage("luyjpom");

        List<TextComponent> tComponentList = new ArrayList<>();

        tComponentList.add(fMenu.tComponentGen_color("取引先ユーザー: "+sellerName+"\n", ChatColor.GRAY));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(fMenu.tComponentGen_color("取引アイテム: "+ibData.itemNameKey+"\n", ChatColor.GRAY));
        tComponentList.add(fMenu.tComponentGen_color("購入バルク: "+ibData.sellBulkAmount+"\n", ChatColor.GRAY));

        if (!ibData.sellShop)
        {
            tComponentList.add(new TextComponent("[購入確認] ========================\n"));
        }
        else
        {
            tComponentList.add(new TextComponent("[売却確認] ========================\n"));
        }

        tComponentList.add(new TextComponent("品名: "+ibData.itemTitle+"\n"));
        tComponentList.add(new TextComponent("価格: "+ibData.itemPrice+"円\n"));
        tComponentList.add(new TextComponent("\n"));

        if (!ibData.sellShop)
        {
            tComponentList.add(fMenu.tComponentGen_color("在庫: "+current_bulkAmountInChest+"個\n", color_left_bulkAmountInChest));
        }
        else
        {
            tComponentList.add(fMenu.tComponentGen_color("売却上限: "+current_bulkAmountInChest+"/ "+ibData.maxBuyAmount+"個\n", color_left_bulkAmountInChest));
        }


        tComponentList.add(new TextComponent("指定個数: "+ibData.buyBulkAmount+"個 "));
        tComponentList.add(fMenu.tComponentGen_color(" x"+ibData.sellBulkAmount+" ", ChatColor.GRAY));
        tComponentList.add(fMenu.tComponentGen_stringInput("[変更]\n","/citm-uie cShop.cShop_putAmount "+ibData.buyBulkAmount, ChatColor.AQUA));//color_sufficientAmountMoney

        if (ibData.sellShop)
        {
            tComponentList.add(fMenu.tComponentGen_color(": "+buySumMoneyAmount+"円/ "+selfMoneyAmount+"個 :所持数\n", color_sufficientAmountMoney));
        }
        else
        {
            tComponentList.add(fMenu.tComponentGen_color("合計: "+buySumMoneyAmount+"円/ "+selfMoneyAmount+"円 :所持金\n", color_sufficientAmountMoney));
        }

        tComponentList.add(new TextComponent("\n"));


        tComponentList.add(new TextComponent("購入しますか？\n"));
        tComponentList.add(fMenu.tComponentGen("[購入]","/citm-uie cShop.cShop_buy", ChatColor.AQUA));


        tComponentList.add(new TextComponent(" "));
        tComponentList.add(fMenu.tComponentGen("[キャンセル]","/citm-uie fMenu.fMenuEnt", ChatColor.RED));

        BaseComponent bComponent = fMenu.bComponentToChatScreen(tComponentList, PlayerID);
        player.sendMessage(bComponent);
    }

    public static void cShop_sell(UUID PlayerID)
    {
        Player player = getPlayer(PlayerID);
        citmBuyInvoice ibData = citmUDB.getCitmBuyInvoice(PlayerID);

        if (ibData.itemNameKey == "null")
        {
            return;
        }

        if (!ibData.sellShop)
        {return;}

        int totalGiveAmountFromPlayer = ibData.buyBulkAmount * ibData.sellBulkAmount;
        int sumAmountInChest = ibData.itemAmountInChest + totalGiveAmountFromPlayer;

        if (ibData.maxBuyAmount < sumAmountInChest)
        {
            return;
        }

        int totalPriceAmount = ibData.itemPrice*ibData.buyBulkAmount;

        int hasAmount = getPlayerInventoryItemAmount(PlayerID, ibData.itemNameKey);
        if (hasAmount < totalGiveAmountFromPlayer) {
            return;
        }

        int buyerRemainMoney = citmUDB.getPlayerMoney(ibData.sellUser);
        if (buyerRemainMoney< totalPriceAmount)
        {return;}

        // take item form player
        Material material = Material.getMaterial(ibData.itemNameKey);
        player.getInventory().remove(new ItemStack(material,totalGiveAmountFromPlayer));

        //put item to chest
        Location chestLoc = new Location(player.getWorld(),ibData.chest_axisXYZ[0],ibData.chest_axisXYZ[1],ibData.chest_axisXYZ[2]);
        Chest chest;
        try
        {
            chest = (Chest)chestLoc.getBlock().getState();
        }
        catch (Exception e)
        {
            return;
        }

        vMoney.transferMoney(player.getName(),ibData.sellUser,totalPriceAmount);

        Inventory cInventory = chest.getInventory();

        // addItem from the chest
        cInventory.addItem(new ItemStack(material,totalGiveAmountFromPlayer));
        chest.getSnapshotInventory().setContents(cInventory.getContents());
        chest.update();

        //
        if (citmUDB.sendPlayerMoney(PlayerID,totalPriceAmount) != 1)
        {
            //取引に失敗
            return;
        }

        citmUDB.getCitmBuyInvoice(PlayerID).sellUser = null;
        fMenu_pri.dConsole_easyMessage(PlayerID, "売却しました！", "/citm-uie fMenu.fMenuEnt", "売却成功");
    }


    public static void cShop_buy(UUID PlayerID)
    {
        Player player = getPlayer(PlayerID);
        citmBuyInvoice ibData = citmUDB.getCitmBuyInvoice(PlayerID);

        if (ibData.itemNameKey == "null")
        {
            return;
        }

        if (ibData.sellShop)
        {return;}

        int totalTakeAmountFromChest = ibData.buyBulkAmount * ibData.sellBulkAmount;
        int leftAmountInChest = ibData.itemAmountInChest-totalTakeAmountFromChest;
        if (leftAmountInChest < 0)
        {
            return;
        }

        int totalPriceAmount = ibData.itemPrice*ibData.buyBulkAmount;

        if (citmUDB.takePlayerMoney(PlayerID,totalPriceAmount) != 1)
        {
            //取引に失敗
            return;
        }

        //take item from chest
        Location chestLoc = new Location(player.getWorld(),ibData.chest_axisXYZ[0],ibData.chest_axisXYZ[1],ibData.chest_axisXYZ[2]);
        Chest chest;
        try
        {
            chest = (Chest)chestLoc.getBlock().getState();
        }
        catch (Exception e)
        {
            return;
        }

        Inventory cInventory = chest.getInventory();
        Material material = Material.getMaterial(ibData.itemNameKey);
        int leftRequestItemAmount = totalTakeAmountFromChest;
        // takeItem from the chest
        cInventory.removeItem(new ItemStack(material,totalTakeAmountFromChest));
        chest.getSnapshotInventory().setContents(cInventory.getContents());
        chest.update();


        // giveItems
        player.getInventory().addItem(new ItemStack(material,totalTakeAmountFromChest));

        // send money to seller
        shareFanc.sendMoney(ibData.sellUser, totalPriceAmount);

        citmUDB.getCitmBuyInvoice(PlayerID).sellUser = null;
        cShop_buy_drawCompleteScreen(PlayerID);
    }


    public static void cShop_buy_drawCompleteScreen(UUID playerID)
    {
        fMenu_pri.dConsole_easyMessage(playerID, "購入しました！", "/citm-uie fMenu.fMenuEnt", "購入成功");
    }

    public static String[] citmSignShop_signPhase(String[] signText)
    {
        String itemName;
        int itemPrice;
        int itemAmount;
        int itemMaxAmount = 0;
        String sellerUserName;

        boolean cShopSellState = false;

        if (signText.length < 4)
        {
            return null;
        }
        Bukkit.getServer().getConsoleSender().sendMessage("7tfv76f");
        // check "B: ~~ part"
        byte[] byteText =  signText[2].getBytes();
        try
        {
            if (((byteText[0] == 'B')&&(byteText[1] == ':')))
            {
                cShopSellState = false;
            }
            else if (((byteText[0] == 'S')&&(byteText[1] == ':')))
            {
                cShopSellState = true;
            }
            else
            {
                return null;
            }

        }
        catch (Exception ignore){return null;}


        if (!cShopSellState)
        {
            // get price value
            String[] priceString = signText[2].split("B:");
            try
            {
                itemPrice = Integer.parseInt(priceString[1]);
            }
            catch (Exception exception)
            {
                return null;
            }
        }
        else
        {
            // get price value
            String[] priceString = signText[2].split("S:");
            try
            {
                itemPrice = Integer.parseInt(priceString[1]);
            }
            catch (Exception exception)
            {
                return null;
            }
        }

        //Bukkit.getServer().getConsoleSender().sendMessage("43f7hh");

        // get mount
        try
        {
            if (!cShopSellState)
            {
                itemAmount = Integer.parseInt(signText[1]);
            }
            else
            {
                String[] signTextL2 = signText[1].split("x");
                if (signTextL2.length < 2)
                {
                    return null;
                }

                itemAmount = Integer.parseInt(signTextL2[0]);
                itemMaxAmount = Integer.parseInt(signTextL2[1]);
            }
        }
        catch (Exception exception)
        {
            return null;
        }
        //Bukkit.getServer().getConsoleSender().sendMessage("bvrn8e9n");

        // getItemName
        itemName = signText[0];

        //getUserName(recipuant)
        sellerUserName = signText[3];

        String[] signShopData = new String[5];
        signShopData[0] = itemName;
        signShopData[1] = String.valueOf(itemAmount);
        signShopData[2] = String.valueOf(itemPrice);
        signShopData[3] = sellerUserName;
        signShopData[4] = "";
        if(cShopSellState)
        {
            signShopData[4] = String.valueOf(itemMaxAmount);
        }

        return signShopData;
    }
    public static void cShop_putAmount(UUID PlayerID, String input_Amount)
    {
        int itemAmount;
        try
        {
            itemAmount = Integer.parseInt(input_Amount);

        }catch (Exception e)
        {
            if (citmUDB.getCitmBuyInvoice(PlayerID).sellShop)
            {
                cShop.displayBuyInvoice_sell(PlayerID);
                return;
            }
            else
            {
                cShop.displayBuyInvoice(PlayerID);
                return;
            }
        }

        if (itemAmount < 1)
        {
            if (citmUDB.getCitmBuyInvoice(PlayerID).sellShop)
            {
                cShop.displayBuyInvoice_sell(PlayerID);
                return;
            }
            else
            {
                cShop.displayBuyInvoice(PlayerID);
                return;
            }

        }

        citmUDB.getCitmBuyInvoice(PlayerID).buyBulkAmount = itemAmount;

        if (citmUDB.getCitmBuyInvoice(PlayerID).sellShop)
        {
            cShop.displayBuyInvoice_sell(PlayerID);
            return;
        }
        else
        {
            cShop.displayBuyInvoice(PlayerID);
            return;
        }
    }
}
