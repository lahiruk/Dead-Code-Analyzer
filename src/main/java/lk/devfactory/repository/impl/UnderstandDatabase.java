package lk.devfactory.repository.impl;

import java.io.File;

import org.springframework.stereotype.Component;

import com.scitools.understand.Database;
import com.scitools.understand.Understand;
import com.scitools.understand.UnderstandException;

import lk.devfactory.utility.SystemConst;

//TODO: Add logger. Add exception handling
@Component
public class UnderstandDatabase {
	
	protected Database openDatabase(String repoId) throws UnderstandException{
		String absPath = SystemConst.TMP_PATH + File.separator + repoId;
		// Open the Understand Database
		Database db = Understand.open(absPath + File.separator + SystemConst.UDB_PROJECT_NAME);
		System.out.println("Project file :" + absPath + File.separator + SystemConst.UDB_PROJECT_NAME);
		return db;
	}
	
}
