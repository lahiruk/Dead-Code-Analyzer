package lk.devfactory.repository.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lk.devfactory.utility.GitUrlProcessor;
import lk.devfactory.utility.SystemConst;

//TODO: Add Javadocs
@Component
public class ProjectDirectory {

	static private final Logger log = LoggerFactory.getLogger(GitZipJavaProjectRepositoryDO.class);
	
	protected void renameProjectFile(String gitUrl, String repoId){
		String absPath = SystemConst.TMP_PATH + File.separator;
		File file = new File(absPath+GitUrlProcessor.getProjectName(gitUrl)+"-master");
		
		log.info("Rename file :"+file.getName() + " to " + absPath+repoId);
		file.renameTo(new File(absPath+repoId));
	}
}
