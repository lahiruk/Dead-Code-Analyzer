package lk.devfactory.repository.Impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import com.scitools.understand.Reference;
import com.scitools.understand.Understand;
import com.scitools.understand.UnderstandException;

import lk.devfactory.ds.RepositoryDS;
import lk.devfactory.models.Repository;
import lk.devfactory.repository.DeadCodeDO;
import lk.devfactory.store.impl.UUID;

//TODO Add logger. Fix the exception throwing
@org.springframework.stereotype.Repository
public class UnderstandDeadCodeDO implements DeadCodeDO {
	
	public static final String projPath = "/Users/lahiru/Development/workspace/SciToolPOC";
	public static final String UDB_PROJECT_NAME = "und_project.udb";
	
	@Autowired
	@Qualifier("deadCodeCacheDS")
	RepositoryDS repositoryDS;
	
	//TODO replace Repository.getID to UUID type. Then reomve this additional UUID parameter
	public void analyse(UUID id, Repository repository){
		Database db;
		try {
			db = openDatabase(id.toString());
			loadProjectEntities(db);
			detectPvtDeadFuncs(db);
			detectPvtDeadVars(db);
			detectPvtDeadFuncParams(db);
		} catch (UnderstandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Database openDatabase(String repoId) throws UnderstandException{
		String absPath = projPath + File.separator + repoId;
		// Open the Understand Database
		Database db = Understand.open(absPath + File.separator + UDB_PROJECT_NAME);
		System.out.println("Project file :" + absPath + File.separator + UDB_PROJECT_NAME);
		return db;
	}
	
	private void loadProjectEntities(Database db) {
		// Get a list of all functions and methods
		Entity[] clazz = db.ents("class ~unknown ~unresolved");
		for (Entity e : clazz) {
			System.out.println(e.simplename() + " : " + e.kind());

			// Get a list of all variables of the class
			Reference[] varRefs = e.refs("define", "variable", true);
			for (Reference vRef : varRefs) {
				System.out.println(e.name() + " => " + vRef.ent().name() + " [" + vRef.line() + ":" + vRef.column()
						+ "] > " + vRef.ent().kind().name());
			}

			// Get a list of all functions and methods of class
			Reference[] methodRefs = e.refs("define", "method", true);
			for (Reference mRef : methodRefs) {
				System.out.println(e.name() + " => " + mRef.ent().name() + " [" + mRef.line() + ":" + mRef.column()
						+ "] > " + mRef.ent().kind().name());

				// Get a list of all method variables
				Reference[] localVarRefs = mRef.ent().refs("define", "Variable Parameter", true);
				for (Reference lVRef : localVarRefs) {
					System.out.println(mRef.ent().name() + " =>* " + lVRef.ent().name() + " [" + lVRef.line() + ":"
							+ lVRef.column() + "] > " + lVRef.ent().kind().name());
				}
			}
		}
	}

	private void detectPvtDeadFuncs(Database db) {
		// Get a list of all functions and methods
		Entity[] funcs = db.ents("function private ~unknown ~unresolved,method private ~unknown ~unresolved");
		for (Entity e : funcs) {
			System.out.println(e.name() + " : " + e.kind());

			// Get a list of all functions and methods has invocations
			Reference[] callRefs = e.refs("callby", null, true);
			for (Reference pRef : callRefs) {
				System.out.println(
						e.name() + " => " + pRef.ent().name() + " [" + pRef.line() + ":" + pRef.column() + "]");
			}
		}
	}

	private void detectPvtDeadVars(Database db) {
		// Get a list of all variables
		Entity[] vars = db.ents("variable ~unknown");

		for (Entity e : vars) {
			System.out.println(e.name() + " : " + e.kind());

			// Get a list of all used variables
			Reference[] useByRefs = e.refs("Useby", null, true);
			for (Reference pRef : useByRefs) {
				System.out.println(
						e.name() + " => " + pRef.ent().name() + " [" + pRef.line() + ":" + pRef.column() + "]");
			}
		}
	}

	private void detectPvtDeadFuncParams(Database db) {
		// Get a list of all functions and methods
		Entity[] funcs = db.ents("function ~unknown ~unresolved,method ~unknown ~unresolved");

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
					System.out.println(e.name() + " =>* " + uRef.ent().name() + " [" + uRef.line() + ":"
							+ uRef.column() + "]");
				}
			}
		}
	}
}
