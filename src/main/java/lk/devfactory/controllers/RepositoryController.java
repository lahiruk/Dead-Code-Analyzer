package lk.devfactory.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import io.swagger.inflector.models.RequestContext;
import io.swagger.inflector.models.ResponseContext;
import lk.devfactory.models.DeadCode;
import lk.devfactory.models.Repository;

@Component
public class RepositoryController {

    Faker faker = new Faker();

    public ResponseContext addRepo(RequestContext request, Repository body) {
    	System.out.println("######test add####### "+body);
        return new ResponseContext()
                .status(Status.OK)
                .entity(body);
    }
    
    public ResponseContext findRepositories(RequestContext request) {
    	System.out.println("######test update#######");
        return new ResponseContext()
                .status(Status.OK);
    }

    public ResponseContext getRepoDeadCodeByID(RequestContext requestContext, Long id)
    {
        Repository repository = new Repository();
        repository.setId(id);
        List<DeadCode> deadCode = new ArrayList<DeadCode>();
        deadCode.add(new DeadCode());
        repository.setDeadCode(deadCode);
        repository.setUrl(faker.cat().name());
        repository.setStatus("active");
        return new ResponseContext().status(Status.OK)
                                    .entity(repository);
    }
}
