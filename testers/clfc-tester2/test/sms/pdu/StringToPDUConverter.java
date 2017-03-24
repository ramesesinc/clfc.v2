/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sms.pdu;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import junit.framework.TestCase;

/**
 *
 * @author louie
 */
public class StringToPDUConverter extends TestCase {
    
    private final int BIT_8_SIZE = 8;
    private final int BIT_4_SIZE = 4;
    //private final String MESSAGE = "This is Part 2.";
    //private LinkedList messageWithBinary = new LinkedList();
    
    public void ttestMain() {
        LinkedList list = new LinkedList();
        
        list.add("01101000");
        list.add("01011000");
        
        ListIterator itr = list.listIterator();
        
        while (itr.hasNext()) {
            String str = itr.next().toString();
            _("str1 " + str);
            str += "1";
            itr.set(str);
        }
        
        _(list.toString());
        
    }
    
    public void testMain() {
        String mobileno = "09222521781";
        String pduMobile = PDUConverter.convertMobileNumToPduMessage(mobileno);
        //pduMobile = Integer.toHexString(pduMobile.length()) + pduMobile;
        _("pdu mobile: " + pduMobile.toUpperCase());
    }
    
    public void tttestMain() {
        String text = "Hello there! ";
        /*
        for (int i=0; i<text.length(); i++) {
            char ch = text.charAt(i);
            int asciiVal = (int) ch;
            _("char: " + ch + " ascii value: " + asciiVal + " binary: " + Integer.toBinaryString(asciiVal));
        }
        _("");
        */
        String UD = PDUConverter.convertToPDUMessage(text);
        //_("UD: "  + UD);
        UD = PDUConverter.applyPadding(UD, 1, BIT_8_SIZE);
        
        UD = "050003030101" + UD;
        _("length: " + ((UD.length()/2) + 2) + " UD2: " + UD);
        
        String mobileno = "09222521781";
        
        String pduMobile = PDUConverter.convertMobileNumToPduMessage(mobileno);
        pduMobile = Integer.toHexString(pduMobile.length()) + pduMobile;
        
        String message = "0141000C91362922257118000012" + UD;
        _("length: " + ((message.length()/2)-1) + " message: " + message);
        //String UD = "E8329BFD06DDDF723619";
        //UD = convertToPduMessage("hello world");
        //_("UD2 " + UD);
        //UD = applyPadding(UD, 1, BIT_8_SIZE);
        //_("after apply padding " + UD);
        //_("length: " + (UD.length()/2));
        
        //String UDL = "0041000C9136292225711800001C050003030101" + UD;
        //_("length2: " + UDL.length());
        //_("content: " + UDL);
        
    }
    
    String convertMobileNumToPduMessage(String mobileno) {
        String pduMessage = "";
        
        
        return pduMessage;
    }
    
    String applyPadding(String UD, int noOfBitPadding, int noOfBits) {
        String convertedUD = "";
        String binary = "", binaryOctet = "";
        char hex = (char) 13;
        for (int i=0; i<UD.length(); i++) {
            hex = UD.charAt(i);
            
            binary = new BigInteger(hex + "", 16).toString(2);
            binary = addLeftPadding(binary, 4, "0");
            
            if (binaryOctet == null) {
                binaryOctet = "";
            }
            binaryOctet += binary;
            
            if (binaryOctet.length() >= noOfBits) {
                binaryOctet = reverseString(binaryOctet);
                if (convertedUD == null) {
                    convertedUD = "";
                }
                convertedUD += binaryOctet;
                binaryOctet = "";
            }
        }
        
        int idx = convertedUD.length() - noOfBitPadding;
        String padding = convertedUD.substring(idx, convertedUD.length());
        convertedUD = padding + convertedUD.substring(0, idx);
        
        String pduMessage = "", str = "", bin = "", reverseBin = "";
        idx = 0;
        while (idx < convertedUD.length()) {
            bin = convertedUD.substring(idx, idx + BIT_8_SIZE);
            reverseBin = reverseString(bin);
            
            int idx2 = 0;
            while (idx2 < reverseBin.length()) {
                str = reverseBin.substring(idx2, idx2 + BIT_4_SIZE);
                str = convertToHexadecimal(str);
                //_("hex: " + str);
                
                pduMessage += str;
                idx2 += BIT_4_SIZE;
            }
            idx += BIT_8_SIZE;
        }
        
        return pduMessage.toUpperCase();
    }
    
