package com.rameses.clfc.report.loan.masterlist

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.common.*;

class LoanMasterListReportController extends ReportModel 
{
    @Service("LoanMasterListReportService")
    def service;

    @Service("DateService")
    def dateSvc;
    
    @Binding
    def binding;
    
    String title = "Loan Master List";

    def mode = 'init'
    def startdate, enddate, criteria;
    def reportCriteriaList = [];

    void init() {
        startdate = dateSvc.getServerDateAsString();
        enddate = startdate;
        //reportCriteriaList = service.getCriteria();
    }
    
    def routecode;
    def getRouteList() {
        return service.getRoutes();
    }
    
    def categoryid;
    def getCategoryList() {
        return service.getCategories();
    }
    
    def close() {
        return "_close";
    }
    
    private def getParams() {
        return [
            routecode   : routecode,
            categoryid  : categoryid
        ];
    }
    
    def rptdata;
    def loadingOpener = Inv.lookupOpener("popup:loading", [:]);
    
    def preview() {
//        service.generate(getParams());

//        println rptdata;
//        println 'passing';
//        rptdata = service.getReportData(getParams());
//        viewReport();
//        mode = 'preview';
//        return 'preview';
        def onMessageP = { o ->
            
            loadingOpener.handle.closeForm();
            if (o == AsyncHandler.EOF) {
                return;
            }
            rptdata = o;
            viewReport();
            mode = 'preview';
            binding.fireNavigation('preview');
           
            
        }

        def handler = [
            onMessage   : onMessageP,
            onError     : { p ->
                loadingOpener.handle.closeForm();
                
//                if (showconfirmation==true) {
//                    def msg = p.message;
//                    msg += 'Do you still want to continue?';
//                    if (MsgBox.confirm(msg)) {
//                        showconfirmation = false;
//                    }
//                } 
//                else {
                    MsgBox.err(p.message);
//                }
            }
        ] as AsyncHandler;
        service.getReportData(getParams(), handler);
        return loadingOpener;
        
    }
    
    def back() {
        mode = 'init';
        return "default";
    }
    public Map getParameters() {
        return [:];
    }

    public Object getReportData() {
        return rptdata; //service.getReportData(getParams());
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/loan/masterlist/LoanMasterListReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/loan/masterlist/LoanMasterListReportDetail.jasper'),
            new SubReport('ROUTE_DETAIL', 'com/rameses/clfc/report/loan/masterlist/LoanMasterListReportRouteDetail.jasper'),
            //new SubReport('CATEGORY_DETAIL', 'com/rameses/clfc/report/loan/masterlist/LoanMasterListReportCategoryDetail.jasper')
        ];
    }
}

