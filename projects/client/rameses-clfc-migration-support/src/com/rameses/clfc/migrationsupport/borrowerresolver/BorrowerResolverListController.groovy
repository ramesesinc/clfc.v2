package com.rameses.clfc.migrationsupport.borrowerresolver

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class BorrowerResolverListController extends ListController
{
    @Binding
    def binding;

    @Service("MigrationBorrowerResolverService")
    def svc;

    String serviceName = "MigrationBorrowerResolverService";
    String entityName = "borrowerresolver";

    boolean allowCreate = false;
    //boolean allowSearch = false;

    void beforeGetColumns(Map params) {
        params.state = selectedOption?.state;
    }

    List getFormActions() {
        def list = super.getFormActions();
        list.each{ o->
            //println o.properties;
        }
        return list;
    }

    void setSelectedEntity( selectedEntity ) {
        super.setSelectedEntity(selectedEntity);
        //println 'selected entity ' + selectedEntity;
        //binding?.refresh('formActions');
    }

    def selectedOption, xlist;
    def optionsModel = [
        getItems: {
            return svc.getStates();
        }, 
        onselect: { o->
            query.state = o.state;
            binding?.refresh('formActions');
            reloadAll();
        }
    ] as ListPaneModel;

    void afterFetchList(List list) {
        list.each{ o->
            if (o.state == 'RESOLVED') o.filetype = 'borrower-resolved';
        }
        xlist = list;
    }

    def mergelist = [], selectedItem;
    def mergeListModel = [
        getItems: { 
            return mergelist; 
        }
    ] as ListPaneModel;

    void saveMergeRequest() {
        if (!MsgBox.confirm("You are about to save merge request. Continue?")) return;
        
        //svc.submitForApproval([list: mergelist]);
        svc.saveMergeRequest([list: mergelist]);
        clearMergeList();        
        MsgBox.alert("Successfully submitted items for merge approval!");
        listHandler?.reload();
    }

    void addMergeItem() {
        def item = mergelist.find{ it.objid == selectedEntity.objid }
        if (item) throw new Exception("This record has already been added.");

        mergelist.add(selectedEntity);
        mergeListModel?.reload();
    }

    void removeMergeItem() {
        if (!MsgBox.confirm("You are about to remove this item. Continue?")) return;

        mergelist.remove(selectedItem);
        mergeListModel?.reload();
    }

    void clearMergeList() {
        mergelist = [];
        mergeListModel?.reload();
    }


    void resolveAll() {
        if (!MsgBox.confirm("You are about to resolve all current items of this list. Continue?")) return;
        //xlist.each{ println it }
        svc.resolveAll([list: xlist]);
        MsgBox.alert("Successfully resolve items.");
        reload();
    }

    def getPagecount() {
        //if (!list) return "Page 1 of ?";
        return "Page " + listHandler.getPageIndex() + " of ?";
    }
}