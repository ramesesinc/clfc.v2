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
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import junit.framework.TestCase;

/**
 *
 * @author cebu01
 */
public class SerialPortComm extends TestCase {
    private String currentCommand = "";
    //private Map commands = new HashMap();
    private OutputStream outputStream;
    private InputStream inputStream;
    
    private LinkedList commands = new LinkedList();
    
    private SerialPort serialPort;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private boolean allowStop = false;
    
    /*
    void showPortList() {
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        
        CommPortIdentifier portId;
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            _("port name: " + portId.getName());
            _("port owner: " + portId.getCurrentOwner());
            _("port type: " + portId.getPortType());
            _("");
        }
    }
    */
    
    public void xtestMain() {
        _("new test main");
        
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        
        _("has more elements: " + portList.hasMoreElements());
        
        CommPortIdentifier portIdentifier;
        while (portList.hasMoreElements()) {
            portIdentifier = (CommPortIdentifier) portList.nextElement();
            
            _("port name: " + portIdentifier.getName());
            _("port type: " + portIdentifier.getPortType());
            _("port owner: " + portIdentifier.getCurrentOwner());
            _("");
        }
    }
    
    void executeCommands() {
        if (commands.size() > 0) {
            currentCommand = commands.poll().toString();
            
            (new Thread(new SerialWriter(outputStream, currentCommand))).start();
        } else {
            allowStop = true;
        }
    }
    
    
    void showCommands() {
        for (int i=0; i < commands.size(); i++) {
            _("idx: " + i + " cmd: " + commands.get(i));
        }
    }
    
    public void xxtestMain() {
        commands.add("cmd1");
        _("commands: " + commands);
        //showCommands();
        
        commands.add("cmd2");
        _("commands2: " + commands);
        //showCommands();
        
        commands.add("cmd3");
        _("commands3: " + commands);
        //showCommands();
        
        commands.add("cmd4");
        _("commands4: " + commands);
        //showCommands();
        
        _("poll: " + commands.poll());
        _("commands: " + commands);
    }
    
    void executeResponse(String response) {
        _("current command: " + currentCommand);
        _("response: " + response);
        
        executeCommands();
    }
    
    public void testMain() {
        _("serial port comm");
        
        //showPortList();
        
        _("name: " + getClass().getSimpleName());
        
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
            /*
            portId2 = CommPortIdentifier.getPortIdentifier(portId.getName());
            _("owner: " + portId2.getCurrentOwner());
            _("owned: " + portId2.isCurrentlyOwned());
            
            serialPort.close();
            
            portId2 = CommPortIdentifier.getPortIdentifier(portId.getName());
            _("owner2: " + portId2.getCurrentOwner());
            _("owned2: " + portId2.isCurrentlyOwned());
            
            serialPort = (SerialPort) portId.open(getClass().getSimpleName(), 2000);
            
            portId2 = CommPortIdentifier.getPortIdentifier(portId.getName());
            _("owner3: " + portId2.getCurrentOwner());
            _("owned3: " + portId2.isCurrentlyOwned());
            */
            
            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();
            
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            
            serialPort.addEventListener(new MySerialPortEventListener(inputStream));
            serialPort.notifyOnDataAvailable(true);
            serialPort.notifyOnBreakInterrupt(true);
            serialPort.enableReceiveTimeout(30);
            
            portId.addPortOwnershipListener(new MyCommPortOwnershipListener());
            
            //scheduler.schedule(new SerialReader(inputStream), 0, TimeUnit.SECONDS);
            
            
            String str = "AT" + ((char) 13);//"ATD09222521781;\r";
//            _("AT Command: " + str);
            commands.add(str);
            
            //(new Thread(new SerialWriter(outputStream, str))).start();
            
            
            str = "AT+CMGF=1" + ((char) 13);//AT+CMGS=09186427795\rHello There!\032\r";
//            _("AT Command: " + str);
            commands.add(str);
            //(new Thread(new SerialReader(inputStream))).start();
            
            str = "AT+CSMP?\r";
//            commands.add(str);
            
//            str = "AT+CMGW=\"+69222521781\"" + ((char) 13);
//            str = "AT+CMGS=\"+639222521781\"" + ((char) 13);
            str = "AT+CMGS=\"+63922521781\"" + ((char) 13);
//            str = "AT+CMGS=\"09423491693\"" + ((char) 13);
//            _("AT Command: " + str);
            commands.add(str);
            
            str = "You have paid 150.00 for your loan." + ((char) 26);
//            _("AT Command: " + str);
            commands.add(str);
            
            /*
            str = "AT+CMSS=0";
//            _("AT Command: " + str);
            commands.add(str);
            
            str = "AT+CMSS=1";
//            _("AT Command: " + str);
            commands.add(str);
            
            str = "AT+CMSS=2";
//            _("AT Command: " + str);
            commands.add(str);
            
            str = "AT+CMSS=3";
//            _("AT Command: " + str);
            commands.add(str);
            */
            
            executeCommands();
            
            
            //(new Thread(new SerialWriter(outputStream, str))).start();
            
            /*
            serialPort = (SerialPort) portId.open(getClass().getSimpleName(), 2000);
            _("serial port name: " + serialPort.getName());
            
            portId2 = CommPortIdentifier.getPortIdentifier(portId.getName());
            _("owner: " + portId2.getCurrentOwner());
            */
            //portId.op
            
            while (allowStop == false) {
                _("allow stop: " + allowStop);
                Thread.sleep(10000);
            }
            _("allow stop: " + allowStop);
        } catch(PortInUseException p) {
            _("port in use exception");
        } catch (Exception e) {
            _("Exception");
            e.printStackTrace();
        }
        
