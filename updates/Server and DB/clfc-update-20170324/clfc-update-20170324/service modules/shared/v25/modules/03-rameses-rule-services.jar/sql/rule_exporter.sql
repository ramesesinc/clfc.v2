[getRulesets]
SELECT * FROM sys_ruleset WHERE domain=$P{domain}

[getRulegroups]
select * from sys_rulegroup where ruleset in ( 
	select name from sys_ruleset where domain=$P{domain} 
) 

[getRulesetActionDefs]
select * from sys_ruleset_actiondef where ruleset in (
	select name from sys_ruleset where domain=$P{domain} 
) 

[getRulesetFacts]
select * from sys_ruleset_fact where ruleset in (
	select name from sys_ruleset where domain=$P{domain} 
)

[getFacts]
select rf.* 
from sys_ruleset rs  
	inner join sys_ruleset_fact rsf on rsf.ruleset=rs.name  
	inner join sys_rule_fact rf on rsf.rulefact=rf.objid 
where rs.domain=$P{domain} 

[getFactFields]
select rff.* 
from sys_ruleset rs  
	inner join sys_ruleset_fact rsf on rsf.ruleset=rs.name 
	inner join sys_rule_fact rf on rsf.rulefact=rf.objid 
	inner join sys_rule_fact_field rff on rff.parentid=rf.objid 
where rs.domain=$P{domain} 

[getActionDefs]
select ra.* 
from sys_ruleset rs  
	inner join sys_ruleset_actiondef rsa on rsa.ruleset=rs.name 
	inner join sys_rule_actiondef ra on ra.objid=rsa.actiondef 
where rs.domain=$P{domain} 

[getActionDefParams]
select rap.* 
from sys_ruleset rs  
	inner join sys_ruleset_actiondef rsa on rsa.ruleset=rs.name  
	inner join sys_rule_actiondef ra on ra.objid=rsa.actiondef 
	inner join sys_rule_actiondef_param rap on rap.parentid=ra.objid 
where rs.domain=$P{domain} 
