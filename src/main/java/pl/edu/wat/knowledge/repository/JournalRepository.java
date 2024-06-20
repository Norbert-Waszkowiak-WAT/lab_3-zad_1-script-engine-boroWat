package pl.edu.wat.knowledge.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import pl.edu.wat.knowledge.entity.Journal;

@RepositoryRestResource(collectionResourceRel = "journals", path = "journals")
public interface JournalRepository extends MongoRepository<Journal, String> {
}
