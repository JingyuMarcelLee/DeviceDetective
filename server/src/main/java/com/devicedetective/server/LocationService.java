package com.devicedetective.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mongodb.client.model.Filters;


/**
 * Service class that manages the storage and retrieval of Location data from a MongoDB database.
 * Provides functionality to insert new location records and to fetch locations by client ID.
 */
@Service
public class LocationService {

    /**
     * Injected MongoDB client to interact with the database
     */
    @Autowired
    private MongoClient mongoClient;

    /**
     * Database name for location data in MongoDB
     */
    private static final String DATABASE_NAME = "test";

    /**
     * Collection name for location data in MongoDB
     */
    private static final String COLLECTION_NAME = "devicedetective";

    /**
     * Saves a new location to the database.
     * Creates a new document (or table row) and inserts it into the MongoDB collection (database).
     *
     * @param location The Location object containing data to be stored.
     */
    public void saveLocation(Location location) {
        // Find the MongoDB collection (database).
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        // Create new MongoDB document (table row) and insert into the database.
        Document doc = new Document("clientId", location.getClientId())
                .append("latitude", location.getLatitude())
                .append("longitude", location.getLongitude());
        collection.insertOne(doc);
    }

    /**
     * Retrieves a location from the database by client ID.
     * If no matching location is found, returns a document indicating an error.
     *
     * @param clientId The client ID to search for in the database.
     * @return Document containing the location data or an error message.
     */
    public Document findLocationByClientId(String clientId) {
        // Find the MongoDB collection (database).
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("devicedetective");

        // Find the most recent location of the client in the database and return.
        Bson filter = Filters.eq("clientId", clientId);
        Document found = collection.find(filter).first();
        if (found != null) {
            return found;
        } else {
            return new Document("error", "Data not found");
        }
    }
}
