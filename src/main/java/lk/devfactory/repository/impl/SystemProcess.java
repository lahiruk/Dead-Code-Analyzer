package lk.devfactory.repository.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Component;

import lk.devfactory.utility.SystemConst;

//TODO: Add logger. Add exception handling
@Component
public class SystemProcess {
	
	protected String getPrepairedCmd(String repoId){
		//TODO Scan src folder in subfolders.
		String absPath = SystemConst.TMP_PATH + File.separator + repoId + File.separator;
		String cmd = SystemConst.TMP_PATH+ File.separator + SystemConst.DIST + File.separator+"und -db " 
				+ absPath + SystemConst.UDB_PROJECT_NAME + " create -languages Java " + "add " + absPath
				+SystemConst.SRC_FOLDER+" settings -javaVersion Java8 analyze";
		System.out.println(cmd);
		return cmd;
	}
	
	protected void executeUnd(String cmd) {
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