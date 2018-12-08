package edu.sga.core.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.sga.core.eis.bo.GrupoAcademico;
import edu.sga.core.eis.bo.GrupoAcademico;
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

@ManagedBean(name="grupoacademicoBean")
@RequestScoped
public class GrupoAcademicoBean {
    private ArrayList <GrupoAcademico> gruposacademicos;
    private GrupoAcademico grupoAcademicoSeleccionado;

    public GrupoAcademicoBean() {
    }
    
    
     @PostConstruct 
    public void inicializar(){
        try{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet("http://localhost:9300/api/v1/grupoacademico");  //URL del API que se desea consumir
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
            gruposacademicos = new ArrayList<GrupoAcademico>();  //Se agregan los alumnos que se consulta del API
        }
        Gson gson = new GsonBuilder().create();
        for(JsonElement elemento: arrayFromString){
            gruposacademicos.add(gson.fromJson(elemento, GrupoAcademico.class));
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
        GrupoAcademico grupoacademico = (GrupoAcademico)event.getObject();
        for(int index = 0; index < gruposacademicos.size(); index++){
            if(gruposacademicos.get(index).getCodigoGrupoAcademico() == grupoacademico.getCodigoGrupoAcademico()){
                gruposacademicos.set(index, grupoacademico);
                break;
            }
        }
    }
    
     public void reiniciarGrupoAcademicoSeleccionado(){
        grupoAcademicoSeleccionado = new GrupoAcademico();
    }

    public ArrayList<GrupoAcademico> getGruposacademicos() {
        return gruposacademicos;
    }

    public void setGruposacademicos(ArrayList<GrupoAcademico> gruposacademicos) {
        this.gruposacademicos = gruposacademicos;
    }

    public GrupoAcademico getGrupoAcademicoSeleccionado() {
        return grupoAcademicoSeleccionado;
    }

    public void setGrupoAcademicoSeleccionado(GrupoAcademico grupoAcademicoSeleccionado) {
        this.grupoAcademicoSeleccionado = grupoAcademicoSeleccionado;
    }
     
    
}
