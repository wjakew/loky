/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all right reserved
 */
package com.jakubwawak.loky.database_engine;

 import com.jakubwawak.loky.LokyApplication;
import com.jakubwawak.loky.maintanance.ConsoleColors;
 import com.mongodb.*;
 import com.mongodb.client.MongoClient;
 import com.mongodb.client.MongoClients;
 import com.mongodb.client.MongoCollection;
 import com.mongodb.client.MongoDatabase;
 import com.mongodb.client.model.Sorts;
 import com.mongodb.client.result.DeleteResult;
 import com.mongodb.client.result.InsertOneResult;
 import com.mongodb.client.result.UpdateResult;
 import com.vaadin.flow.component.notification.Notification;
 import com.vaadin.flow.component.notification.NotificationVariant;
 import org.bson.BsonDocument;
 import org.bson.BsonInt64;
 import org.bson.Document;
 import org.bson.conversions.Bson;
 import org.bson.types.ObjectId;
 
 import com.mongodb.client.model.Filters;
 
 import java.time.LocalDateTime;
 import java.time.ZoneId;
 import java.util.ArrayList;
 import java.util.List;
import java.util.Random;
import java.util.UUID;
 
 /**
  * Object for connecting to MongoDB database
  */
 public class Database {
     public String database_url;
     public boolean connected;
     MongoClient mongoClient;
     MongoDatabase mongoDatabase;
     ArrayList<String> error_collection;
 
     /**
      * Constructor
      */
     public Database(){
         this.database_url = "";
         connected = false;
         error_collection = new ArrayList<>();
     }
 
     /**
      * Function for setting database URL
      * @param database_url
      */
     public void setDatabase_url(String database_url){
         this.database_url = database_url;
     }
 
     /**
      * Function for connecting to database
      * @return boolean
      */
     public void connect(){
         ServerApi serverApi = ServerApi.builder()
                 .version(ServerApiVersion.V1)
                 .build();
         MongoClientSettings settings = MongoClientSettings.builder()
                 .applyConnectionString(new ConnectionString(database_url))
                 .serverApi(serverApi)
                 .build();
         try{
             mongoClient = MongoClients.create(settings);
             MongoDatabase database = mongoClient.getDatabase("admin");
             // Send a ping to confirm a successful connection
             Bson command = new BsonDocument("ping", new BsonInt64(1));
             Document commandResult = database.runCommand(command);
             connected = true;
             mongoDatabase = mongoClient.getDatabase("db_loky");
             log("DB-CONNECTION","Connected succesffully with database - running application");
         }catch(MongoException ex){
             // catch error
             log("DB-CONNECTION-ERROR", "Failed to connect to database ("+ex.toString()+")");
             connected = false;
         }
     }
 
 
     /**
      * Function for getting collection from database
      * @param collection_name
      * @return MongoCollection<Document>
      */
     public MongoCollection<Document> getCollection(String collection_name){
         return mongoDatabase.getCollection(collection_name);
     }
 
         /**
      * Function for inserting document to collection
      * 
      * @param collectionName
      * @param document
      * @return int
      */
     public int insert(String collectionName, Document document) {
         try {
             InsertOneResult result = mongoDatabase.getCollection(collectionName).insertOne(document);
             if (result.getInsertedId() != null) {
                 log("DB-INSERT", "Inserted document to collection (" + result.getInsertedId() + ")");
                 return 1;
             } else {
                 log("DB-INSERT-ERROR", "Failed to insert document to collection");
                 return 0;
             }
         } catch (MongoException ex) {
             log("DB-INSERT-ERROR", "Failed to insert document to collection (" + ex.toString() + ")");
             return -1;
         }
     }
 
     /**
      * Function for updating document in collection
      * @param collectionName
      * @param id
      * @param document
      * @return int
      * 1 - success
      * 0 - failed
      * -1 - error
      */
     public int update(String collectionName, ObjectId id, Document document) {
         try{
             UpdateResult result = getCollection(collectionName).updateOne(Filters.eq("_id", id), new Document("$set", document));
             if (result.getModifiedCount() == 1){
                 log("DB-UPDATE", "Updated document in collection (" + id + ")");
                 return 1;
             }
             else{
                 log("DB-UPDATE-ERROR", "Failed to update document in collection (" + id + ")");
                 return 0;
             }
         } catch (Exception e) {
             log("DB-UPDATE-ERROR", e.getMessage());
             return -1;
         }
     }
 
     /**
      * Function for removing session
      * @param session_id
      * @return int
      * 1 - success
      * 0 - session not found
      */
     public int removeSession(String session_id){
         DeleteResult result = getCollection("sessions").deleteOne(Filters.eq("session_id", session_id));
         if (result.getDeletedCount() == 1){
             log("SESSION-REMOVE", "Session removed (" + session_id + ")");
             return 1;
         }
         return 0;
     }

     /**
      * Function for getting or creating loky configuration
      * @return Document
      */
     public Document getOrCreateLokyConfiguration(){
        try{
            MongoCollection<Document> collection = getCollection("loky_configuration");
            if (collection.countDocuments() == 0){
                Document document = new Document();
                document.put("loky_instance_name", "loky");
                document.put("loky_instance_description", "Loky is a web application for managing your data.");
                document.put("loky_instance_version", LokyApplication.version);
                document.put("loky_instance_created_at", LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString());
                collection.insertOne(document);
                log("LOKY_CONFIGURATION-CREATED", "Created loky_configuration collection with default configuration.");
                return document;
            }
            else{
                Document document = collection.find().first();
                log("LOKY_CONFIGURATION-FOUND", "Loky configuration found in database.");
                return document;
            }
        }catch(Exception ex){
            log("LOKY_CONFIGURATION-ERROR", "Failed to get or create loky configuration (" + ex.toString() + ")");
            return null;
        }
     }

     /**
      * Function for getting or creating loky admin password
      * @return String
      */
     public String getOrCreateLokyAdminPassword() {
         MongoCollection<Document> collection = getCollection("loky_secrets");
         try{
            if (collection.countDocuments() == 0) {
                // Collection does not exist, create it with a random password
                String randomPassword = generateRandomString(30);
                Document document = new Document("loky_admin_password", randomPassword);
                collection.insertOne(document);
                log("LOKY_SECRETS-CREATED", "Created loky_secrets collection with admin password.");
                log("LOKY_SECRETS-PASSWORD", "Loky admin password: " + randomPassword);
                return randomPassword;
            } else {
                // Collection exists, retrieve the password
                Document existingDocument = collection.find().first();
                if (existingDocument != null) {
                    log("LOKY_SECRETS-PASSWORD", "Loky admin password found in secrets collection");
                    return existingDocument.getString("loky_admin_password");
                }
            }
         }catch(Exception ex){
            log("LOKY_SECRETS-ERROR", "Failed to get or create loky admin password (" + ex.toString() + ")");
         }
         return null; // In case something goes wrong
     }

     /**
      * Function for generating random string
      * @param length
      * @return String
      */
     private String generateRandomString(int length) {
         String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
         StringBuilder result = new StringBuilder(length);
         Random random = new Random();
         for (int i = 0; i < length; i++) {
             result.append(characters.charAt(random.nextInt(characters.length())));
         }
         return result.toString();
     }
 
     /**
      * Function to manage user sessions
      * Checks if a user has 3 sessions and removes the oldest if so.
      * 
      * @param userId the ID of the user
      */
     void manageUserSessions(ObjectId userId) {
         List<Document> sessions = getCollection("sessions").find(Filters.eq("user_id", userId)).sort(Sorts.ascending("created_at")).into(new ArrayList<>());
         if (sessions.size() >= 3) {
             // Remove the oldest session
             Document oldestSession = sessions.get(0);
             getCollection("sessions").deleteOne(Filters.eq("session_id", oldestSession.getString("session_id")));
             log("SESSION-MANAGEMENT", "Removed oldest session for user (" + userId + ")");
         }
         else{
             log("SESSION-MANAGEMENT", "No sessions to remove for user (" + userId + "), amount of sessions: " + sessions.size());
         }
     }
 
         /**
      * Function for creating session
      * 
      * @param user
      * @return String
      */
     public String createSession(ObjectId user_id){
         String session_id = UUID.randomUUID().toString();
         while (getCollection("sessions").find(Filters.eq("session_id", session_id)).first() != null) {
             session_id = UUID.randomUUID().toString();
         }
         Document document = new Document();
         document.put("session_id", session_id);
         document.put("user_id", user_id);
         document.put("created_at", LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString());
         document.put("active", true);
         document.put("last_activity", LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString());
 
         if ( insert("sessions", document) == 1 ) {
             manageUserSessions(user_id);
             return session_id;
         } else {
             return null;
         }
     }
 
     /**
      * Function for creating log entry
      * @param log_category
      * @param log_text
      */
     private void createLogEntry(String log_category, String log_text){
         try{
             MongoCollection<Document> collection = mongoDatabase.getCollection("logs");
             Document logEntry = new Document("category", log_category)
                     .append("text", log_text)
                     .append("timestamp", LocalDateTime.now(ZoneId.of("Europe/Warsaw")));
             collection.insertOne(logEntry);
         }catch(Exception ex){
             System.out.println(ConsoleColors.RED_BRIGHT+"DB-LOG-ERROR"+"["+ LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString()+") - "+log_text+"]"+ConsoleColors.RESET);
         }
     }


     /**
      * Function for logging telemetry info
      * @param telemetry_data
      * @param telemetry_type
      */
     public void logTelemetryInfo(String telemetry_data, String telemetry_type){
        try{
            MongoCollection<Document> collection = mongoDatabase.getCollection("telemetry");
            Document telemetryEntry = new Document("data", telemetry_data)
                    .append("type", telemetry_type)
                    .append("timestamp", LocalDateTime.now(ZoneId.of("Europe/Warsaw")));
            InsertOneResult result = collection.insertOne(telemetryEntry);
            if (result.getInsertedId() != null){
                log("TELEMETRY-INFO", "Telemetry data logged (" + telemetry_data + ")");
            }
            else{
                log("TELEMETRY-ERROR", "Failed to log telemetry data (result: "+result.toString()+")");
            }
        }catch(Exception ex){
            log("TELEMETRY-ERROR", "Failed to log telemetry data (" + ex.toString() + ")");
        }
     }
 
     /**
      * Function for story log data
      * @param log_category
      * @param log_text
      */
     public void log(String log_category, String log_text){
         error_collection.add(log_category+"("+ LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString()+") - "+log_text);
         if ( log_category.contains("FAILED") || log_category.contains("ERROR")){
             System.out.println(ConsoleColors.RED_BRIGHT+log_category+"["+ LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString()+") - "+log_text+"]"+ConsoleColors.RESET);
             try{
                 Notification noti = Notification.show(log_text);
                 noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
 
             }catch(Exception ex){}
         }
         else{
             System.out.println(ConsoleColors.GREEN_BRIGHT+log_category+"["+ LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString()+") - "+log_text+"]"+ConsoleColors.RESET);
         }
         if ( connected ){
             createLogEntry(log_category, log_text);
         }
     }
 }