package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.DTO.AtualizarMedico;
import med.voll.api.DTO.DadosDetalhamentoMedico;
import med.voll.api.DTO.ListagemMedico;
import med.voll.api.DTO.MedicoDTO;
import med.voll.api.model.Medico;
import med.voll.api.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    MedicoRepository medicoRepository;

    @PostMapping("/cadastrar") // precisa retornar o 201
    @Transactional // explicitar transação
    public ResponseEntity cadastrar(@RequestBody @Valid MedicoDTO json, UriComponentsBuilder uriBuilder){
        var medico = new Medico(json);
        medicoRepository.save(medico);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        System.out.println(medico.getId());

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<ListagemMedico>> listar(@PageableDefault(size = 10, sort ={"nome"} ) Pageable paginacao){

        var page = medicoRepository.findAllByAtivoTrue(paginacao).map(m -> new ListagemMedico(m));

        return ResponseEntity.ok(page);
    }

    @PutMapping //usar url padrao
    @Transactional
    public ResponseEntity atualizar (@RequestBody @Valid AtualizarMedico json){
        var medico = medicoRepository.getReferenceById(json.id());
        medico.atualizarInformacoes(json);


        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

//    @DeleteMapping("/{id}")
//    @Transactional
//    public void excluirReal (@PathVariable Long id){
//        medicoRepository.deleteById(id);
//    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir (@PathVariable Long id){
        var medico = medicoRepository.getReferenceById(id);
        medico.excluir();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var medico = medicoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

}
