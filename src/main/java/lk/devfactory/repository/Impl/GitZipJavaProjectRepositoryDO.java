package lk.devfactory.repository.Impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.FileCopyUtils;

import lk.devfactory.ds.RepositoryDS;
import lk.devfactory.models.Repository;
import lk.devfactory.repository.DeadCodeDO;
import lk.devfactory.repository.RepositoryDO;
import lk.devfactory.store.impl.UUID;
import lk.devfactory.utility.GitUrlProcessor;
import lk.devfactory.utility.UnzipUtility;

//TODO Logger setting.Fix the exception throwing
@org.springframework.stereotype.Repository
public class GitZipJavaProjectRepositoryDO implements RepositoryDO {

	private static final String PREV_FOLDER = ".."+File.separator;
	private static final String SRC_FOLDER = "src";
	public static final String projPath = "/Users/lahiru/Development/workspace/SciToolPOC";
	public static final String UDB_PROJECT_NAME = "und_project.udb";
	public static final String DOWNLOAD_PROJECT_SUFFIX = "_download.zip";
	
	@Autowired
	@Qualifier("repositoryCacheDS")
	RepositoryDS repositoryDS;
	
	@Autowired
	DeadCodeDO deadCodeDO;
	
	@Override
	//TODO replace Repository.getID to UUID type. Then reomve this additional UUID parameter
	public Repository add(UUID id, Repository repository) {
		download(id.toString(), repository.getUrl());
		extract(id.toString());
		renameProjectFolder(id.toString(),repository.getUrl());
		buildUdb(id.toString());
		deadCodeDO.analyse(id, repository);
		return null;
	}

	@Override
	public Stream<Repository> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<Repository> findById(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void download(String repoId, String gitUrl) {
		try {
			URL url = new URL(GitUrlProcessor.convertToZipDownloadUrl(gitUrl));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			InputStream in = connection.getInputStream();
			FileOutputStream out = new FileOutputStream(PREV_FOLDER + repoId + DOWNLOAD_PROJECT_SUFFIX);
			FileCopyUtils.copy(in, out);
			out.close();

		} catch (IOException e) {
			System.out.println("Failed to download repo:" + e.getMessage());
		}
	}

	private void extract(String repoId) {
		UnzipUtility unzipUtility = new UnzipUtility();
		try {
			unzipUtility.unzip(PREV_FOLDER + repoId + DOWNLOAD_PROJECT_SUFFIX, PREV_FOLDER);
		} catch (IOException e) {
			System.out.println("Failed extract zip file:" + e.getMessage());
		}

	}
	
	private void renameProjectFolder(String repoId, String gitUrl) {
		File file = new File(PREV_FOLDER+GitUrlProcessor.getProjectName(gitUrl)+"-master");
		file.renameTo(new File(PREV_FOLDER+repoId));
	}
	
	private void buildUdb(String repoId) {
		String absPath = projPath + File.separator + repoId;
		String cmd = "und -db " + absPath + File.separator + UDB_PROJECT_NAME + " create -languages Java " + "add " + absPath
				+File.separator+SRC_FOLDER+" settings -javaVersion Java8 analyze";
		System.out.println(cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			System.out.println("Waiting for udb build ...");
			p.waitFor();
			System.out.println("Udb file done.");
		} catch (IOException | InterruptedException e) {
			System.out.println("Failed execute udb command:" + e.getMessage());
		}
	}
}
