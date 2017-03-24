package test.file;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.io.*;

class TestFileController 
{
    @Binding 
    def binding;
    
    @Service(value='FileUploadService', connection='fileserver')
    def proxy;
        
    //@XConnection('filereader')
    
    def fileChooser;
    
    void init() {
        println proxy;
        fileChooser = new javax.swing.JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);         
    }
    
    void upload() {
        
        int opt = fileChooser.showOpenDialog(null); 
        if (opt != fileChooser.APPROVE_OPTION) return; 
        
        def file = fileChooser.getSelectedFile(); 
        def fo = new com.rameses.io.FileObject(file); 
        fo.read();
        
        def header = fo.getInfo(); 
        def result = proxy.create( header );
        
        def counter = 1; 
        fo.getChunks().each{bytes-> 
            def detail = [
                parentid : header.objid,
                indexno  : counter, 
                content  : bytes
            ]; 
            proxy.addDetail( detail ); 
            counter++; 
        } 
        MsgBox.alert('Successfully uploaded.'); 
    }
    
    
    def image; 
    void fetch() {
        def resid = MsgBox.prompt("resource id: ");
        if (!resid) return;
        
        def appenv = com.rameses.rcp.framework.ClientContext.currentContext.appEnv;
        def freader = new com.rameses.io.FileReader();
        freader.conf = [
            'app.host'    : appenv['fileserver.host'],
            'app.cluster' : appenv['fileserver.cluster'],
            'app.context' : appenv['fileserver.context'],
        ]; 
        image = freader.read(resid); 
        binding.refresh('image'); 
    }
}
