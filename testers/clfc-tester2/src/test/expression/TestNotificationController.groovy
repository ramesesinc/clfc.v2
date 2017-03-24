
package test.expression;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*
        
class TestNotificationController
{
    @Notifier
    def notifier;
    
    def entity = [:];
    
    void open() {
        //notifier.removeMessage(entity); 
    }
}