        /*
        try {
            CommPortIdentifier portIden = CommPortIdentifier.getPortIdentifier("COM29");
            _("port identifier: " + portIden);
            _("port owner: " + portIden.getCurrentOwner());
            
            _("currently owned: " + portIden.isCurrentlyOwned());
        } catch (Exception e) {
            _("Exception2");
            e.printStackTrace();
        }
        
        _("serial port name: " + serialPort.getName());
        */
        /*
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        
        if (portList.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
            
            _("port name: " + portId.getName());
            _("port type: " + portId.getPortType());
            _("port owner: " + portId.getCurrentOwner());
            _("port owned: " + portId.isCurrentlyOwned());
            _("");
            
            try {
                CommPortIdentifier portId2 = CommPortIdentifier.getPortIdentifier(portId.getName());
                _("port2 name: " + portId2.getName());
                _("port2 type: " + portId2.getPortType());
                _("port2 owner: " + portId2.getCurrentOwner());
                _("");
            } catch (Exception e) {
                _("Exception");
                e.printStackTrace();
            }
        }
        */
        
        //_("\n\nshow port list");
        //showPortList();
        
        
        //_("port parallel value: " + CommPortIdentifier.PORT_PARALLEL);
        //_("port serial value: " + CommPortIdentifier.PORT_SERIAL + "\n");
        //_("has more elements: " + portList.hasMoreElements());
        
