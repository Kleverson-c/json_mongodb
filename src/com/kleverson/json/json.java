package com.kleverson.json;

import org.bson.Document;

import static com.mongodb.client.model.Filters.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;



public class json {

    private static HttpURLConnection connection;
    static String json;

    public static void main(String[] args) {
        MongoDB.DBconnect();
        Document Doc = getAgenda();
        InsertCheck("agendas", Doc);
        MongoDB.DocumentCount("agendas");

        new Column()
                .addLine("id", "nome", "esfera")
                .addLine(Doc.getInteger("_id").toString(), Doc.getString("nome"), Doc.getString("esfera"))
                .print()
        ;

    }

    private static void InsertCheck(String CollectionName, Document Doc){
        if (MongoDB.findID(CollectionName, Doc.getInteger("_id")) != null) {
            MongoDB.DeleteOne(CollectionName, eq("_id", Doc.getInteger("_id")));

        }
        MongoDB.insertOne("agendas",Doc);
    }
    private static Document getAgenda(){
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        Document agenda = new Document();

        //GET JSON AS STRING
        try {

            URL url = new URL(
                    "https://ics.multieditoras.com.br/ics/agenda/1/2017/12?chave=TFACS-PD6L7-WG5ZF-Q9WU9&cliente=10378405&compacto=0&formato=json");
            connection = (HttpURLConnection) url.openConnection();

            int status = connection.getResponseCode();

            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }

            //CONVERTING FROM STRING TO JSON OBJECT
            json = responseContent.toString();
            JSONObject obj = new JSONObject(json);

            //READING JSON ARRAY
            JSONArray arr = obj.getJSONArray("agenda");
            //for (int i = 0; i < arr.length(); i++) {
            Number id = arr.getJSONObject(0).getNumber("@id");
            String nome = arr.getJSONObject(0).getString("nome");
            String esfera = arr.getJSONObject(0).getString("esfera");

            agenda = new Document("_id", id)
                    .append("nome", nome)
                    .append("esfera", esfera);
            // }





        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return agenda;

    }
}