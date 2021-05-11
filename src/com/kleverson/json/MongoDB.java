package com.kleverson.json;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import org.bson.conversions.Bson;


public class MongoDB {

    public static void insertOne(String CollectionName, Document Doc){
        SelCollection(CollectionName).insertOne(Doc);
        System.out.println("Document added to the collection.");
    }

    public static MongoDatabase DBconnect(){
        MongoClient client = MongoClients.create();
        MongoDatabase database = client.getDatabase("myDB");
        return database;
    }

    public static MongoCollection<Document> SelCollection(String Name){
        MongoCollection<Document> collection = DBconnect().getCollection(Name);
        return collection;
    }

    public static void DocumentCount(String CollectionName){
        long number = SelCollection(CollectionName).countDocuments();
        System.out.println("Documents in collection: "+number);
    }

    public static void DeleteMany(String CollectionName, Bson Criteria){
        DeleteResult deleteResult = SelCollection(CollectionName).deleteMany(Criteria);
        System.out.println(deleteResult.getDeletedCount());
    }

    public static void DeleteOne(String CollectionName, Bson Criteria){
        SelCollection(CollectionName).deleteOne(Criteria);
        System.out.println("Document deleted");
    }

    public static Document findID(String CollectionName, long Value){
        Document Doc = SelCollection(CollectionName).find(eq("_id",Value)).first();
        return Doc;
    }


}