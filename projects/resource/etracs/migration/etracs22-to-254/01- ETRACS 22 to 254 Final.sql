/*=============================================================*/
/*  CREATE IN ETRACS 2.2 DB */
/*=============================================================*/

CREATE TABLE etracs25_migrate_entity (
  objid VARCHAR(50) primary key not NULL 
) ;

CREATE TABLE etracs25_migrate_faas (
  objid VARCHAR(50) primary key NOT NULL
) ;


CREATE TABLE etracs25_migrate_prevfaas (
  objid VARCHAR(50) primary key NOT NULL
) ;


CREATE TABLE etracs25_migrate_prevfaas_log (
  objid VARCHAR(50)  not NULL,
  log TEXT
) ;


CREATE TABLE etracs25_migrate_log (
  objid VARCHAR(50)  not NULL,
  log TEXT
) ;



create index ix_etracs25_migrate_log_objid on etracs25_migrate_log(objid);

/*=============================================================*/



alter table faas alter column ryordinanceno varchar(25) null;
alter table faas alter column ryordinancedate date null;


INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('CC', 'Change Classification', '0', '1', '0', NULL, '0', '0', '0', '1', NULL);
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('CD', 'Change Depreciation', '0', '1', '0', NULL, '0', '0', '0', '1', NULL);
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('CE', 'Correction of Entry', '0', '1', '1', NULL, '1', '0', '1', '1', NULL);
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('CS', 'Consolidation', '1', '1', '1', NULL, '1', '0', '1', '1', 'consolidation');
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('CT', 'Change Taxability', '0', '1', '0', NULL, '0', '0', '0', '1', NULL);
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('CTD', 'Cancellation', '0', '0', '0', NULL, '0', '0', '0', '0', NULL);
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('DC', 'Data Capture', '1', '1', '1', NULL, '1', '1', '1', '1', NULL);
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('GR', 'General Revision', '0', '1', '1', NULL, '0', '0', '1', '1', NULL);
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('MC', 'Multiple Claim', '1', '1', '1', NULL, '1', '1', '1', '1', NULL);
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('MCS', 'Multiple Claim Settlement', '0', '1', '1', NULL, '0', '0', '0', '0', 'mcsettlement');
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('ND', 'New Discovery', '1', '1', '1', NULL, '1', '1', '1', '1', NULL);
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('RE', 'Reassessment', '0', '1', '0', NULL, '0', '1', '0', '1', NULL);
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('RS', 'Resection', '0', '1', '1', NULL, '0', '0', '0', '1', NULL);
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('RV', 'Revision', '0', '1', '1', NULL, '0', '1', '1', '1', NULL);
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('SD', 'Subdivision', '1', '1', '1', NULL, '1', '0', '1', '1', 'subdivision');
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('TR', 'Transfer of Ownership', '0', '0', '0', NULL, '1', '0', '0', '0', NULL);
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('TRC', 'Transfer with Correction', '0', '1', '1', NULL, '1', '0', '1', '1', NULL);
INSERT INTO faas_txntype ([objid], [name], [newledger], [newrpu], [newrealproperty], [displaycode], [allowEditOwner], [allowEditPin], [allowEditPinInfo], [allowEditAppraisal], [opener]) VALUES ('TRE', 'Transfer with Reassessment', '0', '1', '0', NULL, '1', '0', '1', '1', NULL);


INSERT INTO propertyclassification 
SELECT objid,'APPROVED',propertycode,propertydesc,special,orderno 
FROM ligao_etracs..propertyclassification
go


INSERT INTO exemptiontype 
SELECT objid,'APPROVED',exemptcode,exemptdesc,orderno 
FROM ligao_etracs..exemptiontype
go


INSERT INTO canceltdreason 
SELECT objid,'APPROVED',cancelcode,canceltitle,canceldesc 
FROM ligao_etracs..canceltdreason
go



INSERT INTO bldgkind 
SELECT objid,'APPROVED',bldgcode,bldgkind 
FROM ligao_etracs..kindofbuilding
go


INSERT INTO material 
SELECT objid,'APPROVED',materialcode,materialdesc 
FROM ligao_etracs..materials
go


INSERT INTO structure 
(objid, state, code, name, indexno)
SELECT objid,'APPROVED',structurecode,structuredesc,isnull(indexno ,0)
FROM ligao_etracs..structures
go



/* 
structure needs to run script for materials 
http://localhost:8070/osiris3/json/etracs25/RPT22MigrationService.migrateStructureMaterials
*/

update structurematerial set display = 0, idx = 0
go



INSERT INTO machine 
SELECT objid,'APPROVED',machinecode,machinedesc 
FROM ligao_etracs..machines
go


INSERT INTO planttree 
SELECT objid,'APPROVED',planttreecode,planttreedesc 
FROM ligao_etracs..plantsandtrees
go


