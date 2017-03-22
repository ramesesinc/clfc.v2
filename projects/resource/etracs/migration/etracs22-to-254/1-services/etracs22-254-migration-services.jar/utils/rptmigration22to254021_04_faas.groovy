def env = [
'app.host':'localhost:8071',
'app.context':'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254MigrationService');

def list = [
    [objid:'F-2b3e1018:147a9decdd5:-7e1d']
]

list.each{
   svc.migrateFaas(it);
}
println 'done'