package test.explorerview

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.util.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class SampleTreeNodeImpl extends TreeNodeModel {
    SampleExplorerViewController root;

    public SampleTreeNodeImpl(SampleExplorerViewController root) {
        this.root = root;
    }

    public boolean isRootVisible() {
        return this.root.isRootVisible();
    }

    public boolean isAutoSelect() {
        return this.root.isAutoSelect();
    }

    public String getIcon() {
        String icon = this.root.getIcon();
        if ((icon == null) || (icon.length() == 0)) {
            return "Tree.closedIcon";
        }
        return icon;
    }

    public List<Map> getNodeList(Node node) {
        Map params = new HashMap();
        Object item = node.getItem();
        if (item instanceof Map)
            params.putAll((Map)item);
        else {
            params.put("item", node.getItem());
        }
        params.put("root", Boolean.valueOf(node.getParent() == null));
        params.put("caption", node.getCaption());
        List nodes = this.root.getNodes(params);
        if (nodes == null) nodes = new ArrayList();

        if ((node.getParent() == null) && (this.root.isAllowSearch())) {
            Map search = new HashMap();
            search.put("folder", Boolean.valueOf(false));
            search.put("name", "search");
            search.put("caption", "Search");
            search.put("icon", "images/doc-view16.png");
            if (nodes.isEmpty())
                nodes.add(search);
            else
                nodes.add(0, search);
        }
        return nodes;
    }

    public void initChildNodes(Node[] nodes) {
        if (nodes == null) return;

        for (Node node : nodes) {
            if (node.getIcon() != null)
                  continue;
            String icon = node.getPropertyString("filetype");
            if (icon == null) icon = "default_folder";

            String path = "images/explorer/" + this.root.getContext();
            node.getProperties().put("iconpath", path);

            String res = path + "/" + icon.toLowerCase() + ".png";
            if (ControlSupport.isResourceExist(res))
                  node.setIcon(res);
            else
                  node.setIcon(getIcon());
        }
    }

    public Object openFolder(Node node) {
        return openNodeImpl(node);
    }

    public Object openLeaf(Node node) {
        return openNodeImpl(node);
    }

    private Object xopenNodeImpl(Node node) {
        println 'open node impl ' + node;
        println 'query form name ' + this.root.getQueryFormName();
        println 'query form visible ' + this.root.isQueryFormVisible();
    }
    
    private Object openNodeImpl(Node node) {
        println 'open node impl ' + node;
        if (node == null) return null;

        String filetype = node.getPropertyString("filetype");
        if (filetype == null) {
            String childtypes = node.getPropertyString("childtypes"); 
            String[] values = (childtypes == null? null: childtypes.split(",")); 
            if (values != null && values.length > 0) filetype = values[0]; 
        }
        if (filetype == null) filetype = this.root.getDefaultFileType(); 

        this.root.setQueryFormName("queryform");

        String nodeName = (node == null? null: node.getPropertyString("name"));
        if ("search".equals(nodeName+"")) {
            //this.root._showQueryForm = true; 
            this.root.setQueryFormVisible(true);
        } else { 
            String sallowSearch = (node == null? null: node.getPropertyString("allowSearch"));
            String sfiletype = (node == null? null: node.getPropertyString("filetype"));
            if (sfiletype != null) sfiletype = sfiletype.toLowerCase();

            //this.root._showQueryForm = "true".equals(sallowSearch); 
            this.root.setQueryForVisible("true".equals(sallowSearch));
            if (this.root.isQueryFormVisible() && this.root.containsPage(sfiletype+":queryform")) {
                //this.root._queryFormName = sfiletype + ":queryform";
                this.root.setQueryFormName(sfiletype + ":queryform");
            } 
        } 
        println 'query form visible ' + this.root.isQueryFormVisible();
        
        this.root.getListHandler().setNode(node);
        this.root.getListHandler().setExtendedProperties(this.root.getWorkunitProperties()); 
        Map query = this.root.getListHandler().getQuery();
        if (query != null) {
            query.putAll(this.root.getQuery());
        }
        this.root.getListHandler().updateView(); 
        Object ob = this.getBinding();
        if (ob instanceof com.rameses.rcp.framework.Binding) {
            com.rameses.rcp.framework.Binding ab = (com.rameses.rcp.framework.Binding)ob;
            ab.refresh("nodechange"); 
            ab.refresh("listHandler.*");
        }
        return null; 
    }
}