package br.com.styli.domain.usecase;

import br.com.styli.domain.dto.response.ComentarioResponse;
import br.com.styli.domain.exception.BusinessException;
import br.com.styli.domain.exception.ErrorCode;
import br.com.styli.domain.mapper.ComentarioMapper;
import br.com.styli.domain.model.Comentario;
import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.repository.ComentarioRepository;
import br.com.styli.domain.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ComentarioUseCase {

    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    public List<ComentarioResponse> findAll(Long idEmpresa){
        List<ComentarioResponse> comentarios =  new ArrayList<>();

        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new BusinessException(ErrorCode.EMPRESA_NOT_FOUND));

        for (Comentario cmt : empresa.getComentarios()){
            comentarios.add(ComentarioMapper.toResponse(cmt));
        }

        return comentarios ;
    }
}
