package test.text;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class TestTextAreaController 
{
    @Service('DateService')
    def dateSvc;
    
    @Binding 
    def binding;
    
    def loghandler = new TextWriter(); 
    def processing = false;
    def entity = [:];
    
    
    def process1 = [
        run: {
            if (processing == true) return;
            
            processing = true; 
            for (int i=0; i<200; i++) {
                if (processing == false) break;

                Thread.sleep(100);
                loghandler.writeln('appending text... ' + (i+1)); 
            }
            processing = false;                
        }
    ] as Runnable;
    
    def logtext;
    def logger = new StringBuilder();
    def process2 = [
        run: {
            if (processing == true) return;
            
            processing = true; 
            for (int i=0; i<200; i++) {
                if (processing == false) break;

                Thread.sleep(100);
                logger.append('appending text... ' + (i+1) + '\n'); 
                logtext = logger.toString();
                binding.refresh('logtext');
            }
            processing = false;                
        }
    ] as Runnable;    
    
    void init() {
        println dateSvc;
    }
    
    void start() {
        new Thread(process2).start(); 
    }
    
    void stop() {
        processing = false; 
    }
    
    void dump() {
        println entity;
    }
}
