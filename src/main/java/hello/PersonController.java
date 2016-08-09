package hello;

import hello.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavel on 05.08.16.
 */
@Controller
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    List<Person> getThemAll() {
        List<Person> res = new ArrayList<>();
        repository.findAll().forEach(res::add);
        return res;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity getById(@PathVariable Long id) {
        Person person = repository.findOne(id);

        if (person == null) {
            throw new ResourceNotFoundException();
        }

        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @RequestMapping(path = "/async/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    DeferredResult<Person> getAsyncById(@PathVariable Long id) {
        DeferredResult<Person> result = new DeferredResult<>();
        try {
            repository.findOneById(id).thenAccept(result::setResult);
        } catch (Exception e) {
            System.err.print("Error");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Person insertOne(@RequestBody Person person) {
        Person res = repository.save(person);
        return res;
    }

}
