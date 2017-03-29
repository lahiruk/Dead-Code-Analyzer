package lk.devfactory.test.unit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import lk.devfactory.models.DeadCode;
import lk.devfactory.models.Repository;
import lk.devfactory.repository.RepositoryDO;
import lk.devfactory.repository.Impl.GitZipJavaProjectRepositoryDO;
import lk.devfactory.store.impl.UUIDGenerator;

@RunWith(SpringRunner.class)
public class GitZipJavaProjectRepositoryTests {
	Repository repository;
	
	@Before
	public void before(){
		repository = new Repository();
//		List<DeadCode> deadCode = new ArrayList<DeadCode>();
//        deadCode.add(new DeadCode());
//        repository.setDeadCode(deadCode);
        repository.setUrl("https://github.com/lahiruk/dead-code-analyzer");
	}

	@Test
	public void contextLoads() {
		RepositoryDO repoDO = new GitZipJavaProjectRepositoryDO();
		repoDO.add(UUIDGenerator.get(), repository);
		
	}

}
