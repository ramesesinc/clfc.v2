/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ztest;

import junit.framework.TestCase;

/**
 *
 * @author cebu01
 */
public class TextFormatterPrinting extends TestCase {
    
    public TextFormatterPrinting(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testMain() {
        System.out.println("Bold " + ((byte) (0x8 | 0)));
        System.out.println("Height " + ((byte) (0x10 | 0)));
        System.out.println("Width " + ((byte) (0x20 | 0)));
        System.out.println("Underline " + ((byte) (0x80 | 0)));
        System.out.println("Small " + ((byte) (0x1 | 0)));
        
    }
    
    public void xtestMain() {

        TextFormatter tf = new TextFormatter( 32 );
//        ReceiptWriter rw = new ReceiptWriter( btOut ); 
//        rw.init();

        tf.addText("COLLECTION RECEIPT", TextFormatter.ALIGN_CENTER);
        System.out.println(tf.build() + "\n");

        /*
        tf.clear();
        tf.addText( "CEBP140000000001" ); 
        tf.addText( "21 DEC 2015", TextFormatter.ALIGN_RIGHT  ); 
        System.out.println(tf.build());
        */
        tf.clear();
        tf.addText("21 DEC 2015");
        System.out.println(tf.build());
        
        tf.clear();
        tf.addText("16:45:00");
        System.out.println(tf.build());
        
        /*
        tf.clear();
        tf.addText( "16:45:00", TextFormatter.ALIGN_RIGHT  ); 
        System.out.println(tf.build());
        */
        
        tf.clear();
        tf.addText("CEBP1400000001");
        System.out.println(tf.build() + "\n");
        
        tf.clear();
        tf.addText("LOAN#023493483409"); 
        System.out.println(tf.build());

        tf.clear();
        tf.addText("DELA CRUZ, JUAN R. ");
        System.out.println(tf.build());
        
        
        tf.clear();
        tf.addText("CASH");
        tf.addText("1,000.00", TextFormatter.ALIGN_RIGHT); 
        System.out.println(tf.build());
        
        tf.clear();
        tf.addText("PAOLITO"); 
        System.out.println(tf.build() + "\n");
        
        tf.clear();
        tf.addText("Collr: Juanite");
        System.out.println(tf.build());
        
        
        
        /*
        tf.clear();
        tf.addText("Collected By:  Juanite Mariano Dominador");
        System.out.println(tf.build());

        tf.clear();
        tf.addText("DELA CRUZ, JUAN R. ");
        System.out.println(tf.build());

        tf.clear();
        tf.addText("Amount:");
        tf.addText("1,000.00", TextFormatter.ALIGN_RIGHT); 
        System.out.println(tf.build());

        tf.clear();
        tf.addText("Payment Option:");
        tf.addText("Cash", TextFormatter.ALIGN_RIGHT); 
        System.out.println(tf.build());

        tf.clear();
        tf.addText("Paid By:");
        tf.addText("PAOLITO", TextFormatter.ALIGN_RIGHT); 
        System.out.println(tf.build());

        tf.clear();
        tf.addText("Loan No.");
        tf.addText("LOAN#023493483409", TextFormatter.ALIGN_RIGHT); 
        System.out.println(tf.build());
        */
    }
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
}
