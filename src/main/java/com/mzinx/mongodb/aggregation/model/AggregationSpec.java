package com.mzinx.mongodb.aggregation.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonElement;
import org.bson.BsonNull;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.util.BsonUtils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AggregationSpec<T> {
    public static final String PLACEHOLDER_KEY = "_ph";
    private String collectionName;
    private List<? extends Bson> pipelineTemplate;
    private Class<T> documentClass;
    private Long permission;

    public static AggregationSpec<Document> of(String collectionName) {
        return of(collectionName, List.of());
    }

    public static AggregationSpec<Document> of(String collectionName, List<? extends Bson> pipelineTemplate) {
        return new AggregationSpec<Document>(collectionName, pipelineTemplate, Document.class, null);
    }

    public <NewT> AggregationSpec<NewT> withClass(Class<NewT> clazz) {
        return new AggregationSpec<NewT>(this.collectionName, this.pipelineTemplate, clazz, this.permission);
    }

    public AggregationSpec<T> withPermissionCheck(Long permission) {
        return new AggregationSpec<T>(this.collectionName, this.pipelineTemplate, this.documentClass, permission);
    }

    public BsonValue bindVariables() {
        return this.bindVariables(Optional.empty());
    }

    public BsonValue bindVariables(Optional<Map<String, Object>> variables) {
        if (this.pipelineTemplate == null)
            throw new RuntimeException("Empty pipeline template");
        return this.traverse(new BsonArray(this.pipelineTemplate.stream()
                .map(s -> (BsonValue) ((Bson) s).toBsonDocument()).collect(Collectors.toList())), variables.map(vs -> {
                    return vs.entrySet().stream().map(entry -> {
                        // Already-BSON values (e.g. a BsonDocument built from a
                        // change event) pass through unchanged, preserving BSON
                        // type fidelity for dotted-path placeholder resolution.
                        if (entry.getValue() instanceof BsonValue bv) {
                            return Map.entry(entry.getKey(), bv);
                        }
                        if (entry.getValue() instanceof Bson) {
                            return Map.entry(entry.getKey(),
                                    (BsonValue) ((Bson) entry.getValue())
                                            .toBsonDocument());
                        }
                        return Map.entry(entry.getKey(),
                                entry.getValue() != null ? BsonUtils.simpleToBsonValue(entry.getValue()) : BsonNull.VALUE);
                    }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                }).orElse(new HashMap<>()));
    }

    private BsonValue traverse(BsonValue bson, Map<String, BsonValue> map) {
        Function<BsonValue, BsonValue> replaceValue = (value) -> {
            if (value.isDocument()) {
                BsonDocument d = value.asDocument();
                if (d.containsKey(PLACEHOLDER_KEY)) {
                    return resolvePlaceholder(d.getString(PLACEHOLDER_KEY).getValue(), map);
                } else {
                    return traverse(value, map);
                }
            } else if (value.isArray()) {
                return traverse(value, map);
            } else {
                return value==null?new BsonNull():value;
            }
        };
        if (bson.isDocument()) {
            return new BsonDocument(bson.asDocument().entrySet().stream().map(entry -> {
                return Map.entry(entry.getKey(), replaceValue.apply(entry.getValue()));
            }).map(entry -> new BsonElement(entry.getKey(), entry.getValue())).toList());
        } else if (bson.isArray()) {
            return new BsonArray(bson.asArray().stream().map(replaceValue).toList());
        } else {
            return replaceValue.apply(bson);
        }
    }

    /**
     * Resolves a placeholder key against the bound variables.
     * <p>
     * A key is first looked up verbatim so flat keys (including keys that happen
     * to contain dots) keep working exactly as before. If there is no exact
     * match, the key is treated as a dotted path and walked into nested
     * documents — e.g. {@code fullDocument.customer.status} descends
     * {@code fullDocument} -> {@code customer} -> {@code status}. Any missing
     * segment yields {@link BsonNull}, so placeholders that don't resolve are
     * simply substituted with null (mirroring the original behavior).
     */
    private static BsonValue resolvePlaceholder(String key, Map<String, BsonValue> map) {
        if (map.containsKey(key))
            return map.get(key);
        if (key == null || key.indexOf('.') < 0)
            return new BsonNull();
        String[] path = key.split("\\.");
        BsonValue current = map.get(path[0]);
        for (int i = 1; i < path.length && current != null; i++) {
            if (!current.isDocument())
                return new BsonNull();
            current = current.asDocument().get(path[i]);
        }
        return current == null ? new BsonNull() : current;
    }
}
