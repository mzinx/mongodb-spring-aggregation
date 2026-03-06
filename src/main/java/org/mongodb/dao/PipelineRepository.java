package org.mongodb.dao;

import org.mongodb.model.PipelineTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PipelineRepository extends MongoRepository<PipelineTemplate, String>, CustomPipelineRepository {


}
