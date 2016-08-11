package hello;

import hello.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by pavel on 09.08.16.
 */
@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;

    @NotNull
    public Person getById(Long id) {
        Person result = repository.findOne(id);
        if (result == null) {
            throw new ResourceNotFoundException("Person with id " + id + " not found");
        }
        return result;
    }

    @NotNull
    public CompletableFuture<Person> getByIdAsync(Long id) {
        CompletableFuture<Person> future = repository.findOneById(id);
        return future;
    }

    @NotNull
    public List<Person> getAll() {
        List<Person> res = new ArrayList<>();
        repository.findAll().forEach(res::add);
        return res;
    }

    @NotNull
    @Transactional
    public Person save(Person person) {
        return repository.save(person);
    }


}
