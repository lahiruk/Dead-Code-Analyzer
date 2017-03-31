package lk.devfactory.repository.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import lk.devfactory.utility.GitUrlProcessor;
import lk.devfactory.utility.SystemConst;

//TODO: Add logger. Add exception handling
@Component
public class ProjectDownload {
	
	public void download(String gitUrl, String repoId) {
		String absPath = SystemConst.TMP_PATH + File.separator;
		FileOutputStream out = null;
		try {
			URL url = new URL(GitUrlProcessor.convertToZipDownloadUrl(gitUrl));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			InputStream in = connection.getInputStream();
			out = new FileOutputStream(absPath + repoId + SystemConst.DOWNLOAD_PROJECT_SUFFIX);
			FileCopyUtils.copy(in, out);

		} catch (IOException e) {
			System.out.println("Failed to download repo:" + e.getMessage());
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				System.out.println("Failed to download repo:" + e.getMessage());
			}
		}
	}
}
