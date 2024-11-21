package org.citmb2.citmb;

public class cShop
{
    /*
    public static void listener_interact(UUID playerID, int[] signAxisXYZ)
    {
        if (fMenu_system.isSignInteractFrequent(playerID))
        {
            return;
        }

        String[] signData = citmSignShop_signPhase(shareFanc.getSignTexts(playerID, signAxisXYZ[0], signAxisXYZ[1], signAxisXYZ[2]));
        if (signData == null)
        {
            return;
        }

        int[] chestAxisXYZ = shareFanc.getAttachedBlock(playerID,signAxisXYZ);
        String sellItemKeyID = cShop_getSellItemKeyID(playerID, chestAxisXYZ);
        int itemAmountInChest = shareFanc.checkBox_countItem(playerID, sellItemKeyID, chestAxisXYZ);

        boolean isBuyShop = true;
        if (signData[4].equalsIgnoreCase(""))
        {
            isBuyShop = false;
        }

        String itemTitle = signData[0];
        int bulkAmount = Integer.parseInt(signData[1]);
        int sellPrice = Integer.parseInt(signData[2]);
        String sellerName = signData[3];

        int maxBuyAmount = 0;
        if (isBuyShop)
        {
            maxBuyAmount = Integer.parseInt(signData[4]);
        }


        int buyAmount = 1;
        int current_bulkAmountInChest = itemAmountInChest / bulkAmount;
        int left_bulkAmountInChest = current_bulkAmountInChest - buyAmount;

        citmBuyInvoice buyInvoiceData = citmUDB.getCitmBuyInvoice(playerID);
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
        if (isBuyShop)
        {
            buyInvoiceData.maxBuyAmount = maxBuyAmount;
            buyInvoiceData.sellShop = true;
        }



        citmUDB.setCitmBuyInvoiceData(playerID, buyInvoiceData);
        displayBuyInvoice(playerID);
    }

     */
}
