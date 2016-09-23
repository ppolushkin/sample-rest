package hello.application.rest;

import hello.application.vo.ApiPerson;
import hello.domain.client.AsyncVkClient;
import hello.domain.entity.Person;
import hello.domain.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Implementation like on Diagram-3
 * Created by pavel on 05.08.16.
 */
@Controller
@RequestMapping("/non-blocking-async-persons-with-apache")
public class NonBlockingAsyncPersonController {

    @Autowired
    private PersonService service;

    @Autowired
    @Qualifier(value = "apacheVkClient")
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
                whenComplete(((aVoid, throwable) -> { //Non-blocking!
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

}
