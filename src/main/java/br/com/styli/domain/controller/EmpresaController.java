package br.com.styli.domain.controller;

import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {


        @Autowired
        EmpresaService empresaService;

        @GetMapping
        public List<Empresa> findAll(){
            List<Empresa> empresaList = empresaService.findAll();
            return empresaList;
        }

        @GetMapping("/{id}")
        public ResponseEntity<Empresa> findByID(@PathVariable Long id){
            Empresa empresa = empresaService.findById(id);
            return  ResponseEntity.status(200).body(empresa);
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

}
