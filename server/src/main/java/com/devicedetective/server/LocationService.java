package com.devicedetective.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mongodb.client.model.Filters;

@Service
public class LocationService {

    @Autowired
    private MongoClient mongoClient;

    public void saveLocation(Location location) {
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("devicedetective");

        Document doc = new Document("clientId", location.getClientId())
                .append("latitude", location.getLatitude())
                .append("longitude", location.getLongitude());
        collection.insertOne(doc);
    }

    public Document findLocationByClientId(String clientId) {
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("devicedetective");
        Bson filter = Filters.eq("clientId", clientId);
        Document found = collection.find(filter).first();
        if (found != null) {
            return found;
        } else {
            return new Document("error", "Data not found");
        }
    }
}
