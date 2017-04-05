package lk.devfactory.repository.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.scitools.understand.Database;
import com.scitools.understand.Understand;
import com.scitools.understand.UnderstandException;

import lk.devfactory.utility.SystemConst;

//TODO: Add logger. Add exception handling
@Component
public class UnderstandDatabase {
	
	static private final Logger log = LoggerFactory.getLogger(UnderstandDatabase.class);
	//TODO putting this to a threadlocal was not considered due to issue mentioned the understand training
	Database db;
	
	protected Database openDatabase(String repoId) throws UnderstandException{
		String absPath = SystemConst.TMP_PATH + File.separator + repoId;
		log.info("Project file :" + absPath + File.separator + SystemConst.UDB_PROJECT_NAME);
		
		// Open the Understand Database
		db = Understand.open(absPath + File.separator + SystemConst.UDB_PROJECT_NAME);
		
		return db;
	}
	
	protected void loadNativeLibrary(){
		Understand.loadNativeLibrary();
	}
	
	protected void closeDatabase() {
		db.close();
	}
}
