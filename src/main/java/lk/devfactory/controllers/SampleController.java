package lk.devfactory.controllers;

import com.github.javafaker.Faker;
import io.swagger.inflector.models.RequestContext;
import io.swagger.inflector.models.ResponseContext;
import lk.devfactory.models.Category;
import lk.devfactory.models.Pet;
import lk.devfactory.models.Tag;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response.Status;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class SampleController {

    Faker faker = new Faker();

    public ResponseContext getPetById(RequestContext requestContext, Long id)
    {
        Pet pet = new Pet();
        pet.setId(id);
        pet.setCategory(new Category(16, faker.cat().breed()));
        pet.setName(faker.cat().name());
        pet.getTags().add(new Tag(21,faker.cat().registry()));
        pet.setStatus("active");
        return new ResponseContext().status(Status.OK)
                                    .entity(pet);
    }

    public ResponseContext updatePet(RequestContext request, Pet body) {
    	System.out.println("######test update#######");
        return new ResponseContext()
                .status(Status.OK)
                .entity(body);
    }

    public ResponseContext addPet(RequestContext request, Pet body) {
    	System.out.println("######test add#######");
        return new ResponseContext()
                .status(Status.OK)
                .entity(body);
    }

    public ResponseContext uploadFile(RequestContext request, Long petId, String additionalMetadata, java.io.InputStream file) {
        ByteArrayOutputStream outputStream;
        try {
            outputStream = new ByteArrayOutputStream();
            IOUtils.copy(file, outputStream);
            outputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
