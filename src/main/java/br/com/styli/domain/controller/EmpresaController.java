package br.com.styli.domain.controller;

import br.com.styli.domain.dto.request.AgendamentoDinamicoRequest;
import br.com.styli.domain.dto.request.ReservarHorarioRequest;
import br.com.styli.domain.dto.response.AgendamentoResponse;
import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {


        @Autowired
        EmpresaService empresaService;

        @GetMapping
        public ResponseEntity<List<Empresa>> findAll(){
            List<Empresa> empresaList = empresaService.findAll();
            return ResponseEntity.status(200).body(empresaList);
        }


        @GetMapping("/{id}")
        public ResponseEntity<Empresa> findByID(@PathVariable Long id){
            Empresa empresa = empresaService.findById(id);
            return ResponseEntity.status(200).body(empresa);
        }

        @GetMapping("/home")
        public ResponseEntity<List<Empresa> > findByDestaque(){
            List<Empresa>  empresas = empresaService.findByDestaque();
            return ResponseEntity.status(200).body(empresas);
        }

        @PostMapping("/create")
        public ResponseEntity <Empresa> save(@RequestBody Empresa empresas){
            Empresa empresa = empresaService.save(empresas);
            return ResponseEntity.status(201).body(empresas);
        }

        @PostMapping("/{id}/create-func")
        public ResponseEntity <Empresa> createFunc(@PathVariable Long empresaid, @RequestBody Funcionario funcionario){
            Empresa empresa = empresaService.salvarFuncionario(empresaid,funcionario);
            return ResponseEntity.status(201).body(empresa);
        }

        public ResponseEntity<List<Empresa>> findAllByCategoria(@RequestParam(required = false) Long categoriaId){
            List<Empresa> empresas = empresaService.findAllByCategoria(categoriaId);
            return ResponseEntity.status(201).body(empresas);
        }

    @GetMapping("{idEmpresa}/funcionarios/{idFuncionario}/horarios-disponiveis")
    public ResponseEntity<List<LocalTime>> buscarHorariosDisponiveis(
            @PathVariable Long idEmpresa,
            @PathVariable Long idFuncionario,
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam("duracaoMinutos") int duracaoMinutos) {

            List<LocalTime> horarios = empresaService.buscarHorariosDisponiveis(idEmpresa, idFuncionario,
                    data,duracaoMinutos);

        return ResponseEntity.ok(horarios);
    }


    @PostMapping("{idEmpresa}/funcionarios/{idFuncionario}/agendamentos")
    public ResponseEntity<AgendamentoResponse> reservarHorario( @PathVariable Long idEmpresa,
                                                                @PathVariable Long idFuncionario,
                                                                @RequestBody ReservarHorarioRequest request){
        AgendamentoResponse agendamentoResponse = empresaService.reservarHorario(idEmpresa, idFuncionario, request);

        return ResponseEntity.ok(agendamentoResponse);
    }

    @PostMapping("/agendamentos/dinamico")
    public ResponseEntity<AgendamentoResponse> agendarAleatoriamente(
            @PathVariable Long idEmpresa,
            @RequestBody AgendamentoDinamicoRequest request
    ) {
        AgendamentoResponse agendamentoResponse = empresaService.agendarAleatoriamente(idEmpresa, request);

        return ResponseEntity.ok(agendamentoResponse);
    }




}
