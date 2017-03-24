/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ztest;

import gnu.io.CommPortIdentifier;
import gnu.io.CommPortOwnershipListener;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import junit.framework.TestCase;
import sms.pdu.PDUConverter;

/**
 *
 * @author cebu01
 */
public class SerialPortTest extends TestCase {
    private SerialPort serialPort;
    private OutputStream outputStream;
    private InputStream inputStream;
    private boolean allowStop = false;
    private LinkedList commands = new LinkedList();
    private String currentCommand = "";
    private Map currentCommandItem = new HashMap();
    private LinkedList messages = new LinkedList();
    private LinkedBlockingQueue waiter = new LinkedBlockingQueue();
    
    public void xtestMain() {
        String text = "This is Part 1. This is Part 2.";
        for (int i=0; i<text.length(); i++) {
            char ch = text.charAt(i);
            int asciiVal = (int) ch;
            
            String binary = PDUConverter.convertToBinary(asciiVal, 8);
            _("binary: " + binary + " char: " + ch + " ascii: " + asciiVal);
        }
    }
    
    public void xxtestMain() {
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum mollis sapien enim, vel rutrum velit commodo id. In sit amet risus non erat accumsan fermentum et quis erat. Vestibulum varius ut sapien at tristique. Nunc et dolor vel elit porttitor pretium id id ex. Sed ac vestibulum ante. Proin vitae ultricies tortor. Integer efficitur laoreet elit, non vestibulum turpis mattis et. Quisque nec tincidunt risus. Sed ultrices justo id iaculis venenatis.";
        //text += "Pellentesque mollis fermentum risus, imperdiet pharetra mauris posuere a. Mauris at tellus feugiat, fringilla ante quis, finibus elit. Vestibulum faucibus, lectus id accumsan malesuada, nunc diam ullamcorper nibh, eu consequat magna est eget risus. Donec eleifend at dolor nec porta. Integer lobortis fermentum urna dapibus vulputate. Integer luctus pretium eros, placerat malesuada ipsum maximus nec. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. In hac habitasse platea dictumst. Etiam ultricies eleifend bibendum. Suspendisse ac molestie magna.";
        //text += "Aenean malesuada urna eget tortor aliquet euismod. Proin elementum pellentesque blandit. Praesent lobortis hendrerit pulvinar. Curabitur vitae turpis fermentum, blandit massa vitae, porta massa. Nulla blandit tristique libero, nec convallis sapien viverra nec. Mauris nec nisi nec nisi sodales congue. Nam aliquet quis tortor id egestas. Suspendisse ullamcorper fermentum nunc, a dapibus nibh cursus efficitur. Donec urna justo, facilisis congue turpis sit amet, malesuada ultrices erat.";

        
        LinkedList messages = new LinkedList();
        
        int MAX_CHAR_LENGTH = 160;
        int idx = 0;
        String str;
        while (idx < text.length()) {
            int s = idx + MAX_CHAR_LENGTH;
            if (s > text.length()) {
                str = text.substring(idx, text.length());
            } else {
                str = text.substring(idx, idx + MAX_CHAR_LENGTH);
            }
            messages.add(str);
            
            idx += MAX_CHAR_LENGTH;
        }
        
        _("message size: " + messages.size());
    }
    
