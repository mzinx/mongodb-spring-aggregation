package com.mzinx.mongodb.aggregation.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mzinx.mongodb.aggregation.model.PipelineTemplate;



@Repository
public interface PipelineRepository extends MongoRepository<PipelineTemplate, String>, CustomPipelineRepository {


}
