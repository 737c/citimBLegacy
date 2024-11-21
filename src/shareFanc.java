package org.citmb2.citmb;

public class shareFanc
{
    //*
    public static String getTimeInString(UUID PlayerID)
    {
        String timeString;
        World world = Bukkit.getPlayer(PlayerID).getWorld();
        long time = world.getTime() + 6000;
        time = time % 24000;

        int hour = (int) (time / 1000);
        int min = (int) (((time % 1000)* 60)/ 1000);

        timeString = String.format("%02d",hour)+":"+String.format("%02d",min);

        return timeString;
    }

    public static int getUserPRI(UUID PlayerID)
    {
        Player player = Bukkit.getPlayer(PlayerID);
        if (player == null)
        {

        }
        return 0;
    }
    public static int sendMoney(UUID targetID, int moneyAmount)
    {
        if (moneyAmount < 0)
        {
            return -2;
        }

        return requestWallet(targetID, moneyAmount);
    }
    public static int sendMoney(String playerName, int moneyAmount)
    {
        if (playerName.contains("#"))
        {
            return requestOfficialWallet(playerName, moneyAmount);
        }
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        if (player != null)
        {
            UUID playerID = player.getUniqueId();
            return sendMoney(playerID,moneyAmount);
        }

        return -1;
    }
    private static int takeMoney(String playerName, int moneyAmount)
    {
        if (playerName.contains("#"))
        {
            return requestOfficialWallet(playerName, moneyAmount*-1);
        }
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        if (player != null)
        {
            UUID playerID = player.getUniqueId();
            return takeMoney(playerID,moneyAmount);
        }

        return -1;
    }
    public static int takeMoney(UUID targetID, int moneyAmount)
    {
        if (moneyAmount < 0)
        {
            return -2;
        }

        return requestWallet(targetID, (moneyAmount*-1));
    }
    public static int requestOfficialWallet(String accountName, int moneyChangeAmount)
    {
        if (accountName == "#Official")
        {
            //citmUDB.coldWalletOfficial(accountName, moneyChangeAmount);
        }
        return 1;
    }
    public static int requestWallet(UUID targetID, int moneyChangeAmount)
    {
        //UUID targetID = targetID;
        boolean isSellerOnline = false;
        try
        {
            targetID = Bukkit.getPlayer(targetID).getUniqueId();
            isSellerOnline = true;
        }
        catch (Exception e)
        {
            try
            {
                targetID = Bukkit.getOfflinePlayer(targetID).getUniqueId();
                isSellerOnline = false;
            }
            catch (Exception e2)
            {
                Bukkit.getServer().getConsoleSender().sendMessage("the seller doent exist");
                return -1;
            }
        }


        if (isSellerOnline)
        {
            //citmUDB.sendPlayerMoney(targetID, moneyChangeAmount);
        }
        else
        {
            //citmUDB.coldWallet(targetID, moneyChangeAmount);
        }

        return 1;
    }

