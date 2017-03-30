package lk.devfactory.repository.Impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.FileCopyUtils;

import lk.devfactory.ds.RepositoryDS;
import lk.devfactory.models.DeadCode;
import lk.devfactory.models.Repository;
import lk.devfactory.repository.DeadCodeDO;
import lk.devfactory.repository.RepositoryDO;
import lk.devfactory.store.impl.UUID;
import lk.devfactory.utility.GitUrlProcessor;
import lk.devfactory.utility.UnzipUtility;

//TODO Logger setting.Fix the exception throwing. Fix the prjPath.remove DS setter.
@org.springframework.stereotype.Repository
public class GitZipJavaProjectRepositoryDO implements RepositoryDO {

	private static final String SRC_FOLDER = "src";
	public static final String projPath = "/Users/lahiru/tmp";
	public static final String UDB_PROJECT_NAME = "und_project.udb";
	public static final String DOWNLOAD_PROJECT_SUFFIX = "_download.zip";
	
	@Autowired
	@Qualifier("repositoryCacheDS")
	RepositoryDS<UUID,Repository> repositoryDS;
	
	public void setRepositoryDS(RepositoryDS<UUID,Repository> repositoryDS) {
		this.repositoryDS = repositoryDS;
	}

	@Autowired
	DeadCodeDO deadCodeDO;
	
	public void setDeadCodeDO(DeadCodeDO deadCodeDO) {
		this.deadCodeDO = deadCodeDO;
	}

	@Override
	//TODO replace Repository.getID to UUID type. Then reomve this additional UUID parameter
	public Repository add(UUID id, Repository repository) {
		repository.setStatus("pending");
		boolean existing = !repositoryDS.create(id, repository);
		//TODO Cannot add already existing repository with this check.
		//TODO need to make following block of code in if condition concurrent
		if (!existing) {
			repository.setStatus("downloading");
			download(id.toString(), repository.getUrl());
			//TODO Change the default status list in swagger enum
			extract(id.toString());
			renameProjectFolder(id.toString(),repository.getUrl());
			buildUdb(id.toString());
			repository.setStatus("analysing");
			List<DeadCode> deadCodeList = deadCodeDO.analyse(id, repository);
			repository.setStatus("completed");
			repository.setDeadCode(deadCodeList);
			
		} else {
			repository = repositoryDS.findById(id);
		}
		return repository;
	}

	@Override
	public Stream<Repository> findAll() {
		return repositoryDS.findAll();
	}

	@Override
	public Repository findById(UUID id) {
		return repositoryDS.findById(id);
	}
	
	private void download(String repoId, String gitUrl) {
		String absPath = projPath + File.separator;
		try {
			URL url = new URL(GitUrlProcessor.convertToZipDownloadUrl(gitUrl));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			InputStream in = connection.getInputStream();
			FileOutputStream out = new FileOutputStream(absPath + repoId + DOWNLOAD_PROJECT_SUFFIX);
			FileCopyUtils.copy(in, out);
			out.close();

		} catch (IOException e) {
			System.out.println("Failed to download repo:" + e.getMessage());
		}
	}

	private void extract(String repoId) {
		String absPath = projPath + File.separator;
		UnzipUtility unzipUtility = new UnzipUtility();
		try {
			unzipUtility.unzip(absPath + repoId + DOWNLOAD_PROJECT_SUFFIX, projPath);
		} catch (IOException e) {
			System.out.println("Failed extract zip file:" + e.getMessage());
		}

	}
	
	private void renameProjectFolder(String repoId, String gitUrl) {
		String absPath = projPath + File.separator;
		File file = new File(absPath+GitUrlProcessor.getProjectName(gitUrl)+"-master");
		file.renameTo(new File(absPath+repoId));
	}
	
	private void buildUdb(String repoId) {
		String absPath = projPath + File.separator + repoId + File.separator;
		String cmd = "/Applications/Understand.app/Contents/MacOS/und -db " 
				+ absPath + UDB_PROJECT_NAME + " create -languages Java " + "add " + absPath
				+SRC_FOLDER+" settings -javaVersion Java8 analyze";
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
