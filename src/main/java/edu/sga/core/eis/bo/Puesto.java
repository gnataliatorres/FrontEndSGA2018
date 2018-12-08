package edu.sga.core.eis.bo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Puesto implements Serializable{
    private Long codigoPuesto;
    private String descripcion;
}
