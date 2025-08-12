// src/main/java/br/com/styli/usecase/CriarAgendamentoUseCase.java
package br.com.styli.usecase;

import br.com.styli.domain.exception.BadRequestException;
import br.com.styli.domain.exception.NotFoundException;
import br.com.styli.domain.model.*;
import br.com.styli.dto.request.CriarAgendamentoRequest;
import br.com.styli.dto.response.AgendamentoResponse;
import br.com.styli.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;

@Component
@RequiredArgsConstructor
public class CriarAgendamentoUseCase {

    private final AgendamentoRepository agendamentoRepository;
    private final EmpresaServicoRepository servicoRepository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioEmpresaRepository funcionarioEmpresaRepository;
    private final FuncionarioServicoEmpresaRepository funcionarioServicoEmpresaRepository;

    @Transactional
    public AgendamentoResponse executar(CriarAgendamentoRequest request) {
        if (request.getInicio() == null) throw new BadRequestException("Início é obrigatório");
        if (request.getInicio().isBefore(LocalDateTime.now())) throw new BadRequestException("Não é possível agendar no passado");

        EmpresaServico servico = servicoRepository.findById(request.getServicoId())
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado"));
        Empresa empresa = servico.getEmpresa();

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        Funcionario funcionarioEscolhido = null;

        // >>> valor padrão já inicializado
        int duracaoMinutosAplicada = servico.getDuracaoMinutos();

        if (request.getFuncionarioId() != null) {
            Funcionario fun = funcionarioRepository.findById(request.getFuncionarioId())
                    .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

            funcionarioEmpresaRepository.findByFuncionario_IdAndEmpresa_IdAndAtivoTrue(fun.getId(), empresa.getId())
                    .orElseThrow(() -> new BadRequestException("Funcionário não pertence a esta empresa ou está inativo"));

            var hab = funcionarioServicoEmpresaRepository
                    .findByFuncionario_IdAndEmpresaServico_IdAndAtivoTrue(fun.getId(), servico.getId())
                    .orElseThrow(() -> new BadRequestException("Funcionário não está habilitado neste serviço"));

            // >>> aplica override se houver
            if (hab.getDuracaoMinutosOverride() != null) {
                duracaoMinutosAplicada = hab.getDuracaoMinutosOverride();
            }

            funcionarioRepository.lockById(fun.getId())
                    .orElseThrow(() -> new NotFoundException("Funcionário não encontrado para lock"));

            LocalDateTime inicio = request.getInicio();
            LocalDateTime fim = inicio.plusMinutes(duracaoMinutosAplicada);
            if (existeConflito(fun.getId(), inicio, fim)) {
                throw new BadRequestException("Horário indisponível para o profissional selecionado");
            }

            funcionarioEscolhido = fun;

        } else {
            var habilitados = funcionarioServicoEmpresaRepository.findByEmpresaServico_IdAndAtivoTrue(servico.getId());
            if (habilitados.isEmpty()) throw new BadRequestException("Não há profissionais habilitados para este serviço");

            var candidatos = habilitados.stream()
                    .filter(h -> funcionarioEmpresaRepository
                            .findByFuncionario_IdAndEmpresa_IdAndAtivoTrue(h.getFuncionario().getId(), empresa.getId())
                            .isPresent())
                    .sorted(Comparator.comparing(h -> cargaNoDia(h.getFuncionario().getId(), request.getInicio())))
                    .toList();

            LocalDateTime inicio = request.getInicio();
            FuncionarioServicoEmpresa escolhido = null;

            for (FuncionarioServicoEmpresa h : candidatos) {
                Long fid = h.getFuncionario().getId();
                funcionarioRepository.lockById(fid)
                        .orElseThrow(() -> new NotFoundException("Funcionário não encontrado para lock"));

                int dur = (h.getDuracaoMinutosOverride() != null) ? h.getDuracaoMinutosOverride() : servico.getDuracaoMinutos();
                LocalDateTime fim = inicio.plusMinutes(dur);

                if (!existeConflito(fid, inicio, fim)) {
                    escolhido = h;
                    funcionarioEscolhido = h.getFuncionario();
                    duracaoMinutosAplicada = dur; // >>> define aqui ao escolher
                    break;
                }
            }

            if (funcionarioEscolhido == null) {
                throw new BadRequestException("Não há profissionais disponíveis nesse horário");
            }
        }

        LocalDateTime inicio = request.getInicio();
        LocalDateTime fim = inicio.plusMinutes(duracaoMinutosAplicada);

        Agendamento agendamento = Agendamento.builder()
                .empresa(empresa)
                .servico(servico)
                .cliente(cliente)
                .funcionario(funcionarioEscolhido)
                .inicio(inicio)
                .fim(fim)
                .status(StatusAgendamento.RESERVADO)
                .build();

        agendamentoRepository.save(agendamento);

        return AgendamentoResponse.builder()
                .id(agendamento.getId())
                .empresa(empresa.getNome())
                .servico(servico.getNome())
                .cliente(cliente.getNome())
                .funcionario(funcionarioEscolhido != null ? funcionarioEscolhido.getNomeCompleto() : null)
                .inicio(inicio)
                .fim(fim)
                .status(agendamento.getStatus().name())
                .build();
    }


    private boolean existeConflito(Long funcId, LocalDateTime inicio, LocalDateTime fim) {
        return agendamentoRepository.existsByFuncionario_IdAndInicioLessThanAndFimGreaterThan(funcId, fim, inicio);
    }

    private long cargaNoDia(Long funcId, LocalDateTime referencia) {
        return agendamentoRepository.findByFuncionario_Id(funcId).stream()
                .filter(a -> a.getInicio().toLocalDate().equals(referencia.toLocalDate()))
                .count();
    }
}
