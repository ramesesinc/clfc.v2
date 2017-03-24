package test;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.io.*;

class TestCheckListController  
{
    def entity = [:]; 
    
    void init() {
    }
        
    def items = [
        [name:"Monday", key:"mon"], 
        [name:"Tuesday", key:"tue"], 
        [name:"Wednesday", key:"wed"], 
        [name:"Thursday", key:"thu"], 
        [name:"Friday", key:"fri"],
        [name:"Saturday", key:"sat"],
        [name:"Sunday", key:"sun"]
    ]; 
    
    def selectedItem = [items[1].key, items[5].key];
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
    
    void submit() {
        println 'submit... ' + selectedItem;
    }
}


