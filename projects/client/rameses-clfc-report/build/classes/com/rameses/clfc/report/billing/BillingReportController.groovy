package com.rameses.clfc.report.billing

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class BillingReportController extends ReportModel {

    @Service("TestBillingReportService")
    def service;
    
    
    def startdate, enddate, reportdata;
    def page = "init";
    
    void init() {
        startdate = java.sql.Date.valueOf("2016-09-20");
        enddate = startdate;
    }
    
    def close() {
        return "_close";
    }
    
    def back() {
        page = "init";
        return "default";
    }
    
    def preview() {
        reportdata = service.getReportData();
        page = "preview";
        viewReport();
        return page;
    }
    
    public Map getParameters() {
        return [:];
    }

    public Object getReportData() {
        return reportdata;
//        return service.getReportData([startdate: startdate, enddate: enddate]);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/billing/BillingReport.jasper";
//        return "com/rameses/clfc/report/amnesty/expired/ExpiredAmnestySummaryReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
//            new SubReport('DETAIL', 'com/rameses/clfc/report/amnesty/expired/ExpiredAmnestySummaryReportDetail.jasper')
            new SubReport("DATE_DETAIL", "com/rameses/clfc/report/billing/BillingReportPerDate.jasper"),
            new SubReport("ROUTE_DETAIL", "com/rameses/clfc/report/billing/BillingReportPerRoute.jasper"),
            new SubReport("ITEM_DETAIL", "com/rameses/clfc/report/billing/BillingReportPerItem.jasper")
        ];
    }
    
}

