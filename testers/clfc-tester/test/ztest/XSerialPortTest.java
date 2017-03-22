package ztest;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import junit.framework.TestCase;

public class XSerialPortTest extends TestCase {
    String portName = "COM11";
    
    /*
    public void xtestMain() {
        BitSet bits1 = new BitSet(16);
        BitSet bits2 = new BitSet(16);

        // set some bits
        for(int i=0; i<16; i++) {
           if((i%2) == 0) bits1.set(i);
           if((i%5) != 0) bits2.set(i);
        }
        System.out.println("Initial pattern in bits1: ");
        System.out.println(bits1);
        System.out.println("\nInitial pattern in bits2: ");
        System.out.println(bits2);

        // AND bits
        bits2.and(bits1);
        System.out.println("\nbits2 AND bits1: ");
        System.out.println(bits2);

        // OR bits
        bits2.or(bits1);
        System.out.println("\nbits2 OR bits1: ");
        System.out.println(bits2);

        // XOR bits
        bits2.xor(bits1);
        System.out.println("\nbits2 XOR bits1: ");
        System.out.println(bits2);
    }
    */
    
    public void ttestMain() {
        _("new test main");
        
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        
        CommPortIdentifier portId;
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            
            _("\nport name: " + portId.getName());
            _("owner: " + portId.getCurrentOwner());
            _("currently owned: " + portId.isCurrentlyOwned());
        }
    }
    
    public void testMain() {
        try {
            TwoWaySerialCommunication comm = new TwoWaySerialCommunication();
            
            _("is connected " + comm.isConnected());
            comm.connect("COM11");
            if (comm.isConnected()) {
                _("connected");
                SerialPort serialPort = comm.getSerialPort();
                OutputStream os = serialPort.getOutputStream();
                InputStream is = serialPort.getInputStream();
                serialPort.addEventListener(new MySerialPortEventListener(is));
                
                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                serialPort.notifyOnDataAvailable(true);
                
                //serialPort.notifyOnBreakInterrupt(true);
                //serialPort.notifyOnCarrierDetect(true);
                //serialPort.notifyOnBreakInterrupt(true);
                //serialPort.notifyOnCTS(true);
                //addSerialPortConfig(serialPort);
                
                
                String cmd = "";
                cmd = "AT+CMGS=\"+639222521781\"\r";
                //cmd = "ATD09222521781;\r";
                
                _("command " + cmd);
                
                (new Thread(new SerialWriter(os, cmd))).start();
                //(new Thread(new SerialReader(is))).start();
            }
        } catch (Exception e) {
            _("Exception");
            e.printStackTrace();
        }
    }
    
    
    void addSerialPortConfig(SerialPort serialPort) throws Exception {
    }
    
    void _(String msg) {
        System.out.println(msg);
    }
    
    class MySerialPortEventListener implements SerialPortEventListener {
        private InputStream in;
        private byte[] buffer = new byte[1024];
        
        public MySerialPortEventListener(InputStream in) {
            this.in = in;
        }
        
        public void xserialEvent(SerialPortEvent evt) {
            _("event " + evt.getEventType());
            int data;
          
            try
            {
                int len = 0;
                while ( ( data = in.read()) > -1 ) {
                    _("data: " + data);
                    if ( data == '\n' ) {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
                _(new String(buffer,0,len));
            } catch ( IOException e ) {
                e.printStackTrace();
                System.exit(-1);
            }      
        }
        
        public void serialEvent(SerialPortEvent spe) {
            int type = spe.getEventType();
            _("serial event listener TYPE: " + type);
            String event = "";
            
            switch (type) {
                case SerialPortEvent.BI                 : event = "Break Interrupt."; break;
                case SerialPortEvent.CD                 : event = "Carrier Detected."; break;
                case SerialPortEvent.CTS                : event = "Clear to send."; break;
                case SerialPortEvent.DATA_AVAILABLE     : event = "Data Available."; processDataAvailable(); break;
                case SerialPortEvent.DSR                : event = "Data Set Ready."; break;
                case SerialPortEvent.FE                 : event = "Framing Error."; break;
                case SerialPortEvent.OE                 : event = "Overrun Error."; break;
                case SerialPortEvent.OUTPUT_BUFFER_EMPTY: event = "Output Buffer Empty."; break;
                case SerialPortEvent.PE                 : event = "Parity Error."; break;
                case SerialPortEvent.RI                 : event = "Ring Indicator."; break;
            }
            _("serial port event: " + event);
            
        }
        
        private void processDataAvailable() {
            _("processing data available");
            StringBuffer buffer = new StringBuffer();
            int newData = 0;
            
            try {
                while (newData != -1) {
                    newData = in.read();
                    _("new data: " + newData);
                    if (newData == -1) {
                        _("buffer: " + buffer.toString());
                        break;
                    }
                    
                    if (newData == 13 || newData == 10) {
                        //do nothing
                    } else {
                        _("append: " + ((char) newData));
                        buffer.append((char) newData);
                    }
                    _("buffer size: " + buffer.length());
                }
            } catch (Exception e) {
                _("processDataAvailabe Exception");
                e.printStackTrace();
            }
            
            _("response: " + buffer.toString());
        }
    }
    
    class SerialWriter implements Runnable {
        OutputStream os;
        String str;
        
        public SerialWriter(OutputStream os, String str) {
            this.os = os;
            this.str = str;
            _("intializing writer");
        }
        
        
        public void run() {
            try {
                byte[] array = str.getBytes();
                os.write(array);
                Thread.sleep(5000);
                /*
                int c = 0;
                _("System.in.read(): " + System.in.read());
                while ( ( c = System.in.read()) > -1 ) {
                    _("char: " + c);
                    os.write(c);
                } 
                */
            } catch ( IOException e ) {
                e.printStackTrace();
                System.exit(-1);
            } catch (InterruptedException ioe) {
                ioe.printStackTrace();
            }       
        }
        /*
        public void run() {
            try {
                _("executing writer");
                byte[] array = str.getBytes();
                os.write(array);
                os.flush();
                Thread.sleep(5000);
                _("after executing writer");
            } catch (IOException ioe) {
                _("IOException");
                ioe.printStackTrace();
            } catch (InterruptedException ie) {
                _("InterruptedException");
                ie.printStackTrace();
            } catch (Exception e) {
                _("Exception");
                e.printStackTrace();
            }
            
        }
        */
    }
    
    class SerialReader implements Runnable {
        InputStream is;
        
        public SerialReader(InputStream is) {
            this.is = is;
        }
        
        public void run() {
            _("input stream: " + is);
            StringBuffer buffer = new StringBuffer();
            int newData = 0;
            try {
                while (newData != -1) {
                    newData = is.read();
                    _("new data " + newData);
                    
                    if (newData == -1) {
                        _("reading: " + buffer.toString());
                        break;
                    }
                    
                    if (newData == 10 || newData == 13) {
                        //do nothing;
                    } else {
                        _("appending: " + ((char) newData));
                        buffer.append((char) newData);
                    }
                }
            } catch (Exception e) {
                _("SerialReader Exception");
                e.printStackTrace();
            }
        }
    }
}
