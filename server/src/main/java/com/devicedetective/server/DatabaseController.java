package com.devicedetective.server;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


/**
 * REST controller that handles database operations for the 'devicedetective' collection.
 * It exposes methods to retrieve and delete data via HTTP requests.
 * Exposes APIs to retrieve and delete data via HTTP requests. Primarily used for development and testing purposes.
 *
 * "@RestController": Annotation that is used to define a controller and indicate that the return value of the methods
 * should be bound to the web response body.
 *
 * "@RequestMapping("/api")": Annotation that is used to map web requests to Spring Controller methods. Here it defines
 * the base path for all methods in this controller.
 *
 * "@Autowired" is a Spring annotation used to automatically inject dependencies into fields, methods, or constructors.
 * It simplifies configuration by managing beans and dependencies declaratively, reducing boilerplate code and
 * ensuring loose coupling in applications.
 */
@RestController
@RequestMapping("/api")
public class DatabaseController {

    /**
     * MongoClient is automatically injected by Spring into this controller.
     */
    @Autowired
    private MongoClient mongoClient; // Autowire MongoClient bean configured in your Spring application

    /**
     * Spring Data MongoDB operations helper, provides a more abstract way to interact with the database.
     */
    private final MongoOperations mongoOp;


    /**
     * Constructor to MongoOperations, simplifying the operations like insert, delete, and query.
     * @param mongoOp Spring Data MongoDB's template for operations.
     */
    public DatabaseController(MongoOperations mongoOp) {
        this.mongoOp = mongoOp;
    }


    /**
     * HTTP GET method to retrieve data. Maps to "/api/data" and returns the first document found.
     * Uses MongoDB client to execute the find operation.
     *
     * "@GetMapping": Annotation for mapping HTTP GET requests onto specific handler methods.
     * "@return": A Document from MongoDB or an error message if no data is found.
     */
    @GetMapping("/data")
    public Document getData() {
        // Get MongoDB collection (database).
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("devicedetective");

        // Find and return the most recent document with the same clientId.
        Bson filter = Filters.eq("clientId", new String("test"));
        MongoCursor<Document> cursor = collection.find(filter).iterator();
        if (cursor.hasNext()) {
            return cursor.next();
        } else {
            return new Document("error", "Data not found");
        }
    }

    /**
     * HTTP GET method to retrieve all documents from the 'devicedetective' collection that match the given clientId.
     * Maps to "/api/{clientId}" and returns all documents from the 'devicedetective' collection.
     *
     * @return A list of Documents or an error message if no data is found.
     */
    @GetMapping("/data/{clientId}")
    public List<Document> getAllDataByClientId(@PathVariable String clientId) {
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("devicedetective");
        Bson filter = Filters.eq("clientId", clientId);
        List<Document> documents = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find(filter).iterator()) {
            while (cursor.hasNext()) {
                documents.add(cursor.next());
            }
        }
        if (documents.isEmpty()) {
            documents.add(new Document("error", "No data found for clientId: " + clientId));
        }
        return documents;
    }

    /**
     * HTTP POST method to add a new document to the 'devicedetective' collection.
     * Maps to "/api/add" and adds the document.
     *
     * @param document A JSON body of the document to add.
     */
    @PostMapping("/add")
    public Document addDocument(@RequestBody Document document) {
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("devicedetective");
        collection.insertOne(document);
        return new Document("success", "Document added successfully");
    }

    /**
     * HTTP POST method to delete all documents in a collection. Maps to "/api/clear-collection".
     * This method resets the entire MongoDB database.
     *
     * "@PostMapping": Annotation for mapping HTTP POST requests onto specific handler methods.
     * @param collectionName The name of the collection to clear, obtained from the HTTP request.
     */
    @PostMapping("/clear-collection")
    public void deleteAllDocumentsInCollection(String collectionName) {
        // Get MongoDB collection (database).
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("devicedetective");

        // Clears all documents from the specified collection
        mongoOp.remove(new Query(), "devicedetective");
    }
}