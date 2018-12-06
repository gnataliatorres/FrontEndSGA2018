package edu.sga.core.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.sga.core.eis.bo.Curso;
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
@ManagedBean(name="cursoBean")
@RequestScoped
public class CursoBean {
    private ArrayList <Curso> cursos;
    private Curso cursoSeleccionado;

    public CursoBean() {
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
            cursos = new ArrayList<Curso>();
        }
        Gson gson = new GsonBuilder().create();
        for(JsonElement elemento: arrayFromString){
            cursos.add(gson.fromJson(elemento, Curso.class));
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
        Curso curso = (Curso)event.getObject();
        for(int index = 0; index < cursos.size(); index++){
            if(cursos.get(index).getCodigoCurso() == curso.getCodigoCurso()){
                cursos.set(index,curso);
                break;
            }
        }
    }
     
    public void reiniciarCursoSeleccionado(){
        cursoSeleccionado = new Curso();
    }
    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(ArrayList<Curso> cursos) {
        this.cursos = cursos;
    }

    public Curso getCursoSeleccionado() {
        return cursoSeleccionado;
    }

    public void setCursoSeleccionado(Curso cursoSeleccionado) {
        this.cursoSeleccionado = cursoSeleccionado;
    }
    
    
    
}