    public void xxxtestMain() {
        _("parity");
        _("none: " + SerialPort.PARITY_NONE);
        _("odd: " + SerialPort.PARITY_ODD);
        _("mark: " + SerialPort.PARITY_MARK);
        _("even: " + SerialPort.PARITY_EVEN);
        _("space: " + SerialPort.PARITY_SPACE);
        
        _("\ndata bits");
        _("db5: " + SerialPort.DATABITS_5);
        _("db6: " + SerialPort.DATABITS_6);
        _("db7: " + SerialPort.DATABITS_7);
        _("db8: " + SerialPort.DATABITS_8);
        
        _("\nstop bits");
        _("sb1: " + SerialPort.STOPBITS_1);
        _("sb1.5: " + SerialPort.STOPBITS_1_5);
        _("sb2: " + SerialPort.STOPBITS_2);
    }
    
    
    public void testMain() {
        String portName = "COM11";
        
        CommPortIdentifier portId, portId2;
        
        try {
            portId = CommPortIdentifier.getPortIdentifier(portName);
            _("owner: " + portId.getCurrentOwner());
            _("current owned: " + portId.isCurrentlyOwned());
            
            if (portId.isCurrentlyOwned()) {
                throw new RuntimeException("Port is already in use.");
            }
            
            
            serialPort = (SerialPort) portId.open(getClass().getSimpleName(), 2000);
            
            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();
            
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            
            serialPort.addEventListener(new MySerialPortEventListener(inputStream));
            serialPort.notifyOnDataAvailable(true);
            serialPort.notifyOnBreakInterrupt(true);
            serialPort.enableReceiveTimeout(30);
            
            portId.addPortOwnershipListener(new MyCommPortOwnershipListener());
            
            //scheduler.schedule(new SerialReader(inputStream), 0, TimeUnit.SECONDS);
        
            Map item = new HashMap();
            //Map response = new HashMap();
            
            String str = "AT" + ((char) 13);//"ATD09222521781;\r";
            item.put("command", str);
            
//            _("AT Command: " + str);
            commands.add(item);
            
            //(new Thread(new SerialWriter(outputStream, str))).start();
            
            str = "AT+CMGF=?" + ((char) 13);
//            commands.add(str);
            
            item = new HashMap();
            str = "AT+CMGF=1" + ((char) 13);//AT+CMGS=09186427795\rHello There!\032\r";
            item.put("command", str);
//            _("AT Command: " + str);
            commands.add(item);
            //(new Thread(new SerialReader(inputStream))).start();
            
            str = "AT+CSMP?" + ((char) 13);
//            commands.add(str);
            
            item = new HashMap();
            str = "AT+CSMP=1,1,0,0" + ((char) 13);
            item.put("command", str);
//            str = "AT+CSMP?\r";
            commands.add(item);
            
            String text = "This is Part 1. This is Part 2. This is Part 3. This is Part 4. This is Part 5. This is Part 6.";
            _("length: " + text.length());
            
            item = new HashMap();
            str = "AT+CMGS=35" + ((char) 13);
//            str = "AT+CMGS=153" + ((char) 13);
            item.put("command", str);
            item.put("commandType", "MESSAGE_HEADER");
//            commands.add(item);
            
            item = new HashMap();
//            str = "0041000C913629222571180000A0050003000101986F79B90D4AC3E7F53688FC66BFE5A0799A0E0AB7CB741668FC76CFCB637A995E9783C2E4343C3D4F8FD3EE33A8CC4ED359A079990C22BF41E5747DDE7E9341F4721BFE9683D2EE719A9C26D7DD74509D0E6287C56F791954A683C86FF65B5E06B5C36777181466A7E3F5B0AB4A0795DDE936284C06B5D3EE741B642FBBD3E1360B14AFA7E7" + ((char) 26);
            str = "0041000C91362922257118000019050003030101A8E8F41C949E83A061391D147381A868" + ((char) 26);
            item.put("command", str);
            item.put("commandType", "MESSAGE_CONTENT");
//            commands.add(item);
            
            
//            if (1==1) {
//                throw new RuntimeException("stopping");
//            }
//            String text = "This is Part 1. This is Part 2. This is Part 3. This is Part 4. This is Part 5. This is Part 6. This is Part 7.";
//            _("text length: " + text.length());
//            String text = "um et quis erat. Vestibulum varius ut sapien at tristique. Nunc et dolor vel elit porttitor pretium id id ex. ";
            text = ""
                    + "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                    + "Vestibulum mollis sapien enim, vel rutrum velit commodo id. "
                    + "In sit amet risus non erat accumsan fermentum et quis erat. ";
//                    + "Vestibulum varius ut sapien at tristique. "
//                    + "Nunc et dolor vel elit porttitor pretium id id ex. "
//                    + "Sed ac vestibulum ante. Proin vitae ultricies tortor. "
//                    + "Integer efficitur laoreet elit, non vestibulum turpis mattis et. "
//                    + "Quisque nec tincidunt risus. Sed ultrices justo id iaculis venenatis."
//                    + "Pellentesque mollis fermentum risus, imperdiet pharetra mauris posuere a. "
//                    + "Mauris at tellus feugiat, fringilla ante quis, finibus elit. "
//                    + "Vestibulum faucibus, lectus id accumsan malesuada, nunc diam ullamcorper nibh, eu consequat magna est eget risus. "
//                    + "Donec eleifend at dolor nec porta. Integer lobortis fermentum urna dapibus vulputate. "
//                    + "Integer luctus pretium eros, placerat malesuada ipsum maximus nec. "
//                    + "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. "
//                    + "In hac habitasse platea dictumst. Etiam ultricies eleifend bibendum. Suspendisse ac molestie magna.";
            //text += "Aenean malesuada urna eget tortor aliquet euismod. Proin elementum pellentesque blandit. Praesent lobortis hendrerit pulvinar. Curabitur vitae turpis fermentum, blandit massa vitae, porta massa. Nulla blandit tristique libero, nec convallis sapien viverra nec. Mauris nec nisi nec nisi sodales congue. Nam aliquet quis tortor id egestas. Suspendisse ullamcorper fermentum nunc, a dapibus nibh cursus efficitur. Donec urna justo, facilisis congue turpis sit amet, malesuada ultrices erat.";
//            text = "Payment for the loan of TOMADA, LUZVIMINDA with the amount of 50.00 with Ref. No. CEBP1605000483 received by collector BALISTOY, RAY has been voided.";
//            text = PDUConverter.convertToPDUMessage(text);
//            text = PDUConverter.applyPadding(text, 1, PDUConverter.ENCODING_8_BIT);
            text = "Payment for the loan of TOMADA, LUZVIMINDA with the amount of 50.00 with Ref. No. CEBP1605000483 received by collector BALISTOY, RAY.";
//            text = "OMANDAM.";
            buildMessages(text);
//            str = "AT+CMGW=\"+69222521781\"" + ((char) 13);
//            str = "AT+CMGS=\"+639222521781\"" + ((char) 13);
//            str = "AT+CMGS=\"+63922521781\"" + ((char) 13);
//            str = "AT+CMGS=\"09423491693\"" + ((char) 13);
         
            if (messages.size() > 0) {            
                //String mobileno = "09176339529";
                String mobileno = "09222521781";
                String pduMobile = PDUConverter.convertMobileNumToPduMessage(mobileno);
                String UD, UDH, UDL, message, totalPart = "", part = "";

                totalPart = PDUConverter.convertToHexadecimal(messages.size());
                if (totalPart.length() < 2) {
                    totalPart = "0" + totalPart;
                }
                
                for (int i=0; i<messages.size(); i++) {
                    String str1 = messages.get(i).toString();
                    _("string1: " + str1);
//                    UD = messages.get(i).toString();
                    int noOfSeptets = 1;
                    noOfSeptets += Math.abs((str1.length()/8));
                    
                    
                    UD = PDUConverter.convertToPDUMessage(str1);
                    
                    
//                    noOfSeptets += PDUConverter.countNoOfSeptets(UD);
                    UD = PDUConverter.applyPadding(UD, 1, PDUConverter.ENCODING_8_BIT);

                    part = PDUConverter.convertToHexadecimal(i + 1);
                    if (part.length() < 2) {
                        part = "0" + part;
                    }
                    
                    //len += Math.abs(((UD.length()/2)/7));
                    
//                    _("no of septets: " + noOfSeptets);
                    
                    UDH = "05000305" + totalPart + part;
                    UD = UDH + UD;
                    
//                    _("UD Length: " + UD.length());
                    
                    UDL = Integer.toHexString((UD.length()/2) + noOfSeptets);
                    if (UDL.length() < 2) {
                        UDL = "0" + UDL;
                    }

                    UD = UDL.toUpperCase() + UD;

                    message = "4100" + pduMobile + "0000" + UD;
                    //int msgL = (message.length()/2) - 1;
                    int msgL = (message.length()/2);

                    item = new HashMap();
                    //str = "AT+CMGS=" + msgL + ((char) 13);
                    str = "AT+CMGS=\"" + mobileno + "\"" + ((char) 13);
                    item.put("command", str);
                    item.put("commandType", "MESSAGE_HEADER");
                    commands.add(item);

                    item = new HashMap();
                    //str = message + ((char) 26);
                    str = str1 + ((char) 26);
                    item.put("command", str);
                    item.put("commandType", "MESSAGE_CONTENT");
                    commands.add(item);
                }
            }
            
            
            
            
//            _("length: " + msgL + " message: " + message);
            
            //str = ""
//            _("AT Command: " + str);
//            commands.add(str);
            
//            str = "You have paid 150.00 for your loan." + ((char) 26);
//            _("AT Command: " + str);
//            commands.add(str);
            
//            _("commands: " + commands);
            executeCommands();
            
            while (allowStop == false) {
                _("allow stop: " + allowStop);
                waiter.poll(10000, TimeUnit.MILLISECONDS);
            }
            _("allow stop: " + allowStop);
        } catch(PortInUseException p) {
            _("port in use exception");
        } catch (Exception e) {
            _("Exception");
            e.printStackTrace();
        }
    }
    
