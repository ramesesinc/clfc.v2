package com.rameses.waterworks.bluetooth;

import com.rameses.waterworks.bluetooth.BluetoothPort;
import datamaxoneil.connection.ConnectionBase;
import datamaxoneil.connection.Connection_Bluetooth;
import datamaxoneil.printer.DocumentEZ;
import datamaxoneil.printer.ParametersEZ;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AndroidBluetoothPort implements BluetoothPort{
    String selectedPrinter = "";
    
    @Override
    public void setPrinter(String macaddress) {
        selectedPrinter = macaddress;
    }
    
    @Override
    public String getPrinter() {
        return selectedPrinter;
    }

    @Override
    public void print(String message) {
        if(selectedPrinter.equals("")){
            throw new UnsupportedOperationException("No printer selected!");
        }
        try{
           ConnectionBase conn = Connection_Bluetooth.createClient(selectedPrinter);
           conn.open();
           DocumentEZ docEZ = new DocumentEZ("MF204");
           ParametersEZ paramEZ = new ParametersEZ();
           docEZ.writeText(message,5,1);
           docEZ.writeText("\n",30,1);
           conn.write(docEZ.getDocumentData());
           conn.close();
           conn.setIsClosing(true);

        }catch(Exception e){
            Logger.getLogger(AndroidBluetoothPort.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
