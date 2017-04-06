package lk.devfactory.repository.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import lk.devfactory.models.DeadCode;
import lk.devfactory.utility.SystemConst;

//TODO: Add javadocs. 
@Component
public class SystemProcess {
	
	static private final Logger log = LoggerFactory.getLogger(SystemProcess.class);
	
	protected boolean executeUnd(String repoId) {
		try {
			String prog = SystemConst.TMP_PATH+ File.separator + SystemConst.DIST + File.separator+"und";
			String absPath = SystemConst.TMP_PATH + File.separator + repoId + File.separator;
			log.info("Start preparing udb file.." + repoId);
			ProcessExecutor exec = new ProcessExecutor().command(prog, "-db",absPath + SystemConst.UDB_PROJECT_NAME
							,"create", "-languages","Java","add",absPath+SystemConst.SRC_FOLDER,"settings"
							,"-javaVersion","Java8");
			exec.environment("PATH", ".:" + System.getenv("PATH"));
			ProcessResult result =	exec.readOutput(true).execute();
			String output = result.outputUTF8();
			log.info(exec.getEnvironment().get("PATH"));
			log.info("Output from cmd:" + output);
			return output.startsWith("Files added");
		} catch (InvalidExitValueException | TimeoutException | IOException |InterruptedException e)  {
			throw new RuntimeException("Failed execute udb command build project udb for:"+repoId, e);
		}  
	}
	
	protected void executeUndAnalyse(String repoId) {
		try {
			String prog = SystemConst.TMP_PATH+ File.separator + SystemConst.DIST + File.separator+"und";
			String absPath = SystemConst.TMP_PATH + File.separator + repoId + File.separator;
			log.info("Start analysing udb file.." + repoId);
			ProcessExecutor exec = new ProcessExecutor().command(prog, "-db",absPath + SystemConst.UDB_PROJECT_NAME
								, "analyze");
			exec.environment("PATH", ".:" + System.getenv("PATH"));
			ProcessResult result =	exec.readOutput(true).execute();
			String output = result.outputUTF8();
			log.info("Output from cmd:" + output);
		} catch (InvalidExitValueException | TimeoutException | IOException |InterruptedException e)  {
			throw new RuntimeException("Failed analyse udb command for:"+repoId, e);
		}  
	}
	
	protected List<DeadCode> executeDeeadCodeJar(String repoId) {
		try {
			log.info("Start executing dead code analyser jar .." + repoId);
			ProcessExecutor exec = new ProcessExecutor().command("java","-Ddistribution="+SystemConst.DIST,
							"-DtmpPath="+SystemConst.TMP_PATH, "-jar",SystemConst.TMP_PATH + File.separator + 
							"dead-code-worker-1.0.0.jar",repoId);
			exec.environment("PATH", ".:" + System.getenv("PATH"));
			ProcessResult result =	exec.readOutput(true).execute();
			String output = result.outputUTF8();
			log.info("Output from cmd:" + output);
			ObjectMapper mapper = new ObjectMapper();
			List<DeadCode> deadCodeList = 
					mapper.readValue(new File(SystemConst.TMP_PATH + File.separator + repoId + File.separator 
							+ "deadcode-analysis-output.json"), 
					mapper.getTypeFactory().constructCollectionType(List.class, DeadCode.class));
			return deadCodeList;
		} catch (InvalidExitValueException | TimeoutException | IOException |InterruptedException e)  {
			throw new RuntimeException("Failed analyse deadcode / parse Json for:"+repoId, e);
		}  
	}
}