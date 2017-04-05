package lk.devfactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import lk.devfactory.model.DeadCode;
import lk.devfactory.reposiotry.DeadCodeDO;
import lk.devfactory.repository.impl.UnderstandDeadCodeDO;
import lk.devfactory.utility.SystemConst;

//TODO Logging and error handling
@SpringBootApplication
public class DeadCodeApplication {
	
	static private final Logger log = LoggerFactory.getLogger(DeadCodeApplication.class);

	public static void main(String[] args) {
		String repoId = args[0];
		log.info("Received request to analyse dead code:" + repoId);
		ConfigurableApplicationContext context = SpringApplication.run(DeadCodeApplication.class, args);
		DeadCodeDO dc = context.getBean(UnderstandDeadCodeDO.class);
		List<DeadCode> deadCodeList = dc.analyse(repoId);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(SystemConst.TMP_PATH + 
					File.separator + repoId + File.separator + 
					"deadcode-analysis-output.json"), deadCodeList);
		} catch (IOException e) {
			log.error("Unable to convert to Json ",e.getLocalizedMessage());
		} 
	}

}
