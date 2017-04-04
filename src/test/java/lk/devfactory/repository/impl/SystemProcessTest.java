package lk.devfactory.repository.impl;

import java.io.File;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lk.devfactory.SystemPropertyTestSupport;
import lk.devfactory.utility.SystemConst;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SystemProcessTest extends SystemPropertyTestSupport{
	
	String cmd;
	static final String TMP_PATH = "/Users/lahiru/tmp";
	static final String repoId = "123";
	static final String DIST = "MacOS";
	
	@Autowired()
    SystemProcess sp;

	@Before
	public void before(){
		String absPath = TMP_PATH + File.separator + repoId + File.separator;
		cmd = TMP_PATH+ File.separator + DIST + File.separator+"und -db " 
				+ absPath + SystemConst.UDB_PROJECT_NAME + " create -languages Java " + "add " + absPath
				+SystemConst.SRC_FOLDER+" settings -javaVersion Java8 analyze";
	}
	
}
