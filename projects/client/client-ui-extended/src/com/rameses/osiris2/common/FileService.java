/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.osiris2.common;

import com.rameses.io.AbstractChunkHandler;
import com.rameses.io.FileChunker;
import com.rameses.osiris2.client.OsirisContext;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.service.ScriptServiceContext;
import com.rameses.util.Base64Cipher;
import com.rameses.util.Encoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author rameses
 */
public class FileService {
    
    private final static ExecutorService threadPool = Executors.newCachedThreadPool();
    
    private MyProxyService svc;
    
    public Map upload( File file ) {
        FileChunker fc = new FileChunker( file );
        ChunkHandlerImpl handler = new ChunkHandlerImpl();
        Map data = handler.createHeader( fc ); 
        threadPool.submit(new UploadRunnable( fc, handler )); 
        return data; 
    }
    
    public Map upload( byte[] bytes ) {
        return upload( bytes, "png", null ); 
    }
    
    public Map upload( byte[] bytes, String type, String name ) {
        if ( name == null) { name = "unknown"; }
        
        FileChunker fc = new FileChunker( bytes, name, type ); 
        ChunkHandlerImpl handler = new ChunkHandlerImpl(); 
        Map data = handler.createHeader( fc ); 
        threadPool.submit(new UploadRunnable( fc, handler )); 
        return data; 
    } 
    
    
    public void remove( String objid ) {
        Map params = new HashMap(); 
        params.put("objid", objid); 
        getService().remove( params ); 
    } 
    
    
    // <editor-fold defaultstate="collapsed" desc=" Proxy Service "> 
    
    private MyProxyService getService() {
        if (svc == null) {
            ScriptServiceContext context = new ScriptServiceContext( getAppEnv() ); 
            svc = context.create("FileUploadService", OsirisContext.getEnv(), MyProxyService.class); 
        }
        return svc; 
    }
    
    private Map getAppEnv() {
        return ClientContext.getCurrentContext().getAppEnv(); 
    }
    
    interface MyProxyService {
        Map createHeader( Map data ); 
        void createItem( Map data ); 
        void remove( Map data ); 
        
        Map readHeader( Map data ); 
        List getBatchItems( Map params ); 
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" ChunkHandlerImpl "> 
    
    private class ChunkHandlerImpl extends AbstractChunkHandler {

        Base64Cipher base64 = new Base64Cipher();
        List batchList = new ArrayList();        
        String objid = "FS" + new UID(); 
        
        Map createHeader( FileChunker fc ) {
            Map data = new HashMap();
            data.put("objid", objid); 
            data.put("filename", fc.getName()); 
            data.put("filetype", fc.getType()); 
            data.put("filesize", fc.getLength()); 
            data.put("chunkcount", fc.getCount()); 
            return getService().createHeader( data ); 
        }
        
        public void process(int index, byte[] bytes) {
            Map item = new HashMap();
            item.put("objid", objid + "-" + index ); 
            item.put("parentid", objid ); 
            item.put("indexno", index); 
            item.put("content", base64.encode( bytes ) ); 
            item.put("contentsize", bytes.length ); 
            batchList.add( item ); 
            
            if (batchList.size() >= 10) { 
                uploadBatch(); 
            } 
        }

        public void end() { 
            if ( !batchList.isEmpty() ) { 
                 uploadBatch(); 
            } 
            batchList.clear(); 
            batchList = null; 
        } 
        
        private void uploadBatch() {
            Map params = new HashMap(); 
            params.put("objid", objid); 
            params.put("items", batchList); 
            getService().createItem( params ); 
            batchList.clear(); 
        }
    } 
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" UploadRunnable "> 
    
    private class UploadRunnable implements Runnable {
        FileChunker chunker;
        ChunkHandlerImpl handler;
        
        UploadRunnable(FileChunker chunker, ChunkHandlerImpl handler) {
            this.chunker = chunker; 
            this.handler = handler; 
        }
        
        public void run() {
            chunker.parse( handler ); 
        }
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" ResourceObject "> 
    
    private class ResourceObject {
        private Base64Cipher base64;
        private String encid;
        private String id;
        
        ResourceObject(String id) {
            this.base64 = new Base64Cipher();
            this.encid = Encoder.MD5.encode(id); 
            this.id = id;            
        }
        
        InputStream getInputStream() { 
            File basedir = getTempDir(); 
            File resdir = new File(basedir, encid); 
            if ( !resdir.exists() ) resdir.mkdir(); 
            
            File indexfile = new File(resdir, "index"); 
            if ( !indexfile.exists() ) {
                createIndexFile(indexfile);
            }
            
//            Integer[] values = (Integer[]) base64.decode( readContent(indexfile) ); 
//            int filesize = values[0];
//            int indexno = values[2]; 
//            if (indexno >= chunkcount) {
//                try {
//                    File contentfile = new File(resdir, "content"); 
//                    return new FileInputStream( contentfile );
//                } catch (FileNotFoundException ex) {
//                    throw new RuntimeException(ex.getMessage(), ex); 
//                }
//            }
//            
//            downloadData( resdir, indexno, chunkcount );
            
            return null; 
        }
        
