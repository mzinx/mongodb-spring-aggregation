package org.mongodb.dao.impl;

import org.mongodb.dao.CustomPipelineRepository;
import org.mongodb.model.PipelineTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.model.Filters;

public class PipelineRepositoryImpl implements CustomPipelineRepository {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MongoTemplate mongoTemplate;

    public PipelineTemplate findByName(String name) {
        PipelineTemplate p = mongoTemplate.getCollection(mongoTemplate.getCollectionName(PipelineTemplate.class))
                .withDocumentClass(PipelineTemplate.class).find(Filters.eq("_id", name)).first();
        return p;
    }
}
