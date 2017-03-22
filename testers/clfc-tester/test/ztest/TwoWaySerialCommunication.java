package ztest;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;



public class TwoWaySerialCommunication {
    private boolean connected = false;
    private SerialPort serialPort;
    
    
    /*
    public void testMain() {
        try {
            connect(portName);
            
        } catch (Exception e) {
            _("Exception");
            e.printStackTrace();
        }
    }
    */
    
    public SerialPort getSerialPort() {
        return this.serialPort;
    }
    
    public boolean isConnected() {
        return this.connected;
    }
    
    public void connect(String portName) throws Exception {
        if (connected == true) {
            return;
        }
        
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            throw new RuntimeException("Port is currently in use.");
        }
        
        serialPort = (SerialPort) portIdentifier.open(getClass().getSimpleName(), 2000);
        /*
        serialPort.setSerialPortParams(9600, javax.comm.SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        serialPort.notifyOnDataAvailable(true);
        */
        connected = true;
    }
    
    public void disconnect() {
        if (connected == true) {
            if (serialPort != null) {
                serialPort.close();
                connected = false;
            }
        }
    }
    
    void _(String msg) {
        System.out.println(msg);
    }
}
