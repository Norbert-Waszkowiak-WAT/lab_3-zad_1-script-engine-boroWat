package pl.edu.wat.knowledge.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import pl.edu.wat.knowledge.entity.Affiliation;

@RepositoryRestResource(collectionResourceRel = "affiliations", path = "affiliations")
public interface AffiliationRepository extends MongoRepository<Affiliation, String> {
}
