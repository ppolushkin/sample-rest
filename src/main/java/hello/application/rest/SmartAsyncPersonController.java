package hello.application.rest;

import hello.application.vo.ApiPerson;
import hello.domain.client.AsyncVkClient;
import hello.domain.entity.Person;
import hello.domain.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created by pavel on 05.08.16.
 */
@Controller
@RequestMapping("/smart-async-persons")
public class SmartAsyncPersonController {

    @Autowired
    private PersonService service;

    @Autowired
    @Qualifier(value = "apacheVkClient")
//    @Qualifier(value = "dummyVkClient")
    private AsyncVkClient vkClient;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    DeferredResult<List<ApiPerson>> getAllAsync() {
        DeferredResult<List<ApiPerson>> result = new DeferredResult<>();

        List<Person> persons = service.getAll();

        List<ApiPerson> apiPersons = persons.stream().
                map(ApiPerson::of).
                collect(Collectors.toList());

        List<CompletableFuture<ApiPerson>> futures = apiPersons.stream().
                map(apiPerson1 -> vkClient.
                        getUserDataAsync(apiPerson1.getVkId()).
                        thenApply(apiPerson1::enrich)).
                collect(Collectors.toList());

        @SuppressWarnings("unchecked")
        CompletableFuture<ApiPerson>[] array = (CompletableFuture<ApiPerson>[]) new CompletableFuture[futures.size()];
        futures.toArray(array);

        CompletableFuture.allOf(array).
                whenComplete(((aVoid, throwable) -> {
                    if (!result.isSetOrExpired()) {
                        if (throwable != null) {
                            result.setErrorResult(throwable);
                        } else {
                            result.setResult(apiPersons);
                        }
                    }
                }));

        return result;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    DeferredResult<ApiPerson> getAsyncById(
            @PathVariable Long id,
            @RequestParam(value = "extend", required = false) String extend) {
        DeferredResult<ApiPerson> result = new DeferredResult<>();

        try {
            Person person = service.getById(id);
            ApiPerson apiPerson = ApiPerson.of(person);

            if (extend == null || "true".equalsIgnoreCase(extend)) {
                vkClient.getUserDataAsync(person.getVkId()).
                        whenComplete((((vkUserData, throwable) -> {
                            if (!result.isSetOrExpired()) {
                                if (throwable != null) {
                                    result.setErrorResult(throwable);
                                } else {
                                    apiPerson.enrich(vkUserData);
                                    result.setResult(apiPerson);
                                }
                            }
                        })));
            } else {
                result.setResult(apiPerson);
            }

        } catch (Throwable throwable) {
            result.setErrorResult(throwable);
        }

        return result;
    }

}
