package com.rameses.waterworks.bluetooth;

public interface BluetoothPort {
    
    void setPrinter(String macaddress);
    
    String getPrinter();
    
    void print(String message);
    
}