    void buildMessages(String text) {
        int MAX_CHAR_LENGTH = 130;
        int idx = 0;
        String str1;
        while (idx < text.length()) {
            int s = idx + MAX_CHAR_LENGTH;
            if (s > text.length()) {
                str1 = text.substring(idx, text.length());
            } else {
                str1 = text.substring(idx, idx + MAX_CHAR_LENGTH);
            }
            messages.add(str1);

            idx += MAX_CHAR_LENGTH;
        }
    }
    
    
    void executeCommands() {
        if (commands.size() > 0) {
            currentCommandItem = (Map) commands.poll();
            _("current command item: " + currentCommandItem);
            if (currentCommandItem.containsKey("command")) {
                currentCommand = currentCommandItem.get("command").toString();
            }
            _("current command: " + currentCommand);
            
            (new Thread(new SerialWriter(outputStream, currentCommand))).start();
        } else {
            allowStop = true;
        }
    }
    
    
    void _(String str) {
        System.out.println(str);
    }
    
    void executeResponse(String response) {
        boolean allowExecute = true;
//        _("current command: " + currentCommand);
        _("response: " + response);
//        _("command item: " + currentCommandItem);
        
        if (currentCommandItem.containsKey("commandType")) {
            allowExecute = false;
            String cType = currentCommandItem.get("commandType").toString();
            if ("MESSAGE_CONTENT".equals(cType)) {
                String resp = response.trim();
                if (resp.contains("OK") || resp.contains("ERROR")) {
                    allowExecute = true;
                }
                //&& (response != null && !response.isEmpty())) {
//                try {
//                    waiter.poll(1000, TimeUnit.MILLISECONDS);
//                } catch (Exception e) {
//                    //do nothing
//                }
            } else if ("MESSAGE_HEADER".equals(cType)) {
                allowExecute = true;
            }
            _("command type: " + cType + " response: " + response + " allow execute: " + allowExecute);
        }
        
        if (allowExecute == true) {
            executeCommands();
        }
    }
    
