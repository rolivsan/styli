package br.com.styli.domain.controller;

import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("styli/empresa")
public class EmpresaController {


        @Autowired
        EmpresaService empresaService;

        @GetMapping
        public List<Empresa> findAll(){
            List<Empresa> empresaList = empresaService.findAll();
            return empresaList;
        }

//        @GetMapping("{/empresa/id}")
//        public ResponseEntity<Empresa> findByID(@PathVariable Long id){
//            Cliente Empresa = empresaService.findById(id);
//            return  ResponseEntity.status(200).body(Empresa);
//        }
//
//        @PostMapping("{/create}")
//        public ResponseEntity <Empresa> save(@RequestBody Empresa empresas){
//            Empresa empresas = empresaService.save(empresas);
//            return ResponseEntity.status(201).body(empresas);
//        }
}
