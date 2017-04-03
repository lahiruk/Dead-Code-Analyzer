package lk.devfactory.repository.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;

import lk.devfactory.models.DeadCode;
import lk.devfactory.utility.SystemConst;

//TODO: Add logger. Add exception handling
@Component
public class SystemProcess {
	
	protected String getPrepairedCmd(String repoId){
		//TODO Scan src folder in subfolders.
		String absPath = SystemConst.TMP_PATH + File.separator + repoId + File.separator;

		String cmd = SystemConst.TMP_PATH+ File.separator + SystemConst.DIST + File.separator+"und -db " 
				+ absPath + SystemConst.UDB_PROJECT_NAME + " create -languages Java " + "add " + absPath
				+SystemConst.SRC_FOLDER+" settings -javaVersion Java8";
		System.out.println(cmd);
		return cmd;
	}
	
	protected boolean executeUnd(String repoId) {
		try {
			String prog = SystemConst.TMP_PATH+ File.separator + SystemConst.DIST + File.separator+"und";
			String absPath = SystemConst.TMP_PATH + File.separator + repoId + File.separator;
			System.out.println("Start preparing udb file..");
			String output = new ProcessExecutor().command(prog, "-db",absPath + SystemConst.UDB_PROJECT_NAME
					,"create", "-languages","Java","add",absPath+SystemConst.SRC_FOLDER,"settings"
					,"-javaVersion","Java8")
			          .readOutput(true).execute()
			          .outputUTF8();
			System.out.println("Output from cmd:" + output);
			return output.startsWith("Files added");
		} catch (InvalidExitValueException | TimeoutException | IOException |InterruptedException e)  {
			System.out.println("Failed execute udb command:" + e.getMessage());
		}  
		return false;
	}
	
	protected void executeUndAnalyse(String repoId) {
		try {
			String prog = SystemConst.TMP_PATH+ File.separator + SystemConst.DIST + File.separator+"und";
			String absPath = SystemConst.TMP_PATH + File.separator + repoId + File.separator;
			System.out.println("Start analysing udb file..");
			String output = new ProcessExecutor().command(prog, "-db",absPath + SystemConst.UDB_PROJECT_NAME
					, "analyze")
			          .readOutput(true).execute()
			          .outputUTF8();
			System.out.println("Output from cmd:" + output);
		} catch (InvalidExitValueException | TimeoutException | IOException |InterruptedException e)  {
			System.out.println("Failed analyse udb command:" + e.getMessage());
		}  
	}
	
	protected List<DeadCode> executeDeeadCodeJar(String repoId) {
		try {
			System.out.println("Start executing dead code analyser jar ..");
			String output = new ProcessExecutor().command("java","-Ddistribution="+SystemConst.DIST,
					"-DtmpPath="+SystemConst.TMP_PATH,
					"-jar","dead-code-worker-1.0.0.jar",repoId)
			          .readOutput(true).execute()
			          .outputUTF8();
			System.out.println("Output from cmd:" + output);
			ObjectMapper mapper = new ObjectMapper();
			List<DeadCode> deadCodeList = 
					mapper.readValue(new File(SystemConst.TMP_PATH + File.separator + repoId + File.separator + "deadcode-analysis-output.json"), 
					mapper.getTypeFactory().constructCollectionType(List.class, DeadCode.class));
			return deadCodeList;
		} catch (InvalidExitValueException | TimeoutException | IOException |InterruptedException e)  {
			System.out.println("Failed analyse deadcode / parse Json:" + e.getMessage());
		}  
		return new ArrayList<>();
	}
}