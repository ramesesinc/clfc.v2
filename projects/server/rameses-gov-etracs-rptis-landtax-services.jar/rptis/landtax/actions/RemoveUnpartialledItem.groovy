package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;


public class RemoveUnpartialledItem implements RuleActionHandler {
	def numSvc
	def facts 
	def items 

	public void execute(def params, def drools) {
		def item = items.find{ it.objid == params.rptledgeritem.objid }
		items.remove(item)
		facts.remove(params.rptledgeritem)
	}
}	