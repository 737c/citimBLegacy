package org.citmb2.citmb;

public class userOnlineData
{
    public citmUserSave usData;
    public boolean isPRIMode = false;
    public int lastTimeAccessedFMenu = 0;
    public int lastTimeInteractedSign = 0;
    public boolean isMakingInvoice = false;
    public citmInvoiceData invoiceEditData = new citmInvoiceData();
    public citmBuyInvoice buyInvoiceData = new citmBuyInvoice();

    //public PRIEdit PRIEditData = new PRIEdit();
    public String userMessage = "";
    public consoleData cdData = null;
}

