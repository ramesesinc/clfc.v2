/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sms.pdu;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author louie
 */
public class PDUConverter {
    public static final int ENCODING_8_BIT = 8;
    public static final int ENCODING_4_BIT = 4;
    public static final int ENCODING_16_BIT = 16;
    
    public static String convertToPDUMessage(String text) {
        LinkedList list = convertToSeptets(text);
        //_("septets: " + list);
        list = convertToOctets(list);
        
        String pduMessage = "", binary = "";
        
        ListIterator itr = list.listIterator();
        int idx;
        while (itr.hasNext()) {
            binary = itr.next().toString();
            idx = 0;
            while (idx < binary.length()) {
                String str = binary.substring(idx, idx + ENCODING_4_BIT);
                str = convertToHexadecimal(str);
                pduMessage += str;
                
                idx += ENCODING_4_BIT;
            }
        }
        
        return pduMessage.toUpperCase();
    }
    
    private static LinkedList convertToSeptets(String text) {
        LinkedList list = new LinkedList();
        
        String binaryOctet = "", binarySeptet = "", binary = "";
        int CURRENT_IDX = 0, asciiValue = 0;
        char ch = (char) 13;
        //_("length " + text.length());
        while (true) {
            
            ch = text.charAt(CURRENT_IDX);
            asciiValue = (int) ch;
            binary = decimalToBinary(asciiValue, ENCODING_8_BIT);
            
            binaryOctet += binary;
            
            //_("octet length: " + binaryOctet.length() + " octet: " + binaryOctet);
            if (binaryOctet.length() == ENCODING_8_BIT) {
                //System.out.print(binaryOctet + " ");
                binarySeptet = binaryOctet.substring(1, binaryOctet.length());
                list.add(binarySeptet);
                binaryOctet = "";
            }
            
            CURRENT_IDX++;
            
            //_("current idx: " + CURRENT_IDX + " text length: " + text.length());
            if (CURRENT_IDX >= text.length()) {
                if (binaryOctet.length() > 0 && binaryOctet.length() < ENCODING_8_BIT) {
                    binary = decimalToBinary(0);
                    binaryOctet = binary + binaryOctet;
                    //System.out.print(binaryOctet +  " ");
                    binarySeptet = binaryOctet.substring(1, binaryOctet.length());
                    list.add(binarySeptet);
                }
                break;
            }
        }
        //_("");
        
        return list;
    }
    
    private static LinkedList convertToOctets(LinkedList list) {
        int CURRENT_IDX = 0;
        //ListIterator itr;
        String base = "", removeFrom = "";
        boolean flag = true;
        if (list.isEmpty()) {
            flag = false;
        }
        
        //_("size: " + list.size() + " list: " + list);
        while (flag) {
            base = list.get(CURRENT_IDX).toString();
            //_("current idx: " + CURRENT_IDX + " size: " + list.size() + " list: " + list);
            
            removeFrom = null;
            if ((CURRENT_IDX + 1) < list.size()) {
                removeFrom = list.get(CURRENT_IDX + 1).toString();
            }
            /*
            itr = list.listIterator(CURRENT_IDX + 1);
            if (itr.hasNext()) {
                removeFrom = itr.next().toString();
            }
            */
            
            //_("idx: " + CURRENT_IDX + " base: " + base + " remove from: " + removeFrom);
            if ((base != null && !base.isEmpty()) && (removeFrom != null && !removeFrom.isEmpty())) {
                int noOfBitsToAdd = ENCODING_8_BIT - base.length();
                //_("no of bits to add " + noOfBitsToAdd);
                if (noOfBitsToAdd > 0) {
                    int idx = removeFrom.length() - noOfBitsToAdd;
                    
                    String toAdd = removeFrom.substring(idx, removeFrom.length());
                    
                    base = toAdd + base;
                    //_("base: " + base);
                    list.set(CURRENT_IDX, base);
                    
                    //_("before remove: " + removeFrom);
                    removeFrom = removeFrom.substring(0, idx);
                    if (removeFrom == null || removeFrom.isEmpty()) {
                        list.remove(CURRENT_IDX + 1);
                    } else {
                        list.set((CURRENT_IDX + 1), removeFrom);
                    }
                    //_("after remove: " + removeFrom);
                    //_("iterator: " + itr);
                    /*
                    if (removeFrom == null || !removeFrom.isEmpty()) {
                        list.remove(CURRENT_IDX + 1);
                    } else {
                        //_("after remove " + removeFrom);
                        itr.set(removeFrom);
                    }
                    */
                } else {
                    flag = false;
                }
            } else {
                if (removeFrom == null || removeFrom.isEmpty()) {
                    flag = false;
                }
                //_("base " + base + " length: " + base.length());
                if (base == null || base.isEmpty()) {
                    list.remove(CURRENT_IDX);
                } else {
                    if (base.length() > 0 && base.length() < ENCODING_8_BIT) {
                        base = addLeftPadding(base, "0", ENCODING_8_BIT);
                    }
                    //_("base2: " + base);
                    list.set(CURRENT_IDX, base);
                }
            }
            
            CURRENT_IDX++;
            
           // _("idx: " + CURRENT_IDX + " size: " + list.size());
            if (CURRENT_IDX >= list.size()) {
                break;
            }
        }
        
        return list;
    }
    
