/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xtest;

import junit.framework.TestCase;

/**
 *
 * @author cebu01
 */
public class SerialPortComm extends TestCase {
    
    public void testMain() {
        _("serial port comm");
        
        SMSClient smsClient = new SMSClient(SMSClient.SYNCHRONOUS);
        smsClient.sendMessage("09222521781", "Hello There!");
    }
    
    
    void _(String msg) {
        System.out.println("message " + msg);
    }

}
