package com.rameses.waterworks.bean;

public class Account {
    
    private String objid,name,address,appno,sector,zone;
    
    public Account(
           String objid,
           String name,
           String address,
           String appno,
           String sector,
           String zone
    ){
        this.objid = objid;
        this.name = name;
        this.address = address;
        this.appno = appno;
        this.sector = sector;
        this.zone = zone;
    }
    
    public String getObjid(){ return objid; }
    public String getName(){ return name; }
    public String getAddress(){ return address; }
    public String getAppno(){ return appno; }
    public String getSector(){ return sector; }
    public String getZone(){ return zone; }
    
    public void setObjid(String s){ objid = s; }
    public void setName(String s){ name = s; }
    public void setAddress(String s){ address = s; }
    public void setAppno(String s){ appno = s; }
    public void setSector(String s){ sector = s; }
    public void setZone(String s){ zone = s; }
    
}
