package eu.fbk.PapyGame.repository;

import eu.fbk.PapyGame.model.Assignment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends MongoRepository<Assignment, String>{
    @Query("{ 'project_id': ?0 }")
    Assignment findByProjectId(String project_id);
}
