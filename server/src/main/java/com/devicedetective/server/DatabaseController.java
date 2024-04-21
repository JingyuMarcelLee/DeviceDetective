package com.devicedetective.server;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/data")
    public Document getData() {
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("devicedetective");
        Bson filter = Filters.eq("_id", new String("test"));
        MongoCursor<Document> cursor = collection.find(filter).iterator();
        if (cursor.hasNext()) {
            return cursor.next();
        } else {
            return new Document("error", "Data not found");
        }
    }
}