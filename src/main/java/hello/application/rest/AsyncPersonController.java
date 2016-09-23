package hello.application.rest;

import hello.application.vo.ApiPerson;
import hello.domain.client.AsyncVkClient;
import hello.domain.entity.Person;
import hello.domain.service.PersonService;
import hello.domain.vo.VkUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 *
 * Created by pp on 23.09.2016.
 */
@Controller
public class AsyncPersonController {

    @Autowired
    private PersonService service;

    @Autowired
    @Qualifier(value = "dummyVkClient")
    private AsyncVkClient dummyVkClient;

    @Autowired
    @Qualifier(value = "apacheVkClient")
    private AsyncVkClient apacheVkClient;

    /**
     * Implementation like on Diagram-1
     */
    @RequestMapping(value = "/async-persons", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ApiPerson> getAsyncPersons1() {
        return getAllAsync(dummyVkClient);
    }

    /**
     * Implementation like on Diagram-2
     */
    @RequestMapping(value = "/async-persons-with-apache", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ApiPerson> getAsyncPersons2() {
        return getAllAsync(apacheVkClient);
    }

    private List<ApiPerson> getAllAsync(AsyncVkClient asyncVkClient) {
        List<Person> persons = service.getAll();
        List<ApiPerson> apiPersons = new ArrayList<>(persons.size());

        Map<Long, CompletableFuture<VkUserData>> futureMap = new HashMap<>();

        for (Person person : persons) {
            ApiPerson apiPerson = ApiPerson.of(person);
            CompletableFuture<VkUserData> vkUserData = asyncVkClient.getUserDataAsync(person.getVkId());

            futureMap.put(person.getId(), vkUserData);
            apiPersons.add(apiPerson);
        }

        for (ApiPerson apiPerson : apiPersons) {
            CompletableFuture<VkUserData> vkUserData = futureMap.get(apiPerson.getId());
            try {
                apiPerson.enrich(vkUserData.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Exception occurred during async call", e);
            }
        }

        return apiPersons;
    }


}
