package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
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
    public @ResponseBody List<Person> getThemAll() {
        List<Person> res = new ArrayList<>();
        repository.findAll().forEach(res::add);
        return res;
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody Person insertOne(@RequestBody Person person) {
        Person res = repository.save(person);
        return res;
    }

}
