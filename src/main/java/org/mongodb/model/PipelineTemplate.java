package org.mongodb.model;

import java.util.LinkedHashMap;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "#{@aggregationProperties.pipelineCollection}")
public class PipelineTemplate {
    @Id
    @BsonId
    private String name;

    @Field("v")
    @BsonIgnore
    private List<LinkedHashMap<String, Object>> aggs;
    
    @BsonProperty("v")
    @Transient
    private List<BsonDocument> content;

}
