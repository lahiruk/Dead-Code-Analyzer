package lk.devfactory.test.unit;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import lk.devfactory.ds.impl.DeadCodeCacheDS;
import lk.devfactory.ds.impl.RepositoryCacheDS;
import lk.devfactory.models.Repository;
import lk.devfactory.repository.Impl.GitZipJavaProjectRepositoryDO;
import lk.devfactory.repository.Impl.UnderstandDeadCodeDO;
import lk.devfactory.store.impl.DeadCodeCacheStore;
import lk.devfactory.store.impl.RepositoryCacheStore;
import lk.devfactory.store.impl.RepositoryIdentifierCacheStore;
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
		RepositoryCacheDS repositoryDS = new RepositoryCacheDS();
		repositoryDS.setGitUrlCache(new RepositoryIdentifierCacheStore());
		repositoryDS.setRepositoryCache(new RepositoryCacheStore());
		repoDO.setRepositoryDS(repositoryDS);
		repoDO.add(UUIDGenerator.get(), repository);
		repoDO.findAll().forEach(reposiotryQ -> {
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
