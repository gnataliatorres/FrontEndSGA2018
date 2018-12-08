package edu.sga.core.eis.bo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class GrupoAcademico implements Serializable{
    private Long codigoGrupoAcademico;
    private String descripcion;
}
