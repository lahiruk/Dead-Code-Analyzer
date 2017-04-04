package lk.devfactory.controllers;

import java.util.stream.Collectors;

import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import io.swagger.inflector.models.ApiError;
import io.swagger.inflector.models.RequestContext;
import io.swagger.inflector.models.ResponseContext;
import io.swagger.inflector.utils.ApiException;
import lk.devfactory.models.Repository;
import lk.devfactory.repository.RepositoryDO;
import lk.devfactory.repository.impl.GitZipJavaProjectRepositoryDO;
import lk.devfactory.store.impl.UUID;
import lk.devfactory.store.impl.UUIDGenerator;

//TODO : validate input
@Component
public class RepositoryController {
	
	static private final Logger log = LoggerFactory.getLogger(GitZipJavaProjectRepositoryDO.class);

    Faker faker = new Faker();
    
    @Autowired
    RepositoryDO repository;

    //TODO: Handle 405
    public ResponseContext addRepo(RequestContext request, Repository body) {
    	UUID uuid = UUIDGenerator.get();
    	log.info("Id for repo : "+ body + " is "+uuid);
    	body = repository.add(uuid, body);
    	log.info("Repository added for : "+ body + " and uuid is " + uuid);
        return new ResponseContext()
                .status(Status.OK)
                .entity(body);
    }
    
    //TODO: Handle 404
    public ResponseContext findRepositories(RequestContext request) {
    	log.debug("Find all repositories");
        return new ResponseContext()
                .status(Status.OK)
                .entity(repository.findAll().collect(Collectors.toList()));
    }

    //TODO: Handle 404 400
    public ResponseContext getRepoDeadCodeByID(RequestContext requestContext, String id)
    {
    	log.info("Id of the repo to find is "+ id);
    	Repository repositoryObj = repository.findById(UUIDGenerator.get(id));
    	if (repositoryObj == null) {
    		log.info("Unable to find repo with "+ id);
    		ApiError error = new ApiError();
    		error.setCode(404);
    		error.setMessage("Unable to find repository with id "+id);
    		throw new ApiException(error);
    	}
        return new ResponseContext().status(Status.OK)
                                    .entity(repositoryObj);
    }
    
  //TODO: Handle 404 400
    public ResponseContext removeRepo(RequestContext requestContext, String id)
    {
    	log.info("Id of the repo to remove "+" is "+ id);
    	repository.remove(UUIDGenerator.get(id));
    	log.info("Id of the repo removed "+" is "+ id);
        return new ResponseContext().status(Status.OK);
    }
}
