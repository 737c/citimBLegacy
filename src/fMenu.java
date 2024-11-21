package org.citmb2.citmb;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.abs;

public class fMenu_legacy
{
    public static BaseComponent bComponentToChatScreen(List<TextComponent> tComponentList, UUID playerID)
    {
        BaseComponent bComponent = new TextComponent("");
        String userMessage = citmUDB.getUserMessage(playerID);

        for (int i = 0; i < 10; i++)
        {
            bComponent.addExtra("\n");
        }
        bComponent.addExtra(userMessage+"\n");
        for (int i = 0; i < tComponentList.size(); i++)
        {
            bComponent.addExtra(tComponentList.get(i));
        }

        citmUDB.setUserMessage(playerID, "");

        return bComponent;
    }

    public static BaseComponent bComponentToChatScreen(List<TextComponent> tComponentList)
    {
        BaseComponent bComponent = new TextComponent("");

        for (int i = 0; i < 10; i++)
        {
            bComponent.addExtra("\n");
        }
        bComponent.addExtra("\n");
        for (int i = 0; i < tComponentList.size(); i++)
        {
            bComponent.addExtra(tComponentList.get(i));
        }

        return bComponent;
    }

    @Deprecated
    public static TextComponent tComponentGen(String linkText, String linkCommd)
    {
        return tComponentGen(linkText, linkCommd, ChatColor.YELLOW);
    }
    @Deprecated
    public static TextComponent tComponentGen_color(String text, ChatColor textColor)
    {
        TextComponent tComponent = new TextComponent(text);
        tComponent.setColor(textColor);

        return tComponent;
    }

    public static TextComponent tComponent_hoverInfo(String text, String hoverInfo)
    {
        return tComponent_hoverInfo(text, hoverInfo, ChatColor.YELLOW);
    }

