package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * Created by pavel on 12.08.16.
 */
@Controller
@RequestMapping("/blocking-persons")
public class BlockingPersonController {

    @Autowired
    private PersonService service;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    List<Person> getAll() {
        return service.getAll();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Person getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Person save(@RequestBody Person person) {
        return service.save(person);
    }

}