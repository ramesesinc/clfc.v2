def env = [
    'app.host'    :'localhost:8071',
    'app.context' :'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254SettingMigrationService');

def lguid   = '142';
def lguname = 'SAN CARLOS';

/*=== LAND ========================== */
svc.migrateLandSettings(lguname);
svc.migrateLandAssessLevels();
svc.migrateSpecificClasses();
svc.migrateSubClasses();
svc.migrateStrippings();
svc.migrateLandAdjustments();


/*=== BLDG ========================== */
svc.migrateBldgSettings(lguname);
svc.migrateBldgAssessLevels();
svc.migrateBldgTypes();
svc.migrateBldgKindBuccs();
svc.migrateBldgAdditionalItems();


/*=== MACH ========================== */
svc.migrateMachSettings(lguname);
svc.migrateMachAssessLevels();
svc.migrateMachForexes();


/*=== PLANT/TREE ========================== */
svc.migratePlantTreeSettings(lguname);
svc.migratePlantTreeUnitValues();


/*=== PLANT/TREE ========================== */
svc.migrateMiscSettings(lguname);
svc.migrateMiscAssessLevels();
svc.migrateMiscItemUnitValues();


/*=== SETTING LGU ========================== */
svc.migrateLguRySettings(lguid);


println 'done';