    public static TextComponent tComponent_hoverInfo(String text, String hoverInfo, ChatColor textColor)
    {
        TextComponent tComponent = new TextComponent(text);
        tComponent.setColor(textColor);
        tComponent.setUnderlined(true);
        tComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverInfo).create()));

        return tComponent;
    }

    @Deprecated
    public static TextComponent tComponentGen(String linkText, String linkCommd, ChatColor textColor)
    {
        TextComponent tComponent = new TextComponent(linkText);
        tComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, linkCommd));
        tComponent.setUnderlined(true);
        tComponent.setColor(textColor);
        tComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("クリック").create()));

        return tComponent;
    }
    public static TextComponent tComponent(String stringText)
    {
        return new TextComponent(stringText);
    }
    public static TextComponent tComponent(String stringText, ChatColor textColor)
    {
        TextComponent tComponent = new TextComponent(stringText);
        tComponent.setColor(textColor);

        return tComponent;
    }
    public static TextComponent tComponent_link(String linkText, String linkCommd, ChatColor textColor)
    {
        TextComponent tComponent = new TextComponent(linkText);
        tComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, linkCommd));
        tComponent.setUnderlined(true);
        tComponent.setColor(textColor);
        tComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("クリック").create()));

        return tComponent;
    }
    public static TextComponent tComponent_link(String linkText, String linkCommd)
    {
        TextComponent tComponent = new TextComponent(linkText);
        tComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, linkCommd));
        tComponent.setUnderlined(true);
        tComponent.setColor(ChatColor.YELLOW);
        tComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("クリック").create()));

        return tComponent;
    }

    public static TextComponent tComponentGen_stringInput(String linkText, String linkCommd)
    {
        return tComponentGen_stringInput(linkText, linkCommd, ChatColor.YELLOW);
    }

    public static TextComponent tComponentGen_stringInput(String linkText, String linkCommd, ChatColor textColor)
    {
        TextComponent tComponent = new TextComponent(linkText);
        tComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, linkCommd));
        tComponent.setUnderlined(true);
        tComponent.setColor(textColor);
        tComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("クリック").create()));

        return tComponent;
    }


    public static void fMenuEnt(UUID PlayerID)
    {
        int currentTick = Bukkit.getServer().getCurrentTick();
        int lastTimeTick = citmUDB.getLastTimeAccessedFMenu(PlayerID);
        if (abs(currentTick - lastTimeTick) < 20)
        {
            Bukkit.getServer().getConsoleSender().sendMessage("Too frequent access");
            return;
        }
        citmUDB.setLastTimeAccessedFMenu(PlayerID, currentTick);


        if (citmUDB.isPlayerPRIMode(PlayerID))
        {
            fMenu_pri.priInfo_claim(PlayerID);
        }
        else if (citmUDB.isMakingInvoiceMode(PlayerID))
        {
            fMenu_makeInvoice.show(PlayerID);
        }
        else
        {
            showMainMenu(PlayerID);
        }
    }
    public static void showMainMenu(UUID PlayerID)
    {
        Player player = Bukkit.getPlayer(PlayerID);

        String time_String = shareFanc.getTimeInString(PlayerID);
        String wather_string = shareFanc.getWeatherInString(PlayerID);
        //int pMoney = citmUDB.getPlayerMoney(PlayerID);

        BaseComponent bComponent = new TextComponent("");
        List<TextComponent> tComponentList = new ArrayList<>();

        TextComponent tComponent[] = new TextComponent[10];
        tComponentList.add(new TextComponent("[fMenu] =========== " + time_String + " " + wather_string+"\n"));
        //tComponentList.add(new TextComponent("所持金: "+pMoney+"円\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(tComponentGen("アクション\n","/citm-uie fMenu.action"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("ステータス\n"));
        tComponentList.add(tComponentGen("土地管理\n","/citm-uie fMenu_pri.priInfo_mainMenu"));
        tComponentList.add(tComponent_link("アイテム交換\n", "/citm-uie itemExchanger.itemExchangeList"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("[Ad] プレイエリア拡張中！！"));

        bComponent = bComponentToChatScreen( tComponentList);
        player.sendMessage(bComponent);
    }
     static void action(UUID PlayerID)
    {
        Player player = Bukkit.getPlayer(PlayerID);

        boolean flag_makeSignShop = false;
        boolean flag_cShop = false;

        // detect what block you are facing
        Block tBlock = player.getTargetBlock(5);

        if (!tBlock.isEmpty())
        {
            Material bType = tBlock.getType();
            if (shareFanc.isBTypeSign(bType))
            {
                boolean isSignEmpty = true;

                Sign signState = (Sign)tBlock.getState();
                for (int i = 0; i<4; i++)
                {
                    if (!signState.getLine(i).equals(""))
                    {
                        isSignEmpty = false;
                        break;
                    }
                }
                if (isSignEmpty)
                {
                    flag_makeSignShop = true;

                    Directional blockDir = (Directional) tBlock.getBlockData();
                    Block connectedBlock = tBlock.getRelative(blockDir.getFacing().getOppositeFace());
                    if (connectedBlock.getType().equals(Material.CHEST))
                    {
                        flag_makeSignShop = true;
                    }
                }
                else
                {
                    if (player.getInventory().getItemInMainHand().getType()  == Material.AIR)
                    {
                        Location loc = tBlock.getLocation();
                        int[] signXYZ = new int[3];
                        signXYZ[0] = loc.getBlockX();
                        signXYZ[1] = loc.getBlockY();
                        signXYZ[2] = loc.getBlockZ();

                        flag_cShop = true;
                    }
                }
            }

        }

        BaseComponent bComponent;
        List<TextComponent> tComponentList = new ArrayList<>();

        tComponentList.add(new TextComponent("[アクション] ===========\n"));
        tComponentList.add(new TextComponent("\n"));
        if (flag_makeSignShop)
        {
            Location bLoc = player.getTargetBlock(5).getLocation();

            tComponentList.add(tComponent_link("販売","/citm-uie fMenu_makeInvoice.newInvoice "+ bLoc.getBlockX()+" "+bLoc.getBlockY()+" "+bLoc.getBlockZ(),ChatColor.AQUA));
            tComponentList.add(tComponent_link("看板ショップ作成\n","/citm-uie fMenu_makeInvoice.newInvoice buy "+ bLoc.getBlockX()+" "+bLoc.getBlockY()+" "+bLoc.getBlockZ()));

            tComponentList.add(tComponent_link("売却","/citm-uie fMenu_makeInvoice.newInvoice "+ bLoc.getBlockX()+" "+bLoc.getBlockY()+" "+bLoc.getBlockZ(),ChatColor.RED));
            tComponentList.add(tComponent_link("看板ショップ作成\n","/citm-uie fMenu_makeInvoice.newInvoice sell "+ bLoc.getBlockX()+" "+bLoc.getBlockY()+" "+bLoc.getBlockZ()));
        }
        else
        {
            tComponentList.add(new TextComponent("表示可能なアクションがありません。\n"));
        }
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(new TextComponent("\n"));
        tComponentList.add(tComponentGen("<< 戻る","/citm-uie fMenu.fMenuEnt"));

        bComponent = bComponentToChatScreen( tComponentList);
        player.sendMessage(bComponent);
    }

}