    String reverseString(String str) {
        StringBuffer sb = new StringBuffer();
        sb.append(str);
        
        return sb.reverse().toString();
        //return sb.reverse();
    }
    
    
    public void xttestMain() {
        String msg = convertToPduMessage("hello there");
        _("pdu message " + msg);
        
        LinkedList list = new LinkedList();
        LinkedList list2 = new LinkedList();
                
        msg = "E8329BFD06DDDF723619";
        int idx = 0;
        String hex = "", binary = "";
        StringBuffer buffer;
        while (idx < msg.length()) {
            buffer = new StringBuffer();
            //_("idx: " + idx);
            char ch1 = msg.charAt(idx), ch2 = msg.charAt(idx+1);
            //_("char1: " + ch1 + " char2: " + ch2);
            
            //hex = ch1 + "";
            hex = ch1 + "" + ch2;
            //buffer.append(new BigInteger(hex, 16).toString(2));
            binary = new BigInteger(hex, 16).toString(2);
            binary = addLeftPadding(binary, "0");
            
            buffer.append(binary);
            
            list.add(buffer.reverse());
            
            idx += 2;
        }
        
        Iterator itr = list.iterator();
        
        String str = "";
        while (itr.hasNext()) {
            str += itr.next().toString();
        }
        
        if (str.length() > 0) {
            char ch1 = str.charAt(str.length()-1);
            str = ch1 + str.substring(0, str.length() - 1);
        }
        
        
        idx = 0;
        while (idx < str.length()) {
            binary = str.substring(idx, idx + BIT_8_SIZE);
            _("binary " + binary);
            
            list2.add(binary);
            idx += BIT_8_SIZE;
        }
        
        ListIterator listIterator = list2.listIterator();
        
        String itrVal = "", pduMessage = "";
        int itrIntVal = 0;
        while (listIterator.hasNext()) {
            buffer = new StringBuffer();
            buffer.append(listIterator.next().toString());
            itrVal = buffer.reverse().toString();
            itrIntVal = Integer.parseInt(itrVal, 2);
            //_("" + Integer.parseInt(itrVal, 2));
            hex = Integer.toHexString(itrIntVal);
            //hex = new BigInteger(Integer.parseInt(itrVal, 2) + "").toString();
            if (hex.length() < 2) {
                hex = "0" + hex;
            }
            pduMessage += hex;
            //_("binary: " + itrVal + " hex: " + hex);
        }
        
        _("pdu message: " + pduMessage.toUpperCase());
        
        //_(msg + " = " + list);
        //_("list2 " + list2);
        
        //_("hex to decimal " + Integer.parseInt(msg, 16));
        
        //convertToPduMessage("This is Part 2.");
        
        //Queue q = new LinkedBlockingQueue();
        //LinkedList list = new LinkedList();
        
        //Map item = new HashMap();
        
        /*
        int LIMIT = 8;
        int MAX_LIMIT = MESSAGE.length(), CURRENT_IDX = 0;
        char ch = (char)13;
        int asciiVal;
        String pduMessage = "";
        */
        
        /*
        for (int i = 0; i < arr.length; i++) {
            if (list.size() >= LIMIT || i >= arr.length - 1) {
                _("before pdu message " + pduMessage);
                pduMessage += convertToPduMessage(list);
                _("after pdu message")
                list = new LinkedList();
            }
            
            ch = arr[i];
            asciiVal = (int) ch;
            list.add(decimalToBinary(asciiVal));
        }
        */
        
        /*
        while (true) {
            if (list.size() >= LIMIT || CURRENT_IDX >= MAX_LIMIT) {
                pduMessage += convertToPduMessageImpl(list);
                list = new LinkedList();
            }
            
            if (CURRENT_IDX >= MAX_LIMIT) {
                break;
            }
            
            ch = MESSAGE.charAt(CURRENT_IDX);
            asciiVal = (int) ch;
            list.add(decimalToBinary(asciiVal));
            
            CURRENT_IDX++;
        }
        */
        
        /*
        while (true) {
            if (CURRENT_IDX > MAX_LIMIT) {
                break;
            }
            
            if (list.size() < LIMIT && CURRENT_IDX < MAX_LIMIT) {
                ch = MESSAGE.charAt(CURRENT_IDX);
                asciiVal = (int) ch;
                list.add(decimalToBinary(asciiVal));
                //\list.add("01101000");
                //list.add(decimalToBinary(MAX_LIMIT));
                //item.put("asciiValue", "01101000");
                //q.add(item);
                //MAX_LIMIT--;
                CURRENT_IDX++;
            } else {
                _("pdu message " + pduMessage);
                pduMessage += convertToPduMessage(list);
                list = new LinkedList();
                //q = new LinkedBlockingQueue();
            }
        }
        */
        
        //_("PDU Message: " + pduMessage.toUpperCase());
        
    }
    
