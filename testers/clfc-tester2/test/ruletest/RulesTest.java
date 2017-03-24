/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ruletest;

import junit.framework.TestCase;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 *
 * @author cebu01
 */
public class RulesTest extends TestCase {
    
    public void testMain() {
        try {
            
            /*
            File temp = new File("SMCPosting.drl");
            
            boolean isCreated = temp.createNewFile();
            _("is created: " + isCreated);
            
    	    String absolutePath = temp.getAbsolutePath();
    	    _("File path : " + absolutePath);
            
            absolutePath = "file:\\\\\\" + absolutePath;
    	    _("File path2 : " + absolutePath);
            
    	    String filePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
				
    	    _("File path : " + filePath);
            */
            
            /*
            File temp = new File("i-am-a-temp-file.tmp");
    	    //File temp = File.createTempFile("i-am-a-temp-file", ".tmp" );
        	
    	    String absolutePath = temp.getAbsolutePath();
    	    System.out.println("File path : " + absolutePath);
    	    
    	    String filePath = absolutePath.
    	    	     substring(0,absolutePath.lastIndexOf(File.separator));
				
    	    System.out.println("File path : " + filePath);
            */
            
            KnowledgeBase kbase = readKnowledgeBase();
            StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
            
            
            
            
            //_("resource path: " + ResourceFactory.newClassPathResource("SMCPosting.drl"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private KnowledgeBase readKnowledgeBase() throws Exception {
        
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        String filePath = "file:\\\\\\RAMESES\\testers\\clfc-tester\\test\\ruletest";
        //String path = \\SMCPosting.drl";
        //C:\RAMESES\testers\clfc-tester\test\ztest
        //String path = "file:///RAMESES/testers/clfc-tester/SMCPosting.drl";
        //_("resource path: " + ResourceFactory.newUrlResource(path));
        
        //kbuilder.add(ResourceFactory.newClassPathResource("SMCPosting.drl"), ResourceType.DRL);
        kbuilder.add(ResourceFactory.newUrlResource(filePath + "\\Pune.drl"), ResourceType.DRL);
        kbuilder.add(ResourceFactory.newUrlResource(filePath + "\\Nagpur.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();

        if (errors.size() > 0) {
           for (KnowledgeBuilderError error: errors) {
              System.err.println(error);
           }
           throw new IllegalArgumentException("Could not parse knowledge.");
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        
        return kbase;
    }
    
    void _(String str) {
        System.out.println(str);
    }
}
