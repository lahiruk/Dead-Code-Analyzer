package lk.devfactory.repository.impl;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lk.devfactory.SystemPropertyTestSupport;
import lk.devfactory.utility.SystemConst;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectDownloadTest extends SystemPropertyTestSupport{
	
	static final String TMP_PATH = "/Users/lahiru/tmp";
	static final String repoId = "123";
	static final String gitUrl = "https://github.com/lahiruk/exam-conductor";
	File oldZip;
	File newZip;
	
	
	@Autowired()
    ProjectDownload pd;

	@Before
	public void before(){
		oldZip = new File(TMP_PATH+ File.separator + repoId);
	}
	
	@After
	public void after() {
		newZip.delete();
	}
	
	@Test
	public void download() {
		assertFalse("Zip with downloading file should not exist", oldZip.exists());
		pd.download(gitUrl, repoId);
		newZip = new File(TMP_PATH+ File.separator + repoId+SystemConst.DOWNLOAD_PROJECT_SUFFIX);
		assertTrue("Zip download not successful", newZip.exists());
	}
}