INSERT INTO miscitem 
SELECT objid,'APPROVED',misccode,miscdesc 
FROM ligao_etracs..miscitems
go

INSERT INTO rptparameter 
SELECT objid,'DRAFT',paramname,paramcaption,paramdesc,paramtype,parammin,parammax 
FROM ligao_etracs..rptparameters
go


alter table rysetting_lgu add objid varchar(50) null
go 


INSERT INTO rysetting_lgu 
  (objid, rysettingid, lguid, lguname, settingtype, barangayid)
SELECT objid, objid,lguid,lguname,settingtype,null FROM ligao_etracs..rysetting_lgu
go


INSERT INTO landrysetting 
SELECT objid,'DRAFT',ry,previd,'LIGAO CITY' FROM ligao_etracs..landrysetting
go

INSERT INTO bldgrysetting 
SELECT objid,'DRAFT',ry,predominant,depreciatecoreanditemseparately,0,straightdepreciation,calcbldgagebasedondtoccupied,'LIGAO CITY',previd FROM ligao_etracs..bldgrysetting
go

INSERT INTO machrysetting 
SELECT objid,'DRAFT',ry,previd,'LIGAO CITY',0 FROM ligao_etracs..machrysetting
go

INSERT INTO miscrysetting 
SELECT objid,'DRAFT',ry,previd,'LIGAO CITY' FROM ligao_etracs..miscrysetting
go

INSERT INTO planttreerysetting 
SELECT objid,'DRAFT',ry,applyagriadjustment,'LIGAO CITY',previd FROM ligao_etracs..planttreerysetting
go



INSERT INTO landadjustmenttype 
SELECT objid,landrysettingid,adjustmentcode,adjustmentname,expression,appliedto,previd, 0 
FROM ligao_etracs..landadjustment
go

/* 
http://localhost:8070/osiris3/json/etracs25/RPT22MigrationService.migrateLandAdjustmentTypeClassification
*/


INSERT INTO landassesslevel 
SELECT objid,landrysettingid,NULL,classcode,classname,fixrate,rate,previd 
FROM ligao_etracs..landassesslevel
go

update l set 
  l.classification_objid = pc.objid 
from landassesslevel l, propertyclassification pc
where l.code = pc.code 
go

INSERT INTO propertyclassification 
  ([objid], [state], [code], [name], [special], [orderno]) 
VALUES ('OTH', 'APPROVED', 'OTH', 'SPECIAL OTHER', '1', '30')
GO 


  update landassesslevel set classification_objid ='OTH' where classification_objid is null
  go






/*
-- TEST MISSING propertyclassification link 

select * from ligao_etracs..propertyclassification
go
select *from lcuvspecificclass 
go
select * from ligao_etracs..lcuvspecificclass
go
select * from ligao_etracs..lcuv l
where not exists(select * from propertyclassification where code = l.classcode)
go

*/




INSERT INTO lcuvspecificclass 
SELECT spc.objid, spc.landrysettingid, p.objid, spc.classcode, spc.classname, spc.areatype, spc.previd 
FROM ligao_etracs..lcuvspecificclass spc
inner join ligao_etracs..lcuv l on spc.lcuvid = l.objid
inner join ligao_etracs..propertyclassification p on l.classcode = p.propertycode
go



INSERT INTO lcuvsubclass 
SELECT objid,specificclassid,landrysettingid,subclasscode,subclassname,unitvalue,previd 
FROM ligao_etracs..lcuvsubclass sub 
WHERE exists(
  select * from lcuvspecificclass where objid = sub.specificclassid
)
go


INSERT INTO lcuvstripping 
SELECT st.objid,st.landrysettingid,p.objid,st.striplevel,st.rate,st.previd 
FROM ligao_etracs..lcuvstripping st 
inner join ligao_etracs..lcuv l on st.lcuvid = l.objid
inner join ligao_etracs..propertyclassification p on l.classcode = p.propertycode
go

/* CHECK DUPLICATE

select top 10 * from ligao_etracs..bldgadditionalitem where name = 'GARAGE'

select name, count(*)  
from ligao_etracs..bldgadditionalitem  group by name having count(*)> 1

*/


update ligao_etracs..bldgadditionalitem set name = 'GARAGE1'  where objid = 'FRADI00000225'
go 


INSERT INTO bldgadditionalitem 
SELECT objid,bldgrysettingid,code,name,unit,expr,previd,NULL 
FROM ligao_etracs..bldgadditionalitem bi 
where previd is null
go
  

INSERT INTO bldgadditionalitem 
SELECT objid,bldgrysettingid,code,name,unit,expr,previd,NULL 
FROM ligao_etracs..bldgadditionalitem bi 
where previd is not null and exists(
  select * from bldgadditionalitem where objid = bi.previd
)
go




