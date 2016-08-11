package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * Created by pavel on 05.08.16.
 */
@Controller
@RequestMapping("/async-persons")
public class AsyncPersonController {

    @Autowired
    private PersonService service;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    DeferredResult<List<Person>> getAllAsync() {
        DeferredResult<List<Person>> result = new DeferredResult<>();
        service.getAllAsync().
                whenComplete(((persons, throwable) -> {
                    if (!result.isSetOrExpired()) {
                        if (throwable != null) {
                            result.setErrorResult(throwable);
                        } else {
                            result.setResult(persons);
                        }
                    }
                }));
        return result;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    DeferredResult<Person> getAsyncById(@PathVariable Long id) {
        DeferredResult<Person> result = new DeferredResult<>();
        service.getByIdAsync(id).
                whenComplete((person, exception) -> {
                    if (!result.isSetOrExpired()) {
                        if (exception != null) {
                            result.setErrorResult(exception);
                        } else {
                            result.setResult(person);
                        }
                    }
                });
        return result;
    }

}
