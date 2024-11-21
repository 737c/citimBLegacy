package org.citmb2.citmb;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.Math.abs;
import static org.bukkit.Bukkit.getServer;

public class fMenu_pri
{
    public static void priInfo_listUp(UUID PlayerID)
    {
        Player player = Bukkit.getPlayer(PlayerID);

        //shareFanc.getUserPRI(PlayerID);



        BaseComponent bComponent;
        List<TextComponent> tComponentList = new ArrayList<>();
        tComponentList.add(new TextComponent("[取得済みの土地] =========== "+"\n"));

        int itemAmount = 2;
        Map<String, PRI> PRIData = citmUDB.getPlayerPriData(PlayerID);
        for (String PRINameKey : PRIData.keySet())
        {
            tComponentList.add(fMenu.tComponent(PRINameKey+" "));
            tComponentList.add(fMenu.tComponent_link("[詳細]","/citm-uie fMenu_pri.priInfo_priQuery "+PRINameKey));
            tComponentList.add(fMenu.tComponent("(期間: "+PRIData.get(PRINameKey).contractLimitTime.toString()+"まで)\n", ChatColor.GRAY));
            itemAmount++;
        }
        if (PRIData.size() == 0)
        {
            tComponentList.add(new TextComponent("\n"));
            tComponentList.add(new TextComponent("表示できる内容がありません。\n"));
            tComponentList.add(new TextComponent("\n"));
            itemAmount = itemAmount +3;
        }

        for (int i = 0; i < (10 - itemAmount); i++)
        {
            tComponentList.add(new TextComponent("\n"));
        }


        tComponentList.add(fMenu.tComponentGen("<<戻る","/citm-uie fMenu_pri.priInfo_mainMenu"));

        bComponent = fMenu.bComponentToChatScreen(tComponentList);
        player.sendMessage(bComponent);
    }
    public static void priInfo_claim_posReset(UUID PlayerID, String resetPosString)
    {
        int resetPos;
        try
        {
            resetPos = Integer.parseInt(resetPosString);
        }
        catch (Exception e)
        {
            return;
        }

        Player player = Bukkit.getPlayer(PlayerID);
        if (resetPos == 1)
        {
            citmUDB.resetPRIPos(PlayerID, 1, false);
        }
        if (resetPos == 2)
        {
            citmUDB.resetPRIPos(PlayerID, 2, false);
        }
        player.performCommand("citm-uie fMenu_pri.priInfo_claim");
    }
    public static void priInfo_claim_posSet2(UUID PlayerID)
    {
        //citm-uie priInfo priClaim doSetPos2
        Player player = Bukkit.getPlayer(PlayerID);
        Location pLoc = player.getLocation();

        citmUDB.setPos2Selected(PlayerID, true);
        citmUDB.setPos2PRI(PlayerID, pLoc.getBlockX(), pLoc.getBlockZ());

        player.performCommand("citm-uie fMenu_pri.priInfo_claim");
    }
    public static void priInfo_claim_posSet1(UUID PlayerID)
    {
        //citm-uie priInfo priClaim doSetPos1
        Player player = Bukkit.getPlayer(PlayerID);
        Location pLoc = player.getLocation();

        citmUDB.setPos1Selected(PlayerID, true);
        citmUDB.setPos1PRI(PlayerID, pLoc.getBlockX(), pLoc.getBlockZ());

        player.performCommand("citm-uie fMenu_pri.priInfo_claim");
    }
    public static void priInfo_claim_cancel(UUID PlayerID)
    {
        citmUDB.setPlayerAMMode(PlayerID, false);
        Bukkit.getPlayer(PlayerID).performCommand("citm-uie fMenu.fMenuEnt");
    }
    public static int getSqSize(int[] pos1XZ, int[] pos2XZ)
    {
        int sizeX = abs(pos2XZ[0] - pos1XZ[0]);
        int sizeZ = abs(pos2XZ[1] - pos1XZ[1]);
        return (sizeX+1) * (sizeZ+1);
    }
    public static int[] getPriSizeXZ(UUID playerID, String priName)
    {
        Player player = Bukkit.getPlayer(playerID);
        World world = player.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));
        ProtectedRegion region = regions.getRegion(priName);


        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        int sizeX = max.getBlockX() - min.getBlockX();
        int sizeZ = max.getBlockZ() - min.getBlockZ();

        return new int[]{sizeX, sizeZ};
    }
    public static void priInfo_changePrice(UUID playerID, String priName, String PriChangedPriceString)
    {
        Player player = Bukkit.getPlayer(playerID);

        int priChangedPrice;
        try
        {
            priChangedPrice = Integer.parseInt(PriChangedPriceString);
        }catch (Exception e)
        {
            return;
        }
        int[] sizeXZ = getPriSizeXZ(playerID, priName);
        int sizeSq = sizeXZ[0] * sizeXZ[1];

        Map<String, PRI> userPriData = citmUDB.getPlayerPriData(playerID);
        PRI priData = userPriData.get(priName);

        int current_value = priData.valuedMoney;
        int current_weekRent = getWeekRent(current_value);
        int current_perceptibleAmount = getLandPerceptibleAmount(priData.valuedMoney);

        int after_value = priChangedPrice;
        int after_weekRent = getWeekRent(after_value);
        int after_perceptibleAmount = getLandPerceptibleAmount(priData.valuedMoney);

        int requestedAmount = (after_value - current_value) + after_weekRent;

        int playerMoney = citmUDB.getPlayerMoney(playerID);


        BaseComponent bComponent;
        List<TextComponent> tComponentList = new ArrayList<>();

        tComponentList.add(fMenu.tComponentGen_color("土地ID: "+priName+"\n", ChatColor.GRAY));
        tComponentList.add(new TextComponent("[価値変更]=================================\n"));
        tComponentList.add(fMenu.tComponent("現在価値: "+current_value+"円 (購入可能価格: "+current_perceptibleAmount+"円), 維持費: "+current_weekRent+"円 /週\n", ChatColor.BLUE));
        tComponentList.add(fMenu.tComponent("変更後: "+after_value+"円 (購入可能価格: "+after_perceptibleAmount+"円), 維持費: "+after_weekRent+"円 /週\n", ChatColor.AQUA));
        tComponentList.add(fMenu.tComponentGen_color("最低価値: "+sizeSq+"円\n", ChatColor.GRAY));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(fMenu.tComponent("必要金額: "+requestedAmount+"円 [("+after_value+" - "+current_value+"円)+"+after_weekRent+"円(新維持費)]/ "+playerMoney+"円 :所持金\n",ChatColor.YELLOW));
        tComponentList.add(fMenu.tComponentGen_color("すでに支払われた維持費が破棄され変更後の維持費を支払う必要があります。\n", ChatColor.GRAY));
        tComponentList.add(new TextComponent("変更しますか？ "));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(fMenu.tComponentGen("[はい]", "/citm-uie fMenu_pri.priInfo_changePrice_accept "+priName+" "+priChangedPrice));
        tComponentList.add(new TextComponent(" "));
        tComponentList.add(fMenu.tComponent_link("[いいえ]\n", "/citm-uie fMenu_pri.priInfo_priQuery　"+priName));
        tComponentList.add(fMenu.tComponent_link("<< 戻る", "/citm-uie fMenu_pri.priInfo_priQuery　"+priName));

        bComponent = fMenu.bComponentToChatScreen(tComponentList);
        player.sendMessage(bComponent);
    }
    public static void priInfo_changePrice_accept(UUID playerID, String priName, String PriChangedPriceString)
    {
        int priChangedPrice;
        try
        {
            priChangedPrice = Integer.parseInt(PriChangedPriceString);
        }catch (Exception e)
        {
            return;
        }
        if (citmUDB.takePlayerMoney(playerID ,priChangedPrice + getWeekRent(priChangedPrice)) < 0)
        {
            dConsole_easyMessage(playerID, "支払いに失敗しました。", "");
            return;
        }

        citmUDB.getPlayerPriData(playerID).get(priName).valuedMoney = priChangedPrice;

        dConsole_easyMessage(playerID, "変更しました！","", "変更完了");
    }

    public static void dConsole_easyMessage(UUID playerID, String messageText, String dialBackCommand)
    {
        dConsole_easyMessage(playerID, messageText, dialBackCommand, "メッセージ");
    }
    public static void dConsole_easyMessage(UUID playerID, String messageText, String dialBackCommand, String messageTitle)
    {
        if (dialBackCommand == "")
        {
            dialBackCommand = "/citm-uie fMenu.fMenuEnt";
        }

        Player player = Bukkit.getPlayer(playerID);
        BaseComponent bComponent;
        List<TextComponent> tComponentList = new ArrayList<>();

        tComponentList.add(new TextComponent("["+messageTitle+"]=================================\n"));
        tComponentList.add(new TextComponent(messageText+"\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(fMenu.tComponent_link("<< 戻る", dialBackCommand));

        bComponent = fMenu.bComponentToChatScreen(tComponentList);
        player.sendMessage(bComponent);
    }

    public static void priInfo_claim(UUID playerID)
    {
        // /citm-uie priInfo priClaim
        Player player = Bukkit.getPlayer(playerID);
        citmUDB.setPlayerAMMode(playerID, true);

        String pos1String = "設定されていません";
        String pos2String = "設定されていません";

        PRIEdit PRIEditData = citmUDB.getPRIEditData(playerID);
        int[] posAxis1 = {PRIEditData.pos1_priSelectX, PRIEditData.pos1_priSelectZ};
        int[] posAxis2 = {PRIEditData.pos2_priSelectX, PRIEditData.pos2_priSelectZ};

        boolean getIsPos1Selected = PRIEditData.pos1_isPRISelected;
        boolean getIsPos2Selected = PRIEditData.pos2_isPRISelected;

        if (getIsPos1Selected)
        {
            pos1String = "("+posAxis1[0]+", "+posAxis1[1]+")";
        }
        if (getIsPos2Selected)
        {
            pos2String = "("+posAxis2[0]+", "+posAxis2[1]+")";
        }

        int selectedPRISizeSq = 0;
        int userCurrentLeftPRI = citmUDB.getLeftPRI(playerID);

        if (getIsPos1Selected && getIsPos2Selected)
        {
            selectedPRISizeSq = getSqSize(posAxis1, posAxis2);
        }

        ChatColor sizeIndicatorColor = ChatColor.WHITE;

        int totalSelfMoney = citmUDB.getPlayerMoney(playerID);

        if (PRIEditData.setPrice < selectedPRISizeSq)
        {
            PRIEditData.setPrice = selectedPRISizeSq;
        }
        int landPrice = PRIEditData.setPrice;

        int expectedLandRent = (int)(landPrice*0.1);
        int totalCost = landPrice + expectedLandRent;

        boolean isPriOccupied = false;
        if ((selectedPRISizeSq < userCurrentLeftPRI) && (totalCost < totalSelfMoney) && (getIsPos1Selected && getIsPos2Selected))
        {
            int[] posXZSizeData = posConvert(posAxis1,posAxis2);
            int posX = posXZSizeData[0];
            int posZ = posXZSizeData[1];
            int sizeX = posXZSizeData[2];
            int sizeZ = posXZSizeData[3];

            isPriOccupied = isPriOccupied(posX, posZ, sizeX, sizeZ);
        }

        BaseComponent bComponent;
        List<TextComponent> tComponentList = new ArrayList<>();

        tComponentList.add(new TextComponent("[土地取得]===========\n"));
        tComponentList.add(fMenu.tComponentGen("地点1: "+pos1String+"","/citm-uie fMenu_pri.priInfo_claim_posSet1"));
        tComponentList.add(new TextComponent(" "));
        tComponentList.add(fMenu.tComponentGen("[リセット]\n","/citm-uie fMenu_pri.priInfo_claim_posReset 1"));
        tComponentList.add(fMenu.tComponentGen("地点2: "+pos2String+"","/citm-uie fMenu_pri.priInfo_claim_posSet2"));
        tComponentList.add(new TextComponent(" "));
        tComponentList.add(fMenu.tComponentGen("[リセット]\n","/citm-uie fMenu_pri.priInfo_claim_posReset 2"));
        tComponentList.add(new TextComponent("\n"));

        if (!(getIsPos1Selected && getIsPos2Selected))
        {
            tComponentList.add(fMenu.tComponent("土地価格: -円\n",ChatColor.GRAY));
            tComponentList.add(fMenu.tComponentGen_color("購入金額: "+totalCost+"円 ", ChatColor.GRAY));
            tComponentList.add(new TextComponent("/ "+totalSelfMoney+"円 :所持金\n"));

            tComponentList.add(new TextComponent("\n"));
            tComponentList.add(new TextComponent("\n"));
            tComponentList.add(fMenu.tComponentGen_color("選択土地サイズ: - / "+userCurrentLeftPRI+" Bk\n", sizeIndicatorColor));
        }
        else
        {
            tComponentList.add(new TextComponent("土地価格: "+PRIEditData.setPrice+"円 "));
            tComponentList.add(fMenu.tComponent("(最低価格: "+selectedPRISizeSq+"円) ", ChatColor.GRAY));
            tComponentList.add(fMenu.tComponentGen_stringInput("[変更]\n","/citm-uie fMenu_pri.priInfo_setLandPrice "+PRIEditData.setPrice));
            tComponentList.add(new TextComponent("購入金額: "+totalCost+"円 "));
            tComponentList.add(fMenu.tComponentGen_color("= "+landPrice+" + "+expectedLandRent, ChatColor.GRAY));
            tComponentList.add(new TextComponent("/ "+totalSelfMoney+"円 :所持金\n"));

            String hoverInfoText = "";
            if (selectedPRISizeSq < userCurrentLeftPRI)
            {
                if (selectedPRISizeSq < 100)
                {
                    if (totalCost < totalSelfMoney)
                    {
                        if (!isPriOccupied)
                        {
                            hoverInfoText = "";
                        }
                        else
                        {
                            hoverInfoText = "土地がすでに取得されています。";
                        }
                    }
                    else
                    {
                        hoverInfoText = "資金が足りません。";
                    }
                }
                else
                {
                    hoverInfoText = "100 Sq以上の取得はできません。";
                    sizeIndicatorColor = ChatColor.RED;
                }
            }
            else
            {
                hoverInfoText = "範囲がSq限度を超えています。";
                sizeIndicatorColor = ChatColor.RED;
            }

            if (hoverInfoText == "")
            {
                tComponentList.add(fMenu.tComponentGen("土地を購入する\n","/citm-uie fMenu_pri.priInfo_claim_register"));
            }
            else
            {
                tComponentList.add(fMenu.tComponent_hoverInfo("土地を購入する\n", hoverInfoText, ChatColor.GRAY));
            }

            tComponentList.add(new TextComponent("\n"));
            tComponentList.add(fMenu.tComponentGen_color("選択土地サイズ: "+selectedPRISizeSq+" / "+userCurrentLeftPRI+" Bk\n", sizeIndicatorColor));
        }

        tComponentList.add(fMenu.tComponentGen("[選択中止]","/citm-uie fMenu_pri.priInfo_claim_cancel", ChatColor.RED));

        bComponent = fMenu.bComponentToChatScreen( tComponentList);
        player.sendMessage(bComponent);
    }
    public static void priInfo_setLandPrice(UUID playerID, String landPriceString)
    {
        Player player = Bukkit.getPlayer(playerID);

        int landPrice;
        try
        {
            landPrice = Integer.parseInt(landPriceString);
        }
        catch (Exception e)
        {
            citmUDB.addUserMessage(playerID,"数字を入力してください\n");
            player.performCommand("citm-uie fMenu_pri.priInfo_claim");
            return;
        }

        PRIEdit priEdit = citmUDB.getPRIEditData(playerID);
        int[] posXZ1 = {priEdit.pos1_priSelectX, priEdit.pos1_priSelectZ};
        int[] posXZ2 = {priEdit.pos2_priSelectX, priEdit.pos2_priSelectZ};
        int SqSize = getSqSize(posXZ1, posXZ2);

        if (landPrice < SqSize)
        {
            citmUDB.addUserMessage(playerID,"最低金額を下回っています。\n");
            player.performCommand("citm-uie fMenu_pri.priInfo_claim");
            return;
        }

        PRIEdit PRIEditData = citmUDB.getPRIEditData(playerID);
        PRIEditData.setPrice = landPrice;
        citmUDB.setPRIEditData(playerID, PRIEditData);
        player.performCommand("citm-uie fMenu_pri.priInfo_claim");
    }
    public static void addPri(UUID playerID, String priName)
    {
        Map<String, PRI> PRIData = citmUDB.getPlayerPriData(playerID);

        Player player = Bukkit.getPlayer(playerID);
        World world = player.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));
        ProtectedRegion region = regions.getRegion(priName);

        PRIEdit priEdit =  citmUDB.getPRIEditData(playerID);

        PRI newPRI = new PRI();
        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();
        newPRI.landSq =  (max.getBlockX() - min.getBlockX()) * (max.getBlockZ() - min.getBlockZ());
        newPRI.contractLimitTime = LocalDate.now().plusDays(7);
        newPRI.valuedMoney = priEdit.setPrice;

        PRIData.put(priName, newPRI);
        citmUDB.setPlayerPriData(playerID, PRIData);

        userOnlineData onlineData = citmUDB.getPlayerOnlineData(playerID);
        onlineData.usData.PRISqUsed += newPRI.landSq;
        citmUDB.setPlayerOnlineData(playerID, onlineData);

        citmUDB.takePlayerMoney(playerID,(newPRI.valuedMoney + getWeekRent(newPRI.valuedMoney)));
    }
    public static boolean isPriEditClammable(UUID playerID)
    {
        int userLeftPriSq = citmUDB.getLeftPRI(playerID);
        PRIEdit priEdit = citmUDB.getPRIEditData(playerID);

        int[] posXZ1 = {priEdit.pos1_priSelectX, priEdit.pos1_priSelectZ};
        int[] posXZ2 = {priEdit.pos2_priSelectX, priEdit.pos2_priSelectZ};

        if(priEdit.setPrice > citmUDB.getPlayerMoney(playerID))
        {
            citmUDB.addUserMessage(playerID,"資金が不足しています\n");
            Bukkit.getServer().getConsoleSender().sendMessage("資金が不足しています\n");
            return false;
        }

        int sqSize = getSqSize(posXZ1, posXZ2);
        if (userLeftPriSq < sqSize)
        {
            citmUDB.addUserMessage(playerID,"取得可能サイズを超えています。\n");
            Bukkit.getServer().getConsoleSender().sendMessage("取得可能サイズを超えています。\n");
            return false;
        }
        if (100 < sqSize)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("一度に取得可能なサイズは100Sq以下です。\n");
            citmUDB.addUserMessage(playerID,"一度に取得可能なサイズは100Sq以下です。\n");
            return false;
        }
        return true;
    }
    public static void priInfo_claim_register(UUID playerID)
    {
        Player player = Bukkit.getPlayer(playerID);
        if (!isPriEditClammable(playerID))
        {
            Bukkit.getServer().getConsoleSender().sendMessage("取得に失敗しました。\n");
            citmUDB.addUserMessage(playerID,"取得に失敗しました。\n");
            player.performCommand("citm-uie fMenu.fMenuEnt");
            return;
        }

        PRIEdit priEdit = citmUDB.getPRIEditData(playerID);
        int[] posXZ1 = {priEdit.pos1_priSelectX, priEdit.pos1_priSelectZ};
        int[] posXZ2 = {priEdit.pos2_priSelectX, priEdit.pos2_priSelectZ};

        String resultPRIName = userPRI_WGRegister(playerID,posXZ1,posXZ2);
        if (resultPRIName.equals(""))
        {
            Bukkit.getServer().getConsoleSender().sendMessage("エラーが発生しました。\n");
            citmUDB.addUserMessage(playerID,"エラーが発生しました。\n");
            return;
        }
        addPri(playerID, resultPRIName);

        drawPriClaimCompleteScreen(playerID, resultPRIName);
    }
    public static void drawPriClaimCompleteScreen(UUID playerID, String PriName)
    {
        Player player = Bukkit.getPlayer(playerID);
        PRI userPRI = citmUDB.getPlayerPriData(playerID).get(PriName);

        String landEndTermStaring = userPRI.contractLimitTime.toString();

        BaseComponent bComponent;
        List<TextComponent> tComponentList = new ArrayList<>();

        tComponentList.add(new TextComponent("[土地購入成功]=================================\n"));
        tComponentList.add(new TextComponent("土地を購入しました！\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("維持期間: "+landEndTermStaring+" まで\n"));
        tComponentList.add(fMenu.tComponentGen("購入した土地を確認","/citm-uie fMenu_pri.priInfo_priQuery "+PriName));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(fMenu.tComponentGen("<< 戻る","/citm-uie fMenu.fMenuEnt"));

        bComponent = fMenu.bComponentToChatScreen(tComponentList);
        player.sendMessage(bComponent);
    }

    public static void priInfo_priQuery(UUID playerID, String PriName)
    {
        Player player = Bukkit.getPlayer(playerID);
        World world = player.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));

        ProtectedRegion region = regions.getRegion(PriName);
        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();


        UUID regionOwnerID;
        try
        {
            regionOwnerID = region.getOwners().getUniqueIds().iterator().next();
        }
        catch (Exception e)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("cound't find the owner UUID on region\n");
            //cound't find the owner UUID on region
            return;
        }
        PRI priData = citmUDB.getPlayerPriData(regionOwnerID).get(PriName);

        int landPerceptibleAmount = getLandPerceptibleAmount(priData.valuedMoney);
        int weekRent = getWeekRent(priData.valuedMoney);
        int sqSize = getSqSize(new int[]{min.getBlockX(), min.getBlockZ()},new int[]{max.getBlockX(),max.getBlockZ()});

        BaseComponent bComponent;
        List<TextComponent> tComponentList = new ArrayList<>();

        tComponentList.add(fMenu.tComponentGen_color("土地ID: "+PriName+"\n", ChatColor.GRAY));
        tComponentList.add(new TextComponent("[土地詳細]=================================\n"));
        tComponentList.add(fMenu.tComponent("愛称:  "+PriName, ChatColor.DARK_GRAY));
        tComponentList.add(fMenu.tComponent_hoverInfo("[変更]\n", "現在変更できません。",ChatColor.DARK_GRAY));
        tComponentList.add(new TextComponent("位置: ("+min.getBlockX()+", "+min.getBlockZ()+"),("+max.getBlockX()+", "+max.getBlockZ()+")/ サイズ: "+sqSize+" Sq\n"));
        tComponentList.add(new TextComponent("維持期間: "));
        tComponentList.add(fMenu.tComponent(priData.contractLimitTime+" ", ChatColor.AQUA));
        tComponentList.add(new TextComponent("まで "));
        tComponentList.add(fMenu.tComponent_link("[延長]\n","/citm-uie fMenu_pri.priInfo_extendTerm "+PriName));

        tComponentList.add(fMenu.tComponent("維持費: "));
        tComponentList.add(fMenu.tComponent(weekRent+"", ChatColor.AQUA));
        tComponentList.add(fMenu.tComponent("円 "));
        tComponentList.add(fMenu.tComponent("/1週間 ", ChatColor.GRAY));
        tComponentList.add(fMenu.tComponent_hoverInfo("(?)\n","維持費は土地の価値の10%です。\n毎週支払うと土地を維持できます。"));

        tComponentList.add(new TextComponent("価値: "+priData.valuedMoney+"円 (1 Bk当たり"+(priData.valuedMoney/sqSize)+"円) "));
        tComponentList.add(fMenu.tComponentGen_stringInput("[変更]\n","/citm-uie fMenu_pri.priInfo_changePrice "+PriName+" "+priData.valuedMoney));

        tComponentList.add(fMenu.tComponent("買戻し予算: 0円 [変更] ",ChatColor.GRAY));
        tComponentList.add(fMenu.tComponent_hoverInfo("(?)","誰かがこの土地を購入したときに買い戻すためのお金です。\nこの金額を超えて購入された場合は手動で買い戻すことになります。"));
        tComponentList.add(new TextComponent(" (実装中)\n"));
        tComponentList.add(fMenu.tComponent_link(">土地を売却する<\n","/citm-uie fMenu_pri.sellPri_accept "+PriName,  ChatColor.RED));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(fMenu.tComponentGen("<< 戻る","/citm-uie fMenu.fMenuEnt"));

        bComponent = fMenu.bComponentToChatScreen(tComponentList);
        player.sendMessage(bComponent);
    }
    public static void sellPri_accept(UUID playerID, String priName)
    {
        if (!isPlayerPRIOwner(playerID, priName))
        {
            return ;
        }

        Map<String, PRI> priPlayerData = citmUDB.getPlayerPriData(playerID);
        PRI priData = getPriData(playerID,priName);

        priPlayerData.remove(priName);
        citmUDB.setPlayerPriData(playerID,priPlayerData);

        Player player = Bukkit.getPlayer(playerID);
        World world = player.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));

        regions.removeRegion(priName);

        int sellPrice = priData.valuedMoney;
        citmUDB.sendPlayerMoney(playerID, sellPrice);

        dConsole_easyMessage(playerID,"売却しました", "");
    }
    public static void draw_sellPri(UUID playerID, String PriName)
    {
        Player player = Bukkit.getPlayer(playerID);

        if (isPlayerPRIOwner(playerID, PriName))
        {
            return ;
        }

        BaseComponent bComponent;
        List<TextComponent> tComponentList = new ArrayList<>();

        tComponentList.add(fMenu.tComponent("土地ID: "+PriName+"\n", ChatColor.GRAY));
        tComponentList.add(fMenu.tComponent("[土地売却]=================================\n"));
        tComponentList.add(fMenu.tComponent("土地名: pri-1001\n"));
        tComponentList.add(fMenu.tComponent("土地ID: pri-1001\n",ChatColor.GRAY));
        tComponentList.add(fMenu.tComponent("\n"));
        tComponentList.add(fMenu.tComponent("売却価格: 1000円\n"));
        tComponentList.add(fMenu.tComponent("支払いを終えた維持費は返金されません。\n", ChatColor.GRAY));
        tComponentList.add(fMenu.tComponent("土地を売却しますか？\n"));
        tComponentList.add(fMenu.tComponent_link("[はい]", "/citm-uie sellPri_accept "+PriName));
        tComponentList.add(fMenu.tComponent(" "));
        tComponentList.add(fMenu.tComponent_link("[いいえ]\n","/citm-uie priInfo_priQuery "+PriName));
        tComponentList.add(fMenu.tComponent_link("<< 戻る","/citm-uie priInfo_priQuery "+PriName));
        tComponentList.add(fMenu.tComponent("\n"));

        bComponent = fMenu.bComponentToChatScreen(tComponentList);
        player.sendMessage(bComponent);
    }
    public static int getLandPerceptibleAmount(int valuedMoney)
    {
        return (int) (valuedMoney+(valuedMoney * 0.25));
    }
    public static void priInfo_extendTerm(UUID playerID, String PriName)
    {

        if (!isPlayerPRIOwner(playerID, PriName))
        {
            Bukkit.getServer().getConsoleSender().sendMessage("Ownerではありません\n");
            return;
        }
        PRI PRIData = citmUDB.getPlayerPriData(playerID).get(PriName);

        int weekRent = getWeekRent(PRIData.valuedMoney);
        String plus1w = PRIData.contractLimitTime.plusDays(7).toString();
        int rentPrice_1w = weekRent;

        BaseComponent bComponent;
        List<TextComponent> tComponentList = new ArrayList<>();

        tComponentList.add(fMenu.tComponentGen_color("土地ID: "+PriName+"\n", ChatColor.GRAY));
        tComponentList.add(new TextComponent("[維持期間延長]=================================\n"));
        tComponentList.add(new TextComponent("現在期間: "+PRIData.contractLimitTime+" まで\n"));
        tComponentList.add(fMenu.tComponentGen_color("最大 1カ月 まで延長できます。\n", ChatColor.GRAY));
        tComponentList.add(fMenu.tComponent_link("1週間("+plus1w+" まで)延長する。\n","/citm-uie fMenu_pri.priInfo_extendTerm_accept "+PriName));
        tComponentList.add(new TextComponent("金額: "+rentPrice_1w+"円\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("所持金: "+citmUDB.getPlayerMoney(playerID)+"円\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(fMenu.tComponentGen("<< 戻る","/citm-uie fMenu_pri.priInfo_listUp"));

        bComponent = fMenu.bComponentToChatScreen(tComponentList);
        Player player = Bukkit.getPlayer(playerID);
        player.sendMessage(bComponent);
    }
    public static PRI getPriData(UUID playerID, String PriName)
    {
        Map<String, PRI> playerPriData = citmUDB.getPlayerPriData(playerID);
        return playerPriData.get(PriName);
    }
    public static void setPriData(UUID playerID, String PriName, PRI priData)
    {
        //Map<String, PRI> playerPriData = citmUDB.getPlayerPriData(playerID);
        //return playerPriData.get(PriName);
        Map<String, PRI> playerPriData = citmUDB.getPlayerPriData(playerID);
        playerPriData.put(PriName, priData);
        citmUDB.setPlayerPriData(playerID, playerPriData);
    }
    public static void priInfo_extendTerm_accept(UUID playerID, String PriName)
    {
        if (!isPlayerPRIOwner(playerID, PriName))
        {
            return;
        }

        PRI priData  = getPriData(playerID, PriName);

        LocalDate maxExtensionTerm = LocalDate.now().plusDays(7*4);// 1month/4 weeks
        LocalDate extendedTime = priData.contractLimitTime.plusDays(7);

        if (extendedTime.compareTo(maxExtensionTerm) > 0)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("延長期間が1カ月を超えています\n");
            return;
        }

        if (citmUDB.getPlayerMoney(playerID) < getWeekRent(priData.valuedMoney))
        {
            Bukkit.getServer().getConsoleSender().sendMessage("資金が足りません。\n");
            return;
        }

        priData.contractLimitTime = extendedTime;
        setPriData(playerID, PriName, priData);
        citmUDB.takePlayerMoney(playerID, getWeekRent(priData.valuedMoney));

        dConsole_easyMessage(playerID, "延長しました！", "fMenu_pri.priInfo_priQuery "+PriName,"延長完了");
    }
    public static int getWeekRent(int landValue)
    {
        return (int) (landValue * 0.1);
    }
    public static boolean isPlayerPRIOwner(UUID playerID, String PriName)
    {
        Player player = Bukkit.getPlayer(playerID);
        World world = player.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));

        ProtectedRegion region = regions.getRegion(PriName);
        try
        {
            return region.getOwners().getUniqueIds().contains(playerID);
        }
        catch (Exception e)
        {
            //cound't find the owner UUID on region
            return false;
        }
    }
    public static int[] posConvert(int[] posXZ_A, int[] posXZ_B)
    {
        int[] returnValue = new int[4];

        int[] pos1XZ = new  int[2];
        int[] pos2XZ = new  int[2];

        if (posXZ_A[0] < posXZ_B[0])
        {
            pos1XZ[0] = posXZ_A[0];
            pos2XZ[0] = posXZ_B[0];
        }
        else
        {
            pos1XZ[0] = posXZ_B[0];
            pos2XZ[0] = posXZ_A[0];
        }

        if (posXZ_A[1] < posXZ_B[1])
        {
            pos1XZ[1] = posXZ_A[1];
            pos2XZ[1] = posXZ_B[1];
        }
        else
        {
            pos1XZ[1] = posXZ_B[1];
            pos2XZ[1] = posXZ_A[1];
        }

        int sizeX = (pos1XZ[0] - pos2XZ[0])+1;
        int sizeZ = (pos1XZ[1] - pos2XZ[1])+1;

        returnValue[0] = pos1XZ[0];
        returnValue[1] = pos1XZ[1];
        returnValue[2] = sizeX;
        returnValue[3] = sizeZ;

        return returnValue;
    }

    public static boolean isPriOccupied(int posX, int posZ, int sizeX, int sizeZ)
    {
        // find pre occupated PRI
        List<String> foundPRI = adaptor.findPRI(posX, posZ, sizeX, sizeZ);
        if (0 < foundPRI.size())
        {
            // return message "there is pre occupated PRI"
            return true;
        }
        return false;
    }
    public static String userPRI_WGRegister(UUID playerID, int[] posXZ1, int[] posXZ2)
    {
        int[] posXZSizeData = posConvert(posXZ1,posXZ2);
        int posX = posXZSizeData[0];
        int posZ = posXZSizeData[1];
        int sizeX = posXZSizeData[2];
        int sizeZ = posXZSizeData[3];

        Player player = Bukkit.getPlayer(playerID);

        // find that the area is within PUB area
        if (!adaptor.checkWithinPUB(posX, posZ, sizeX, sizeZ))
        {
            // return message "out of world bounce"
            return "";
        }

        if (isPriOccupied(posX, posZ, sizeX, sizeZ))
        {
            return "";
        }

        // get PUB middle ground
        List<String>[] regionID = new List[4];
        regionID[0] = adaptor.findPUB(posX, posZ,1,1);
        regionID[1] = adaptor.findPUB(posX, (posZ+sizeZ)-1,1,1);
        regionID[2] = adaptor.findPUB((posX+sizeX)-1, posZ,1,1);
        regionID[3] = adaptor.findPUB((posX+sizeX)-1, (posZ+sizeZ)-1,1,1);

        World world = player.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));

        int[] PUBmidY = new int[4];
        for (int i = 0; i < 4; i++)
        {
            PUBmidY[i] = regions.getRegion(regionID[i].get(0)).getMinimumPoint().getBlockY();
        }
        int PRIMidY = (PUBmidY[0]+PUBmidY[1]+PUBmidY[2]+PUBmidY[3])/4;

        BlockVector3 cMin = BlockVector3.at(posX, PRIMidY -2,posZ);
        BlockVector3 cMax = BlockVector3.at(posX+sizeX, PRIMidY +6,posZ+sizeZ);

        Plugin plugin = getServer().getPluginManager().getPlugin("CitmPlugin");
        int newPRIID = shareFanc.getConfigInt("newPRIID");

        String areaName = "pri-" + newPRIID;
        ProtectedRegion region = new ProtectedCuboidRegion(areaName, cMin, cMax);
        region.setPriority(20);
        //region.setFlag(Flags.BUILD, StateFlag.State.ALLOW);

        DefaultDomain owners = region.getMembers();
        owners.addPlayer(playerID);
        if (!player.isOp())
        {
            region.setOwners(owners);
        }
        regions.addRegion(region);



        shareFanc.setConfigInt("newPRIID", newPRIID+1);

        return areaName;
    }
    public static void priInfo_mainMenu(UUID PlayerID)
    {
        Player player = Bukkit.getPlayer(PlayerID);
        Location pLoc = player.getLocation();

        String PRIAvailable = "利用可";
        ChatColor PRIAvailableColor = ChatColor.AQUA;
        List<String> foundPRI = adaptor.findPRI(pLoc.getBlockX(), pLoc.getBlockZ(), 1, 1);
        boolean PRIOccupied = false;
        boolean outOfBounce = false;
        if (0 < foundPRI.size())
        {
            PRIAvailable = "利用中";
            PRIAvailableColor = ChatColor.GRAY;
            PRIOccupied = true;
        }
        List<String> foundPUB = adaptor.findPUB(pLoc.getBlockX(), pLoc.getBlockZ(), 1, 1);
        if (foundPUB.size() < 1)
        {
            PRIAvailable = "利用不可";
            PRIAvailableColor = ChatColor.RED;
            outOfBounce = true;
        }

        int leftUserPRI = citmUDB.getLeftPRI(PlayerID);
        int usedUserPRI = citmUDB.getUsedPRI(PlayerID);

        BaseComponent bComponent;
        List<TextComponent> tComponentList = new ArrayList<>();
        tComponentList.add(new TextComponent("[土地管理] =========== "+"\n"));
        tComponentList.add(new TextComponent("現在位置: ("+pLoc.getBlockX()+", "+pLoc.getBlockZ()+")\n"));
        tComponentList.add(fMenu.tComponent("利用状況: "+PRIAvailable+"\n",PRIAvailableColor));

        if (!PRIOccupied && (leftUserPRI > 0) && !outOfBounce)
        {
            tComponentList.add(fMenu.tComponentGen("土地を取得する\n","/citm-uie fMenu_pri.priInfo_claim"));
        }
        else
        {
            tComponentList.add(fMenu.tComponentGen_color("土地を取得する\n", ChatColor.GRAY));
        }

        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("取得可能土地サイズ: "+leftUserPRI+" Bk\n"));
        tComponentList.add(new TextComponent("取得済み土地サイズ: "+usedUserPRI+" Bk\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(fMenu.tComponent_link("取得済み土地一覧\n","/citm-uie fMenu_pri.priInfo_listUp", ChatColor.AQUA));
        tComponentList.add(fMenu.tComponentGen("<<戻る","/citm-uie fMenu.fMenuEnt"));

        bComponent = fMenu.bComponentToChatScreen( tComponentList);
        player.sendMessage(bComponent);
    }
}
