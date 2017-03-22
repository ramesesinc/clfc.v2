package com.rameses.gov.treasury.remittance;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import javax.swing.*
import com.rameses.io.*
        
class RemittanceImportController
{
    @Binding
    def binding;
    
    @Service('RemittanceImportExportService')
    def svc;
    
    def MODE_INIT = 'init';
    def MODE_READ = 'read';
    
    def data;
    def mode;
    
    String getTitle(){
        return 'Import Remittance';
    }
    
    void init(){
        data = [
            state : 'UNPOSTED',
        ];
        mode   = MODE_INIT;
    }
    
    def getEntity(){
        return data?.entity
    }
    
    void upload(){
        if (MsgBox.confirm('Upload Remittance?')){
            svc.importRemittance(data);
            MsgBox.alert('Remittance has been successfully uploaded.');
        }
    }
    
    void doImport(){
        def chooser = new JFileChooser();
        int i = chooser.showOpenDialog(null);
        if(i==0) {
            data = FileUtil.readObject( chooser.selectedFile );
            data.state = 'POSTED';
            receiptListHandler.reload();
            afcontrolListHandler.reload();
            ctListHandler.reload();
            checkListHandler.reload();
            mode = MODE_READ;
        }      
    }
          
    
    /*=================================================================
     *
     * HANDLERS 
     *
     *================================================================= */
    
    def receiptListHandler = [
            fetchList : { return data.cashreceipts; }
    ] as BasicListModel;
    
    
    def afcontrolListHandler = [
            fetchList    : { return data.afserialinventories; },
    ] as BasicListModel;
            
    def ctListHandler = [
            fetchList    : { return data.ctinventories; },
    ] as BasicListModel;      
            
    def checkListHandler = [
            fetchList    : { return data.checkpayments; },
    ] as BasicListModel;
    
}

