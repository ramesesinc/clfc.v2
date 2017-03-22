package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;

public class SplitByQtr implements RuleActionHandler {
	def createRPTLedgerItemFact
	def numSvc
	def facts
	def items 

	public void execute(def params, def drools) {
		def rptledger = params.rptledgeritem.rptledger 
		def mainidx = params.rptledgeritem.idx
		def itemyear = params.rptledgeritem.year 
		def firstitem = params.rptledgeritem.firstitem 

		drools.retract(params.rptledgeritem)
		facts.remove(params.rptledgeritem)

		def item = items.find{ it.objid == params.rptledgeritem.objid }
		items.remove(item)

		def oldid = item.objid 
		def qtrlyav  = numSvc.round( item.originalav / 4)
		def fourthqtrav = item.originalav - numSvc.round((qtrlyav * 3))

		for (int qtr=item.fromqtr; qtr <= item.toqtr; qtr++){
			def newitem = [:]
			newitem.putAll(item)
			newitem.objid = oldid + '-' + qtr
			newitem.year = itemyear 
			newitem.qtr = qtr 
			newitem.fromqtr = qtr
			newitem.toqtr = qtr
			newitem.av = qtrlyav
			newitem.firstitem = firstitem 
			if (qtr == 4) 
				newitem.av = fourthqtrav

			items << newitem

			def ledgeritem = createRPTLedgerItemFact(rptledger, newitem, mainidx)
			mainidx += 1
			facts << ledgeritem 
			drools.insert(ledgeritem)
			firstitem = false;
		}

		//renumber items greater than this year 
		def ledgeritemfacts = getLedgerItemFacts(itemyear)
		ledgeritemfacts.each{ lf ->
			def itm = items.find{it.objid == lf.objid }
			lf.idx = mainidx
			itm.idx = mainidx 
			mainidx += 1
			drools.update(lf)
		}
		
	}

	def getLedgerItemFacts(itemyear){
		return facts.findAll{
			if (it instanceof RPTLedgerItemFact && it.year > itemyear)
				return true 
			return false
		}
	}
}