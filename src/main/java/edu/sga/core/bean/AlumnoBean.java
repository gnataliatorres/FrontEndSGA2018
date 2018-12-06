
package edu.sga.core.bean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.sga.core.eis.bo.Alumno;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.primefaces.event.RowEditEvent;
/**
 *
 * @author Natalia
 */
@ManagedBean(name="alumnoBean")
@RequestScoped
public class AlumnoBean {
    private ArrayList <Alumno> alumnos;
    private Alumno alumnoSeleccionado;
     public AlumnoBean(){
         
     }
    @PostConstruct 
    public void inicializar(){
        try{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet("http://localhost:9300/api/v1/alumno");  //URL del API que se desea consumir
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
            alumnos = new ArrayList<Alumno>();  //Se agregan los alumnos que se consulta del API
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        for(JsonElement elemento: arrayFromString){
            alumnos.add(gson.fromJson(elemento, Alumno.class));
        }
        httpClient.getConnectionManager().shutdown();
        }catch(ClientProtocolException e){
            e.printStackTrace();
        }
        catch(Exception e ){
            e.printStackTrace();
        }
        /*
        alumnos = new ArrayList<Alumno>();
        alumnos.add(new Alumno(1L,"Perez", "Juan", new Date(), "2018001"));
        alumnoSeleccionado = new Alumno();
        */
        
    }
    public void editListener(RowEditEvent event){
        Alumno alumno = (Alumno)event.getObject();
        for(int index = 0; index < alumnos.size(); index++){
            if(alumnos.get(index).getCodigoAlumno() == alumno.getCodigoAlumno()){
                 try{
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    System.out.println(gson.toJson(alumno));
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpPut putRequest = new HttpPut("http://localhost:9300/api/v1/alumno/" + alumno.getCodigoAlumno());
                    StringEntity registro = new StringEntity(gson.toJson(alumno),"utf-8");
                    registro.setContentType("application/json");
                    putRequest.setEntity(registro);
                    HttpResponse response = httpClient.execute(putRequest);
                    System.out.println(response.getStatusLine().getStatusCode());
                    /*if(response.getStatusLine().getStatusCode() == 200){
                        throw new RuntimeException("Failed: HTTP error code :" +
                            response.getStatusLine().getStatusCode());
                    }*/
                    httpClient.getConnectionManager().shutdown();
                    alumnos.set(index, alumno);
                }catch(ClientProtocolException e){
                    e.printStackTrace();                
                }catch(Exception e){
                    e.printStackTrace();
                }             
                break;
            }
        }
    }
    
    public void agregar(){
        alumnos.add(alumnoSeleccionado);
        alumnoSeleccionado = null;
    }
    
    public void eliminar(){
         try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpDelete deleteRequest = new HttpDelete("http://localhost:9300/api/v1/alumno/" + alumnoSeleccionado.getCodigoAlumno());
            HttpResponse response = httpClient.execute(deleteRequest);
            System.out.println(response.getStatusLine().getStatusCode());
            httpClient.getConnectionManager().shutdown();
            alumnos.remove(alumnoSeleccionado);
        }catch(ClientProtocolException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
         FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Success","Registro eliminado");
         FacesContext.getCurrentInstance().addMessage(null, message);
    }
    public void reiniciarAlumnoSeleccionado(){
        alumnoSeleccionado = new Alumno();
    }

    public ArrayList<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(ArrayList<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public Alumno getAlumnoSeleccionado() {
        return alumnoSeleccionado;
    }

    public void setAlumnoSeleccionado(Alumno alumnoSeleccionado) {
        this.alumnoSeleccionado = alumnoSeleccionado;
    }
    
    
}
