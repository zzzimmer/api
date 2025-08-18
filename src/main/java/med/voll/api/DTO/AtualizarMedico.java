package med.voll.api.DTO;

import jakarta.validation.constraints.NotNull;

public record AtualizarMedico(
        @NotNull
        Long id,
        String nome,
        String telefone,
        EnderecoDTO endereco
) {
}
