
package edu.sga.core.eis.bo;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *
 * @author Natalia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alumno implements Serializable{
    private Long codigoAlumno;
    private String apellidos;
    private String nombres;
    private Date fechaNacimiento;
    private String carne;
}
