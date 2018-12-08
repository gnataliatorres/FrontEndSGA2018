package edu.sga.core.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.sga.core.eis.bo.Salon;
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

/**
 *
 * @author Natalia
 */
@ManagedBean(name="salonBean")
@RequestScoped
public class SalonBean {
    private ArrayList <Salon> salones;
    private Salon salonSeleccionado;

    public SalonBean() {
    }

    @PostConstruct 
    public void inicializar(){
        try{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet("http://localhost:9300/api/v1/salon");  //URL del API que se desea consumir
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
            salones = new ArrayList<Salon>();
        }
        Gson gson = new GsonBuilder().create();
        for(JsonElement elemento: arrayFromString){
            salones.add(gson.fromJson(elemento, Salon.class));
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
        Salon salon = (Salon)event.getObject();
        for(int index = 0; index < salones.size(); index++){
            if(salones.get(index).getCodigoSalon() == salon.getCodigoSalon()){
                salones.set(index,salon);
                break;
            }
        }
    }
     public void reiniciarSalonSeleccionado(){
        salonSeleccionado = new Salon();
    }

    public ArrayList<Salon> getSalones() {
        return salones;
    }

    public void setSalones(ArrayList<Salon> salones) {
        this.salones = salones;
    }

    public Salon getSalonSeleccionado() {
        return salonSeleccionado;
    }

    public void setSalonSeleccionado(Salon salonSeleccionado) {
        this.salonSeleccionado = salonSeleccionado;
    }
     
    
    
}