    public static int checkBox_countItem(UUID PlayerID, String itemNameKey, int[] chestAxisXYZ)
    {
        Player player = getPlayer(PlayerID);
        World world = player.getWorld();

        Location cLoc = new Location(world, chestAxisXYZ[0], chestAxisXYZ[1], chestAxisXYZ[2]);
        Chest chest = (Chest)cLoc.getBlock().getState();

        Inventory inventory = chest.getInventory();
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

    public static String[] getSignTexts(UUID PlayerID, int axisX,int axisY,int axisZ)
    {
        World world = Bukkit.getPlayer(PlayerID).getWorld();
        Block block = world.getBlockAt(axisX, axisY, axisZ);
        Sign sign = (Sign)block.getState();

        return sign.getLines();
    }

    public static int getTimeInHour(UUID PlayerID)
    {
        World world = Bukkit.getPlayer(PlayerID).getWorld();
        long time = world.getTime() + 6000;
        time = time % 24000;

        int hour = (int) (time / 1000);
        return hour;
    }


    public static String getWeatherInString(UUID PlayerID)
    {
        String weatherString = "☀";
        World world = Bukkit.getPlayer(PlayerID).getWorld();

        // weatherState:
        // 0: clear, 1: rain, 2: storm
        int weatherState;
        if (world.hasStorm())
        {
            if (world.isThundering())
            {
                weatherState = 2;
            }
            else
            {
                weatherState = 1;
            }
        }
        else
        {
            weatherState = 0;
        }


        if (weatherState == 0)
        {
            int timeHour = getTimeInHour(PlayerID);
            if ((18 < timeHour) || (timeHour < 7))
            {
                weatherString = "☽";
            }
            else
            {
                weatherString = "☀";
            }
        }
        if (weatherState == 1)
        {
            weatherString = "☂";
        }
        if (weatherState == 2)
        {
            weatherString = "⚡";
        }

        return weatherString;
    }


    public static Block getAttachedBlock(Block block)
    {
        BlockData blockData = block.getBlockData();
        Directional directional = (Directional) blockData;
        return block.getRelative(directional.getFacing().getOppositeFace());
    }

    public static void fMenu_makeInvoice_quit(UUID playerID)
    {
        citmUDB.resetInvoice(playerID);
        Bukkit.getPlayer(playerID).performCommand("citm-uie fMenu");
    }


    public static void test_seeUserName()
    {

        try
        {
            Bukkit.getServer().getConsoleSender().sendMessage(Bukkit.getOfflinePlayer("lll0_0lll").getUniqueId().toString());
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage("fail to search");
            throw new RuntimeException(e);
        }

    }


    public static int[] getAttachedBlock(UUID playerID, int[] signAxisXYZ)
    {
        Player player = getPlayer(playerID);
        World world = player.getWorld();
        Location loc = new Location(world, signAxisXYZ[0], signAxisXYZ[1], signAxisXYZ[2]);
        Block signBlock = loc.getBlock();

        Location chestLoc = getAttachedBlock(signBlock).getLocation();
        int[] chestAxisXYZ = new int[3];
        chestAxisXYZ[0] = chestLoc.getBlockX();
        chestAxisXYZ[1] = chestLoc.getBlockY();
        chestAxisXYZ[2] = chestLoc.getBlockZ();

        return chestAxisXYZ;
    }

    public static String checkBox_itemSell(UUID playerID, int signAxisX, int signAxisY, int signAxisZ)
    {
        Player player = getPlayer(playerID);
        World world = player.getWorld();

        Location loc = new Location(world, signAxisX, signAxisY, signAxisZ);
        Block signBlock = loc.getBlock();

        Block connectedBlock = getAttachedBlock(signBlock);
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
            return null;
        }

        return itemType.name();
    }



    public static boolean isBTypeSign(Material bType)
    {
        if (bType.equals(Material.OAK_WALL_SIGN)||
                bType.equals(Material.SPRUCE_WALL_SIGN)||
                bType.equals(Material.BIRCH_WALL_SIGN)||
                bType.equals(Material.JUNGLE_WALL_SIGN)||
                bType.equals(Material.ACACIA_WALL_SIGN)||
                bType.equals(Material.DARK_OAK_WALL_SIGN)||
                bType.equals(Material.CRIMSON_WALL_SIGN)||
                bType.equals(Material.WARPED_WALL_SIGN))
        {
            return true;
        }
        return false;
    }

    public static void setConfigInt(String keyPath, Object setValue)
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("CitmB");
        plugin.getConfig().set(keyPath, setValue);
        try
        {
            plugin.getConfig().save("citmConfig.data");
        }
        catch (IOException e)
        {
            //save failed
        }

    }

    public static int getConfigInt(String keyPath)
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("CitmB");

        try
        {
            plugin.getConfig().load("citmConfig.data");
        }

        catch (IOException | InvalidConfigurationException e)
        {
            //load failed
        }
        assert plugin != null;
        if (!plugin.getConfig().contains(keyPath))
        {
            plugin.getConfig().set(keyPath,0);
        }
        return plugin.getConfig().getInt(keyPath);
    }

    public static void fMenu_email()
    {
        
    }
    //*/
}


