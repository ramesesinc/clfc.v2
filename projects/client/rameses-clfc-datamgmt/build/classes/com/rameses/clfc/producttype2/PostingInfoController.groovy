package com.rameses.clfc.producttype2

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class PostingInfoController {

    @Binding
    def binding;
    
    @Script('Template')
    def template
    
    @Service("NewLoanProducttypeService")
    def service;
    
    def entity, mode = "read";
    
    void init() {
        if (!entity) entity = [:];
        if (!entity.postinginfo) entity.postinginfo = [title: "POSTINGINFO"];
    }
    
    def selectedHeader;
    def headersHandler = [
        fetchList: { o->
           if (!entity.postinginfo.postingheader) entity.postinginfo.postingheader = [];
           def list = entity.postinginfo.postingheader;
           list.sort{ it.sequence }
           list.each{ i->
               i.isfirst = false;
               i.islast = false;
           }
           if (list.size() > 0) {
               list[0].isfirst = true;
               list[list.size() - 1].islast = true;
           }
           return list;
        }
    ] as ListPaneModel; 
    
    void generateDefaultPosting() {
        def header = service.getDefaultPostingHeader();
        if (!entity.postinginfo.postingsequence) entity.postinginfo.postingsequence = [];

        entity.postinginfo.postingsequence = [];
        header.each{ o->
            def item = entity?.postinginfo.postingsequence?.find{ it.code == o.code }
            if (!item) {
                item = [
                    code            : o.code, 
                    title           : o.title, 
                    name            : o.name, 
                    index           : o.index,
                    sequence        : o.sequence,
                    isfirst         : o.isfirst, 
                    islast          : o.islast,
                    //postingexpr     : o.postingexpr,
                    postingexpr     : '',
                    datatype        : o.datatype,
                    postonlastitem  : true
                ];
                entity?.postinginfo.postingsequence << item; 
            }
        }
        headersHandler.reload();
        binding?.refresh("postingHtml");
    }
    
    def addHeader() {
        
    }
    
    void removeHeader() {
        if (!selectedHeader) return;
        
        
    }
    
    void moveUpHeader() {
        if (!selectedHeader) return;
        
        def idx = selectedHeader.index;
        
        def item = entity.postinginfo.postingheader.find{ it.index == (idx - 1) }
        if (item) {
            item.index++;
            item.sequence = item.index + 1;
        }
        selectedHeader.index--;
        selectedHeader.sequence = selectedHeader.index + 1;
        headersHandler?.reload();
    }
    
    void moveDownHeader() {
        if (!selectedHeader) return;
        
        def idx = selectedHeader.index;
        
        def item = entity.postinginfo.postingheader.find{ it.index == (idx + 1) }
        if (item) {
            item.index--;
            item.sequence = item.index + 1;
        }
        selectedHeader.index++;
        selectedHeader.sequence = selectedHeader.index + 1;
        headersHandler?.reload();
    }
    
    def getPostingHtml() {
        if (!entity?.postinginfo.postingsequence) entity.postinginfo.postingsequence = [];
        def params = [
            list    : entity?.postinginfo.postingsequence,
            mode    : mode
        ]
        params.list.sort{ it.sequence }
        return template.render('html/producttype_posting_sequence_html.gtpl', params);
    }
    
    void moveUpCondition( params ) {
        def list = entity.postinginfo.postingsequence;
        def item = list.find{ it.code==params.code }
        if (item) {
            def idx = item.index;
            
            def item2 = list.find{ it.index==(idx - 1) }
            if (item2) {
                item2.index++;
                item2.sequence = item2.index + 1;
            }
            item.index--;
            item.sequence = item.index + 1;
        }
        binding?.refresh("postingHtml");
    }
    
    void moveDownCondition( params ) {
        def list = entity.postinginfo.postingsequence;
        def item = list.find{ it.code==params.code }
        if (item) {
            def idx = item.index;
            
            def item2 = list.find{ it.index==(idx + 1) }
            if (item2) {
                item2.index--;
                item2.sequence = item2.index + 1;
            }
            item.index++;
            item.sequence = item.index + 1;
        }
        binding?.refresh("postingHtml");
    }
    
    def editCondition( params ) {
        
        def item = entity.postinginfo.postingsequence.find{ it.code==params.code }
        if (!item) return;
        
        def handler = { o->
            
        }
        
        def xprm = [
            entity  : item,
            mode    : mode,
            handler : handler
        ];
        def op = Inv.lookupOpener("producttype:postinginfo:constraint:open", xprm);
        if (!op) return null;
        return op;
    }
}

