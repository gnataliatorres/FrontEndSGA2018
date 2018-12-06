
package edu.sga.core.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.sga.core.eis.bo.Alumno;
import edu.sga.core.eis.bo.Carrera;
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
@ManagedBean(name="carreraBean")
@RequestScoped
public class CarreraBean {
    private ArrayList <Carrera> carreras;
    private Carrera carreraSeleccionada;
    public CarreraBean() {
    }
     @PostConstruct 
    public void inicializar(){
        try{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet("http://localhost:9300/api/v1/carrera");  //URL del API que se desea consumir
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
            carreras = new ArrayList<Carrera>();
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        for(JsonElement elemento: arrayFromString){
            carreras.add(gson.fromJson(elemento, Carrera.class));
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
        Carrera carrera = (Carrera)event.getObject();
        for(int index = 0; index < carreras.size(); index++){
            if(carreras.get(index).getCodigoCarrera() == carrera.getCodigoCarrera()){
                carreras.set(index,carrera);
                break;
            }
        }
    }
     
    public void reiniciarCarreraSeleccionada(){
        carreraSeleccionada = new Carrera();
    }

    public ArrayList<Carrera> getCarreras() {
        return carreras;
    }

    public void setCarreras(ArrayList<Carrera> carreras) {
        this.carreras = carreras;
    }

    public Carrera getCarreraSeleccionada() {
        return carreraSeleccionada;
    }

    public void setCarreraSeleccionada(Carrera carreraSeleccionada) {
        this.carreraSeleccionada = carreraSeleccionada;
    }
     
     
     
    
    
}
