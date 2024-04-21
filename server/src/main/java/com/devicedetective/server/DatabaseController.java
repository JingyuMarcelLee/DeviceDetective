package com.devicedetective.server;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

@RestController
@RequestMapping("/api")
public class DatabaseController {

    @Autowired
    private MongoClient mongoClient; // Autowire MongoClient bean configured in your Spring application
    private final MongoOperations mongoOp;

    public DatabaseController(MongoOperations mongoOp) {
        this.mongoOp = mongoOp;
    }

    @GetMapping("/data")
    public Document getData() {
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("devicedetective");
        Bson filter = Filters.eq("clientId", new String("test"));
        MongoCursor<Document> cursor = collection.find(filter).iterator();
        if (cursor.hasNext()) {
            return cursor.next();
        } else {
            return new Document("error", "Data not found");
        }
    }

    @PostMapping("/clear-collection")
    public void deleteAllDocumentsInCollection(String collectionName) {
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("devicedetective");
        mongoOp.remove(new Query(), "devicedetective");
    }


}