INSERT INTO bldgassesslevel 
SELECT objid,bldgrysettingid,NULL,code,name,fixrate,rate,previd 
FROM ligao_etracs..bldgassesslevel
go 


update bal set 
  bal.classification_objid = pc.objid 
from bldgassesslevel bal 
  inner join propertyclassification pc on bal.code = pc.code 
go


/*
http://localhost:8070/osiris3/json/etracs25/RPT22MigrationService.migrateBldgAssessLevel
*/


INSERT INTO bldgtype 
SELECT objid,bldgrysettingid,code,name,basevaluetype,residualrate,previd 
FROM ligao_etracs..bldgtype
WHERE previd is null
go 


INSERT INTO bldgtype 
SELECT objid,bldgrysettingid,code,name,basevaluetype,residualrate,previd 
FROM ligao_etracs..bldgtype
where previd is not null
go 



/*
SELECT
bldgkindbucc.*
FROM
bldgtype
RIGHT JOIN bldgkindbucc ON bldgkindbucc.bldgtypeid = bldgtype.objid
WHERE bldgtype.objid IS NULL

*/

INSERT INTO bldgkindbucc 
SELECT 
  objid,bldgrysettingid,bldgtypeid,bldgkindid,basevaluetype,basevalue,minbasevalue,maxbasevalue,
 gapvalue,minarea,maxarea,bldgclass,previd 
FROM ligao_etracs..bldgkindbucc bk 
WHERE previd is null
and exists (
  select * from bldgkind where objid = bk.bldgkindid
) 
go

INSERT INTO bldgkindbucc 
SELECT 
  objid,bldgrysettingid,bldgtypeid,bldgkindid,basevaluetype,basevalue,minbasevalue,maxbasevalue,
 gapvalue,minarea,maxarea,bldgclass,previd 
FROM ligao_etracs..bldgkindbucc bk 
WHERE previd is not null
and exists (
  select * from bldgkind where objid = bk.bldgkindid
)
and exists(
select  * from bldgtype where objid = bk.bldgtypeid
)
go



/*
http://localhost:8070/osiris3/json/etracs25/RPT22MigrationService.migrateBldgTypeDepreciation
*/





/*

SELECT code, count(*) 
FROM ligao_etracs..machassesslevel 
group by code having count(*) > 1

select * from ligao_etracs..machassesslevel 
select * from machassesslevel

*/

INSERT INTO machassesslevel 
SELECT mal.objid,mal.machrysettingid,pc.objid,pc.code,pc.name,mal.fixrate,mal.rate,mal.previd 
FROM ligao_etracs..machassesslevel mal 
  inner join etracs254_ligao..propertyclassification pc on mal.code = pc.code 
where mal.code is not null 
go


INSERT INTO machforex 
SELECT objid,machrysettingid,iyear,forex,previd 
FROM ligao_etracs..machforex
go


INSERT INTO planttreeunitvalue 
SELECT objid,planttreerysettingid,planttreeid,code,name,unitvalue,previd 
FROM ligao_etracs..planttreeunitvalue
go



/*
http://localhost:8070/osiris3/json/etracs25/RPT22MigrationService.migratePlantTreeAssessLevel
*/


alter table faas add prevadministrator varchar(500);
alter table planttreedetail add areacovered decimal(16,6) not null;



INSERT INTO faasannotationtype 
SELECT objid,annotationtype 
FROM ligao_etracs..faasannotationtype
go

delete from sys_wf_transition where processname in ('faas', 'subdivision', 'consolidation');
delete from sys_wf_node where processname in ('faas', 'subdivision', 'consolidation');
delete from sys_wf where name in ('faas', 'subdivision', 'consolidation');

INSERT INTO sys_wf ([name], [title], [domain]) VALUES ('consolidation', 'Consolidation Transaction Workflow', NULL);
INSERT INTO sys_wf ([name], [title], [domain]) VALUES ('faas', 'FAAS Transaction Workflow', NULL);
INSERT INTO sys_wf ([name], [title], [domain]) VALUES ('subdivision', 'Subdivision Transaction Workflow', NULL);


INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('start', 'consolidation', 'Start', 'start', '1', NULL, 'RPT', NULL);
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('start', 'faas', 'Start', 'start', '1', NULL, 'RPT', NULL);
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('start', 'subdivision', 'Start', 'start', '1', NULL, 'RPT', NULL);
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('receiver', 'consolidation', 'Review and Verification', 'state', '5', NULL, 'RPT', 'RECEIVER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('receiver', 'faas', 'Review and Verification', 'state', '5', NULL, 'RPT', 'RECEIVER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('receiver', 'subdivision', 'Review and Verification', 'state', '5', NULL, 'RPT', 'RECEIVER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-examiner', 'consolidation', 'For Examination', 'state', '10', NULL, 'RPT', 'EXAMINER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-examiner', 'faas', 'For Examination', 'state', '10', NULL, 'RPT', 'EXAMINER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-examiner', 'subdivision', 'For Examination', 'state', '10', NULL, 'RPT', 'EXAMINER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('examiner', 'consolidation', 'Examination', 'state', '15', NULL, 'RPT', 'EXAMINER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('examiner', 'faas', 'Examination', 'state', '15', NULL, 'RPT', 'EXAMINER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('examiner', 'subdivision', 'Examination', 'state', '15', NULL, 'RPT', 'EXAMINER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-taxmapper', 'consolidation', 'For Taxmapping', 'state', '20', NULL, 'RPT', 'TAXMAPPER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-taxmapper', 'faas', 'For Taxmapping', 'state', '20', NULL, 'RPT', 'TAXMAPPER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-taxmapper', 'subdivision', 'For Taxmapping', 'state', '20', NULL, 'RPT', 'TAXMAPPER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('taxmapper', 'consolidation', 'Taxmapping', 'state', '25', NULL, 'RPT', 'TAXMAPPER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('taxmapper', 'faas', 'Taxmapping', 'state', '25', NULL, 'RPT', 'TAXMAPPER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('taxmapper', 'subdivision', 'Taxmapping', 'state', '25', NULL, 'RPT', 'TAXMAPPER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-taxmapping-approval', 'consolidation', 'For Taxmapping Approval', 'state', '30', NULL, 'RPT', 'TAXMAPPER_CHIEF');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-taxmapping-approval', 'faas', 'For Taxmapper Chief Approval', 'state', '30', NULL, 'RPT', 'TAXMAPPER_CHIEF');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-taxmapping-approval', 'subdivision', 'For Taxmapping Approval', 'state', '30', NULL, 'RPT', 'TAXMAPPER_CHIEF');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('taxmapper_chief', 'consolidation', 'Taxmapping Approval', 'state', '35', NULL, 'RPT', 'TAXMAPPER_CHIEF');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('taxmapper_chief', 'faas', 'Taxmapping Approval', 'state', '35', NULL, 'RPT', 'TAXMAPPER_CHIEF');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('taxmapper_chief', 'subdivision', 'Taxmapping Approval', 'state', '35', NULL, 'RPT', 'TAXMAPPER_CHIEF');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-appraiser', 'consolidation', 'For Appraisal', 'state', '40', NULL, 'RPT', 'APPRAISER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-appraiser', 'faas', 'For Appraisal', 'state', '40', NULL, 'RPT', 'APPRAISER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-appraiser', 'subdivision', 'For Appraisal', 'state', '40', NULL, 'RPT', 'APPRAISER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('appraiser', 'consolidation', 'Appraisal', 'state', '45', NULL, 'RPT', 'APPRAISER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('appraiser', 'faas', 'Appraisal', 'state', '45', NULL, 'RPT', 'APPRAISER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('appraiser', 'subdivision', 'Appraisal', 'state', '45', NULL, 'RPT', 'APPRAISER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-appraisal-chief', 'consolidation', 'For Appraisal Approval', 'state', '50', NULL, 'RPT', 'APPRAISAL_CHIEF');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-appraisal-chief', 'faas', 'For Appraisal Chief Approval', 'state', '50', NULL, 'RPT', 'APPRAISAL_CHIEF');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-appraisal-chief', 'subdivision', 'For Appraisal Approval', 'state', '50', NULL, 'RPT', 'APPRAISAL_CHIEF');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('appraiser_chief', 'consolidation', 'Appraisal Approval', 'state', '55', NULL, 'RPT', 'APPRAISAL_CHIEF');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('appraiser_chief', 'faas', 'Appraisal Chief Approval', 'state', '55', NULL, 'RPT', 'APPRAISAL_CHIEF');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('appraiser_chief', 'subdivision', 'Appraisal Approval', 'state', '55', NULL, 'RPT', 'APPRAISAL_CHIEF');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-recommender', 'consolidation', 'For Recommending Approval', 'state', '70', NULL, 'RPT', 'RECOMMENDER,ASSISTANT_ASSESSOR');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-recommender', 'faas', 'For Recommending Approval', 'state', '70', NULL, 'RPT', 'RECOMMENDER,ASSISTANT_ASSESSOR');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-recommender', 'subdivision', 'For Recommending Approval', 'state', '70', NULL, 'RPT', 'RECOMMENDER,ASSISTANT_ASSESSOR');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('recommender', 'consolidation', 'Recommending Approval', 'state', '75', NULL, 'RPT', 'RECOMMENDER,ASSISTANT_ASSESSOR');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('recommender', 'faas', 'Recommending Approval', 'state', '75', NULL, 'RPT', 'RECOMMENDER,ASSISTANT_ASSESSOR');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('recommender', 'subdivision', 'Recommending Approval', 'state', '75', NULL, 'RPT', 'RECOMMENDER,ASSISTANT_ASSESSOR');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-approver', 'consolidation', 'Assign Approver', 'state', '76', NULL, 'RPT', 'APPROVER,ASSESSOR');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-approver', 'subdivision', 'Assign Approver', 'state', '76', NULL, 'RPT', 'APPROVER');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('assign-approver', 'faas', 'For Assessor Approval', 'state', '80', NULL, 'RPT', 'APPROVER,ASSESSOR');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('approver', 'faas', 'Assessor Approval', 'state', '85', NULL, 'RPT', 'APPROVER,ASSESSOR');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('forapproval', 'consolidation', 'For Assessor Approval', 'state', '85', NULL, 'RPT', 'RECOMMENDER,ASSISTANT_ASSESSOR');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('forapproval', 'subdivision', 'For Assessor Approval', 'state', '85', NULL, 'RPT', 'APPROVER,ASSESSOR');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('approver', 'subdivision', 'Assessor Approval', 'state', '90', NULL, 'RPT', 'APPROVER,ASSESSOR');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('approver', 'consolidation', 'Assessor Approval', 'state', '90', NULL, 'RPT', 'RECOMMENDER,ASSISTANT_ASSESSOR');
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('end', 'consolidation', 'End', 'end', '1000', NULL, 'RPT', NULL);
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('end', 'faas', 'End', 'end', '1000', NULL, 'RPT', NULL);
INSERT INTO sys_wf_node ([name], [processname], [title], [nodetype], [idx], [salience], [domain], [role]) VALUES ('end', 'subdivision', 'End', 'end', '1000', NULL, 'RPT', NULL);


INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('start', 'consolidation', NULL, 'receiver', '1', NULL, NULL, NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('start', 'faas', NULL, 'receiver', '1', NULL, NULL, NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('start', 'subdivision', NULL, 'receiver', '1', NULL, NULL, NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('receiver', 'faas', 'delete', 'end', '5', NULL, '[caption:''Delete'', confirm:''Delete record?'',closeonend:true]', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('receiver', 'subdivision', 'submit', 'assign-examiner', '5', NULL, '[caption:''Submit For Examination'', confirm:''Submit?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('receiver', 'consolidation', 'submit', 'assign-examiner', '5', NULL, '[caption:''Submit For Examination'', confirm:''Submit?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('receiver', 'consolidation', 'delete', 'end', '6', NULL, '[caption:''Delete'', confirm:''Delete?'', closeonend:true]', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('receiver', 'faas', 'submit', 'assign-examiner', '6', NULL, '[caption:''Submit For Examination'', confirm:''Submit?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('receiver', 'subdivision', 'delete', 'end', '6', NULL, '[caption:''Delete'', confirm:''Delete?'', closeonend:true]', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-examiner', 'consolidation', NULL, 'examiner', '10', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-examiner', 'faas', NULL, 'examiner', '10', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-examiner', 'subdivision', NULL, 'examiner', '10', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('examiner', 'consolidation', 'returnreceiver', 'receiver', '15', NULL, '[caption:''Return to Receiver'', confirm:''Return to receiver?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('examiner', 'faas', 'return', 'receiver', '15', NULL, '[caption:''Return to Receiver'',confirm:''Return to receiver?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('examiner', 'subdivision', 'returnreceiver', 'receiver', '15', NULL, '[caption:''Return to Receiver'', confirm:''Return to receiver?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('examiner', 'subdivision', 'submit', 'assign-taxmapper', '16', NULL, '[caption:''Submit to Taxmapping'', confirm:''Submit?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('examiner', 'faas', 'submit', 'assign-taxmapper', '16', NULL, '[caption:''Submit for Taxmapping'', confirm:''Submit for taxmapping?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('examiner', 'consolidation', 'submit', 'assign-taxmapper', '16', NULL, '[caption:''Submit to Taxmapping'', confirm:''Submit?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-taxmapper', 'consolidation', NULL, 'taxmapper', '20', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-taxmapper', 'faas', NULL, 'taxmapper', '20', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-taxmapper', 'subdivision', NULL, 'taxmapper', '20', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('taxmapper', 'consolidation', 'returnexaminer', 'examiner', '25', NULL, '[caption:''Return to Examiner'', confirm:''Return to examiner?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('taxmapper', 'faas', 'return_receiver', 'receiver', '25', NULL, '[caption:''Return to Receiver'',confirm:''Return to receiver?'',messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('taxmapper', 'subdivision', 'returnexaminer', 'examiner', '25', NULL, '[caption:''Return to Examiner'', confirm:''Return to examiner?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('taxmapper', 'faas', 'return_examiner', 'examiner', '26', NULL, '[caption:''Return to Examiner'',confirm:''Return to examiner?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('taxmapper', 'subdivision', 'submit', 'assign-appraiser', '26', NULL, '[caption:''Submit for appraisal?'', confirm:''Submit?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('taxmapper', 'consolidation', 'submit', 'assign-appraiser', '26', NULL, '[caption:''Submit for appraisal?'', confirm:''Submit?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('taxmapper', 'faas', 'submit', 'assign-appraiser', '27', NULL, '[caption:''Submit for Appraisal'', confirm:''Submit for Appraisal?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-appraiser', 'consolidation', NULL, 'appraiser', '40', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-appraiser', 'faas', NULL, 'appraiser', '40', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-appraiser', 'subdivision', NULL, 'appraiser', '40', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('appraiser', 'consolidation', 'returntaxmapper', 'taxmapper', '45', NULL, '[caption:''Return to Taxmapper'', confirm:''Return to taxmapper?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('appraiser', 'faas', 'return_examiner', 'examiner', '45', NULL, '[caption:''Return to Examiner'',confirm:''Return to examiner?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('appraiser', 'subdivision', 'returntaxmapper', 'taxmapper', '45', NULL, '[caption:''Return to Taxmapper'', confirm:''Return to taxmapper?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('appraiser', 'faas', 'return_taxmapper', 'taxmapper', '46', NULL, '[caption:''Return to Taxmapper'',confirm:''Return to taxmapper?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('appraiser', 'consolidation', 'returnexaminer', 'examiner', '46', NULL, '[caption:''Return to Examiner'', confirm:''Return to examiner?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('appraiser', 'subdivision', 'returnexaminer', 'examiner', '46', NULL, '[caption:''Return to Examiner'', confirm:''Return to examiner?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('appraiser', 'consolidation', 'submit', 'assign-recommender', '47', NULL, '[caption:''Submit for recommending approval'', confirm:''Submit?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('appraiser', 'faas', 'submit', 'assign-recommender', '47', NULL, '[caption:''Submit for Recommending Approval'', confirm:''Submit for approval?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('appraiser', 'subdivision', 'submit', 'assign-recommender', '47', NULL, '[caption:''Submit for recommending approval'', confirm:''Submit?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-recommender', 'consolidation', NULL, 'recommender', '70', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-recommender', 'faas', NULL, 'recommender', '70', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-recommender', 'subdivision', NULL, 'recommender', '70', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'faas', 'return_examiner', 'examiner', '71', NULL, '[caption:''Return to Examiner'',confirm:''Return to examiner?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'faas', 'return_appraiser', 'appraiser', '72', NULL, '[caption:''Return to Appraiser'',confirm:''Return to appraiser?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'faas', 'return_taxmapper', 'taxmapper', '73', NULL, '[caption:''Return to Taxmapper'',confirm:''Return to taxmapper?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'faas', 'submit', 'assign-approver', '74', NULL, '[caption:''Submit for Assessor Approval'', confirm:''Submit to assessor?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'subdivision', 'returnexaminer', 'examiner', '75', NULL, '[caption:''Return to Examiner'', confirm:''Return to examiner?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'consolidation', 'returnexaminer', 'examiner', '75', NULL, '[caption:''Return to Examiner'', confirm:''Return to examiner?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'consolidation', 'returntaxmapper', 'taxmapper', '76', NULL, '[caption:''Return to Taxmapper'', confirm:''Return to taxmapper?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'subdivision', 'returntaxmapper', 'taxmapper', '76', NULL, '[caption:''Return to Taxmapper'', confirm:''Return to taxmapper?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'subdivision', 'returnappraiser', 'appraiser', '77', NULL, '[caption:''Return to Appraiser'', confirm:''Return to appraiser?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'consolidation', 'returnappraiser', 'appraiser', '77', NULL, '[caption:''Return to Appraiser'', confirm:''Return to appraiser?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'consolidation', 'submit', 'assign-approver', '78', NULL, '[caption:''Submit to Assessor'', confirm:''Submit to Assessor?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('recommender', 'subdivision', 'submit', 'assign-approver', '78', NULL, '[caption:''Submit to Assessor'', confirm:''Submit to Assessor?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-approver', 'subdivision', '', 'forapproval', '79', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-approver', 'consolidation', '', 'forapproval', '79', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('assign-approver', 'faas', NULL, 'approver', '80', NULL, '[caption:''Assign To Me'', confirm:''Assign task to you?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('approver', 'faas', 'return_recommender', 'recommender', '85', NULL, '[caption:''Return to Taxmapper'',confirm:''Return to Recommender?'', messagehandler:''default'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('forapproval', 'consolidation', 'approve', 'approver', '85', NULL, '[caption:''Approve'', confirm:''Approve consolidation?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('forapproval', 'subdivision', 'approve', 'approver', '85', NULL, '[caption:''Approve'', confirm:''Approve subdivision?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('approver', 'subdivision', 'backapproval', 'forapproval', '90', NULL, '[caption:''Cancel Posting'', confirm:''Cancel posting record?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('approver', 'consolidation', 'backapproval', 'forapproval', '90', NULL, '[caption:''Cancel Posting'', confirm:''Cancel posting record?'']', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('approver', 'subdivision', 'completed', 'end', '95', NULL, '[caption:''Approved'', visible:false]', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('approver', 'consolidation', 'completed', 'end', '95', NULL, '[caption:''Approved'', visible:false]', NULL);
INSERT INTO sys_wf_transition ([parentid], [processname], [action], [to], [idx], [eval], [properties], [permission]) VALUES ('approver', 'faas', 'approve', 'end', '100', NULL, '[caption:''Approve'', confirm:''Approve FAAS?'', closeonend:false]', NULL);





alter table faas_txntype add allowEditOwner int;
alter table faas_txntype add allowEditPin int;
alter table faas_txntype add allowEditPinInfo int;
alter table faas_txntype add allowEditAppraisal int;
alter table faas_txntype add opener varchar(50);

UPDATE faas_txntype SET objid='CC', name='Change Classification', newledger='0', newrpu='1', newrealproperty='0', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='0', opener=NULL, allowEditAppraisal='1' WHERE (objid='CC');
UPDATE faas_txntype SET objid='CD', name='Change Depreciation', newledger='0', newrpu='1', newrealproperty='0', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='0', opener=NULL, allowEditAppraisal='1' WHERE (objid='CD');
UPDATE faas_txntype SET objid='CE', name='Correction of Entry', newledger='0', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='1', allowEditPin='0', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='CE');
UPDATE faas_txntype SET objid='CS', name='Consolidation', newledger='1', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='1', allowEditPin='0', allowEditPinInfo='1', opener='consolidation', allowEditAppraisal='1' WHERE (objid='CS');
UPDATE faas_txntype SET objid='CS/SD', name='Consolidation/Subdivision', newledger='1', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='0', opener='subdivision', allowEditAppraisal='1' WHERE (objid='CS/SD');
UPDATE faas_txntype SET objid='CT', name='Change Taxability', newledger='0', newrpu='1', newrealproperty='0', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='0', opener=NULL, allowEditAppraisal='1' WHERE (objid='CT');
UPDATE faas_txntype SET objid='CTD', name='Cancellation', newledger='0', newrpu='0', newrealproperty='0', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='0', opener=NULL, allowEditAppraisal='0' WHERE (objid='CTD');
UPDATE faas_txntype SET objid='DC', name='Data Capture', newledger='1', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='1', allowEditPin='1', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='DC');
UPDATE faas_txntype SET objid='GR', name='General Revision', newledger='0', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='GR');
UPDATE faas_txntype SET objid='MC', name='Multiple Claim', newledger='1', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='1', allowEditPin='1', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='MC');
UPDATE faas_txntype SET objid='MCS', name='Multiple Claim Settlement', newledger='0', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='0', opener='mcsettlement', allowEditAppraisal='0' WHERE (objid='MCS');
UPDATE faas_txntype SET objid='ND', name='New Discovery', newledger='1', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='1', allowEditPin='1', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='ND');
UPDATE faas_txntype SET objid='RE', name='Reassessment', newledger='0', newrpu='1', newrealproperty='0', displaycode=NULL, allowEditOwner='0', allowEditPin='1', allowEditPinInfo='0', opener=NULL, allowEditAppraisal='1' WHERE (objid='RE');
UPDATE faas_txntype SET objid='RS', name='Resection', newledger='0', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='0', opener=NULL, allowEditAppraisal='1' WHERE (objid='RS');
UPDATE faas_txntype SET objid='RV', name='Revision', newledger='0', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='0', allowEditPin='1', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='RV');
UPDATE faas_txntype SET objid='SD', name='Subdivision', newledger='1', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='1', allowEditPin='0', allowEditPinInfo='1', opener='subdivision', allowEditAppraisal='1' WHERE (objid='SD');
UPDATE faas_txntype SET objid='TR', name='Transfer of Ownership', newledger='0', newrpu='0', newrealproperty='0', displaycode=NULL, allowEditOwner='1', allowEditPin='0', allowEditPinInfo='0', opener=NULL, allowEditAppraisal='0' WHERE (objid='TR');
UPDATE faas_txntype SET objid='TRC', name='Transfer with Correction', newledger='0', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='1', allowEditPin='0', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='TRC');
UPDATE faas_txntype SET objid='TRE', name='Transfer with Reassessment', newledger='0', newrpu='1', newrealproperty='0', displaycode=NULL, allowEditOwner='1', allowEditPin='0', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='TRE');





/* SUBDIVISION */

alter table subdividedland alter column itemno varchar(50) null;
alter table subdividedland alter column newtdno varchar(50) null;
alter table subdividedland alter column newutdno varchar(50) null;
alter table subdividedland alter column newtitletype varchar(50) null;
alter table subdividedland alter column newtitleno varchar(50) null;
alter table subdividedland drop constraint DF__subdivide__newti__6DB73E6A;
alter table subdividedland alter column newtitledate varchar(50) null;
alter table subdividedland alter column areasqm decimal(16,6) null;
alter table subdividedland alter column areaha decimal(16,6) null;
alter table subdividedland alter column memoranda text null;
alter table subdividedland alter column administrator_objid varchar(50) null;
alter table subdividedland alter column administrator_name varchar(200) null;
alter table subdividedland alter column administrator_address varchar(200) null;
alter table subdividedland alter column taxpayer_objid varchar(50) null;
drop index subdividedland.ix_subdividedland_taxpayer_name;
alter table subdividedland alter column taxpayer_name text  null;
alter table subdividedland alter column taxpayer_address varchar(200) null;
alter table subdividedland alter column owner_name text null;
alter table subdividedland alter column owner_address varchar(200) null;

create UNIQUE INDEX ux_newpin on subdividedland(subdivisionid,newpin);

alter table subdivisionaffectedrpu add prevpin varchar(50);
alter table subdivisionaffectedrpu add prevtdno varchar(50);
alter table subdivisionaffectedrpu alter column newsuffix int null;

alter table subdivisionaffectedrpu alter column newtdno varchar(50) null;
alter table subdivisionaffectedrpu alter column newutdno varchar(50) null;
alter table subdivisionaffectedrpu alter column memoranda text null;
alter table subdivisionaffectedrpu alter column itemno varchar(50) null;




/* CONSOLIDATION */


alter table consolidation alter column txntype_objid varchar(50) null;
alter table consolidation alter column autonumber int  null;
alter table consolidation alter column effectivityyear int null;
alter table consolidation alter column effectivityqtr int null;
alter table consolidation alter column newtdno varchar(50) null;
alter table consolidation alter column newutdno varchar(50) null;
alter table consolidation alter column newtitletype varchar(50) null;
alter table consolidation alter column newtitleno varchar(50) null;
alter table consolidation drop constraint DF__consolida__newti__6A85CC04;
alter table consolidation alter column newtitledate varchar(50) null;
alter table consolidation alter column lguid varchar(50) null;
alter table consolidation alter column lgutype varchar(50) null;
alter table consolidation alter column taxpayer_objid varchar(50) null;
drop index consolidation.ix_consolidation_taxpayername;
alter table consolidation alter column taxpayer_name text null;
alter table consolidation alter column taxpayer_address varchar(200) null;
drop index consolidation.ix_consolidation_ownername;
alter table consolidation alter column owner_name text null;
alter table consolidation alter column owner_address varchar(200) null;
alter table consolidation alter column administrator_objid varchar(50) null;
alter table consolidation alter column administrator_name varchar(500) null;
alter table consolidation alter column administrator_address varchar(200) null;
alter table consolidation alter column administratorid varchar(50) null;
alter table consolidation alter column administratorname varchar(500) null;
alter table consolidation alter column administratoraddress varchar(200) null;
alter table consolidation alter column signatories text null;


alter table consolidationaffectedrpu alter column newtdno varchar(50);
alter table consolidationaffectedrpu alter column newutdno varchar(50);
alter table consolidationaffectedrpu alter column memoranda varchar(500);


alter table consolidation alter column txnno varchar(25) not null;
alter table subdivision alter column txnno varchar(25) not null;



update planttreedetail set areacovered = 0 where areacovered is null;

alter table planttreedetail drop constraint DF__planttree__nonpr__3C54ED00;
alter table planttreedetail alter column nonproductiveage varchar(25);  

INSERT INTO sys_var (name, value, description, datatype, category) 
VALUES ('faas_formalize_owner_name', '0', 'Formalize Owner Name upon lookup', 'checkbox', 'ASSESSOR');


update ar set 
  ar.prevpin = pr.fullpin, ar.prevtdno = pf.tdno
from subdivision s
  inner join subdivisionaffectedrpu ar on s.objid = ar.subdivisionid
  inner join faas pf on ar.prevfaasid = pf.objid 
  inner join rpu pr on pf.rpuid = pr.objid 
where s.state ='draft' 
  and ar.prevpin is null;

  