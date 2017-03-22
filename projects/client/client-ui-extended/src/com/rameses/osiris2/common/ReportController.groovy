package com.rameses.osiris2.common;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

public abstract class ReportController {
    
    @Binding
    def binding
    
    @Service("ReportParameterService") 
    def svcParams; 
    
    def mode = "initial";
    def entity = [:];
    def params = [:];
    def reportdata
    
    abstract def getReportData();
    abstract String getReportName();
    
    def getFormControl(){
        return null;
    }
    
    SubReport[] getSubReports(){
        return null;
    }
    
    Map getParameters(){
        return [:]
    }
    
    def initReport(){
        return 'default'
    }
    
    def init() { 
        mode = 'initial';
        return initReport();
    }
    
    def preview() {
        buildReport();
        mode = 'view';
        return 'preview';
    }
    
    void print(){
        buildReport()
        ReportUtil.print( report.report, true )
    }
    
    void buildReport(){
        reportdata = getReportData();
        params = getParameters();
        report.viewReport();
    }
        
    def report = [
        getReportName : { return getReportName() }, 
        getReportData : { return reportdata },
        getSubReports : { return getSubReports() },
        getParameters : { return  params },
    ] as ReportModel;
    
    def back() {
        mode = 'initial'
        return 'default' 
    }
    
}
