package org.citmb2.citmb;

import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class citmUDB
{
    static Map<UUID, userOnlineData> onlinePlayerData = new HashMap<>();
    //*
    //static Map<UUID, Map<String, PRI>> userPRIData = new HashMap<>();

    public static void playerLoginInit(UUID playerID)
    {
        loadUSDataToUDB(playerID);
        //loadPRIData(playerID);
    }
    /*
    public static Map<String, PRI> loadPlayerPriData(UUID playerID)
    {
        String fileName = "userPRI\\"+playerID+".pri";
        Map<String, PRI> userPRI = null;
        try
        {
            userPRI = (Map<String, PRI>) citm_stdio.loadFile(fileName);
        }
        catch (IOException e)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("loadFileFailed(or new User): "+fileName);
            Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("fileVersionError"+fileName);
        }
        if (userPRI == null)
        {
            userPRI = new HashMap<>();
        }

        return userPRI;
        //setPRIData(playerID, userPRI);
    }
    */
    public static void loadUSDataToUDB(UUID playerID)
    {
        String fileName = "userData\\"+playerID+".data";
        citmUserSave loginSData = new citmUserSave();

        loginSData.pUID = playerID;
        loginSData.money = 0;
        loginSData.PRISqAll = 0;
        loginSData.PRISqUsed = 0;
        try
        {
            loginSData = (citmUserSave) citm_stdio.loadFile(fileName);
        }
        catch (IOException e)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("loadFileFailed(or new User): "+fileName);
            Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("fileVersionError"+fileName);
            //throw new RuntimeException(e);
        }

        userOnlineData loginPlayerData = new userOnlineData();
        loginPlayerData.usData = loginSData;

        onlinePlayerData.put(playerID, loginPlayerData);
    }
    /*
    private static int getPlayerMoney(UUID playerUID)
    {
        return onlinePlayerData.get(playerUID).usData.money;
    }
    public static int getPlayerMoney(String playerName)
    {
        if (playerName.equalsIgnoreCase("#Official"))
        {
            return 1000;
        }
        else
        {
            return getPlayerMoney(Bukkit.getPlayerUniqueId(playerName));
        }

        //return 0;
    }
    public static int takePlayerMoney(UUID playerUID, int requestAmount)
    {
        int playerMoney = onlinePlayerData.get(playerUID).usData.money;

        // if requested money is less the 0
        if (requestAmount < 0)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("requested money is less the 0");
            return -1;
        }

        // if the balance is too low
        if (playerMoney < requestAmount)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("the balance is too low :"+playerUID);
            return -2;
        }

        playerMoney = playerMoney - requestAmount;
        onlinePlayerData.get(playerUID).usData.money = playerMoney;

        return 1;
    }
    private static int sendPlayerMoney(UUID playerUID, int requestAmount)
    {
        int playerMoney = onlinePlayerData.get(playerUID).usData.money;

        if (requestAmount < 0)
        {
            return -1;
        }

        playerMoney = playerMoney + requestAmount;
        onlinePlayerData.get(playerUID).usData.money = playerMoney;

        return 1;
    }
    */
    public static void setPlayerMoneyAmount(UUID playerID, int moneyAmount)
    {
        onlinePlayerData.get(playerID).usData.money = moneyAmount;
    }
    /*
    private static boolean coldWallet(UUID playerID, int moneyAmount)
    {
        String fileName = "userData\\"+playerID+".data";
        citmUserSave coldWalletData;

        try
        {
            coldWalletData = (citmUserSave) citm_stdio.loadFile(fileName);
        }
        catch (IOException e)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("loadFileFailed(or new User): "+fileName);
            Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
            return false;
        }
        catch (ClassNotFoundException e)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("fileVersionError"+fileName);
            return false;
        }

        //changeAmount
        coldWalletData.money = coldWalletData.money + moneyAmount;

        // saveData
        try
        {
            citm_stdio.saveFile(fileName, coldWalletData);
        }
        catch (IOException e)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("saveFailed: "+fileName);
            Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
            //Bukkit.getServer().getConsoleSender().sendMessage(e.getCause().toString());
            return false;
        }
        return true;
    }
    private static boolean coldWalletOfficial(String accountName, int moneyAmount)
    {
        String fileName = "official\\"+accountName+".data";
        citmUserSave coldWalletData;// = new citmUSData();

        try
        {
            coldWalletData = (citmUserSave) citm_stdio.loadFile(fileName);
        }
        catch (IOException e)
        {
            coldWalletData = new citmUserSave();
            return false;
        }
        catch (ClassNotFoundException e)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("fileVersionError"+fileName);
            coldWalletData = new citmUserSave();
        }

        //changeAmount
        coldWalletData.money = coldWalletData.money + moneyAmount;

        // saveData
        try
        {
            citm_stdio.saveFile(fileName, coldWalletData);
        }
        catch (IOException e)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("saveFailed: "+fileName);
            Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
            //Bukkit.getServer().getConsoleSender().sendMessage(e.getCause().toString());
            return false;
        }
        return true;
    }
    public static PRIEdit getPRIEditData(UUID playerID)
    {
        return onlinePlayerData.get(playerID).PRIEditData;
    }
    setPRIEditData(UUID playerID, PRIEdit PRIEditData)
    {
        onlinePlayerData.get(playerID).PRIEditData = PRIEditData;
    }
    public static boolean isPlayerPRIMode(UUID playerUID)
    {
        return onlinePlayerData.get(playerUID).isPRIMode;
    }
    public static void setPlayerAMMode(UUID playerUID, boolean state)
    {
        onlinePlayerData.get(playerUID).isPRIMode = state;
    }

    public static void setMakingInvoiceMode(UUID playerUID, boolean state)
    {
        onlinePlayerData.get(playerUID).isMakingInvoice = state;
    }
    public static void initInvoiceMode(UUID playerID, boolean sellShop, int bAxisX, int bAxisY, int bAxisZ)
    {
        resetInvoice(playerID);
        Location pLoc = Bukkit.getPlayer(playerID).getLocation();
        userOnlineData uData = onlinePlayerData.get(playerID);

        uData.invoiceEditData.SignAxisX = bAxisX;
        uData.invoiceEditData.SignAxisY = bAxisY;
        uData.invoiceEditData.SignAxisZ = bAxisZ;

        uData.invoiceEditData.sellShop = sellShop;

        setMakingInvoiceMode(playerID,true);
    }

    public static void resetInvoice(UUID playerID)
    {
        userOnlineData uData = onlinePlayerData.get(playerID);

        uData.isMakingInvoice = false;
        uData.invoiceEditData.itemName = "";
        uData.invoiceEditData.itemPrice = 0;
        uData.invoiceEditData.itemAmount = 0;
        uData.invoiceEditData.itemID = "";
        uData.invoiceEditData.SignAxisX = 0;
        uData.invoiceEditData.SignAxisY = 0;
        uData.invoiceEditData.SignAxisZ = 0;

        onlinePlayerData.put(playerID, uData);
    }

    public static citmBuyInvoice getCitmBuyInvoice(UUID playerID)
    {
        return onlinePlayerData.get(playerID).buyInvoiceData;
    }


    public static void setCitmBuyInvoiceData(UUID playerID, citmBuyInvoice buyInvoiceData)
    {
        onlinePlayerData.get(playerID).buyInvoiceData = buyInvoiceData;
    }

    public static boolean isMakingInvoiceMode(UUID PlayerID)
    {
        return onlinePlayerData.get(PlayerID).isMakingInvoice;
    }

    public static citmInvoiceData getInvoiceEditData(UUID PlayerID)
    {
        return onlinePlayerData.get(PlayerID).invoiceEditData;
    }
    public static void putInvoiceEditData(UUID PlayerID, citmInvoiceData invoiceData)
    {
        onlinePlayerData.get(PlayerID).invoiceEditData = invoiceData;
    }

    public static String getUserMessage(UUID PlayerID)
    {
        return onlinePlayerData.get(PlayerID).userMessage;
    }
    public static void setUserMessage(UUID PlayerID, String userMessage)
    {
        onlinePlayerData.get(PlayerID).userMessage = userMessage;
    }
    public static void addUserMessage(UUID PlayerID, String userMessage)
    {
        onlinePlayerData.get(PlayerID).userMessage = onlinePlayerData.get(PlayerID).userMessage + userMessage;
    }

    public static int getLastTimeAccessedFMenu(UUID PlayerID)
    {
        return onlinePlayerData.get(PlayerID).lastTimeAccessedFMenu;
    }

    public static void setLastTimeAccessedFMenu(UUID PlayerID, int lastAccessTimeTick)
    {
        onlinePlayerData.get(PlayerID).lastTimeAccessedFMenu = lastAccessTimeTick;
    }

    public static int getlastTimeInteractedSign(UUID PlayerID)
    {
        return onlinePlayerData.get(PlayerID).lastTimeInteractedSign;
    }
    */
    public static userOnlineData getPlayerOnlineData(UUID PlayerID)
    {
        return onlinePlayerData.get(PlayerID);
    }
    //*
    public static userOnlineData setPlayerOnlineData(UUID PlayerID, userOnlineData playerOnlineData)
    {
        return onlinePlayerData.put(PlayerID, playerOnlineData);
    }

    public static void setlastTimeInteractedSign(UUID PlayerID, int lastAccessTimeTick)
    {
        onlinePlayerData.get(PlayerID).lastTimeInteractedSign = lastAccessTimeTick;
    }

    public static void setPos1Selected(UUID PlayerID,boolean pos1IsSelected)
    {
        //onlinePlayerData.get(PlayerID).PRIEditData.pos1_isPRISelected = pos1IsSelected;
    }
    public static void setPos2Selected(UUID PlayerID,boolean pos2IsSelected)
    {
        //onlinePlayerData.get(PlayerID).PRIEditData.pos2_isPRISelected = pos2IsSelected;
    }
    public static boolean getIsPos1Selected(UUID PlayerID)
    {
        //return onlinePlayerData.get(PlayerID).PRIEditData.pos1_isPRISelected;
    }
    public static boolean getIsPos2Selected(UUID PlayerID)
    {
        return onlinePlayerData.get(PlayerID).PRIEditData.pos2_isPRISelected;
    }
    public static void resetPRIPos(UUID PlayerID, int posNumber, boolean state)
    {
        userOnlineData uData = onlinePlayerData.get(PlayerID);

        if (posNumber == 1)
        {
            uData.PRIEditData.pos1_isPRISelected = state;
        }
        else if (posNumber == 2)
        {
            uData.PRIEditData.pos2_isPRISelected = state;
        }
    }
    public static void setPos1PRI(UUID PlayerID, int axisX, int axisZ)
    {
        userOnlineData uData = onlinePlayerData.get(PlayerID);
        uData.PRIEditData.pos1_priSelectX = axisX;
        uData.PRIEditData.pos1_priSelectZ = axisZ;
    }
    public static void setPos2PRI(UUID PlayerID, int axisX, int axisZ)
    {
        userOnlineData uData = onlinePlayerData.get(PlayerID);
        uData.PRIEditData.pos2_priSelectX = axisX;
        uData.PRIEditData.pos2_priSelectZ = axisZ;
    }
    public static int getLeftPRI(UUID PlayerID)
    {
        userOnlineData uData = onlinePlayerData.get(PlayerID);
        return uData.usData.PRISqAll - uData.usData.PRISqUsed;
    }
    public static int getMaxPRI(UUID PlayerID)
    {
        return onlinePlayerData.get(PlayerID).usData.PRISqAll;
    }
    public static int getUsedPRI(UUID PlayerID)
    {
        return onlinePlayerData.get(PlayerID).usData.PRISqUsed;
    }
    //*/
    public static void playerLogoutSave(UUID uniqueID)
    {
        saveUDB(uniqueID);
    }
    //*

    public static Map<String, PRI> getPlayerPriData(UUID playerID)
    {
        //return userPRIData.get(PlayerID);
        return loadPlayerPriData(playerID);
    }
    public static void setPlayerPriData(UUID playerID, Map<String, PRI> PRIData)
    {
        savePRIData(playerID, PRIData);
        //userPRIData.put(PlayerID, PRIData);
    }
    public static void savePRIData(UUID PlayerID, Map<String, PRI> PRIData)
    {
        String fileName = "userPRI\\"+PlayerID+".pri";
        try
        {
            //Map<String, PRI> playerPRIData = getPRIData(PlayerID);
            citm_stdio.saveFile(fileName, PRIData);
        }
        catch (IOException e)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("saveFailed: "+fileName);
            Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
            Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
        }
    }
    */
    public static void saveUDB(UUID uniqueID)
    {
        String fileName = "userData\\"+uniqueID+".data";
        try
        {
            citmUserSave playerSData = onlinePlayerData.get(uniqueID).usData;
            //playerSData.money = playerSData.money +100;

            citm_stdio.saveFile(fileName, playerSData);
        }
        catch (IOException e)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("saveFailed: "+fileName);
            Bukkit.getServer().getConsoleSender().sendMessage(e.getMessage());
        }
    }
}

