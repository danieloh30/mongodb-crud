package io.quarkus.sample;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(database = "todo", collection = "todo")
public class Todo extends PanacheMongoEntity {

    public String title;

    public boolean completed;

    public int order;

    public String url;

    public double anumber;
}