    public static String applyPadding(String UD, int noOfBitPadding, int noOfBits) {
        
        String convertedUD = "";
        String binary = "", binaryOctet = "";
        char hex = (char) 13;
        for (int i=0; i<UD.length(); i++) {
            hex = UD.charAt(i);
            
            binary = new BigInteger(hex + "", 16).toString(2);
            binary = addLeftPadding(binary, "0", 4);
            
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
        
//        _("converted UD: " + convertedUD);
        /*
        int idx3 = 0;
        while (idx3 < convertedUD.length()) {
            String str = convertedUD.substring(idx3, idx3 + ENCODING_8_BIT);
            System.out.print(str + " ");
            idx3 += ENCODING_8_BIT;
        }
        _("");
        */
        
        int idx = convertedUD.length() - noOfBitPadding;
        String padding = convertedUD.substring(idx, convertedUD.length());
        convertedUD = padding + convertedUD.substring(0, idx);
        
        String pduMessage = "", str = "", bin = "", reverseBin = "";
        idx = 0;
        while (idx < convertedUD.length()) {
            bin = convertedUD.substring(idx, idx + ENCODING_8_BIT);
            reverseBin = reverseString(bin);
            
            int idx2 = 0;
            while (idx2 < reverseBin.length()) {
                str = reverseBin.substring(idx2, idx2 + ENCODING_4_BIT);
                str = convertToHexadecimal(str);
                //_("hex: " + str);
                
                pduMessage += str;
                idx2 += ENCODING_4_BIT;
            }
            idx += ENCODING_8_BIT;
        }
        
        return pduMessage.toUpperCase();
    }
    
    /*
    public static int countNoOfSeptets(String UD) {
        int count = 0;
        
        if (UD != null && !UD.isEmpty()) {
            count = Math.abs((UD.length()/8));
        }
        
        if (count < 0) count = 0;
        
        return count;
    }
    */
    
    private static String reverseString(String text) {
        StringBuffer sb = new StringBuffer();
        sb.append(text);
        return sb.reverse().toString();
    }
    
    private static String decimalToBinary(int decimal) {
        return decimalToBinary(decimal, ENCODING_4_BIT);
    }
    
    private static String decimalToBinary(int decimal, int bitSize) {
        String binary = Integer.toBinaryString(decimal);
        binary = addLeftPadding(binary, "0", bitSize);
        return binary;
    }
    
    private static String addLeftPadding(String str, String ch, int size) {
        while (str.length() < size) {
            str = ch + str;
        }
        return str;
    }
    
    public static String convertToBinary(int decimal) {
        return convertToBinary(decimal, ENCODING_4_BIT);
    }
    
    public static String convertToBinary(int decimal, int noOfBits) {
        String binary = Integer.toBinaryString(decimal);
        
        binary = addLeftPadding(binary, "0", noOfBits);
        
        return binary;
    }
    
    public static String convertToHexadecimal(String binary) {
        return convertToHexadecimal(Integer.parseInt(binary, 2));
    }
    
    public static String convertToHexadecimal(int decimal) {
        return Integer.toHexString(decimal);
    }
    
    public static String convertMobileNumToPduMessage(String mobileno) {
        String pduMessage = "";
        String number = convertToInternationalNumber(mobileno);
        
        String str = String.valueOf(number.charAt(0));
        if (str.equals("+")) {
            pduMessage += "91";
        }
        String newNum = number.substring(1, number.length());
        int idx = 0;
        while (idx < newNum.length()) {
            str = newNum.substring(idx, idx + 2);
            str = reverseString(str);
            pduMessage += str;
            idx += 2;
        }
        
        String mobileL = convertToHexadecimal(newNum.length());
        if (mobileL.length() < 2) {
            mobileL = "0" + mobileL;
        }
        
        pduMessage = mobileL + pduMessage;
        
        return pduMessage.toUpperCase();
    }
    
    private static String convertToInternationalNumber(String mobileno) {
        String newMobileno = "+";
        String prefix = "0", REGEX = "^" + prefix;
        
        
        if (mobileno.startsWith(prefix)) {
            mobileno = mobileno.replaceFirst(REGEX, "63");
        }
        
        newMobileno += mobileno;
        
        if ((newMobileno.length() % 2) == 0) {
            throw new RuntimeException("Invalid Mobile Number.");
        }
        
        return newMobileno;
    }
    
    private static void _(String msg) {
        System.out.println(msg);
    }
}
