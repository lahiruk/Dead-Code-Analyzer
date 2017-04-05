package lk.devfactory.repository.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import lk.devfactory.utility.GitUrlProcessor;
import lk.devfactory.utility.SystemConst;

//TODO: Add javadocs
@Component
public class ProjectDownload {
	
	static private final Logger log = LoggerFactory.getLogger(ExtractProject.class);
	
	protected void download(String gitUrl, String repoId) {
		String absPath = SystemConst.TMP_PATH + File.separator;
		FileOutputStream out = null;
		
		try {
			URL url = new URL(GitUrlProcessor.convertToZipDownloadUrl(gitUrl));
			log.info("Downloading file "+ url.getPath());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			InputStream in = connection.getInputStream();
			out = new FileOutputStream(absPath + repoId + SystemConst.DOWNLOAD_PROJECT_SUFFIX);
			FileCopyUtils.copy(in, out);
			out.close();
			log.debug("Downloading file completed for " + url.getPath());
		} catch (IOException e) {
			throw new RuntimeException("Failed to download repo:"+repoId, e);
		} 
	}
}
