package test.explorerview

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class SampleExplorerViewListController extends ExplorerViewListController
{
    
    private SampleExplorerViewController parentController2;
    
    String getTitle() {
        println 'get title';
        
        def props = this.node.properties;
        def total = props.totalaccounts;
        if (!total) total = 0;
        
//        println 'get title total ' + total;
        
        return total + ' Accounts';
        //println 'get title query ' + getQuery();
        //return 'Title';
    }
    
    private void resetTotalAccounts() {
        resetTotalAccounts([:]);
    }
    
    private void resetTotalAccounts( params ) {
        //println 'total ' + svc.getTotalAccounts([:]);
        //println 'params ' + params;
        def total = this.parentController2.svc.getTotalAccounts(params);
        if (!total) total = 0;
        def props = node.properties;
        
        //prinltn' reset total accounts total ' + total;
        props.totalaccounts = total; //service.getTotalAccounts([:]);
        binding?.refresh('title');
    }
    
    public void updateView() {
        def qrymap = [:];
        qrymap.putAll(this.parentController2.queryMap);
        super.updateView();
        //getQuery().putAll(this.parentController2?.getQuery());
        getQuery().putAll(qrymap)
        reload();
        //println 'title ' + getTitle() + ' query map2 ' + this.parentController2.queryMap;
    }
    
    void setNode(Node node) {
        super.setNode(node);
        resetTotalAccounts();
    }
    
    List fetchList(Map params) {
        //this.parentController2.setQuery(query);
        def list = super.fetchList(params);
        try {
            if (this.parentController2 && query) {
                this.parentController2.setQueryMap(query);
            }
            
            println 'params ' + params;
            def prm = [typeid: params.typeid, rootid: params.rootid, searchtext: params.searchtext];
            resetTotalAccounts(prm);
            //resetTotalAccounts();
            /*
            println 'refresh title1 ' + getTitle();
            binding?.refresh('title');
            println 'refresh title2 ' + getTitle();
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public void setParent(ExplorerViewController parentController) {
        //println 'parent controller ' + parentController;
        this.parentController2 = parentController;
        super.setParent(parentController);
    }
}

