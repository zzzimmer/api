package med.voll.api.model;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.DTO.EnderecoDTO;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    String logradouro;
    String numero;
    String complemento;
    String bairro;
    String cidade;
    String uf;
    String cep;

    public Endereco(EnderecoDTO endereco) {
        this.logradouro = endereco.logradouro();
        this.numero = endereco.numero();
        this.complemento = endereco.complemento();
        this.bairro = endereco.bairro();
        this.cidade = endereco.cidade();
        this.uf = endereco.uf();
        this.cep = endereco.cep();
    }

    public void atualizarInformacoes(EnderecoDTO endereco) {
        // Verifica se o DTO não é nulo para evitar NullPointerException
        if (endereco != null) {
            // Atualiza os campos do objeto Endereco atual com os valores do DTO
            // A lógica aqui é permitir atualizações parciais,
            // ou seja, só atualiza o campo se um novo valor foi enviado no DTO.
            if (endereco.logradouro() != null) {
                this.logradouro = endereco.logradouro();
            }
            if (endereco.numero() != null) {
                this.numero = endereco.numero();
            }
            if (endereco.complemento() != null) {
                this.complemento = endereco.complemento();
            }
            if (endereco.bairro() != null) {
                this.bairro = endereco.bairro();
            }
            if (endereco.cidade() != null) {
                this.cidade = endereco.cidade();
            }
            if (endereco.uf() != null) {
                this.uf = endereco.uf();
            }
            if (endereco.cep() != null) {
                this.cep = endereco.cep();
            }
        }
    }

}
