package lk.devfactory.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import com.scitools.understand.Reference;
import com.scitools.understand.UnderstandException;

import lk.devfactory.model.DeadCode;
import lk.devfactory.model.Function;
import lk.devfactory.model.FunctionParameter;
import lk.devfactory.model.GlobalVariable;
import lk.devfactory.model.LocalVariable;
import lk.devfactory.reposiotry.DeadCodeDO;

//TODO Add logger. Fix the exception throwing. Fix the project path. remove DS setter.
//@org.springframework.stereotype.Repository
@Component
public class UnderstandDeadCodeDO implements DeadCodeDO {
	static private final Logger log = LoggerFactory.getLogger(UnderstandDeadCodeDO.class);
	
	@Autowired
	UnderstandDatabase ud;
	
	//TODO replace Repository.getID to UUID type. Then remove this additional UUID parameter
	public List<DeadCode> analyse(String repoId){
		Database db = null;
		List<DeadCode> deadCodeList = null;
		try {
			log.info("Starting to analyse udb with id:"+repoId);
			db = ud.openDatabase(repoId);
			deadCodeList = loadProjectEntities(db);
//			detectPvtDeadFuncs(db, deadCodeList);
//			detectPvtDeadVars(db, deadCodeList);
//			detectPvtDeadFuncParams(db, deadCodeList);
		} catch (UnderstandException e) {
			log.error("Failed execute analyse :" + e.getMessage());
			throw new RuntimeException("Failed execute analyse :",e);
		} finally {
			if (db != null) {
				db.close();
				log.info("Db closed..");
			}
		}
		return deadCodeList;
	}
	
