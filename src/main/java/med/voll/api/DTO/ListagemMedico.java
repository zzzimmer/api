package med.voll.api.DTO;

import med.voll.api.model.EEspecialidade;
import med.voll.api.model.Medico;

public record ListagemMedico(
        
        String nome,
        String email,
        String crm,
        EEspecialidade especialidade
) {
    public ListagemMedico(Medico m) {
        this(m.getNome(),
                m.getEmail(),
                m.getCrm(),
                m.getEspecialidade());
    }
}
