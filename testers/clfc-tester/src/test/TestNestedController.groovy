package test;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.io.*;

class TestNestedController 
{
    def myclass;
    
    void init() {
        myclass = new MyClass(); 
        
        def session = OsirisContext.getSession();
        def folders = session.getFolders('home');
        folders?.each {
            println it.caption + '-> ' + it.properties;
        }
    }
    
    def items = [
        [name:"Item#1"], 
        [name:"Item#2"], 
        [name:"Item#3"], 
        [name:"Item#4"], 
        [name:"Item#5"] 
    ]; 
    def listhandler = [
        fetchList:{o-> 
            return items; 
        }
    ] as BasicListModel;
}

class MyClass {
    def selectedItem;
    
    void edit() {
        println 'you click edit...'; 
    }
    
    void save() {
        println 'you click save...'; 
    }
    
    void setSelectedItem(obj) {
        this.selectedItem = obj;
        println 'selectedItem-> ' + this.selectedItem;
    }
}
