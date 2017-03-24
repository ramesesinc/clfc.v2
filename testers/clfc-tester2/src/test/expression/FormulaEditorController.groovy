package test.expression;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class FormulaEditorController 
{
    def value;
    def handler;
    
    def doOk() {
        if (handler) handler(value); 
        
        return '_close'; 
    }
    
    def doCancel() {
        return '_close'; 
    }    
}
