package eu.fbk.PapyGame.repository;

import eu.fbk.PapyGame.model.Project;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String>{
    @Query("{ 'project_id': ?0 }")
    Project findByProjectId(String project_id);

    @Query("{ 'ctxId': ?0 }")
    List<Project> findByCtxId(String ctxId);
}