	protected List<DeadCode> loadProjectEntities(Database db) {
		List<DeadCode> deadCodeList = new ArrayList<DeadCode>();
		// Get a list of all functions and methods
		Entity[] clazz = db.ents("class ~unknown ~unresolved");
		log.debug("No of classes : "+(clazz != null?clazz.length:0));
		for (Entity e : clazz) {
			log.debug(e.name() + " : " + e.kind());

			DeadCode dead = new DeadCode();
			dead.setName(e.name());
			
			//TODO following Global variable and Function sections can be done in parallel
			// Get a list of all global variables of the class
			Reference[] varRefs = e.refs("Java Define", "Java Variable", true);
			for (Reference vRef : varRefs) {
				log.debug(e.name() + " => " + vRef.ent().name() + " [" + vRef.line() + ":" + vRef.column()
						+ "] > " + vRef.ent().kind().name());
				
				Reference[] usedVRef = vRef.ent().refs("Java Useby", null, true); 
				if (usedVRef == null || usedVRef.length == 0) {
					GlobalVariable gloVar = new GlobalVariable(vRef.ent().name(), vRef.line(), vRef.column());
					dead.getGlobalVariables().add(gloVar);
				} else {
					log.debug("Variable is "+vRef.ent().name()+" is used. "+
							"Variable is used by : "+usedVRef[0].ent().name());
				}
				
			}

			// Get a list of all functions and methods of class
			Reference[] methodRefs = e.refs("Java Define", "Java Method", true);

			for (Reference mRef : methodRefs) {
				log.debug(e.name() + " =>^ " + mRef.ent().name() + " [" + mRef.line() + ":" + mRef.column()
						+ "] > " + mRef.ent().kind().name());
				
				Function func = null;
				boolean addFunction = false;
			    if (mRef.ent().kind().name().contains("Method")) {

					Reference[] usedMRef = mRef.ent().refs("Java Callby", null, true); 
					//Add function to deadcode list if its not used with column numbers
					if (usedMRef == null || usedMRef.length == 0) { 
						func = new Function(mRef.ent().name(),mRef.line(),mRef.column()
								,new ArrayList<FunctionParameter>(),new ArrayList<LocalVariable>());
						addFunction = true;
					} else {
						log.debug("Function "+mRef.ent().name()+" is used. "+
								"Function is used by : "+usedMRef[0].ent().name());
					}

					// Get a list of all method variables
					Reference[] localVarRefs = mRef.ent().refs("Java Define", "Variable Parameter", true);
					for (Reference lVRef : localVarRefs) { //TODO following parameter and Variable sections can be done in parallel
						log.debug(mRef.ent().name() + " =>* " + lVRef.ent().name() + " [" + lVRef.line() + ":"
								+ lVRef.column() + "] > " + lVRef.ent().kind().name());
						
						if (lVRef.ent().kind().name().contains("Parameter")) { 
							
							Reference[] usedlVRef = lVRef.ent().refs("Java Useby", null, true); 
							//Add function param to functions and to deadcode list if its not used with column numbers
							if (usedlVRef == null || usedlVRef.length == 0) { 
								if (!addFunction) {
									//making line number and col number 0 since function is used but not the param of the function
									func = new Function(mRef.ent().name(),0,0 
											,new ArrayList<FunctionParameter>(),new ArrayList<LocalVariable>());
									addFunction = true;
								}
								FunctionParameter param = new FunctionParameter(lVRef.ent().name(), lVRef.line(), lVRef.column());
								func.getParameters().add(param);
							} else {
								log.debug("Function param "+lVRef.ent().name()+" is used. "+
										"Function param is used by : "+usedlVRef[0].ent().name());
							}
						}
						
						if (lVRef.ent().kind().name().contains("Variable")) { 
							
							Reference[] usedlVRef = lVRef.ent().refs("Java Useby", null, true); 
							//Add function local var to functions and to deadcode list if its not used with column numbers
							if (usedlVRef == null || usedlVRef.length == 0) {
								if (!addFunction) {
									//making line number and col number 0 since function is used but not the local var of the function
									func = new Function(mRef.ent().name(),0,0
											,new ArrayList<FunctionParameter>(),new ArrayList<LocalVariable>());
									addFunction = true;
								}
								LocalVariable var = new LocalVariable(lVRef.ent().name(), lVRef.line(), lVRef.column());
								func.getVariables().add(var);
							} else {
								log.debug("Function local var "+lVRef.ent().name()+" is used. "+
										"Function local var is used by : "+usedlVRef[0].ent().name());
							}
						}						
					}
			    }
				if (addFunction) {
					dead.getFunctions().add(func);
				}
			}
			deadCodeList.add(dead);
		}
		return deadCodeList;
	}

//	protected void detectPvtDeadFuncs(Database db, List<DeadCode> deadCodeList) {
//		// Get a list of all functions and methods
//		Entity[] funcs = db.ents("function private ~unknown ~unresolved,method private ~unknown ~unresolved");
//		
//		deadCodeList.forEach(dead -> {
//			for (Entity e : funcs) {
//				log.debug(e.name() + " : " + e.kind());
//				
//				// Get a list of all functions and methods has invocations
//				Reference[] callRefs = e.refs("callby", null, true);
//				for (Reference pRef : callRefs) {
//					log.debug(
//							e.name() + " =>$ " + pRef.ent().name() + " [" + pRef.line() + ":" + pRef.column() + "]");
//					//Remove used functions
//					dead.getFunctions().remove(new Function(e.name(),0,0,null,null));
//				}
//			}
//		});
//	}
//
//	protected void detectPvtDeadVars(Database db, List<DeadCode> deadCodeList) {
//		// Get a list of all variables
//		Entity[] vars = db.ents("variable ~unknown");
//
//		deadCodeList.forEach(dead -> {
//			for (Entity e : vars) {
//				log.debug(e.name() + " : " + e.kind());
//	
//				// Get a list of all used variables
//				Reference[] useByRefs = e.refs("Useby", null, true);
//				for (Reference pRef : useByRefs) {
//					log.debug(
//							e.name() + " => " + pRef.ent().name() + " [" + pRef.line() + ":" + pRef.column() + "]");
//					
//					//Remove used Global variables
//					dead.getGlobalVariables().remove(new GlobalVariable(e.name(),0,0));
//					
//					//Remove used local variables
//					for (Function func : dead.getFunctions()) {
//						func.getVariables().remove(new LocalVariable(e.name(),0,0));
//					}
//				}
//			}
//		});
//	}
//
//	protected void detectPvtDeadFuncParams(Database db, List<DeadCode> deadCodeList) {
//		// Get a list of all functions and methods
//		Entity[] funcs = db.ents("function ~unknown ~unresolved,method ~unknown ~unresolved");
//
//		deadCodeList.forEach(dead -> {
//			for (Entity e : funcs) {
//				log.debug(e.name() + " : " + e.kind());
//	
//				// Get a list of all params of functions and methods
//				Reference[] paramterRefs = e.refs("define", "parameter", true);
//				for (Reference pRef : paramterRefs) {
//					e = pRef.ent();
//					log.debug("Param => " + e.name());
//	
//					// Get a list of all used params
//					Reference[] useByRefs = e.refs("Useby", null, true);
//					for (Reference uRef : useByRefs) {
//						log.debug(e.name() + " =>+ " + uRef.ent().name() + " [" + uRef.line() + ":"
//								+ uRef.column() + "]");
//						
//						//Remove used function parameters
//						for (Function func : dead.getFunctions()) {
//							func.getParameters().remove(new FunctionParameter(e.name(),0,0));
//						}
//					}
//				}
//			}
//		});
//	}
}
