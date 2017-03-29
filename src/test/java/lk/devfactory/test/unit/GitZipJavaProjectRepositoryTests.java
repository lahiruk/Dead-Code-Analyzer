package lk.devfactory.test.unit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import lk.devfactory.ds.impl.DeadCodeCacheDS;
import lk.devfactory.models.Repository;
import lk.devfactory.repository.Impl.GitZipJavaProjectRepositoryDO;
import lk.devfactory.repository.Impl.UnderstandDeadCodeDO;
import lk.devfactory.store.impl.DeadCodeCacheStore;
import lk.devfactory.store.impl.UUIDGenerator;

@RunWith(SpringRunner.class)
public class GitZipJavaProjectRepositoryTests {
	Repository repository;
	
	@Before
	public void before(){
		repository = new Repository();
        repository.setUrl("https://github.com/lahiruk/exam-conductor");
	}

	@Test
	public void repositoryAdd() {
		GitZipJavaProjectRepositoryDO repoDO = new GitZipJavaProjectRepositoryDO();
		UnderstandDeadCodeDO understandDCDO = new UnderstandDeadCodeDO();
		DeadCodeCacheDS cacheDS = new DeadCodeCacheDS();
		cacheDS.setDeadCodeCache(new DeadCodeCacheStore());
		understandDCDO.setRepositoryDS(cacheDS);
		repoDO.setDeadCodeDO(understandDCDO);
		repository = repoDO.add(UUIDGenerator.get(), repository);
		System.out.println(repository);
	}

}
