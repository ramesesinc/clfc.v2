package com.rameses.clfc.treasury.ledger.amnesty.smc

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class SMCConditionsListController 
{
    @Binding
    def binding;
    
    @Script("Template")
    def template;
    
    def entity, mode = 'read';
    def defaultvarlist;
    
    void init() {
        if (!entity) entity = [:];
    }
    
    def getRuleHtml() {
        if (!entity?.conditions) entity.conditions = [];
        //return template.render( "html/smc_condition_html.gtpl", [rule: entity, editable:true] );
                
        def params = [
            conditions  : entity?.conditions,
            mode        : mode
        ]
        return template.render('html/smc_condition_html.gtpl', params);
    }
    
    
    def getVarList() {
        def idx = (entity?.conditions? entity?.conditions?.size() : 0);
        return getVarList(idx);
    }
    
    def getVarList( currentindex ) {
        def list = [];
        if (defaultvarlist) list.addAll(defaultvarlist);
        if (entity?.conditions) {
            def xlist = entity?.conditions?.findAll{ it.index < currentindex && it.varname != null }
            
            def item;
            xlist?.each{ o->
                item = [:];
                item.caption = o.varname;
                item.title = o.varname;
                item.signature = o.varname;
                if (o.datatype) {
                    item.description = '(' + o.datatype + ')';
                } else {
                    item.description = '(' + o.handler + ')';
                }
                list << item;
            }
        }
        
        return list;
    }
    
    void moveUp( params ) {
        def item = entity?.conditions?.find{ it.objid == params.objid }
        
        if (item) {
            def idx = item.index;
            
            def item2 = entity?.conditions?.find{ it.index == (idx - 1) }
            if (item2) {
                item2.index++;
            }
            item.index--;
        }
        
        binding?.refresh('ruleHtml');
    }
    
    void moveDown( params ) {
        def item = entity?.conditions?.find{ it.objid == params.objid }
        
        if (item) {
            def idx = item.index;
            
            def item2 = entity?.conditions?.find{ it.index == (idx + 1) }
            if (item2) {
                item2.index--;
            }
            item.index++;
        }
        
        binding?.refresh('ruleHtml');
    }
    
    def addCondition() {
        def handler = { o->
            o.objid = 'SMCCOND' + new UID();
            o.parentid = entity.objid;
            o._allowremove = true;
            o.index = (entity?.conditions? entity?.conditions?.size() : 0) + 1;
            
            if (!entity?._addedconditions) entity._addedconditions = [];
            entity._addedconditions << o;
            
            if (!entity?.conditions) entity.conditions = [];
            entity.conditions << o;
            
            binding?.refresh('ruleHtml');
        }
        
        def op = Inv.lookupOpener('smc:condition:create', [handler: handler, mode: mode, varlist: getVarList()]);
        if (!op) return null;
        return op;
    }
    
    def editCondition( params ) {
        def item = entity?.conditions?.find{ it.objid == params.objid }
        
        def handler = { o->
            def data = [:];
            if (entity?._addedconditions) {
                item = entity?._addedconditions?.find{ it.objid == o.objid }
                if (item) item.putAll(o);
            }
            
            if (entity?.conditions) {
                item = entity?.conditions?.find{ it.objid == o.objid }
                if (item) item.putAll(o);
            }
            
            binding?.refresh('ruleHtml');
        }
        
        def op = Inv.lookupOpener('smc:condition:open', [handler: handler, mode: mode, data: item, varlist: getVarList(item.index)]);
        if (!op) return null;
        return op;
    }
    
    def removeCondition( params ) {
        def item = entity?.conditions?.find{ it.objid == params.objid }
        
        if (!entity?._removedconditions) entity._removedconditions = [];
        entity._removedconditions << item;
        
        if (entity?._addedconditions) entity._addedconditions.remove(item);
        
        if (entity?.conditions) entity.conditions.remove(item);
        
        binding?.refresh('ruleHtml');
    }
}

