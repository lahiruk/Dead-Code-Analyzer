package lk.devfactory.repository.impl;

//import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import lk.devfactory.SystemPropertyTestSupport;
import lk.devfactory.ds.RepositoryDS;
import lk.devfactory.models.DeadCode;
import lk.devfactory.models.Repository;
import lk.devfactory.repository.DeadCodeDO;
import lk.devfactory.repository.RepositoryDO;
import lk.devfactory.store.impl.UUID;
import lk.devfactory.store.impl.UUIDGenerator;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class UnderstandDeadCOdeDOTests extends SystemPropertyTestSupport {
	
	Repository originalRepo;
	UUID uuid;
	List<DeadCode> deadCodeList;
	//TODO : path should be fixed to run on other user machine
	static final String cmd = "/Users/lahiru/tmp/MacOS/und -db /Users/lahiru/tmp/9f78a002-2237-44b9-9d9b-aa401dcc771c/und_project.udb create -languages Java add /Users/lahiru/tmp/9f78a002-2237-44b9-9d9b-aa401dcc771c/src settings -javaVersion Java8 analyze";
	
	@Autowired
	RepositoryDO reposiotryDO;
	
	@MockBean
	RepositoryDS<UUID, Repository, String> repositoryDS;
	
	@MockBean
	DeadCodeDO deadCodeDO;
	
	@MockBean
	SystemProcess sp;
	
	@MockBean
	ProjectDownload pd;
	
	@MockBean
	public ProjectDirectory pj;
	
	@MockBean
	ExtractProject ep;
	
	@Before
	public void before(){
		originalRepo = new Repository();
        originalRepo.setUrl("https://github.com/lahiruk/exam-conductor");
        uuid = UUIDGenerator.get();
        deadCodeList = new ArrayList<DeadCode>();
	}
	
	
	// DeadCodeDO has been injected into the reposiotryDO bean
    //given(this.deadCodeDO.analyse(uuid, originalRepo)).willReturn(deadCodeList);
    //given(this.sp.getPrepairedCmd(uuid.toString())).willReturn(cmd);
    //doNothing().when(this.sp).executeUnd(cmd);
	
//	repoDO.findAll().forEach(reposiotryQ -> {
//	System.out.println("Repository:");
//	System.out.println(ReflectionToStringBuilder.toString(reposiotryQ));
//
//	reposiotryQ.getDeadCode().forEach(dead ->{
//
//		System.out.println("Dead code:");
//		System.out.println(ReflectionToStringBuilder.toString(dead));
//		
//		dead.getGlobalVariables().forEach(gv -> {
//			System.out.println("Global Var:");
//			System.out.println(ReflectionToStringBuilder.toString(gv));			
//		});
//
//		dead.getFunctions().forEach(function -> {
//			System.out.println("Function:");
//			System.out.println(ReflectionToStringBuilder.toString(function));
//			System.out.println("Function Parameter:");
//			System.out.println(function.getParameters());
//			System.out.println("Local var:");
//			System.out.println(function.getVariables());
//		});
//	});
//});

}
