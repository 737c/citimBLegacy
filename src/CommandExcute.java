package org.citmb2.citmb;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandExcute implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        //*
        if (command.getName().equalsIgnoreCase("citm-admin"))
        {
            if (args[0].equalsIgnoreCase("admin_newGame.newGame"))
            {
                admin_newGame.newGame(((Player)sender).getUniqueId(), args[1]);
            }
            if (args[0].equalsIgnoreCase("aat-pub"))
            {
                if (args[1].equalsIgnoreCase("add"))
                {
                    adminTools.adminTools_areaAssignTool_mcaa(
                            Integer.parseInt(args[2]),
                            Integer.parseInt(args[3]),
                            Integer.parseInt(args[4]),
                            Integer.parseInt(args[5])
                    );
                }
                //adminTools_areaAssignTool_deletePubArea
                if (args[1].equalsIgnoreCase("del"))
                {
                    adminTools.adminTools_areaAssignTool_deletePubArea(
                            Integer.parseInt(args[2]),
                            Integer.parseInt(args[3]),
                            Integer.parseInt(args[4]),
                            Integer.parseInt(args[5])
                    );
                }
            }
            if (args[0].equalsIgnoreCase("aat-pri"))
            {
                if (args[1].equalsIgnoreCase("add"))
                {
                    adminTools.adminTools_areaAssignTool_PRI(
                            ((Player)sender).getUniqueId(),
                            true,
                            Integer.parseInt(args[2]),
                            Integer.parseInt(args[3]),
                            Integer.parseInt(args[4]),
                            Integer.parseInt(args[5])
                    );
                }
                //adminTools_areaAssignTool_deletePubArea
                if (args[1].equalsIgnoreCase("del"))
                {
                    adminTools.adminTools_areaAssignTool_deletePRIArea(
                            ((Player)sender).getUniqueId(),
                            Integer.parseInt(args[2]),
                            Integer.parseInt(args[3]),
                            Integer.parseInt(args[4]),
                            Integer.parseInt(args[5])
                    );
                }
            }
            if (args[0].equalsIgnoreCase("test"))
            {
                shareFanc.test_seeUserName();
            }
            if (args[0].equalsIgnoreCase("test-sendMoney"))
            {
                if (args.length == 3)
                {
                    shareFanc.sendMoney(args[1], Integer.parseInt(args[2]));
                }
            }
            if (args[0].equalsIgnoreCase("addLandSq"))
            {
                adminTools.addLandSq(((Player)sender).getUniqueId(), args[1]);
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("citm-uie"))
        {
            if (args[0].equalsIgnoreCase("fMenu_pri.priInfo_priQuery"))
            {
                fMenu_pri.priInfo_priQuery(((Player)sender).getUniqueId(), args[1]);
            }
            if (args[0].equalsIgnoreCase("fMenu_pri.priInfo_claim"))
            {
                fMenu_pri.priInfo_claim(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("fMenu_pri.priInfo_listUp"))
            {
                fMenu_pri.priInfo_listUp(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("fMenu_pri.priInfo_extendTerm"))
            {
                try
                {
                    fMenu_pri.priInfo_extendTerm(((Player)sender).getUniqueId(), args[1]);
                }
                catch (Exception ignore){}

            }//priInfo_extendTerm_accept
            if (args[0].equalsIgnoreCase("fMenu_pri.priInfo_extendTerm_accept"))
            {
                try
                {
                    fMenu_pri.priInfo_extendTerm_accept(((Player)sender).getUniqueId(), args[1]);
                }
                catch (Exception ignore){}
            }
            if (args[0].equalsIgnoreCase("fMenu_pri.draw_sellPri"))
            {
                try
                {
                    fMenu_pri.draw_sellPri(((Player)sender).getUniqueId(), args[1]);
                }
                catch (Exception ignore){}
            }
            if (args[0].equalsIgnoreCase("fMenu_pri.sellPri_accept"))
            {
                try
                {
                    fMenu_pri.sellPri_accept(((Player)sender).getUniqueId(), args[1]);
                }
                catch (Exception ignore){}
            }
            if (args[0].equalsIgnoreCase("itemExchanger.itemExchangeList"))
            {
                itemExchanger.itemExchangeList(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("itemExchanger.itemExchange"))
            {
                try
                {
                    itemExchanger.itemExchange(((Player)sender).getUniqueId(),args[1]);
                }catch (Exception ignore){}

            }
            if (args[0].equalsIgnoreCase("fMenu_pri.priInfo_changePrice"))
            {
                fMenu_pri.priInfo_changePrice(((Player)sender).getUniqueId(), args[1], args[2]);
            }
            if (args[0].equalsIgnoreCase("fMenu_pri.priInfo_changePrice_accept"))
            {
                fMenu_pri.priInfo_changePrice_accept(((Player)sender).getUniqueId(), args[1], args[2]);
            }
            if (args[0].equalsIgnoreCase("fMenu.fMenuEnt"))
            {
                fMenu.fMenuEnt(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("fMenu_pri.priInfo_mainMenu"))
            {
                fMenu_pri.priInfo_mainMenu(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("fMenu.action"))
            {
                fMenu.action(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("fMenu_makeInvoice.newInvoice"))
            {
                fMenu_makeInvoice.newInvoice(((Player)sender).getUniqueId(),args[1],args[2],args[3],args[4]);
            }
            if (args[0].equalsIgnoreCase("fMenu_makeInvoice.putMaxAmount"))
            {
                fMenu_makeInvoice.putMaxAmount(((Player)sender).getUniqueId(), args[1]);
            }
            if (args[0].equalsIgnoreCase("shareFanc.fMenu_makeInvoice_quit"))
            {
                shareFanc.fMenu_makeInvoice_quit(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("fMenu_makeInvoice.show"))
            {
                fMenu_makeInvoice.show(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("fMenu_makeInvoice.putItemName"))
            {
                String nameString = "";
                for (int i = 1; i<args.length; i++)
                {
                    nameString = nameString + args[i];
                }
                fMenu_makeInvoice.putItemName(((Player)sender).getUniqueId(), nameString);
            }
            if (args[0].equalsIgnoreCase("fMenu_makeInvoice.putPrice"))
            {
                try {fMenu_makeInvoice.putPrice(((Player)sender).getUniqueId(), args[1]);}catch (Exception ignore){}
            }
            if (args[0].equalsIgnoreCase("fMenu_makeInvoice.scanBox"))
            {
                fMenu_makeInvoice.scanBox(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("fMenu_makeInvoice.putAmount"))
            {
                try{fMenu_makeInvoice.putAmount(((Player)sender).getUniqueId(), args[1]);}
                catch (Exception e){}
            }
            if (args[0].equalsIgnoreCase("fMenu_makeInvoice.register"))
            {
                fMenu_makeInvoice.register(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("cShop.displayBuyInvoice"))
            {
                cShop.displayBuyInvoice(((Player)sender).getUniqueId());
            }//
            if (args[0].equalsIgnoreCase("cShop.cShop_sell"))
            {
                cShop.cShop_sell(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("cShop.cShop_putAmount"))
            {
                try
                {
                    cShop.cShop_putAmount(((Player)sender).getUniqueId(), args[1]);
                }
                catch (Exception ignore){}

            }
            if (args[0].equalsIgnoreCase("cShop.cShop_buy"))
            {
                cShop.cShop_buy(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("fMenu_pri.priInfo_claim_posReset"))
            {
                try
                {
                    fMenu_pri.priInfo_claim_posReset(((Player)sender).getUniqueId(), args[1]);
                }
                catch (Exception ignored){}
            }
            if (args[0].equalsIgnoreCase("fMenu_pri.priInfo_claim_posSet1"))
            {
                fMenu_pri.priInfo_claim_posSet1(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("fMenu_pri.priInfo_claim_posSet2"))
            {
                fMenu_pri.priInfo_claim_posSet2(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("fMenu_pri.priInfo_setLandPrice"))
            {
                try
                {
                    fMenu_pri.priInfo_setLandPrice(((Player)sender).getUniqueId(), args[1]);
                }
                catch (Exception ignored){}
            }
            if (args[0].equalsIgnoreCase("fMenu_pri.priInfo_claim_cancel"))
            {
                fMenu_pri.priInfo_claim_cancel(((Player)sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("fMenu_pri.priInfo_claim_register"))
            {
                fMenu_pri.priInfo_claim_register(((Player)sender).getUniqueId());
            }

        }

         */

        return false;
    }
}
