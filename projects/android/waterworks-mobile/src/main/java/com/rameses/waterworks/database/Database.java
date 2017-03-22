package com.rameses.waterworks.database;

import com.rameses.waterworks.bean.Setting;
import java.util.List;

public interface Database {
    
    public void createSetting(Setting s);
    
    public void updateSetting(Setting s);
    
    public List<Setting> getAllSettings();
    
}
