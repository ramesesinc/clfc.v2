package test.expression;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class TestExpressionController 
{
    def entity = [:]; 
    
    def exprModel = [
        getValue: { return null; }, 
        setValue: {o-> },
        getVariables: {
            return [];
        }
    ] as ExpressionModel;
    def exprhandler = Inv.lookupOpener('expression:editor', [model:exprModel]);
    
    def selectedItem;
    def listItems = [];    
    def listhandler = [
        fetchList: { 
            return listItems;
        }
    ] as EditorListModel;
}
