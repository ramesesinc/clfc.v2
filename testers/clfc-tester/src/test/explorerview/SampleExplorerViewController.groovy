package test.explorerview

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*; 
import com.rameses.osiris2.common.*;

class SampleExplorerViewController extends ExplorerViewController 
{
    @Service('LoanLedgerService')
    def svc;
    
    String serviceName = 'LoanLedgerService';
    String defaultFileType = 'loanledger';
    private SampleExplorerViewListController listHandler;
    private Map queryMap = [:];
    
    public void setQueryMap( queryMap ) {
        this.queryMap = queryMap;
    }
    
    public Map getQueryMap() {
        if (!this.queryMap) queryMap = [:];
        return this.queryMap;
    }
    
    /*
    void setQuery( query ) {
        //this.query = query;
        println 'sample explorer view controller ' + query;
        this.queryMap = query;
    }
    */
    
    
    /*
    void setupWorkunitProperties() {
        super.setupWorkunitProperties();
        Map map = getWorkunitProperties(); 
        if (queryMap) map.putAll(queryMap);
    }
    */
    
    public ExplorerViewListController getListHandler() {
        if (this.listHandler == null) {
            this.listHandler = new SampleExplorerViewListController();
            this.listHandler.setParent(this);
        }
        
        return this.listHandler;
    }
}

