package lk.devfactory.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import com.scitools.understand.Reference;
import com.scitools.understand.UnderstandException;

import lk.devfactory.model.Clazz;
import lk.devfactory.model.Function;
import lk.devfactory.model.FunctionParameter;
import lk.devfactory.model.GlobalVariable;
import lk.devfactory.model.LocalVariable;
import lk.devfactory.repository.DeadCodeDO;

@Component
public class UnderstandDeadCodeDO implements DeadCodeDO {
	static private final Logger log = LoggerFactory.getLogger(UnderstandDeadCodeDO.class);
	
	@Autowired
	UnderstandDatabase ud;
	
	public List<Clazz> analyse(String repoId){
		Database db = null;
		List<Clazz> deadCodeList = null;
		try {
			log.info("Starting to analyse udb with id:"+repoId);
			db = ud.openDatabase(repoId);
			deadCodeList = loadProjectEntities(db);
			log.info("Completed the analyse udb with id:"+repoId);
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
	
	@SuppressWarnings("unchecked")
	protected List<Clazz> loadProjectEntities(Database db) {
		List<Clazz> deadCodeList = new ArrayList<Clazz>();
		// Get a list of all functions and methods
		Entity[] clazz = db.ents("java class ~unused ~unknown");
		log.debug("No of classes : "+(clazz != null?clazz.length:0));
		
		CollectionUtils.arrayToList(clazz).stream().forEach(e -> {
			Clazz dead = addUnusedGlobalVar((Entity) e);

			addUnusedFunctionsAndFunctionAttribs((Entity) e, dead);
			
			if (!CollectionUtils.isEmpty(dead.getFunctions()) ||
			    !CollectionUtils.isEmpty(dead.getGlobalVariables()) ||
                !CollectionUtils.isEmpty(dead.getParameters()) ||
                !CollectionUtils.isEmpty(dead.getVariables())) {
			  deadCodeList.add(dead);
			}
		});
		return deadCodeList;
	}
	
    private Clazz addUnusedGlobalVar(Entity e) {
      log.debug(e.name() + " : " + e.kind());

      Clazz dead = new Clazz();
      dead.setName(e.longname(true));
      
      // Get a list of all global variables of the class
      Reference[] varRefs = e.refs("Java Define", "Java Private Variable", true);
      for (Reference vRef : varRefs) {
          log.debug(e.name() + " => " + vRef.ent().name() + " [" + vRef.line() + ":" + vRef.column()
                  + "] > " + vRef.ent().kind().name());
          
          Reference[] usedVRef = vRef.ent().refs("Java Useby", null, true); 
          if (usedVRef == null || usedVRef.length == 0) {
              GlobalVariable gloVar = new GlobalVariable(vRef.ent().longname(true), vRef.line(), vRef.column());
              dead.getGlobalVariables().add(gloVar);
          } else {
              log.debug("Variable is "+vRef.ent().name()+" is used. "+
                      "Variable is used by : "+usedVRef[0].ent().name());
          }
          
      }
      return dead;
    }
	   
	@SuppressWarnings("unchecked")
	private void addUnusedFunctionsAndFunctionAttribs(Entity e, Clazz dead) {
		// Get a list of all functions and methods of class
		Reference[] methodRefs = e.refs("Java Define", "Java Method Private", true);

		CollectionUtils.arrayToList(methodRefs).stream().forEach(mRefObj -> {
			
			Reference mRef = (Reference) mRefObj;
			log.debug(e.name() + " =>^ " + (mRef.ent().name() + " [" + mRef.line() + ":" + mRef.column()
					+ "] > " + mRef.ent().kind().name()));
			
			
		    if (mRef.ent().kind().name().contains("Method")) {
				addUnusedFunction(mRef, dead);

				// Get a list of all method variables
				Reference[] localVarRefs = mRef.ent().refs("Java Define", "Variable Parameter", true);
				for (Reference lVRef : localVarRefs) {
					log.debug(mRef.ent().name() + " =>* " + lVRef.ent().name() + " [" + lVRef.line() + ":"
							+ lVRef.column() + "] > " + lVRef.ent().kind().name());
					
					if (lVRef.ent().kind().name().contains("Parameter")) { 
						addUnusedFunctionParam(mRef, dead, lVRef);					
					}
					
					if (lVRef.ent().kind().name().contains("Variable")) { 
					    addUnusedFunctionVar(mRef, dead, lVRef);			    
					}						
				}
		    }
		});
	}
	
    private void addUnusedFunction(Reference mRef, Clazz dead) {
      Reference[] usedMRef = mRef.ent().refs("Java Callby", null, true); 
      Function func = null;
      //Add function to deadcode list if its not used with column numbers
      if (usedMRef == null || usedMRef.length == 0) { 
          func = new Function(mRef.ent().longname(true),mRef.line(),mRef.column());
          dead.getFunctions().add(func);
      } else {
          log.debug("Function "+mRef.ent().name()+" is used. "+
                  "Function is used by : "+usedMRef[0].ent().name());
      }
    }

	private void addUnusedFunctionVar(Reference mRef, Clazz dead, Reference lVRef) {
		Reference[] usedlVRef = lVRef.ent().refs("Java Useby", null, true); 
		//Add function local var to functions and to deadcode list if its not used with column numbers
		LocalVariable var = null;
		if (usedlVRef == null || usedlVRef.length == 0) {
			 var = new LocalVariable(lVRef.ent().longname(true), lVRef.line(), lVRef.column());
			 dead.getVariables().add(var);
		} else {
			log.debug("Function local var "+lVRef.ent().name()+" is used. "+
					"Function local var is used by : "+usedlVRef[0].ent().name());
		}
	}

	private void addUnusedFunctionParam(Reference mRef, Clazz dead, Reference lVRef) {
		Reference[] usedlVRef = lVRef.ent().refs("Java Useby", null, true); 
		FunctionParameter param = null;
		//Add function param to functions and to deadcode list if its not used with column numbers
		if (usedlVRef == null || usedlVRef.length == 0) { 
			param = new FunctionParameter(lVRef.ent().longname(true), lVRef.line(), lVRef.column());
			dead.getParameters().add(param);
		} else {
			log.debug("Function param "+lVRef.ent().name()+" is used. "+
					"Function param is used by : "+usedlVRef[0].ent().name());
		}
	}
}
