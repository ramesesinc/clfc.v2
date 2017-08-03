package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class CalculateBillingOthers implements RuleActionHandler {
	def NS;
	public void execute(def params, def drools) {
		//def LEDGER = params.ledger;
		def BILLITEM = params.billitem;
		BILLITEM.others = NS.round( params.amount.decimalValue );
	}

}