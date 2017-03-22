package com.rameses.waterworks.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.rameses.Main;
import javafxports.android.FXActivity;

public class AndroidSystem implements System{

    @Override
    public String getMacAddress() {
        String macaddress = "";
        try{
            WifiManager manager = (WifiManager) FXActivity.getInstance().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            macaddress = info.getMacAddress();
        }catch(Exception e){
            Main.LOG.error("Get MacAddress", e.toString());
        }
        return macaddress;
    }
    
}
