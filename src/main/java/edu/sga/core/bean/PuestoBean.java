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

import edu.sga.core.eis.bo.Puesto;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.primefaces.event.RowEditEvent;

@ManagedBean(name="puestoBean")
@RequestScoped
public class PuestoBean {
    private ArrayList <Puesto> puestos;
    private Puesto puestoSeleccionado;

    public PuestoBean() {
    }
    
    @PostConstruct 
    public void inicializar(){
        try{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet("http://localhost:9300/api/v1/puesto");  //URL del API que se desea consumir
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
            puestos = new ArrayList<Puesto>();
        }
        Gson gson = new GsonBuilder().create();
        for(JsonElement elemento: arrayFromString){
            puestos.add(gson.fromJson(elemento, Puesto.class));
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
        Puesto puesto = (Puesto)event.getObject();
        for(int index = 0; index < puestos.size(); index++){
            if(puestos.get(index).getCodigoPuesto() == puesto.getCodigoPuesto() ){
                puestos.set(index,puesto);
                break;
            }
        }
    }
    public void reiniciarPuestoSeleccionado(){
        puestoSeleccionado = new Puesto();
    }

    public ArrayList<Puesto> getPuestos() {
        return puestos;
    }

    public void setPuestos(ArrayList<Puesto> puestos) {
        this.puestos = puestos;
    }

    public Puesto getPuestoSeleccionado() {
        return puestoSeleccionado;
    }

    public void setPuestoSeleccionado(Puesto puestoSeleccionado) {
        this.puestoSeleccionado = puestoSeleccionado;
    }
    
    
}
