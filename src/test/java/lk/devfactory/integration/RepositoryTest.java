package lk.devfactory.integration;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.RepositoryApi;
import io.swagger.client.model.Repository;
import lk.devfactory.SystemPropertyTestSupport;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RepositoryTest extends SystemPropertyTestSupport {

	static private final Logger log = LoggerFactory.getLogger(RepositoryTest.class);
	
    @LocalServerPort
    int randomServerPort;
    
    RepositoryApi api;
    
    Repository repository;
    
    @Before
    public void before() {
    	ApiClient client = new ApiClient();
    	client.getHttpClient().setReadTimeout(0, TimeUnit.MILLISECONDS);
    	client.getHttpClient().setWriteTimeout(0, TimeUnit.MILLISECONDS);
    	client.setBasePath("http://localhost:"+ randomServerPort +"/api/v1");
    	
    	api = new RepositoryApi(client);
    	
    	repository = loadRepositoryIfExists();
    }
    
    private Repository loadRepositoryIfExists() {
        List<Repository> repos = null;
        try {
			repos = api.findRepositories();
			return repos.get(0);
		} catch (ApiException e) {
		}
		return null;
	}

	@Test
    public void addRepositorySuccessfullPath() {	
    	repository = new Repository();
    	repository.setUrl("https://github.com/lahiruk/exam-conductor");
    	
    	try {
			repository = api.addRepo(repository);
			
			while (!Repository.StatusEnum.COMPLETED.equals(repository.getStatus())) {
				log.info("Sleeping 5 seconds till process state change to completed from "+repository.getStatus());
				Thread.sleep(5000);
				repository = loadRepositoryIfExists();
			}
		} catch (ApiException | InterruptedException e) {
			fail(e.getCause().getMessage());
		}
    	
        assertNotNull("Repo Id is null", repository.getId());
        assertEquals("Repo status is "+repository.getStatus(), Repository.StatusEnum.COMPLETED, repository.getStatus());
        assertNotNull("Repo dead code not found", repository.getDeadCode());
        assertTrue("Repo dead code not found",repository.getDeadCode().size()> 0);
    }
    
    @Test
    public void findAllRepos() {	
        //Find all repos
        List<Repository> repos = null;
        try {
			repos = api.findRepositories();
		} catch (ApiException e) {
			fail(e.getCause().getMessage());
		}
        
        assertNotNull("Repos list should not be null", repos);
        assertTrue("Repos list should have one element",repos.get(0).getDeadCode().size() == 1);
    }
    
    @Test
    public void findSingleRepo() {	
        //Find single repo
        Repository repoFound = null;
        try {
        	repoFound = api.getRepoDeadCodeByID(repository.getId());
		} catch (ApiException e) {
			fail(e.getCause().getMessage());
		}
        
        assertNotNull("Repo should not be null", repoFound);
        assertNotNull("Repo Id is null", repoFound.getId());
        assertEquals("Repo id ", repository.getId() , repoFound.getId());
    }
    
    @Test
    public void removeRepo() {
        //Remove repo
        try {
        	api.removeRepo(repository.getId());
		} catch (ApiException e) {
			fail(e.getCause().getMessage());
		}
        
        //Find after remove
        Repository noRepo = null;
        try {
        	noRepo = api.getRepoDeadCodeByID(repository.getId());
		} catch (ApiException e) {
			assertEquals("Http error code for delete operation ",HttpStatus.NOT_FOUND.value(), e.getCode());
		}
        
        assertNull("repo "+ repository.getId()+" should not be available after delete", noRepo);
        
        //Find all after remove
        List<Repository> repos = null;
        try {
			repos = api.findRepositories();
		} catch (ApiException e) {
			assertEquals("Http error code for delete operation ",HttpStatus.NOT_FOUND.value(), e.getCode());
		}
        
        assertNull("Repos list should be null after delete operation", repos);
        
        try {
        	api.removeRepo(repository.getId());
		} catch (ApiException e) {
			assertEquals("Http error code for delete operation ",HttpStatus.NOT_FOUND.value(), e.getCode());
		}
    }
}
