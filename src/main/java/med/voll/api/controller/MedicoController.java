package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.DTO.ListagemMedico;
import med.voll.api.DTO.MedicoDTO;
import med.voll.api.model.Medico;
import med.voll.api.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    MedicoRepository medicoRepository;

    @PostMapping("/cadastrar")
    @Transactional // explicitar transação
    public void cadastrar(@RequestBody @Valid MedicoDTO json ){
        medicoRepository.save(new Medico(json));
    }

    @GetMapping("/listar")
    public Page<ListagemMedico> listar(@PageableDefault(size = 10, sort ={"nome"} ) Pageable paginacao){
        return medicoRepository.findAll(paginacao).map(m -> new ListagemMedico(m));
    }
}