        /*
        try {
            CommPortIdentifier portIdentifier  = CommPortIdentifier.getPortIdentifier("COM31");
            //_("port type: " + portIdentifier.getPortType());
            //_("port name: " + portIdentifier.getName());
            
            //SerialPort serialPort = (SerialPort) portIdentifier.open(getClass().getSimpleName(), 2000);
            serialPort = (SerialPort) portIdentifier.open(getClass().getSimpleName(), 2000);
            //_("serial port " + serialPort.getName());
            
            outputStream = serialPort.getOutputStream();
            //_("ouptut stream " + outputStream.toString());
            
            inputStream = serialPort.getInputStream();
            //_("input stream " + inputStream.toString());
            
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            
            serialPort.addEventListener(new MySerialPortEventListener(inputStream));
            serialPort.notifyOnDataAvailable(true);
            serialPort.notifyOnBreakInterrupt(true);
            serialPort.enableReceiveTimeout(30);
            
            portIdentifier.addPortOwnershipListener(new MyCommPortOwnershipListener());
            
            String str = "";
            
            str = "AT\r";
            _("AT Command: " + str);
            commands.put(commands.size(), str);
            
            executeCommands();
            
            //(new Thread(new SerialWriter(outputStream, str))).start();
            
            //str = "ATD09222521781;\r";
            //str = "AT+CMGF=1\rAT+CMGS=09222521781\rHello There!\032\r";
            
            //_("input stream1");
            //(new Thread(new SerialReader(inputStream))).start();
            
            //str = "AT+CMGF=1\rAT+CMGS=09186427795\rHello There!\032\r";
            //_("output stream1");
            //(new Thread(new SerialWriter(outputStream, str))).start();
            
            //str = "AT+CMGS=09222521781\r";
            //_("output stream2");
            //(new Thread(new SerialWriter(outputStream, str))).start();
            
            //str = "Hello There!\032\r";
            //_("output stream3");
            //(new Thread(new SerialWriter(outputStream, str))).start();
            //inputStream = serialPort.getInputStream();
            //_("input stream2");
            //(new Thread(new SerialReader(inputStream))).start();
            //PortWriter portWriter = new PortWriter();
        } catch (Exception e) {
            _("exception");
            e.printStackTrace();
        }
        */
    }
    
    public OutputStream getOutputStream() {
        return this.outputStream;
    }
    
    public InputStream getInputStream() {
        return this.inputStream;
    }
    
    public String getCurrentCommand() {
        return this.currentCommand;
    }
    
    
    void xexecuteCommands() {
        int size = commands.size();
        if (size > 0) {
            String cmd = commands.get(0).toString();
            //executeCommandImpl(cmd);
        }
    }
    
    void xexecuteCommandImpl(String cmd) {
        _("command " + cmd);
        currentCommand = cmd;
        (new Thread(new SerialWriter(outputStream, cmd))).start();
    }
    
    void xexecuteResponse(String response) {
        _("printing response " + response);
    }
    
    void _(String msg) {
        System.out.println(msg);
    }
    
    class SerialWriter implements Runnable {
        OutputStream out;
        String str;
        public SerialWriter(OutputStream out, String str) {
            this.out = out;
            this.str = str;
//            _("initializing writer");
        }
        
        public void run() {
            try {
//                _("executing writer");
                byte[] array = str.getBytes();
                out.write(array);
                Thread.sleep(1000);
                //out.flush();
                out.close();
            } catch (InterruptedException e) {
                _("Interrupted Exception");
                e.printStackTrace();
            } catch (IOException e) {
                _("IOException");
                e.printStackTrace();
            }
        }
    }
    
    class SerialReader implements Runnable {
        InputStream is;
        
        public SerialReader(InputStream is) {
            this.is = is;
        }
        
        public void run() {
            _("reader");
            scheduler.schedule(new SerialReader(is), 5, TimeUnit.SECONDS);
        }
        
        public void Xrun() {
            _("current command: " + currentCommand);
            StringBuffer buffer = new StringBuffer();
            int newData = 0;
            
            boolean flag = false;
            while (newData != -1) {
                //_("newData: "  + newData);
                //_("reading");
                try {
                    newData = is.read();
                    
                    if (newData == -1) {
                        flag = true;
                        break;
                    }
                    
                    if (newData == 13 || newData == 10) {
                        //do nothing;
                    } else {
                        buffer.append((char) newData);
                    }
                } catch (Exception e) {
                    _("Exception");
                }
            }
            _("flag: " + flag);
            
            //run();
            
            /*
            if (flag == false) {
                run();
            } else if (flag == true) {
                _("buffer: " + buffer.toString());
            }
            */
        }
        
        /*
        public void xrun() {
            try {
                byte[] buffer = new byte[1024];
                int len = input.read();
                _("length " + len);
                //_("read " + new String(buffer, 0, len));
                input.close();
            } catch (IOException e) {
                _("IOException");
                e.printStackTrace();
            }
        }
        */
    }
    
    class MySerialPortEventListener implements SerialPortEventListener {
        InputStream is;
        
        public MySerialPortEventListener(InputStream is) {
            this.is = is;
        }
        
        public void serialEvent(SerialPortEvent ev) {
            int type = ev.getEventType();
            
            /*
            switch (type) {
                case SerialPortEvent.DATA_AVAILABLE     : _("Data Available"); break;
                case SerialPortEvent.BI                 : _("Break Interrupt"); break;
                case SerialPortEvent.CD                 : _("Carrirer Detect"); break;
                case SerialPortEvent.CTS                : _("Clear to Send"); break;
                case SerialPortEvent.DSR                : _("Data Set Ready"); break;
                case SerialPortEvent.FE                 : _("Framing Error"); break;
                case SerialPortEvent.OE                 : _("Overrun Error"); break;
                case SerialPortEvent.OUTPUT_BUFFER_EMPTY: _("Output Buffer Empty"); break;
                case SerialPortEvent.PE                 : _("Parity Error"); break;
                case SerialPortEvent.RI                 : _("Ring Indicator"); break;
            }
            */
            
            StringBuffer buffer = new StringBuffer();
            int newData = 0;
            
            _("Event Type: " + type);
            switch (type) {
                case SerialPortEvent.DATA_AVAILABLE:
                    /*
                    do {
                        try {
                            newData = is.read();
                        } catch (IOException ioe) {
                            _("IOException");
                        } catch (Exception e) {
                            _("Exception");
                        }
                        _("new data " + newData);
                        if (newData == -1) {
                            _("true");
                        }
                    } while (newData != -1);
                    _("finished");
                    */
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
                        _("IOException");
                        //return;
                    } catch (Exception e) {
                        _("Exception");
                    } finally {
                        _("finished");
                    }
                    break;
            }
            /*
            switch (type) {
                case SerialPortEvent.DATA_AVAILABLE: 
                    while (newData != -1) {
                        try {
                            newData = is.read();
                            _("new data "  + newData + " data-> " + ((char) newData));
                            if (newData == -1) {
                                _("buffer " + buffer.toString());
                                //response = new String(buffer).trim();
                                break;
                            }
                            
                            if (newData == 13 || newData == 10) {
                                //do nothing
                            } else {
                                //_("append " + ((char) newData));
                                buffer.append((char) newData);
                            }
                            
                            //if ('\r' == (char) newData) {
                            //    _("append r ");
                                //buffer.append('\n');
                            //} else {
                            //    _("append " + ((char) newData));
			    //	buffer.append((char)newData);
                            //}
                            //_("buffer size " + buffer.length());
                        } catch (IOException e) {
                            _("IOException");
                            return;
                        }
                    }
            }
            */
            
            _("buffer " + buffer.toString());
            //executeResponse(buffer.toString());
            
            /*
            _("event type: " + ev.getEventType());
            _("DATA AVAILABLE: " + SerialPortEvent.DATA_AVAILABLE);
            _("BI: " + SerialPortEvent.BI);
            */ 
            /*
            _("old value: " + ev.getOldValue());
            _("new value: " + ev.getNewValue());
            */
            
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
}
