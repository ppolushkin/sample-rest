package hello;

import hello.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * Created by pavel on 05.08.16.
 */
@Controller
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService service;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    DeferredResult<Person> getAsyncById(@PathVariable Long id) {
        DeferredResult<Person> result = new DeferredResult<>(500L);

        service.getByIdAsync(id).
                whenCompleteAsync((person, exception) -> {
                    if (!result.isSetOrExpired()) {
                        if (exception != null) {
                            result.setErrorResult(exception);
                        } else if (person == null) {
                            result.setErrorResult(new ResourceNotFoundException());
                        } else {
                            result.setResult(person);
                        }
                    }
                });
        return result;
    }

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    List<Person> getAll() {
        return service.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Person save(@RequestBody Person person) {
        return service.save(person);
    }

}
