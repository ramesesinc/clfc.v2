/*
 * TestOpener.java
 * JUnit based test
 *
 * Created on August 13, 2014, 3:35 PM
 */

package ztest;

import com.rameses.osiris2.client.Inv;
import com.rameses.osiris2.client.OsirisAppLoader;
import com.rameses.platform.interfaces.Platform;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.UIControllerContext;
import com.rameses.rcp.framework.UIControllerPanel;
import com.rameses.rcp.util.ControlSupport;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import junit.framework.*;

/**
 *
 * @author compaq
 */
public class TestOpener extends TestCase {
    
    public TestOpener(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    public void testMain() throws Exception {
        showOpener("test:textarea", null); 
    }
    
    private void showOpener(String invtype, Map params) throws Exception {
        Map env = new HashMap(); 
        
        OsirisAppLoader loader = new OsirisAppLoader();
        Platform platform = ClientContext.getCurrentContext().getPlatform();
        loader.load(Thread.currentThread().getContextClassLoader(), env, platform);
        
        Opener opener = Inv.lookupOpener(invtype, params); 
        ControlSupport.initOpener(opener, null);
        UIControllerContext uic = new UIControllerContext( opener.getController() );
        UIControllerPanel panel = new UIControllerPanel( uic );
        
        JDialog d = new JDialog();
        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
        d.setTitle(opener.getCaption()); 
        d.setModal(true);
        d.setContentPane(panel);
        d.pack();
        d.setVisible(true);         
    } 
}
