package lk.devfactory.controllers;

import java.util.stream.Collectors;

import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import io.swagger.inflector.models.RequestContext;
import io.swagger.inflector.models.ResponseContext;
import lk.devfactory.models.Repository;
import lk.devfactory.repository.RepositoryDO;
import lk.devfactory.store.impl.UUIDGenerator;

//TODO : validate input
@Component
public class RepositoryController {

    Faker faker = new Faker();
    
    @Autowired
    RepositoryDO repository;

    //TODO: Handle already submitted repo
    //TODO: Handle 405
    public ResponseContext addRepo(RequestContext request, Repository body) {
    	body = repository.add(UUIDGenerator.get(), body);
        return new ResponseContext()
                .status(Status.OK)
                .entity(body);
    }
    
    //TODO: Handle 404
    public ResponseContext findRepositories(RequestContext request) {
        return new ResponseContext()
                .status(Status.OK)
                .entity(repository.findAll().collect(Collectors.toList()));
    }

    //TODO: Handle 404 400
    public ResponseContext getRepoDeadCodeByID(RequestContext requestContext, String id)
    {
        return new ResponseContext().status(Status.OK)
                                    .entity(repository.findById(UUIDGenerator.get(id)));
    }
    
  //TODO: Handle 404 400
    public ResponseContext removeRepo(RequestContext requestContext, String id)
    {
    	System.out.println("######remove repo#######");
    	repository.remove(UUIDGenerator.get(id));
        return new ResponseContext().status(Status.OK);
    }
}
