/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sga.core.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.sga.core.eis.bo.Profesor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Natalia
 */
public class ProfesorBean implements Serializable{
    private ArrayList <Profesor> profesores;
    private Profesor profesorSeleccionado;

    public ProfesorBean() {
    }
    
     @PostConstruct 
    public void inicializar(){
        try{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet("http://localhost:9300/api/v1/curso");  //URL del API que se desea consumir
        getRequest.addHeader("accept", "application/json");
        HttpResponse response = httpClient.execute(getRequest);
        if(response.getStatusLine().getStatusCode() != 200){
            throw new RuntimeException("Failed: HTTP error code:" + response.getStatusLine().getStatusCode());
        }
        BufferedReader br= new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
        JsonParser jsonParser = new JsonParser();
        JsonArray arrayFromString = null;
        String output = "";
        while((output = br.readLine()) != null){
            arrayFromString = jsonParser.parse(output).getAsJsonArray();
            profesores = new ArrayList<Profesor>();
        }
        Gson gson = new GsonBuilder().create();
        for(JsonElement elemento: arrayFromString){
            profesores.add(gson.fromJson(elemento, Profesor.class));
        }
        httpClient.getConnectionManager().shutdown();
        }catch(ClientProtocolException e){
            e.printStackTrace();
        }
        catch(Exception e ){
            e.printStackTrace();
        }
    }
    public void editListener(RowEditEvent event){
        Profesor profesor = (Profesor)event.getObject();
        for(int index = 0; index < profesores.size(); index++){
            if(profesores.get(index).getCodigoProfesor() == profesor.getCodigoProfesor()){
                profesores.set(index,profesor);
                break;
            }
        }
    }
     
    public void reiniciarProfesorSeleccionado(){
        profesorSeleccionado = new Profesor();
    }
}
