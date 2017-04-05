package lk.devfactory.repository.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;

import lk.devfactory.SystemPropertyTestSupport;
import lk.devfactory.model.DeadCode;
import lk.devfactory.reposiotry.DeadCodeDO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnderstandDeadCodeDOTests extends SystemPropertyTestSupport {
	
	Repository repository;
	UUID uuid;
	List<DeadCode> deadCodeList;
	
	@Autowired
	DeadCodeDO deadCodeDO; //SUT
	
	@Before
	public void before(){
        deadCodeList = new ArrayList<DeadCode>();
	}
	
	@Test
	public void analysis() {
		deadCodeList = deadCodeDO.analyse("ead82e91-846a-4f65-95ce-e480639a5838");
		//TODO add asserts
		deadCodeList.forEach(dead -> {
			System.out.println("Dead code:");
			System.out.println(dead);
			
			dead.getGlobalVariables().forEach(gv -> {
				System.out.println("Global Var:");
				System.out.println(gv);			
			});

			dead.getFunctions().forEach(function -> {
				System.out.println("Function:");
				System.out.println(function);
				System.out.println("Function Parameter:");
				System.out.println(function.getParameters());
				System.out.println("Local var:");
				System.out.println(function.getVariables());
			});
		});
	}
}
