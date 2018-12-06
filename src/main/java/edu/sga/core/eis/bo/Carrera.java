
package edu.sga.core.eis.bo;

import java.io.Serializable;
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
public class Carrera implements Serializable{
    private Long codigoCarrera;
    private String descripcion;
    
}
