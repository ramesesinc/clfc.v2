/*
 * TestNotification.java
 * JUnit based test
 *
 * Created on July 21, 2014, 1:29 PM
 */

package ztest;

import com.rameses.service.ScriptServiceContext;
import com.rameses.service.ServiceProxy;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author compaq
 */
public class TestNotification extends TestCase {
    
    private ScriptServiceContext ctx;
    private Map env = new HashMap(); 
    
    public TestNotification(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        Map appenv = new HashMap();
        appenv.put("app.host", "localhost:8070");
        appenv.put("app.cluster", "osiris3");
        appenv.put("app.context", "clfc");        
        ctx = new ScriptServiceContext(appenv); 
        
        env.put("USER", "sa");
        env.put("USERID", "sa");
    }

    protected void tearDown() throws Exception {
    }
   
    public void testMain() throws Exception {
        Map data = new HashMap();
        data.put("objid", new UID().toString());
        data.put("sender", "SYSTEM ADMINISTRATOR");
        data.put("senderid", "sa");
        data.put("recipientid", "APPROVER");
        data.put("recipienttype", "group");
        data.put("message", "Welcome APPROVER Group");
        data.put("filetype", "xxx");
        data.put("data", new HashMap()); 
        
        ServiceProxy proxy = ctx.create("NotificationService", env);
        proxy.invoke("addMessage", new Object[]{ data });
    }  
}
