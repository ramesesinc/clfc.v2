package com.rameses.client.notification;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.framework.ClientContext;

class NotificationListController extends ListController 
{
    @Notifier
    def notifier;
    
    @FormTitle
    def title = 'Notifications';
    
    String serviceName  = 'NotificationService';
    boolean allowSearch = false;
    boolean allowCreate = false;
    
    def categories = [];
    def eventhandle;

    @Close
    void onclose() { 
        eventhandle?.unregister(); 
    } 
    
    void init() {
        categories << [name:'user', caption:'My Notifications', type:'user'];        
        Inv.lookup('notification-group').each{
            def groupname = it.properties.group;
            if (groupname) {
                def caption = (it.caption == null? groupname: it.caption);
                categories << [name: groupname.toUpperCase(), caption:caption, type:'group'];
            } 
        } 
        
        eventhandle = notifier.register({ reload(); });
    }
    
    public void beforeFetchList(Map params) {
        params.userid = ClientContext.currentContext.headers.USERID;
        if (selectedMenu) { 
            params.putAll(selectedMenu); 
            if (selectedMenu.type == 'user') {
                params.recipientid = params.userid;
            } else if (selectedMenu.type == 'group') { 
                params.groupid = selectedMenu.name; 
            } 
        }
    } 
    
    public def open() {
        def item = selectedEntity;
        if (item != null) {
            item.type = selectedMenu?.type;
            if (!item.filetype) item.filetype='notification-item';
        }        
        return super.open();
    } 
    
    void removeMessage() {
        if (!MsgBox.confirm('You are about to remove this message. Continue?')) return;
        
        if (!notifier) {
            MsgBox.alert('@Notifier is not set'); 
            return;
        }
        
        def item = selectedEntity;
        if (item) notifier.removeMessage(item);
    }
    
    def selectedMenu;    
    def menuHandler = [
        getDefaultIcon: {
            return 'Tree.closedIcon'; 
        },         
        getItems: { 
            return categories;
        }, 
        onselect: {o->
            selectedMenu = o;
            reload();
        } 
    ] as ListPaneModel;     
} 
