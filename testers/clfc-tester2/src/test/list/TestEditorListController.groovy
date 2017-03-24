package test.list;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class TestEditorListController 
{
    def items = []; 
    
    def selectedItem;
    def listhandler = [
        fetchList:{params-> 
            return items; 
        }
    ] as EditorListModel;
    
    
    void init() {
        
    }
    
    def getLookupName() {
        return Inv.lookupOpener('var:lookup', [:]);
    }
}
