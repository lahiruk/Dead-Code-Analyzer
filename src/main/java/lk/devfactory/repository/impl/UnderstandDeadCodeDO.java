package lk.devfactory.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import com.scitools.understand.Reference;
import com.scitools.understand.UnderstandException;

import lk.devfactory.ds.DeadCodeDS;
import lk.devfactory.models.DeadCode;
import lk.devfactory.models.Function;
import lk.devfactory.models.FunctionParameter;
import lk.devfactory.models.GlobalVariable;
import lk.devfactory.models.LocalVariable;
import lk.devfactory.models.Repository;
import lk.devfactory.repository.DeadCodeDO;
import lk.devfactory.store.impl.UUID;

//TODO Add logger. Fix the exception throwing. Fix the project path. remove DS setter.
@org.springframework.stereotype.Repository
public class UnderstandDeadCodeDO implements DeadCodeDO {
	
	@Autowired
	@Qualifier("deadCodeCacheDS")
	DeadCodeDS<String, DeadCode> repositoryDS;
	
	@Autowired
	UnderstandDatabase ud;

	//TODO replace Repository.getID to UUID type. Then reomve this additional UUID parameter
	public List<DeadCode> analyse(UUID id, Repository repository){
		Database db;
		List<DeadCode> deadCodeList = null;
		try {
			db = ud.openDatabase(id.toString());
			deadCodeList = loadProjectEntities(db);
			detectPvtDeadFuncs(db);
			detectPvtDeadVars(db);
			detectPvtDeadFuncParams(db);
		} catch (UnderstandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return deadCodeList;
	}
	

	
	private List<DeadCode> loadProjectEntities(Database db) {
		List<DeadCode> deadCodeList = new ArrayList<DeadCode>();
		// Get a list of all functions and methods
		Entity[] clazz = db.ents("class ~unknown ~unresolved");
		for (Entity e : clazz) {
			System.out.println(e.name() + " : " + e.kind());

			DeadCode dead = new DeadCode();
			dead.setName(e.name());
			
			// Get a list of all global variables of the class
			Reference[] varRefs = e.refs("define", "variable", true);
			for (Reference vRef : varRefs) {
				System.out.println(e.name() + " => " + vRef.ent().name() + " [" + vRef.line() + ":" + vRef.column()
						+ "] > " + vRef.ent().kind().name());
				
				if (vRef.ent().kind().name().contains("Variable")) {
					GlobalVariable gloVar = new GlobalVariable(vRef.ent().name(), vRef.line(), vRef.column());
					if (!dead.getGlobalVariables().contains(gloVar)) {//Just to check global is already added. Potentially slow
						dead.getGlobalVariables().add(gloVar);
					} else {
						System.out.println("Some issue here in global var list");
					}
				} 
				
			}

			// Get a list of all functions and methods of class
			Reference[] methodRefs = e.refs("define", "method", true);
			for (Reference mRef : methodRefs) {
				System.out.println(e.name() + " =>^ " + mRef.ent().name() + " [" + mRef.line() + ":" + mRef.column()
						+ "] > " + mRef.ent().kind().name());
				
				Function func = null;
			    if (mRef.ent().kind().name().contains("Method")) {
					func = new Function(mRef.ent().name(),mRef.line(),mRef.column()
							,new ArrayList<FunctionParameter>(),new ArrayList<LocalVariable>());
					if (!dead.getFunctions().contains(func)) { //Just to check function is already added. Potentially slow
						dead.getFunctions().add(func);
					} else {
						System.out.println("Some issue here in function list");
					}

					// Get a list of all method variables
					Reference[] localVarRefs = mRef.ent().refs("define", "Variable Parameter", true);
					for (Reference lVRef : localVarRefs) {
						System.out.println(mRef.ent().name() + " =>* " + lVRef.ent().name() + " [" + lVRef.line() + ":"
								+ lVRef.column() + "] > " + lVRef.ent().kind().name());
						if (lVRef.ent().kind().name().contains("Parameter")) { 
							FunctionParameter param = new FunctionParameter(lVRef.ent().name(), lVRef.line(), lVRef.column());
							if (!func.getParameters().contains(param)) { //Just to check parameter is already added. Potentially slow
								func.getParameters().add(param);
							} else {
								System.out.println("Some issue here in function parameter list");
							}
						}
						
						if (lVRef.ent().kind().name().contains("Variable")) { 
							LocalVariable var = new LocalVariable(lVRef.ent().name(), lVRef.line(), lVRef.column());
							if (!func.getVariables().contains(var)) { //Just to check parameter is already added. Potentially slow
								func.getVariables().add(var);
							} else {
								System.out.println("Some issue here local variable list");
							}
						}						
					}
			    }
			}
			repositoryDS.create(e.name(), dead);
			deadCodeList.add(dead);
		}
		return deadCodeList;
	}

	private void detectPvtDeadFuncs(Database db) {
		// Get a list of all functions and methods
		Entity[] funcs = db.ents("function private ~unknown ~unresolved,method private ~unknown ~unresolved");
		
		repositoryDS.findAll().forEach(dead -> {
			for (Entity e : funcs) {
				System.out.println(e.name() + " : " + e.kind());
				
				// Get a list of all functions and methods has invocations
				Reference[] callRefs = e.refs("callby", null, true);
				for (Reference pRef : callRefs) {
					System.out.println(
							e.name() + " =>$ " + pRef.ent().name() + " [" + pRef.line() + ":" + pRef.column() + "]");
					//Remove used functions
					dead.getFunctions().remove(new Function(e.name(),0,0,null,null));
				}
			}
		});
	}

	private void detectPvtDeadVars(Database db) {
		// Get a list of all variables
		Entity[] vars = db.ents("variable ~unknown");

		repositoryDS.findAll().forEach(dead -> {
			for (Entity e : vars) {
				System.out.println(e.name() + " : " + e.kind());
	
				// Get a list of all used variables
				Reference[] useByRefs = e.refs("Useby", null, true);
				for (Reference pRef : useByRefs) {
					System.out.println(
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

	private void detectPvtDeadFuncParams(Database db) {
		// Get a list of all functions and methods
		Entity[] funcs = db.ents("function ~unknown ~unresolved,method ~unknown ~unresolved");

		repositoryDS.findAll().forEach(dead -> {
			for (Entity e : funcs) {
				System.out.println(e.name() + " : " + e.kind());
	
				// Get a list of all params of functions and methods
				Reference[] paramterRefs = e.refs("define", "parameter", true);
				for (Reference pRef : paramterRefs) {
					e = pRef.ent();
					System.out.println("Param => " + e.name());
	
					// Get a list of all used params
					Reference[] useByRefs = e.refs("Useby", null, true);
					for (Reference uRef : useByRefs) {
						System.out.println(e.name() + " =>+ " + uRef.ent().name() + " [" + uRef.line() + ":"
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
