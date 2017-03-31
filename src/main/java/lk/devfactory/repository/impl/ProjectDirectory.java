package lk.devfactory.repository.impl;

import java.io.File;

import org.springframework.stereotype.Component;

import lk.devfactory.utility.GitUrlProcessor;
import lk.devfactory.utility.SystemConst;

//TODO: Add logger. Add exception handling
@Component
public class ProjectDirectory {

	public void renameProjectFile(String gitUrl, String repoId){
		String absPath = SystemConst.TMP_PATH + File.separator;
		File file = new File(absPath+GitUrlProcessor.getProjectName(gitUrl)+"-master");
		file.renameTo(new File(absPath+repoId));
	}
}