    String convertToPduMessage(String message) {
        
        LinkedList list = new LinkedList();
        final String MESSAGE = message;
        //Map item = new HashMap();
        
        int LIMIT = 8;
        int MAX_LIMIT = MESSAGE.length(), CURRENT_IDX = 0;
        char ch = (char)13;
        int asciiVal;
        String pduMessage = "", binary = "";
        
        /*
        for (int i = 0; i < arr.length; i++) {
            if (list.size() >= LIMIT || i >= arr.length - 1) {
                _("before pdu message " + pduMessage);
                pduMessage += convertToPduMessage(list);
                _("after pdu message")
                list = new LinkedList();
            }
            
            ch = arr[i];
            asciiVal = (int) ch;
            list.add(decimalToBinary(asciiVal));
        }
        */
        
        while (true) {
            if (list.size() >= LIMIT || CURRENT_IDX >= MAX_LIMIT) {
                //_("before conversion");
                //_(list.toString());
                pduMessage += convertToPduMessageImpl(list);
                list = new LinkedList();
            }
            
            if (CURRENT_IDX >= MAX_LIMIT) {
                break;
            }
            
            ch = MESSAGE.charAt(CURRENT_IDX);
            asciiVal = (int) ch;
            binary = decimalToBinary(asciiVal);
            list.add(binary);
            
            //_("char: " + ch + " ascii value: " + asciiVal + " binary: "  + binary);
            
            CURRENT_IDX++;
        }
        
        /*
        while (true) {
            if (CURRENT_IDX > MAX_LIMIT) {
                break;
            }
            
            if (list.size() < LIMIT && CURRENT_IDX < MAX_LIMIT) {
                ch = MESSAGE.charAt(CURRENT_IDX);
                asciiVal = (int) ch;
                list.add(decimalToBinary(asciiVal));
                //\list.add("01101000");
                //list.add(decimalToBinary(MAX_LIMIT));
                //item.put("asciiValue", "01101000");
                //q.add(item);
                //MAX_LIMIT--;
                CURRENT_IDX++;
            } else {
                _("pdu message " + pduMessage);
                pduMessage += convertToPduMessage(list);
                list = new LinkedList();
                //q = new LinkedBlockingQueue();
            }
        }
        */
        /*
        int len = pduMessage.length();
        if ((len % 2) != 0) {
            pduMessage += "1";
        }
        */
        
        //pduMessage = "0001" + pduMessage;
        
        //_("Message: " + MESSAGE + " PDU Message: " + pduMessage.toUpperCase() + " length: " + pduMessage.length() + " message size: " + (pduMessage.length()/2));
        return pduMessage.toUpperCase();
    }
    
    String convertToPduMessageImpl(LinkedList list) {
        list = removeMostSignificantBit(list);
        list = convertTo7Octets(list);
        
        String pduMessage = "", binary = "";
        int idx = 0;
        for (int i=0; i<list.size(); i++) {
            binary = list.get(i).toString();
            idx = 0;
            while (idx < binary.length()) {
                String str = binary.substring(idx, idx + BIT_4_SIZE);
                str = convertToHexadecimal(str);
                pduMessage += str;
                idx += BIT_4_SIZE;
            }
        }
        
        return pduMessage.toUpperCase();
    }
    
    LinkedList removeMostSignificantBit(LinkedList list) {
        //Queue qNew = new LinkedBlockingQueue();
        //LinkedList listNew = new LinkedList();
        ListIterator itr = list.listIterator();
        
        while (itr.hasNext()) {
            String binary = itr.next().toString();
            binary = binary.substring(1, binary.length());
            itr.set(binary);
        }
        /*
        while (q.peek() != null) {
            String asciiVal = q.poll().toString();
            asciiVal = asciiVal.substring(1, asciiVal.length());
            qNew.add(asciiVal);
        }
        */
        
        
        return list;
    }
    
