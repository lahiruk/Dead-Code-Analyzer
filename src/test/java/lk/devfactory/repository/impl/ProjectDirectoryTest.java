package lk.devfactory.repository.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lk.devfactory.SystemPropertyTestSupport;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectDirectoryTest extends SystemPropertyTestSupport{
	
	static final String TMP_PATH = "/Users/lahiru/tmp";
	static final String repoId = "123";
	static final String gitUrl = "https://github.com/lahiruk/exam-conductor";
	File oldDir;
	File newDir;
	
	
	@Autowired()
    ProjectDirectory sf;

	@Before
	public void before(){
		oldDir = new File(TMP_PATH+ File.separator +"exam-conductor-master");
		try {
			oldDir.createNewFile();
		} catch (IOException e) {
			fail("Unable to create temp file for rename folder "+e.getMessage());
		}
		
		newDir = new File(TMP_PATH+ File.separator + repoId);
		
	}
	
	@After
	public void after() {
		oldDir.delete(); //Clean up
		newDir.delete(); //Clean up
	}
	
	@Test
	public void renameProjectFile(){
		sf.renameProjectFile(gitUrl, repoId);
		assertTrue("Folder didn't rename", newDir.exists());
		assertFalse("Folder didn't rename",oldDir.exists());
	}
}
