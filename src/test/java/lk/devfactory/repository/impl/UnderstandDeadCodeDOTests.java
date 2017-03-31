package lk.devfactory.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lk.devfactory.SystemPropertyTestSupport;
import lk.devfactory.models.DeadCode;
import lk.devfactory.models.Repository;
import lk.devfactory.repository.RepositoryDO;
import lk.devfactory.store.impl.UUID;
import lk.devfactory.store.impl.UUIDGenerator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnderstandDeadCodeDOTests extends SystemPropertyTestSupport {
	
	Repository repository;
	UUID uuid;
	List<DeadCode> deadCodeList;
	
	@Autowired
	RepositoryDO reposiotryDO;
	
	
	@Before
	public void before(){
		repository = new Repository();
        repository.setUrl("https://github.com/lahiruk/exam-conductor");
        uuid = UUIDGenerator.get();
        deadCodeList = new ArrayList<DeadCode>();
	}

	//TODO : convert this to a proper assert base test.
	@Test
	public void deadCodeVisualAnalyse() {
          
		reposiotryDO.add(uuid, repository);
		reposiotryDO.findAll().forEach(reposiotryQ -> {
			System.out.println("Repository:");
			System.out.println(ReflectionToStringBuilder.toString(reposiotryQ));

			reposiotryQ.getDeadCode().forEach(dead ->{

				System.out.println("Dead code:");
				System.out.println(ReflectionToStringBuilder.toString(dead));
				
				dead.getGlobalVariables().forEach(gv -> {
					System.out.println("Global Var:");
					System.out.println(ReflectionToStringBuilder.toString(gv));			
				});
	
				dead.getFunctions().forEach(function -> {
					System.out.println("Function:");
					System.out.println(ReflectionToStringBuilder.toString(function));
					System.out.println("Function Parameter:");
					System.out.println(function.getParameters());
					System.out.println("Local var:");
					System.out.println(function.getVariables());
				});
			});
		});
		
		
	}

}
