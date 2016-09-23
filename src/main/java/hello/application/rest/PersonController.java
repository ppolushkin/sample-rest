package hello.application.rest;

import hello.application.vo.ApiPerson;
import hello.domain.client.VkClient;
import hello.domain.entity.Person;
import hello.domain.service.PersonService;
import hello.domain.vo.VkUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation like on Diagram-0
 * <p>
 * Created by pavel on 12.08.16.
 */
@Controller
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService service;

    @Autowired
    private VkClient vkClient;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    List<ApiPerson> getUsers() {
        List<Person> persons = service.getAll();
        List<ApiPerson> apiPersons = new ArrayList<>(persons.size());

        for (Person person : persons) {
            ApiPerson apiPerson = ApiPerson.of(person);
            VkUserData vkUserData = vkClient.getUserData(person.getVkId());
            apiPerson.enrich(vkUserData);
            apiPersons.add(apiPerson);
        }
        return apiPersons;
    }

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Person save(@RequestBody Person person) {
        return service.save(person);
    }

}