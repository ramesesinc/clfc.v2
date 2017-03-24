/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on July 25, 2014, 1:53 PM
 */

package ztest;

import com.rameses.osiris2.test.OsirisTestPlatform;
import com.rameses.rcp.support.ImageIconSupport;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import junit.framework.*;

/**
 *
 * @author compaq
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void xtest1() throws Exception {
        LabelIcon l = new LabelIcon();
        l.setText("The quick brown fox jumps");
        l.setOpaque(true);
        l.setBackground(Color.WHITE);
        l.setPreferredSize(new Dimension(50, 20)); 
        l.setMinimumSize(new Dimension(50, 20)); 
        l.setMaximumSize(new Dimension(50, 20)); 
        
        JDialog d = new JDialog(); 
        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
        d.setTitle("Test");
        d.setModal(true);
        d.setContentPane(l); 
        d.pack();
        d.setVisible(true); 
    }

    public void xtest2() throws Exception {
        ImageIcon icon = ImageIconSupport.getInstance().getIcon("home/icons/folder.png");
        System.out.println(icon);
        TileItem ti = new TileItem(); 
        ti.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));         
        ti.setPreferredSize(new Dimension(80, 80));
        ti.setText("Cash Breakdown Sheet"); 
        ti.setImageIcon(icon); 
        
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout()); 
        panel.add(ti); 
        
        JDialog d = new JDialog(); 
        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
        d.setTitle("Test");
        d.setModal(true);
        d.setContentPane(panel); 
        d.setSize(300, 200);
        d.setVisible(true); 
    }
 
    public void test3() throws Exception {
        //OsirisTestPlatform.testWorkunit("bank:create", null);
        String datetime = new java.sql.Timestamp(System.currentTimeMillis()).toString().replaceAll("-|:|\\.", "");
        System.out.println(datetime.replaceAll(" ", ""));
    }
}
