package org.mongodb.dao;

import org.mongodb.model.PipelineTemplate;

public interface CustomPipelineRepository {

    public PipelineTemplate findByName(String name);
}
