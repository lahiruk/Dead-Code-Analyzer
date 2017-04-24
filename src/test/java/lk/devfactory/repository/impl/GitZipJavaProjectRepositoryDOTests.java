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
import lk.devfactory.model.Clazz;
import lk.devfactory.model.Repository;
//import lk.devfactory.reposiotry.DeadCodeDO;
import lk.devfactory.repository.RepositoryDO;
import lk.devfactory.store.impl.UUID;
import lk.devfactory.store.impl.UUIDGenerator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GitZipJavaProjectRepositoryDOTests extends SystemPropertyTestSupport {
	
	Repository originalRepo;
	UUID uuid;
	List<Clazz> deadCodeList;
	
	@Autowired
	RepositoryDO reposiotryDO;
	
	@MockBean
	RepositoryDS<UUID, Repository, String> repositoryDS;
	
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
        deadCodeList = new ArrayList<Clazz>();
	}

	@Test
	public void repositoryAddNew() {
		
		given(this.repositoryDS.create(uuid, originalRepo)).willReturn(true);
		
		Repository savedRepo = reposiotryDO.add(uuid, originalRepo);

		assertNotNull("Status", savedRepo.getStatus());
		assertTrue("Status is "+savedRepo.getStatus(),!"".equals(savedRepo.getStatus()));
		assertEquals("UUID", uuid.toString(), savedRepo.getId());
	}
	
	@Test
	public void repositoryAddExisting() {
		
		given(this.repositoryDS.create(uuid, originalRepo)).willReturn(true);
		Repository savedRepo = reposiotryDO.add(uuid, originalRepo);
		
		UUID newUuid = UUIDGenerator.get();
		given(this.repositoryDS.create(uuid, originalRepo)).willReturn(false);
		given(this.repositoryDS.findByNonIdUniqueKey(originalRepo.getUrl())).willReturn(savedRepo);
		Repository sameRepo = reposiotryDO.add(newUuid, originalRepo);

		assertEquals("UUID", savedRepo.getId(), sameRepo.getId());
	}

}
