package hello;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    @Async
    @Query("select p from Person p")
    CompletableFuture<List<Person>> findAllAsync();

    @Async
    CompletableFuture<Person> findOneById(@Param("id") Long id);

}
