package com.rameses.waterworks.service;

import com.rameses.Main;
import com.rameses.service.ScriptServiceContext;
import java.util.List;
import java.util.Map;

public class TerminalService {
    
    private Service service;
    
    public TerminalService(){
        try{
            ScriptServiceContext ctx = ServiceProxy.getContext();
            service = (Service) ctx.create("TerminalService", Service.class);
        }catch(Exception e){
            if(Main.LOG != null){
                Main.LOG.error("TerminalService Error",e.toString());
            }else{
                e.printStackTrace();
            }
        }
    }
    
    public List<Map> getList(Object params){
        List<Map> result = null;
        try{
            result = service.getList(params);
        }catch(Exception e){
            if(Main.LOG != null){
                Main.LOG.error("TerminalService Error",e.toString());
            }else{
                e.printStackTrace();
            }
            result = null;
        }
        return result;
    }
    
    public Map register(Object params){
        Map result = null;
        try{
            result = service.register(params);
        }catch(Exception e){
            if(Main.LOG != null){
                Main.LOG.error("TerminalService Error",e.toString());
            }else{
                e.printStackTrace();
            }
            result = null;
        }
        return result;
    }
    
    public Map recover(Object params){
        Map result = null;
        try{
            result = service.recover(params);
        }catch(Exception e){
            if(Main.LOG != null){
                Main.LOG.error("TerminalService Error",e.toString());
            }else{
                e.printStackTrace();
            }
            result = null;
        }
        return result;
    }
    
    public Map findTerminal(Object params){
        Map result = null;
        try{
            result = service.findTerminal(params);
        }catch(Exception e){
            if(Main.LOG != null){
                Main.LOG.error("TerminalService Error",e.toString());
            }else{
                e.printStackTrace();
            }
            result = null;
        }
        return result;
    }
    
    static interface Service {
        
        public List<Map> getList(Object params);
        
        public Map register(Object params);
        
        public Map recover(Object params);
        
        public Map findTerminal(Object params);
        
    }
    
}
