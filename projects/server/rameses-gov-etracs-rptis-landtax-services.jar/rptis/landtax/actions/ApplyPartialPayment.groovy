package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;


public class ApplyPartialPayment implements RuleActionHandler {
	def numSvc
	def items 

	public void execute(def params, def drools) {
		def item = items.find{ it.objid == params.rptledgeritem.objid }
		def partial = params.partialpayment
		def payment = partial.amount 

		// MUST BE SET
		// This flag is verified upon posting of receipt 
		// and update the RPTLedger status accordingly
		item.partialled = 1;
		
		def idletaxtotal = item.basicidle + item.basicidleint - item.basicidledisc

		if ( payment >= item.firecode + idletaxtotal){
			payment -= (item.firecode + idletaxtotal)
		}
		else {
			if (item.firecode > 0 && payment >= item.firecode){
				payment -= item.firecode 
			}
			else if (item.firecode > 0 ) {
				item.firecode = payment
				payment = 0.0 
			}

			if (payment > 0.0){
				def partialbasicidle     	= numSvc.round(  payment * (item.basicidle - item.basicidledisc) / idletaxtotal )
				def partialbasicidleint  	= numSvc.round(  payment * item.basicidleint / idletaxtotal )
				def partialbasicidledisc 	= numSvc.round(  payment * item.basicidledisc / idletaxtotal )

				item.basicidle 		= partialbasicidle + partialbasicidledisc
				item.basicidledisc 	= partialbasicidledisc
				item.basicidleint 	= partialbasicidleint
			}
		}

		if (payment == 0.0 ){
			item.basic 		= 0.0
			item.basicdisc 	= 0.0
			item.basicint 	= 0.0
			item.sef 		= 0.0
			item.sefdisc 	= 0.0
			item.sefint 	= 0.0
		}

		if (payment > 0.0){
			def linetotal 			= item.total - item.firecode - idletaxtotal
			def partialbasic     	= numSvc.round(  payment * (item.basic - item.basicdisc) / linetotal )
			def partialbasicint  	= numSvc.round(  payment * item.basicint / linetotal )
			def partialbasicdisc 	= numSvc.round(  payment * item.basicdisc / linetotal )

			def partialsefint  		= numSvc.round(  payment * item.sefint / linetotal )
			def partialsefdisc 		= numSvc.round(  payment * item.sefdisc / linetotal )
			def partialsef 			= numSvc.round( payment - partialsefint - partialbasic - partialbasicint  )

			item.basic 		= partialbasic + partialbasicdisc
			item.basicdisc 	= partialbasicdisc
			item.basicint 	= partialbasicint
			item.sef 		= partialsef + partialsefdisc
			item.sefdisc 	= partialsefdisc
			item.sefint 	= partialsefint
		}

		item.basicnet 		= item.basic - item.basicdisc + item.basicint
		item.sefnet 		= item.sef - item.sefdisc + item.sefint
		item.totalbasicsef 	= item.basicnet + item.sefnet

		item.total = item.totalbasicsef + item.firecode + (item.basicidle - item.basicidledisc + item.basicidleint)

		def rli = params.rptledgeritem
		rli.basic = item.basic
		rli.basicdisc = item.basicdisc
		rli.basicint = item.basicint
		rli.basicnet = item.basicnet
		rli.basicidle = item.basicidle
		rli.basicidledisc = item.basicidledisc
		rli.basicidleint = item.basicidleint
		rli.sef = item.sef
		rli.sefdisc = item.sefdisc
		rli.sefint = item.sefint
		rli.sefnet = item.sefnet 
		rli.firecode = item.firecode 
		rli.totalbasicsef = item.totalbasicsef
		rli.total = item.total 
	}
}	

