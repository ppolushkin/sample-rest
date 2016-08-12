package hello.application.rest;

import hello.application.vo.ApiPerson;
import hello.domain.client.VkClient;
import hello.domain.entity.Person;
import hello.domain.service.PersonService;
import hello.domain.vo.VkUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavel on 12.08.16.
 */
@Controller
@RequestMapping("/blocking-persons")
public class BlockingPersonController {

    @Autowired
    private PersonService service;

    @Autowired
    private VkClient vkClient;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    List<ApiPerson> getAll(@RequestParam(value = "extend", required = false) String extend) {
        List<Person> persons = service.getAll();
        List<ApiPerson> apiPersons = new ArrayList<>(persons.size());
        for (Person person : persons) {
            ApiPerson apiPerson = ApiPerson.of(person);

            if (extend == null || "true".equalsIgnoreCase(extend)) {
                VkUserData vkUserData = vkClient.getUserData(person.getVkId());
                apiPerson.enrich(vkUserData);
            }

            apiPersons.add(apiPerson);
        }
        return apiPersons;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiPerson getById(@PathVariable Long id, @RequestParam(value = "extend", required = false) String extend) {
        Person person = service.getById(id);
        ApiPerson apiPerson = ApiPerson.of(person);
        if (extend == null || "true".equalsIgnoreCase(extend)) {
            VkUserData vkUserData = vkClient.getUserData(person.getVkId());
            apiPerson.enrich(vkUserData);
        }
        return apiPerson;
    }

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Person save(@RequestBody Person person) {
        return service.save(person);
    }

}