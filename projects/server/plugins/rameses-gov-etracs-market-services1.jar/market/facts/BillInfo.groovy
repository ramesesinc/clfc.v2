package market.facts;

import java.util.*;
import com.rameses.util.*;

public class BillInfo {
    
    Date startdate;
    Date billdate;   
    double rate;
    int type;
    int day;
    
    public BillInfo(Map map) {
    	if(map.rate) {
    		this.rate = Double.parseDouble(map.rate+"");
    	}
        def df = new DateBean( map.startdate );
        this.startdate = df.date;
        this.day = df.day;

        if(map.lastpmtdate) {
            def dd = new DateBean( map.lastpmtdate );
            int mon = dd.month-1;
            if( dd.day >= this.day) mon=mon+1;
            Calendar cal = Calendar.getInstance();
            cal.set( Calendar.YEAR, dd.year );
            cal.set( Calendar.MONTH, mon );
            cal.set( Calendar.DATE, this.day );
            this.startdate = cal.getTime();
        } 

        def df1 = new DateBean( (map.billdate)? map.billdate : map.today );
        this.billdate = df1.date;
 		if(!map.billingtype) map.billingtype = "0";
       	type = Integer.parseInt( map.billingtype + "" );   
    }



}