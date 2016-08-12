package hello.domain.service;

import hello.domain.entity.Person;
import hello.domain.repository.PersonRepository;
import hello.domain.entity.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
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
        return repository.findOneById(id).
                thenApply(person -> {
                    if (person == null) {
                        throw new ResourceNotFoundException("Person with ID = " + id + " does not exist");
                    }
                    return person;
                });
    }

    @NotNull
    public List<Person> getAll() {
        List<Person> res = new ArrayList<>();
        repository.findAll().forEach(res::add);
        return res;
    }

    @Null
    public CompletableFuture<List<Person>> getAllAsync() {
        return repository.findAllAsync();
    }

    @NotNull
    @Transactional
    public Person save(Person person) {
        return repository.save(person);
    }


}
