package test;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.io.*;

class TestDropDownListController  
{
    void init() {
    }
        
    def items = [
        [name:"The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog", key:"item01"],
        [name:"The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog", key:"item02"]
        /*[name:"Item#2", key:"item02"], 
        [name:"Item#3", key:"item03"], 
        [name:"Item#4", key:"item04"], 
        [name:"Item#5", key:"item05"]*/
    ]; 
    
    def selectedItem;
    def listhandler = [
        fetchList:{params-> 
            println 'fetchList-> ' + params;
            return items; 
        },
        onselect: {o-> 
            println 'onselect-> ' + o;
        },
        getTitle: {
            return 'My Messages';
        }, 
        getItemText: {o-> 
            return o.name; 
        },
        getItemHeight: {
            return 50;
        },
        getWidth: {
            return 300;
        }
    ] as BasicListModel;
    
    
    def messagelist = listhandler;
}


