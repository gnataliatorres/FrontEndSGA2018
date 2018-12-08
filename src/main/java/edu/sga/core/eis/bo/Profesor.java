
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
public class Profesor implements Serializable{
    private Long codigoProfesor;
    private String apellidos;
    private Date fechaNacimiento;
    private String nombres;
    private Integer numPeriodos;
    
}
