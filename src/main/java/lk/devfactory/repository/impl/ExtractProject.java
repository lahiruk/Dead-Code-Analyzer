package lk.devfactory.repository.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lk.devfactory.utility.SystemConst;
import lk.devfactory.utility.UnzipUtility;

//TODO: Add logger. Add exception handling
@Component
public class ExtractProject {

	@Autowired
	UnzipUtility unzipUtility;
	
	public void extract(String repoId){
		String absPath = SystemConst.TMP_PATH + File.separator;
		try {
			unzipUtility.unzip(absPath + repoId + SystemConst.DOWNLOAD_PROJECT_SUFFIX, SystemConst.TMP_PATH);
		} catch (IOException e) {
			System.out.println("Failed extract zip file:" + e.getMessage());
		}
	}
}
