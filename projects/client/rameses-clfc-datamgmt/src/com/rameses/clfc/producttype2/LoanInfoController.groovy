package com.rameses.clfc.producttype2

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class LoanInfoController {

    @Binding
    def binding;
    
    @Script('Template')
    def template
    
    @Service("NewLoanProductTypeService")
    def service;
    
    def entity, mode = "read";
    def defaultvarlist = [];
    
    void init() {
        if (!entity) entity = [:];
        if (!entity.loaninfo) entity.loaninfo = [title: "LOANINFO"];
        defaultvarlist = service.getLoanInfoVarlist();
    }
    
    def getHtml() {
        if (!entity?.loaninfo?.attributes) entity.loaninfo.attributes = [];
        entity.loaninfo.attributes.sort{ it.index }
        def params = [
            list    : entity?.loaninfo?.attributes,
            mode    : mode
        ];
        
        //println 'list';
        //params.list.each{ println it.index }
        
        return template.render('html/producttype_loan_html.gtpl', params);
    }
    
    void changeIndex( objid, list, index ) {
        def data = list.find{ it.objid == objid }
        if (data) data.index = index;
    }
    
    void moveUpAttr( params ) {
        def item = entity?.loaninfo?.attributes?.find{ it.objid == params.objid }
        
        def data;
        if (item) {
            def idx = item.index;
            
            def item2 = entity?.loaninfo?.attributes?.find{ it.index == (idx - 1) }
            if (item2) {
                item2.index++;
                changeIndex(item2.objid, entity.loaninfo._addedattr, item2.index);
            }
            item.index--;
            changeIndex(item.objid, entity.loaninfo._addedattr, item.index);
            //println 'changed index ' + item.index + ' ' + item2.index;
        }
        
        binding?.refresh('html');
        
    }
    
    void moveDownAttr( params ) {
        def item = entity?.loaninfo?.attributes?.find{ it.objid == params.objid }
        
        if (item) {
            def idx = item.index;
            
            def item2 = entity?.loaninfo?.attributes?.find{ it.index == (idx + 1) }
            if (item2) {
                item2.index--;
                changeIndex(item2.objid, entity.loaninfo._addedattr, item2.index);
            }
            item.index++;
            changeIndex(item.objid, entity.loaninfo._addedattr, item.index);
        }
        binding?.refresh('html');
    }
    
    def buildVarList() {
        def idx = entity.loaninfo.attributes?.size();
        if (!idx) idx = 0;
        return buildVarList(idx);
    }
    
    def buildVarList( index ) {
        def list = defaultvarlist;
        def item;
        
        entity?.generalinfo?.attributes?.each{ o->
            item = [
                caption     : o.attribute?.varname,
                title       : o.attribute?.varname,
                signature   : o.attribute?.varname,
                handler     : o.attribute?.type
            ];
            
            if (item.handler == "expression") {
                item.description = "(decimal)";
            } else if (item.handler) {
                item.description = "(" + item.handler + ")";
            }
            
            list << item;
        }
        
        def xl = entity.loaninfo.attributes?.findAll{ it.index < index }
        xl?.each{ o->
            item = [
                caption     : o.attribute?.varname,
                title       : o.attribute?.varname,
                signature   : o.attribute?.varname,
                handler     : o.attribute?.type
            ];
            
            if (item.handler == "expression") {
                item.description = "(decimal)";
            } else if (item.handler) {
                item.description = "(" + item.handler + ")";
            }
            
            list << item;
        }
        
        return list;
    }
    
    def addAttr() {
        def handler = { o->
            def i = entity.loaninfo.attributes?.find{ it.attributeid==o.attributeid }
            if (i) throw new RuntimeException("Attribute has already been selected.");
            
            if (!o.index) o.index = entity.loaninfo.attributes.size();
            
            if (!entity.loaninfo.addedattr) entity.loaninfo._addedattr = [];
            entity.generalinfo._addedattr << o;
            
            if (!entity.loaninfo.attributes) entity.loaninfo.attributes = [];
            entity.generalinfo.attributes << o;
            
            binding?.refresh("html");
        }
        def params = [
            entity  : [:],
            mode    : "create",
            handler : handler,
            varlist : buildVarList()
        ];
        
        def op = Inv.lookupOpener("loan:producttype:loan:attribute:popup:create", params);
        if (!op) return null;
        return op;
    }
    
    def editAttr( params ) {
        def handler = { o->
            def i = entity.loaninfo.attributes?.find{ it.attributeid==o.attributeid && it.objid!=o.objid }
            if (i) throw new RuntimeException("Attribute has alread been selected.");

            def data = entity.loaninfo.attributes.find{ it.objid==o.objid }
            if (data) {
                data._updated=true;
                data.putAll(o);
            }

            data = entity.loaninfo._addedattr?.find{ it.objid==o.objid }
            if (data) {
                data.putAll(o);
            }

            binding?.refresh("html");
        }
        
        
        def item = entity?.loaninfo?.attributes?.find{ it.objid == params.objid }
        
        def op;
        if (item) {
            def xparam = [
                entity  : item,
                mode    : mode,
                handler : handler,
                varlist : buildVarList(item.index)
            ];
            op = Inv.lookupOpener("loan:producttype:loan:attribute:popup:open", xparam);
            
        }
        if (!op) return null;
        return op;
    }
    
    void removeAttr( params ) {
        def item = entity.loaninfo.attributes.find{ it.objid==params.objid }
        if (!item) return;
        
        if (!entity.loaninfo._removedattr) entity.loaninfo._removedattr = [];
        entity.loaninfo._removedattr << item;
        
        entity.loaninfo.attributes.remove(item);
        
        binding?.refresh("html");
    }
}