        void downloadData( File resdir, int indexno, int chunkcount ) {
            if ( indexno > chunkcount ) return; 
            
            Map params = new HashMap();
            params.put("objid", this.id); 
            params.put("startindex", indexno); 
            params.put("endindex", indexno + 5); 
            List items = getService().getBatchItems( params );
            
            FileOutputStream fos = null; 
            try { 
                fos = new FileOutputStream( new File(resdir, "tmpfile") );
                for ( Object o : items ) { 
                    MapInfo mi = new MapInfo((Map) o);
                    byte[] bytes = (byte[]) base64.decode( mi.getString("content") ); 
                    fos.write(bytes, 0, bytes.length); 
                }
                fos.flush();
            } catch(RuntimeException re) {
                throw re; 
            } catch(Exception e) {
                throw new RuntimeException(e.getMessage(), e); 
            } finally {
                try { fos.close(); }catch(Throwable t){;} 
            }

        }
        
        void createIndexFile( File file ) {
            MapInfo minfo = new MapInfo();
            minfo.put("objid", this.id); 
            minfo.data = getService().readHeader( minfo.data ); 
            if (minfo.data == null) {
                throw new RuntimeException("file header record not found");
            }

            Integer ofilesize = minfo.getInt("filesize");
            Integer ochunkcount = minfo.getInt("chunkcount"); 
            String encstr = base64.encode( new Integer[]{ ofilesize, ochunkcount } );  
            
            FileOutputStream fos = null; 
            try {
                fos = new FileOutputStream( file ); 
                fos.write(encstr.getBytes()); 
                fos.flush(); 
            } catch(RuntimeException re) {
                throw re; 
            } catch(Exception e) {
                throw new RuntimeException(e.getMessage(), e); 
            } finally {
                try { fos.close(); }catch(Throwable t){;} 
            }
        }
        
        byte[] readContent( File file ) {
            FileInputStream fis = null;
            BufferedInputStream bis = null; 
            ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
            try {
                fis = new FileInputStream( file ); 
                bis = new BufferedInputStream( fis ); 

                int bytesRead = -1; 
                byte[] bytes = new byte[ 32000 ]; 
                while ((bytesRead=bis.read(bytes)) != -1) {
                    baos.write(bytes, 0, bytesRead); 
                }
                return baos.toByteArray(); 
            } catch(RuntimeException re) {
                throw re; 
            } catch(Exception e) {
                throw new RuntimeException(e.getMessage(), e); 
            } finally {
                try { bis.close(); }catch(Throwable t){;} 
                try { fis.close(); }catch(Throwable t){;} 
                try { baos.flush(); }catch(Throwable t){;} 
                try { baos.close(); }catch(Throwable t){;} 
            } 
        }
    } 
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" CleanupTask "> 
    
    public void runCleanupTask() { 
        threadPool.submit( new CleanupTask() ); 
    } 
    
    private File getTempDir() { 
        Map appenv = getAppEnv(); 
        Object objval = (appenv == null? null: appenv.get("tempdir")); 
        String tempdir = (objval == null? null: objval.toString()); 
        if (tempdir == null || tempdir.length() == 0) {
            tempdir = System.getProperty("java.io.tmpdir");
        }
        File ftempdir = new File(tempdir);
        File basedir = new File(ftempdir, "rameses");
        if (!basedir.exists()) basedir.mkdir();
        
        return basedir; 
    }    
    
    private class CleanupTask implements Runnable { 
        
        public void run() { 
            removeObsoleteFiles();
        }
        
        void removeObsoleteFiles() {
            File basedir = getTempDir(); 
            
            Calendar cal = Calendar.getInstance();
            File[] files = basedir.listFiles();
            for (File f : files) { 
                cal.setTimeInMillis(f.lastModified()); 
                cal.add(Calendar.HOUR, 24); 
                if (cal.getTimeInMillis() < System.currentTimeMillis()) { 
                    try { 
                        delete(f); 
                    } catch(Throwable t) {
                        //do nothing 
                    }
                }
            }
        }
        
        void delete(File f) { 
            if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (File o : files) delete(o); 
            } 
            f.delete(); 
        } 
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" MapInfo ">
    
    private class MapInfo {
        
        Map data;
        
        MapInfo() {
            this( new HashMap() ); 
        } 
        
        MapInfo(Map data) {
            this.data = data; 
        }
        
        public Object put(Object key, Object value ) {
            return data.put( key, value );
        }
        
        public Object get( Object key ) {
            return (data == null? null: data.get(key)); 
        }
        public String getString( Object key ) {
            Object val = get( key ); 
            return (val == null? null: val.toString()); 
        }
        public Integer getInt( Object key ) {
            Object val = get( key );
            if ( val == null ) return null; 
            
            if ( val instanceof Integer ) {
                return (Integer) val; 
            } else {
                return new Integer( val.toString() ); 
            }
        }
    }
    
    // </editor-fold>
} 
 