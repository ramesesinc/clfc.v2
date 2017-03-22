package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;




public class AddMiscAccount implements RuleActionHandler {
	def numSvc
	def taxes 

	public void execute(def params, def drools) {
		def amount = numSvc.round(params.expr.getDecimalValue())
		if (amount > 0.0){
			def tax = taxes.find{it.item.objid 	== params.acct.key}
			if (!tax){
				tax = [
					objid 		: 'BI' + new java.rmi.server.UID(),
					revperiod	: 'current',
					revtype 	: 'misc',
					item 		: [objid:params.acct.key, title:params.acct.value],
					amount 		: amount,
				]
				taxes << tax
			}
			tax.amount = amount 
		}
	}
}	
