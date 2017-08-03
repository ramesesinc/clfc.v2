package com.rameses.clfc.producttype2.attribute

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class AttributeController extends CRUDController {
    
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    @PropertyChangeListener
    def listener = [
        "type": { o->
            entity.type = o?.type;
            entity.defaultvalue = null;
            binding?.refresh("opener");
        }
    ]
    
    String serviceName = "LoanProductTypeAttributeService";
    def typeList = [], type;
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    Map createEntity() {
        return [txnstate: "DRAFT", conditiontype: "USER", _allowedit: true];
    }
    
    void checkEditable( data ) {
        allowEdit = false;
        if (data._allowedit) {
            allowEdit = data._allowedit;
        }
        binding?.refresh("formActions");
    }
    
    void afterCreate( data ) {
        type = null;
        resetTypeList();
        checkEditable(data);
    }
    
    void resetTypeList() {
        typeList = [];
        def list = Inv.lookupOpeners("producttype:attribute:plugin");
        list?.each{ o->
            def props = o.properties;
            if (props) {
                typeList << [caption: o.caption, type: props.reftype];
            }
        }
        
        typeList.sort{ it.type }
    }
    
    def getCategoryList() {
        def list = [];
        def x = Inv.lookupOpeners("producttype:attribute:category:plugin");
        x?.each{ o->
            println 'o-> ' + o;
            list << o.caption;
        }
        
        list.sort{ it }
        
        return list;
    }
    
    void afterOpen( data ) {
        resetTypeList();
        type = typeList.find{ it.type==data.type }
        
        checkEditable(data);
        binding?.refresh();
    }
    /*
    def getTypeList() {
        def list = [];
        
        return list;
    }
    */
    
    def getOpener() {
        if (!entity.type) entity.type = "blank";
        def params = [
            entity  : entity,
            mode    : mode
        ]
        
        def op = Inv.lookupOpener("producttype:attribute:" + entity.type, params);
        if (!op) return null;
        return op;
    }
    
    void activate() {
        entity = service.activate(entity);
        checkEditable(entity);
        caller?.reload();
    }
    
    void deactivate() {
        entity = service.deactivate(entity);
        checkEditable(entity);
        caller?.reload();
    }
}

