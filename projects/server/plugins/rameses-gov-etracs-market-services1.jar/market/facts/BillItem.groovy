package market.facts;

import java.util.*;
import com.rameses.util.*;

public class BillItem {

    double amount;
    private Date duedate;
    int month;
    int year;

    double amtdue;
    boolean expired;
    double surcharge = 0;
    double interest = 0;
    boolean compromise;
    double amtpaid = 0;

    
    public BillItem( Date duedate, double rate ) {
        setDuedate( duedate );
        amount = rate;
        amtdue = rate;
    }


    public double getTotal() {
        return NumberUtil.round(amtdue + surcharge + interest);
    }

    def toItem() {
        return [
            amount: amount,
            amtdue: amtdue,
            expired: expired,
            surcharge:surcharge,
            interest: interest,
            total: total,
            duedate: duedate,
            amtpaid: amtpaid,
            compromise: compromise,
            year: year,
            month: month
        ]
    }
    
    void updatePayment(double d) {
        this.amtpaid = d;
        //if amtpaid less than total, we need to correct how much proportion is applied to 
        if( amtpaid < total ) {
            double _total = total;
            amtdue = NumberUtil.round( amtdue / _total * amtpaid);    
            if( surcharge > 0.0 ) {
                surcharge = NumberUtil.round( surcharge / _total * amtpaid );
            }
            if( interest > 0.0 ) {
                interest = NumberUtil.round( amtpaid - amtdue - surcharge );
            }
        }
    }

    public void setDuedate( Date d ) {
        def db = new DateBean(d);    
        this.duedate = db.date;
        this.month = db.month;
        this.year = db.year;
    }

    public Date getDuedate() {
        return this.duedate;        
    }

}
