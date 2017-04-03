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
			detectPvtDeadFuncs(db, deadCodeList);
			detectPvtDeadVars(db, deadCodeList);
			detectPvtDeadFuncParams(db, deadCodeList);
		} catch (UnderstandException e) {
			log.error("Failed execute analyse :" + e.getMessage());
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
		for (Entity e : clazz) {
			log.info(e.name() + " : " + e.kind());

			DeadCode dead = new DeadCode();
			dead.setName(e.name());
			
			// Get a list of all global variables of the class
			Reference[] varRefs = e.refs("define", "variable", true);
			for (Reference vRef : varRefs) {
				log.info(e.name() + " => " + vRef.ent().name() + " [" + vRef.line() + ":" + vRef.column()
						+ "] > " + vRef.ent().kind().name());
				
				if (vRef.ent().kind().name().contains("Variable")) {
					GlobalVariable gloVar = new GlobalVariable(vRef.ent().name(), vRef.line(), vRef.column());
					if (!dead.getGlobalVariables().contains(gloVar)) {//Just to check global is already added. Potentially slow
						dead.getGlobalVariables().add(gloVar);
					} else {
						log.info("Some issue here in global var list");
					}
				} 
				
			}

			// Get a list of all functions and methods of class
			Reference[] methodRefs = e.refs("define", "method", true);
			for (Reference mRef : methodRefs) {
				log.info(e.name() + " =>^ " + mRef.ent().name() + " [" + mRef.line() + ":" + mRef.column()
						+ "] > " + mRef.ent().kind().name());
				
				Function func = null;
			    if (mRef.ent().kind().name().contains("Method")) {
					func = new Function(mRef.ent().name(),mRef.line(),mRef.column()
							,new ArrayList<FunctionParameter>(),new ArrayList<LocalVariable>());
					if (!dead.getFunctions().contains(func)) { //Just to check function is already added. Potentially slow
						dead.getFunctions().add(func);
					} else {
						log.info("Some issue here in function list");
					}

					// Get a list of all method variables
					Reference[] localVarRefs = mRef.ent().refs("define", "Variable Parameter", true);
					for (Reference lVRef : localVarRefs) {
						log.info(mRef.ent().name() + " =>* " + lVRef.ent().name() + " [" + lVRef.line() + ":"
								+ lVRef.column() + "] > " + lVRef.ent().kind().name());
						if (lVRef.ent().kind().name().contains("Parameter")) { 
							FunctionParameter param = new FunctionParameter(lVRef.ent().name(), lVRef.line(), lVRef.column());
							if (!func.getParameters().contains(param)) { //Just to check parameter is already added. Potentially slow
								func.getParameters().add(param);
							} else {
								log.info("Some issue here in function parameter list");
							}
						}
						
						if (lVRef.ent().kind().name().contains("Variable")) { 
							LocalVariable var = new LocalVariable(lVRef.ent().name(), lVRef.line(), lVRef.column());
							if (!func.getVariables().contains(var)) { //Just to check parameter is already added. Potentially slow
								func.getVariables().add(var);
							} else {
								log.info("Some issue here local variable list");
							}
						}						
					}
			    }
			}
			deadCodeList.add(dead);
		}
		return deadCodeList;
	}

	protected void detectPvtDeadFuncs(Database db, List<DeadCode> deadCodeList) {
		// Get a list of all functions and methods
		Entity[] funcs = db.ents("function private ~unknown ~unresolved,method private ~unknown ~unresolved");
		
		deadCodeList.forEach(dead -> {
			for (Entity e : funcs) {
				log.info(e.name() + " : " + e.kind());
				
				// Get a list of all functions and methods has invocations
				Reference[] callRefs = e.refs("callby", null, true);
				for (Reference pRef : callRefs) {
					log.info(
							e.name() + " =>$ " + pRef.ent().name() + " [" + pRef.line() + ":" + pRef.column() + "]");
					//Remove used functions
					dead.getFunctions().remove(new Function(e.name(),0,0,null,null));
				}
			}
		});
	}

	protected void detectPvtDeadVars(Database db, List<DeadCode> deadCodeList) {
		// Get a list of all variables
		Entity[] vars = db.ents("variable ~unknown");

		deadCodeList.forEach(dead -> {
			for (Entity e : vars) {
				log.info(e.name() + " : " + e.kind());
	
				// Get a list of all used variables
				Reference[] useByRefs = e.refs("Useby", null, true);
				for (Reference pRef : useByRefs) {
					log.info(
							e.name() + " => " + pRef.ent().name() + " [" + pRef.line() + ":" + pRef.column() + "]");
					
					//Remove used Global variables
					dead.getGlobalVariables().remove(new GlobalVariable(e.name(),0,0));
					
					//Remove used local variables
					for (Function func : dead.getFunctions()) {
						func.getVariables().remove(new LocalVariable(e.name(),0,0));
					}
				}
			}
		});
	}

	protected void detectPvtDeadFuncParams(Database db, List<DeadCode> deadCodeList) {
		// Get a list of all functions and methods
		Entity[] funcs = db.ents("function ~unknown ~unresolved,method ~unknown ~unresolved");

		deadCodeList.forEach(dead -> {
			for (Entity e : funcs) {
				log.info(e.name() + " : " + e.kind());
	
				// Get a list of all params of functions and methods
				Reference[] paramterRefs = e.refs("define", "parameter", true);
				for (Reference pRef : paramterRefs) {
					e = pRef.ent();
					log.info("Param => " + e.name());
	
					// Get a list of all used params
					Reference[] useByRefs = e.refs("Useby", null, true);
					for (Reference uRef : useByRefs) {
						log.info(e.name() + " =>+ " + uRef.ent().name() + " [" + uRef.line() + ":"
								+ uRef.column() + "]");
						
						//Remove used function parameters
						for (Function func : dead.getFunctions()) {
							func.getParameters().remove(new FunctionParameter(e.name(),0,0));
						}
					}
				}
			}
		});
	}
}
