package test;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.io.*;

class TestRadioListController  
{
    void init() {
    }
        
    def items = [
        [name:"Item#1", key:"item01"], 
        [name:"Item#2", key:"item02"], 
        [name:"Item#3", key:"item03"], 
        [name:"Item#4", key:"item04"], 
        [name:"Item#5", key:"item05"] 
    ]; 
    
    def selectedItem = items[1].key;
    def listhandler = [
        fetchList:{params-> 
            println 'fetchList-> ' + params;
            return items; 
        },
        onselect:{o-> 
            println 'onselect-> ' + o; 
            selectedItem = o;
        }
    ] as BasicListModel;
    
    void refresh() {
        println 'refresh...'; 
    }
    
    boolean enabled = true; 
    void enable() {
        enabled = true; 
    }
    
    void disable() {
        enabled = false; 
    }
}


