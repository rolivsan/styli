package br.com.styli.usecase;

import br.com.styli.dto.request.CriarAgendamentoRequest;
import br.com.styli.dto.response.AgendamentoResponse;
import br.com.styli.domain.exception.BadRequestException;
import br.com.styli.domain.exception.NotFoundException;
import br.com.styli.domain.model.*;
import br.com.styli.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CriarAgendamentoUseCase {

    private final AgendamentoRepository agendamentoRepository;
    private final EmpresaServicoRepository servicoRepository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioEmpresaRepository funcionarioEmpresaRepository;
    private final FuncionarioServicoEmpresaRepository funcionarioServicoEmpresaRepository;

    public AgendamentoResponse executar(CriarAgendamentoRequest request) {
        // 1) Carrega serviço e deduz empresa
        EmpresaServico servico = servicoRepository.findById(request.getServicoId())
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado"));
        Empresa empresa = servico.getEmpresa();

        // 2) Cliente
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        // 3) Determina funcionário
        Funcionario funcionario = null;
        Integer duracao = servico.getDuracaoMinutos();

        if (request.getFuncionarioId() != null) {
            // Validar que o funcionário pertence à MESMA empresa
            funcionario = funcionarioRepository.findById(request.getFuncionarioId())
                    .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

            funcionarioEmpresaRepository.findByFuncionario_IdAndEmpresa_IdAndAtivoTrue(funcionario.getId(), empresa.getId())
                    .orElseThrow(() -> new BadRequestException("Funcionário não pertence a esta empresa ou está inativo"));

            // Validar habilitação no serviço (e capturar override de duração, se existir)
            var habilitacao = funcionarioServicoEmpresaRepository
                    .findByFuncionario_IdAndEmpresaServico_IdAndAtivoTrue(funcionario.getId(), servico.getId())
                    .orElseThrow(() -> new BadRequestException("Funcionário não está habilitado neste serviço"));

            if (habilitacao.getDuracaoMinutosOverride() != null) {
                duracao = habilitacao.getDuracaoMinutosOverride();
            }
        } else {
            // Sem funcionário → escolher aleatório habilitado e disponível
            List<FuncionarioServicoEmpresa> habilitados =
                    funcionarioServicoEmpresaRepository.findByEmpresaServico_IdAndAtivoTrue(servico.getId());

            if (habilitados.isEmpty()) {
                throw new BadRequestException("Não há profissionais habilitados para este serviço");
            }

            // filtra os que pertencem à empresa (por segurança extra) e não conflitam
            var candidatos = habilitados.stream()
                    .filter(h -> funcionarioEmpresaRepository
                            .findByFuncionario_IdAndEmpresa_IdAndAtivoTrue(h.getFuncionario().getId(), empresa.getId())
                            .isPresent())
                    .filter(h -> !existeConflito(h.getFuncionario().getId(), request.getInicio(),
                            request.getInicio().plusMinutes(h.getDuracaoMinutosOverride() != null ? h.getDuracaoMinutosOverride() : servico.getDuracaoMinutos())))
                    // critério simplão: menor carga atual no horário (poderia ser randômico)
                    .sorted(Comparator.comparing(h -> cargaNoHorario(h.getFuncionario().getId(), request.getInicio())))
                    .toList();

            if (candidatos.isEmpty()) {
                throw new BadRequestException("Não há profissionais disponíveis nesse horário");
            }

            var escolhido = candidatos.get(0);
            funcionario = escolhido.getFuncionario();
            if (escolhido.getDuracaoMinutosOverride() != null) {
                duracao = escolhido.getDuracaoMinutosOverride();
            }
        }

        // 4) Monta horários e valida conflito final
        LocalDateTime inicio = request.getInicio();
        LocalDateTime fim = inicio.plusMinutes(duracao);
        if (funcionario != null && existeConflito(funcionario.getId(), inicio, fim)) {
            throw new BadRequestException("Horário indisponível para o profissional selecionado");
        }

        // 5) Persiste
        Agendamento agendamento = Agendamento.builder()
                .empresa(empresa)
                .servico(servico)
                .cliente(cliente)
                .funcionario(funcionario) // pode ser null? aqui já não será, pois sempre escolhemos
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
                .funcionario(funcionario != null ? funcionario.getNomeCompleto() : "Indefinido")
                .inicio(inicio)
                .fim(fim)
                .status(agendamento.getStatus().name())
                .build();
    }

    // Conflito simples: há agendamento com (inicio < fimNovo) e (fim > inicioNovo)
    private boolean existeConflito(Long funcId, LocalDateTime inicio, LocalDateTime fim) {
        return agendamentoRepository.existsByFuncionario_IdAndInicioLessThanAndFimGreaterThan(funcId, fim, inicio);
    }

    // "Carga" no horário: quantos agendamentos o profissional tem nesse dia (heurística simples)
    private long cargaNoHorario(Long funcId, LocalDateTime referencia) {
        return agendamentoRepository.findByFuncionario_Id(funcId).stream()
                .filter(a -> a.getInicio().toLocalDate().equals(referencia.toLocalDate()))
                .count();
    }
}
