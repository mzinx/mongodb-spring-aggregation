# MongoDB Spring Aggregation

A Spring Boot starter library that provides advanced MongoDB aggregation capabilities with template support, variable substitution, permission-based access control, and pagination.

## Features

- **Pipeline Templates**: Define reusable MongoDB aggregation pipelines with placeholders for dynamic variables
- **Variable Substitution**: Seamlessly replace placeholders in pipelines with runtime values
- **Permission Checks**: Integrate with Spring Security for fine-grained access control on aggregation results
- **Pagination Support**: Built-in pagination for handling large result sets efficiently
- **Template Storage**: Store and manage pipeline templates in MongoDB collections
- **Auto-Configuration**: Zero-configuration setup with Spring Boot auto-configuration

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.mzinx</groupId>
    <artifactId>mongodb-spring-aggregation</artifactId>
    <version>0.0.3</version>
</dependency>
```

## Configuration

The library can be configured using the following properties in your `application.properties` or `application.yml`:

```properties
# Enable/disable the aggregation functionality (default: true)
aggregation.enabled=true

# MongoDB collection name for storing pipeline templates (default: _pipelines)
aggregation.pipelineCollection=_pipelines
```

## Usage

### Basic Aggregation Execution

```java
@Autowired
private AggregationService aggregationService;

// Create a simple aggregation
Aggregation<Document> agg = Aggregation.of("yourCollection", pipeline);

// Execute the aggregation
List<Document> results = aggregationService.execute(agg);
```

### Aggregation with Variables

```java
// Define pipeline with placeholders
List<Bson> pipeline = List.of(
    Aggregates.match(Filters.eq("status", Map.of("_ph", "statusVar")))
);

// Create aggregation
Aggregation<Document> agg = Aggregation.of("yourCollection", pipeline);

// Execute with variables
Map<String, Object> variables = Map.of("statusVar", "active");
List<Document> results = aggregationService.execute(agg, variables);
```

### Aggregation with Pagination

```java
Pageable pageable = PageRequest.of(0, 10);
Page<Document> page = aggregationService.execute(agg, pageable, variables);
```

### Aggregation with Permission Checks

```java
// Add permission check (requires Spring Security)
Aggregation<Document> agg = Aggregation.of("yourCollection", pipeline)
    .withPermissionCheck(1L); // permission bit mask

List<Document> results = aggregationService.execute(agg);
```

### Working with Pipeline Templates

```java
@Autowired
private PipelineRepository pipelineRepository;

// Save a pipeline template
PipelineTemplate template = PipelineTemplate.builder()
    .name("userSummary")
    .aggs(List.of(
        Map.of("$match", Map.of("active", true)),
        Map.of("$group", Map.of("_id", "$department", "count", Map.of("$sum", 1)))
    ))
    .build();

pipelineRepository.save(template);

// Retrieve and use template
PipelineTemplate saved = pipelineRepository.findByName("userSummary");
// Convert to Aggregation and execute
```

### Custom Document Types

```java
// Use with custom POJO
Aggregation<UserSummary> agg = Aggregation.of("users", pipeline)
    .withClass(UserSummary.class);

List<UserSummary> results = aggregationService.execute(agg);
```

## Permission System

The library integrates with Spring Security to provide row-level security on aggregation results. When permission checks are enabled:

- The current user's ID is extracted from `SecurityContextHolder`
- A lookup is performed against a `permission` collection
- Results are filtered based on permission bit masks

Ensure you have a `permission` collection with documents containing `eId` (entity ID), `uId` (user ID), and `p` (permission bits).

## License

This project is licensed under the Apache License, Version 2.0 - see the [LICENSE](LICENSE) file for details.
