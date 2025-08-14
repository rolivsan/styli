package br.com.styli.service;

import br.com.styli.dto.request.BuscarEmpresasRequest;
import br.com.styli.dto.request.CriarEmpresaRequest;
import br.com.styli.dto.request.CriarEmpresaServicoRequest;
import br.com.styli.dto.request.VincularFuncionarioRequest;
import br.com.styli.dto.response.EmpresaDetalheResponse;
import br.com.styli.dto.response.EmpresaResponse;
import br.com.styli.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final BuscarEmpresaPorIdUseCase buscarEmpresaPorIdUseCase;
    private final ListarFuncionariosDaEmpresaUseCase listarFuncionariosDaEmpresaUseCase;
    private final CriarEmpresaUseCase criarEmpresaUseCase;
    private final CriarEmpresaServicoUseCase criarEmpresaServicoUseCase;
    private final VincularFuncionarioEmpresaUseCase vincularFuncionarioEmpresaUseCase;
    private final ListarServicosDaEmpresaUseCase listarServicosDaEmpresaUseCase;
    private final ListarServicosComFuncionariosUseCase listarServicosComFuncionariosUseCase;
    private final BuscarEmpresasUseCase buscarEmpresasUseCase;

    public EmpresaDetalheResponse buscarPorId(Long id) {
        // 1) Busca informações básicas da empresa
        var base = buscarEmpresaPorIdUseCase.executar(id);

        // 2) Adiciona lista simples de funcionários
        var funcionarios = listarFuncionariosDaEmpresaUseCase.executar(id);
        base.setFuncionarios(funcionarios);

        // 3) Adiciona lista detalhada de serviços com funcionários
        var servicosComFuncs = listarServicosComFuncionariosUseCase.executar(id);
        base.setServicosComFuncionarios(servicosComFuncs);

        return base;
    }


    public EmpresaResponse criarEmpresa(CriarEmpresaRequest req) {
        return criarEmpresaUseCase.executar(req);
    }

    public EmpresaDetalheResponse.ServicoResumo criarServico(Long empresaId, CriarEmpresaServicoRequest req) {
        return criarEmpresaServicoUseCase.executar(empresaId, req);
    }

    public EmpresaDetalheResponse.FuncionarioResumo vincularFuncionario(Long empresaId, VincularFuncionarioRequest req) {
        return vincularFuncionarioEmpresaUseCase.executar(empresaId, req);
    }

    public List<EmpresaDetalheResponse.ServicoResumo> listarServicos(Long empresaId) {
        return listarServicosDaEmpresaUseCase.executar(empresaId);
    }

    public List<EmpresaResponse> buscarEmpresas(BuscarEmpresasRequest request) {
        return buscarEmpresasUseCase.executar(request);
    }
}