    LinkedList convertTo7Octets(LinkedList list) {
        boolean flag = true;
        ListIterator itr;
        int CURRENT_IDX = 0;
        String base = null, removeFrom = null;
        while (flag) {
            base = list.get(CURRENT_IDX).toString();
            
            itr = list.listIterator(CURRENT_IDX + 1);
            removeFrom = null;
            if (itr.hasNext()) {
                removeFrom = itr.next().toString();
            }
            
            if (removeFrom != null) {
                int noOfBitsToAdd = BIT_8_SIZE - base.length();
                //_("no of bits to add " + noOfBitsToAdd);
                if (noOfBitsToAdd > 0) {
                    int idx = removeFrom.length() - noOfBitsToAdd;
                    String toAdd = removeFrom.substring(idx, removeFrom.length());
                    //_("before add " + base);
                    base = toAdd + base;
                    //_("after add " + base);
                    //list[CURRENT_IDX] = base;
                    //_("base: " + base);
                    list.set(CURRENT_IDX, base);
                    
                    //_("before remove " + removeFrom);
                    removeFrom = removeFrom.substring(0, idx);
                    if (removeFrom == null || !removeFrom.isEmpty()) {
                        list.remove(CURRENT_IDX + 1);
                    } else {
                        //_("after remove " + removeFrom);
                        itr.set(removeFrom);
                    }
                } else {
                    //_("remove from " + removeFrom);
                    //removeFrom = addLeftPadding(removeFrom);
                    flag = false;
                }
            } else if (removeFrom == null) {
                if (base == null || base.isEmpty()) {
                    list.remove(CURRENT_IDX);
                } else {
                    base = addLeftPadding(base, "0");
                    //_("base2: " + base);
                    list.set(CURRENT_IDX, base);
                }
                flag = false;
            }
            CURRENT_IDX++;
            _("idx: " + CURRENT_IDX + " size: " + list.size());
            if (CURRENT_IDX > list.size()) {
                break;
            }
        }
        
        //_("list " + list);
        
        return list;
    }
    
    String convertToHexadecimal(LinkedList list) {
        String str = "";
        
        Iterator itr = list.iterator();
        while (itr.hasNext()) {
            String bin = itr.next().toString();
            int decimal = Integer.parseInt(bin, 2);
            String hex = Integer.toHexString(decimal);
            
            if (hex.length() < 2) {
                hex = "0" + hex;
            }
            
            str += hex;
        }
        
        return str;
    }
    
    String convertToHexadecimal(String binary) {
        return convertToHexadecimal(Integer.parseInt(binary, 2));
    }
    
    String convertToHexadecimal(int decimal) {
        return Integer.toHexString(decimal);
    }
    
    void processConvertToPduMessage(Queue q) {
        Iterator itr = q.iterator();
        while (itr.hasNext()) {
            String asciiVal = itr.next().toString();
            asciiVal = asciiVal.substring(1, asciiVal.length());
        }
        _("queue size " + q.size());
        //_("first " + q.peek());
    }
    
    /*
    public void xxtestMain() {
        int len = MESSAGE.length();
        Map item = new HashMap();
        for (int i = 0; i < len; i++) {
            item = new HashMap();
            item.put("index", i);
            
            char c = MESSAGE.charAt(i);
            item.put("char", c);
            
            int asciiVal = (int) c;
            item.put("asciiValue", asciiVal);
            
            //_("character: " + c + " -> " + asciiVal);
            String bin = decimalToBinary(asciiVal);
            item.put("binaryValue", bin);
            
            messageWithBinary.add(item);
        }
        
        for (int i = 0; i < messageWithBinary.size(); i++) {
            _(messageWithBinary.get(i).toString());
        }
        
    }
    */
    
    void _(String str) {
        System.out.println(str);
    }
    
    
    String decimalToBinary( int decimal ) {
        String binary = Integer.toBinaryString(decimal);
        
        binary = addLeftPadding(binary, "0");
        
        return binary;
    }
    
    String addLeftPadding(String str, String ch) {
        return addLeftPadding(str, BIT_8_SIZE, ch);
    }
    
    String addLeftPadding(String str, int size) {
        return addLeftPadding(str, size, "0");
    }
    
    String addLeftPadding(String str, int size, String ch) {
        while (str.length() < size) {
            str = ch + str;
        }
        return str;
    }
}
