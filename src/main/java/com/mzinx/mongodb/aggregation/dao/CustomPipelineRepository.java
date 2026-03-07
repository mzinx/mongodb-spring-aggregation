package com.mzinx.mongodb.aggregation.dao;

import com.mzinx.mongodb.aggregation.model.PipelineTemplate;

public interface CustomPipelineRepository {

    public PipelineTemplate findByName(String name);
}
