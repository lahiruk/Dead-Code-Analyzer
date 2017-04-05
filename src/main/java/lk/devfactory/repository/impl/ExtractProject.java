package lk.devfactory.repository.impl;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lk.devfactory.utility.SystemConst;
import lk.devfactory.utility.UnzipUtility;

//TODO: Addjavadocs
@Component
public class ExtractProject {

	static private final Logger log = LoggerFactory.getLogger(ExtractProject.class);
	
	@Autowired
	UnzipUtility unzipUtility;
	
	public void extract(String repoId){
		String absPath = SystemConst.TMP_PATH + File.separator;
		try {
			log.info("Extracting file " + absPath + repoId + SystemConst.DOWNLOAD_PROJECT_SUFFIX);
			unzipUtility.unzip(absPath + repoId + SystemConst.DOWNLOAD_PROJECT_SUFFIX, SystemConst.TMP_PATH);
		} catch (IOException e) {
			throw new RuntimeException("Failed extract zip file:"+repoId, e);
		}
	}
}
