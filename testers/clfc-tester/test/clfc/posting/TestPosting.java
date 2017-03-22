/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clfc.posting;

import com.rameses.osiris2.reports.ReportDataSource;
import com.rameses.service.ScriptServiceContext;
import com.rameses.service.ServiceProxy;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

/**
 *
 * @author cebu01
 */
public class TestPosting extends TestCase {
    
    private ScriptServiceContext ctx;
    private Map env = new HashMap(); 
    
    public TestPosting(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        Map appenv = new HashMap();
        appenv.put("app.host", "localhost:8070");
        appenv.put("app.cluster", "osiris3");
        appenv.put("app.context", "clfc");        
        ctx = new ScriptServiceContext(appenv); 
//        
        env.put("USER", "sa");
        env.put("USERID", "sa");
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
    
    public void xtestMain() throws Exception {
        
        ServiceProxy proxy = ctx.create("TestPostingService", env);
        proxy.invoke("testPost", new Object[]{});
    }
    
    public void testMain() throws Exception {
            
//            println("report data " + reportdata);

        ServiceProxy proxy = ctx.create("TestDailyCashReceiptReportService", env);
        
        try {
//            Map reportdata = new HashMap();
            Map reportdata = (Map) proxy.invoke("getReportData", new Object[]{});
//            ArrayList items = (ArrayList) reportdata.get("items");
//            
//            Map data = new HashMap();
//            data.put("objid", "1");
            
//            items.add(data);
//            println("report data " + reportdata);
            
            
            ReportDataSource reportds = new ReportDataSource(reportdata);
            
            String reportPath = "C:/report/daily_cash_receipts/";
            String mainReport = reportPath + "DCRReport.jasper";
            println("pass");
            
            
            Map params = new HashMap();
            params.put(JRParameter.REPORT_CLASS_LOADER, new CustomReportClassLoader(reportPath));
//            params.put("DETAIL", new SubReport("DETAIL", "c:/report/billing/BillingReportPerDate.jasper"));

            JasperPrint jp = JasperFillManager.fillReport(mainReport, params, reportds);
            println("pass 1");
            JasperPrintManager.printReport(jp, false);
            println("pass 2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void println(Object msg) {
        System.out.println(msg);
    }
    
    class CustomReportClassLoader extends ClassLoader {
        
        private String basepath;
        
        public  CustomReportClassLoader(String basepath ){
            this.basepath = basepath;
            if ( basepath != null && basepath.trim().length() > 0 ) { 
                this.basepath = basepath + "/"; 
            } else { 
                this.basepath = "/"; 
            } 
        }
        public URL getResource(String name) { 
//            URL url = ReportUtil.getResource( this.basepath +  name ); 
            URL url = getClass().getClassLoader().getResource( this.basepath +  name );
            if ( url != null ) return url; 
            
            try {
                return new File(this.basepath + name).toURI().toURL();
            } catch (Throwable t) {
                t.printStackTrace();
                return null;
            }
        }
    } 
    
}
