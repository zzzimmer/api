package med.voll.api.DTO;

import med.voll.api.model.EEspecialidade;
import med.voll.api.model.Endereco;
import med.voll.api.model.Medico;

public record DadosDetalhamentoMedico(Long id, String nome, String email, String crm, String telefone, EEspecialidade especialidade, Endereco endereco) {

    public DadosDetalhamentoMedico(Medico medico) {
        this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getTelefone(), medico.getEspecialidade(), medico.getEndereco());
    }
}