package com.rameses.clfc.report.loan.fullypaid

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class LoanFullyPaidReportController extends ReportModel {
    
    @Service("LoanFullyPaidReportService")
    def service;

    @Service("DateService")
    def dateSvc;
    
    String title = "Loan Fully Paid";

    def mode = 'init'
    def startdate, enddate;

    void init() {
        startdate = dateSvc.getServerDateAsString();
        enddate = startdate;
    }
    
    def close() {
        return "_close";
    }
    
    private def getParams() {
        return [
            startdate   : startdate, 
            enddate     : enddate
        ];
    }
    
    def rptdata;
    def preview() {
        //service.generate(getParams());
        rptdata = service.getReportData(getParams());
        viewReport();
        mode = 'preview';
        return 'preview';
    }
    
    def back() {
        mode = 'init';
        return "default";
    }
    public Map getParameters() {
        return [:];
    }

    public Object getReportData() {
        return rptdata;
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/loan/fullypaid/LoanFullyPaidSummaryReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/loan/fullypaid/LoanFullyPaidSummaryReportDetail.jasper')
        ];
    }
}

