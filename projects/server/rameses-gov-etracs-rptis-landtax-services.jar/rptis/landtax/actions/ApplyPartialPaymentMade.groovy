package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;


public class ApplyPartialPaymentMade implements RuleActionHandler {
	def numSvc
	def items 

	public void execute(def params, def drools) {
		def item = items.find{ it.objid == params.rptledgeritem.objid }
		def pmt = params.partialpayment

		item.basic =  numSvc.round( item.basic - pmt.partialbasic )
		item.basicdisc =  numSvc.round( item.basicdisc - pmt.partialbasicdisc )
		item.basicint =  numSvc.round( item.basicint - pmt.partialbasicint )
		item.sef =  numSvc.round( item.sef - pmt.partialsef )
		item.sefdisc =  numSvc.round( item.sefdisc - pmt.partialsefdisc )
		item.sefint =  numSvc.round( item.sefint - pmt.partialsefint )

		item.basicnet = numSvc.round( item.basic - item.basicdisc + item.basicint )
		item.sefnet = numSvc.round( item.sef - item.sefdisc + item.sefint )
		item.totalbasicsef = numSvc.round( item.basicnet + item.sefnet )

		item.total = numSvc.round( item.totalbasicsef + item.firecode )

		def rli = params.rptledgeritem
		rli.basic = item.basic
		rli.basicdisc = item.basicdisc
		rli.basicint = item.basicint
		rli.basicnet = item.basicnet
		rli.sef = item.sef
		rli.sefdisc = item.sefdisc
		rli.sefint = item.sefint
		rli.sefnet = item.sefnet 
		rli.totalbasicsef = item.totalbasicsef
		rli.total = item.total 
	}
}	
