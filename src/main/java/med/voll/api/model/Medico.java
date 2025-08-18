package med.voll.api.model;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.DTO.AtualizarMedico;
import med.voll.api.DTO.MedicoDTO;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id") //todo o que é isso?
public class Medico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String crm;
    @Enumerated(EnumType.STRING)
    private EEspecialidade especialidade;
    @Embedded // todo ver como isso funciona + exige notação em Endereco
    private Endereco endereco;

    private Boolean ativo;


    public Medico(MedicoDTO json) {
        this.ativo = true;
        this.nome = json.nome();
        this.email = json.email();
        this.telefone = json.telefone();
        this.crm = json.crm();
        this.endereco = new Endereco(json.endereco());
        this.especialidade = json.especialidade();
    }

    public void atualizarInformacoes(@Valid AtualizarMedico json) {
        if (json.nome() != null){
            this.nome = json.nome();
        }
        if (json.telefone() != null){
            this.telefone = json.telefone();
        }
        if (json.endereco() != null){
            this.endereco.atualizarInformacoes(json.endereco());
        }

    }

    public void excluir() {
        this.ativo = false;
    }
}