    class MySerialPortEventListener implements SerialPortEventListener {
        private InputStream is;
        
        MySerialPortEventListener(InputStream is) {
            this.is = is;
        }
        
        public void serialEvent(SerialPortEvent spe) {
            int type = spe.getEventType();
            
            StringBuffer buffer = new StringBuffer();
            int newData = 0;
            _("event type " + type);
            switch (type) {
                case SerialPortEvent.DATA_AVAILABLE:
                    try {
                        int counter = 0;
                        //_("available: " + is.available());
                        while (newData != -1) {
                            //_("inside while loop");
                            newData = is.read();
                            //_("counter: " + counter + " new data " + newData);
                            if (newData == -1) {
                                //_("buffer1 " + buffer.toString());
                                executeResponse(buffer.toString());
                                return;
                            }
                            
                            if (newData == 13) {
                                //do nothing;
                            } else {
                                buffer.append((char) newData);
                            }
                            //_("buffer " + buffer.toString());
                        //_("new data: " + newData);
                            counter++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        _("IOException");
                        //return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        _("Exception");
                    } finally {
                        _("finished");
                    }
                    break;
            }
        }
        
    }
    
    class MyCommPortOwnershipListener implements CommPortOwnershipListener {
        public void ownershipChange(int type) {
            _("ownership: " + type);
            _("OWNED " + PORT_OWNED);
            _("UNOWNED " + PORT_UNOWNED);
            _("OWNERSHIP REQUESTED " + PORT_OWNERSHIP_REQUESTED);
        }
    }
    
    class SerialWriter implements Runnable {
        private String cmd = "";
        private OutputStream os;
        
        SerialWriter(OutputStream os, String cmd) {
            this.os = os;
            this.cmd = cmd;
        }
        
        public void run() {
            try {
                _("running");
                byte[] array = cmd.getBytes();
                os.write(array);
                waiter.poll(1000, TimeUnit.MILLISECONDS);
                os.flush();
                os.close();
            } catch ( IOException e ) {
                e.printStackTrace();
                System.exit(-1);
            } catch (InterruptedException ioe) {
                ioe.printStackTrace();
            }       
        }
    }
}
