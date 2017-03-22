def env = [
'app.host':'localhost:8070',
'app.context':'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254MigrationService');



void migrate(svc, params){
  def size = svc.migrateFaasData(params);
  while( size > 0 ){
    params.migrated += size;
    println 'Migrated Count -> ' + params.migrated
    size = svc.migrateFaasData(params);  
  }
}


def params = [rputype:'land', count:25, migrated:0]
migrate(svc, params)
println 'Done migrating land...'

params.rputype = 'bldg'
migrate(svc, params)
println 'Done migrating bldg...'


params.rputype = 'mach'
migrate(svc, params)
println 'Done migrating mach...'




void migratePrevFaas(svc, params){
  def size = svc.migratePreviousFaasData(params);
  while( size > 0 ){
    params.migrated += size;
    println 'Migrated Count -> ' + params.migrated
    size = svc.migratePreviousFaasData(params);  
  }
}

params = [count:50, migrated:0]
migratePrevFaas(svc, params)


print 'Migration completed.'