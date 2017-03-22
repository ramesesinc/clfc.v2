package test.explorerview

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.util.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class SampleTreeController 
{
    @Service('LoanLedgerService')
    def service;
    
    private boolean allowSearch = true;
    
    
    
    def getNodes( params ) { 
        return service.getNodes(params);
    }
    
    String getContext() {
        return 'explorer';
    }
    
    String getIcon() {
        return "Tree.closedIcon"; 
    }
    
    
    private List<Map> getNodeListImpl( node ) {
        Map params = new HashMap(); 
        Object item = node.getItem(); 
        
        println 'item ' + item;
        if (item instanceof Map) 
            params.putAll((Map) item); 
        else 
            params.put("item", node.getItem()); 

        params.put("root", (node.getParent() == null));
        params.put("caption", node.getCaption());
        List<Map> nodes = []; //getNodes(params);//root.getNodes(params);
        
        Map data = new HashMap();
        data.put('caption', 'Node 1');
        data.put('name', 'node1');
        data.put('folder', false);
        data.put('icon', "images/doc-view16.png");
        nodes.add(data);
        
        data = new HashMap();
        data.put('caption', 'Node 2');
        data.put('name', 'node2');
        data.put('folder', false);
        data.put('icon', "images/doc-view16.png");
        nodes.add(data);
        
        //nodes << [caption: 'Node 1', name: 'node1', folder: false];
        //nodes << [caption: 'Node 2', name: 'node2', folder: false];
        
        if (nodes == null) nodes = new ArrayList();

        if (node.getParent() == null && allowSearch) {
            Map search = new HashMap();
            search.put("folder", false);                 
            search.put("name", "search");
            search.put("caption", "Search"); 
            search.put("icon", "images/doc-view16.png");
            /*
            if (nodes.isEmpty()) 
                nodes.add(search); 
            else 
                nodes.add(0, search); 
            */
        }
        return nodes; 
    }
    
    void initChildNodesImpl( nodes ) {
        if (nodes == null) return;

        for (Node node: nodes) {
            if (node.getIcon() != null) continue;

            String icon = node.getPropertyString("filetype");
            if (icon == null) icon = "default_folder"; 

            String path = "images/explorer/" + getContext();
            node.getProperties().put("iconpath", path); 

            String res = path+"/"+icon.toLowerCase()+".png";
            if (ControlSupport.isResourceExist(res)) { 
                node.setIcon(res);  
            } else {
                node.setIcon(getIcon());
            }
        }
    }
    
    
    public Node[] fetchNodesImpl( node ) {
        def list = nodeModel.getNodeList(node);
        if (!list) return null;
        
        def nodes = [];
        list?.each{ o->
            if (o) nodes << o;
        }
        
        return nodes;
        /*
    List nodes = new ArrayList();
    for (Map data : list) {
      if ((data != null) && (!data.isEmpty())) {
        Node childNode = new Node(data);
        nodes.add(childNode);
      }
    }
    return (Node[])nodes.toArray(new Node[0]);
        return [];
        */
    }
    
    def selectedNode;
    def nodeModel = [
        isRootVisible: { return true; },
        isAutoSelect: { return false; },
        getNodeList: { node->
            //return null;
            println 'get node list';
            return getNodeListImpl(node);
        },
        fetchNodes: { node->
            def list = nodeModel.fetchNodes(node);
            println 'list ' + list;
            /*
            def list = fetchNodesImpl(node);
            println 'fetch nodes';
            
            //return [[caption: 'Node 1']];
            return [[caption: 'Node 1'], [caption: 'Node 2']];
            */
           return [];
        },
        initChildNodes: { nodes->
            println 'init child nodes';
            initChildNodesImpl(nodes);
        }
    ] as TreeNodeModel;
}

