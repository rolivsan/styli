package br.com.styli.usecase;

import br.com.styli.domain.exception.BadRequestException;
import br.com.styli.domain.exception.NotFoundException;
import br.com.styli.domain.model.*;
import br.com.styli.dto.response.DisponibilidadeDiaResponse;
import br.com.styli.dto.response.DisponibilidadeFuncionarioResponse;
import br.com.styli.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ListarDisponibilidadeUseCase {

    private static final int GRADE_MINUTOS = 15; // passo da grade

    private final EmpresaRepository empresaRepository;
    private final EmpresaServicoRepository servicoRepository;
    private final FuncionarioServicoEmpresaRepository funcServicoRepo;
    private final FuncionarioEmpresaRepository funcEmpresaRepo;
    private final HorarioAtendimentoFuncionarioRepository horarioRepo;
    private final BloqueioAgendaRepository bloqueioRepo;
    private final AgendamentoRepository agendamentoRepo;

    public DisponibilidadeDiaResponse executar(Long empresaId, Long servicoId, LocalDate data, Long funcionarioIdOpcional) {
        if (data == null) throw new BadRequestException("Data é obrigatória (YYYY-MM-DD)");

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada"));
        EmpresaServico servico = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado"));

        if (!Objects.equals(servico.getEmpresa().getId(), empresa.getId())) {
            throw new BadRequestException("Serviço não pertence à empresa informada");
        }

        // habilitados neste serviço
        List<FuncionarioServicoEmpresa> habilitacoes = funcServicoRepo.findByEmpresaServico_IdAndAtivoTrue(servicoId);
        if (habilitacoes.isEmpty()) {
            return DisponibilidadeDiaResponse.builder()
                    .empresaId(empresaId)
                    .servicoId(servicoId)
                    .data(data)
                    .funcionarios(List.of())
                    .build();
        }

        // se funcionárioId veio, filtra
        if (funcionarioIdOpcional != null) {
            habilitacoes = habilitacoes.stream()
                    .filter(h -> h.getFuncionario().getId().equals(funcionarioIdOpcional))
                    .collect(Collectors.toList());
        }

        // filtra que de fato pertencem à empresa (vínculo ativo)
        habilitacoes = habilitacoes.stream()
                .filter(h -> funcEmpresaRepo
                        .findByFuncionario_IdAndEmpresa_IdAndAtivoTrue(h.getFuncionario().getId(), empresaId).isPresent())
                .collect(Collectors.toList());

        DayOfWeek diaSemana = data.getDayOfWeek();
        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.atTime(23, 59, 59);

        List<DisponibilidadeFuncionarioResponse> respostas = new ArrayList<>();

        for (var hab : habilitacoes) {
            Funcionario func = hab.getFuncionario();

            // horários semanais do funcionário para esse dia da semana
            List<HorarioAtendimentoFuncionario> faixas = horarioRepo
                    .findByFuncionario_IdAndDiaSemanaAndAtivoTrue(func.getId(), diaSemana);

            if (faixas.isEmpty()) {
                // sem expediente nesse dia
                respostas.add(DisponibilidadeFuncionarioResponse.builder()
                        .funcionarioId(func.getId())
                        .nomeCompleto(func.getNomeCompleto())
                        .duracaoMinutos(resolveDuracao(servico, hab))
                        .horarios(List.of())
                        .build());
                continue;
            }

            // bloqueios e agendamentos no dia
            List<BloqueioAgenda> bloqueios = bloqueioRepo
                    .findByFuncionario_IdAndInicioLessThanAndFimGreaterThan(func.getId(), fimDia, inicioDia);

            List<Agendamento> agendamentos = agendamentoRepo
                    .findByFuncionario_IdAndInicioBetween(func.getId(), inicioDia, fimDia);

            // constroi "ocupados" (agendamentos + bloqueios)
            List<Intervalo> ocupados = new ArrayList<>();
            for (Agendamento a : agendamentos) {
                ocupados.add(new Intervalo(a.getInicio(), a.getFim()));
            }
            for (BloqueioAgenda b : bloqueios) {
                ocupados.add(new Intervalo(b.getInicio(), b.getFim()));
            }

            // normaliza ocupados (merge) pra acelerar checagem
            ocupados = merge(ocupados);

            int duracao = resolveDuracao(servico, hab);

            // slots disponíveis em todas as faixas do dia
            Set<String> horariosLivres = new TreeSet<>(); // ordenado
            for (HorarioAtendimentoFuncionario faixa : faixas) {
                LocalDateTime inicioFaixa = LocalDateTime.of(data, faixa.getHoraInicio());
                LocalDateTime fimFaixa = LocalDateTime.of(data, faixa.getHoraFim());
                horariosLivres.addAll(gerarSlotsLivres(inicioFaixa, fimFaixa, duracao, ocupados));
            }

            respostas.add(DisponibilidadeFuncionarioResponse.builder()
                    .funcionarioId(func.getId())
                    .nomeCompleto(func.getNomeCompleto())
                    .duracaoMinutos(duracao)
                    .horarios(new ArrayList<>(horariosLivres))
                    .build());
        }

        return DisponibilidadeDiaResponse.builder()
                .empresaId(empresaId)
                .servicoId(servicoId)
                .data(data)
                .funcionarios(respostas)
                .build();
    }

    private int resolveDuracao(EmpresaServico servico, FuncionarioServicoEmpresa hab) {
        return hab.getDuracaoMinutosOverride() != null ? hab.getDuracaoMinutosOverride() : servico.getDuracaoMinutos();
    }

    private List<String> gerarSlotsLivres(LocalDateTime inicioFaixa, LocalDateTime fimFaixa, int duracaoMin, List<Intervalo> ocupados) {
        List<String> livres = new ArrayList<>();
        LocalDateTime cursor = alignToGrade(inicioFaixa);

        while (!cursor.plusMinutes(duracaoMin).isAfter(fimFaixa)) {
            LocalDateTime fimSlot = cursor.plusMinutes(duracaoMin);
            if (!sobrepoe(cursor, fimSlot, ocupados)) {
                livres.add(cursor.toLocalTime().toString().substring(0,5)); // "HH:mm"
            }
            cursor = cursor.plusMinutes(GRADE_MINUTOS);
        }
        return livres;
    }

    private LocalDateTime alignToGrade(LocalDateTime t) {
        int m = t.getMinute();
        int resto = m % GRADE_MINUTOS;
        if (resto == 0) return t.withSecond(0).withNano(0);
        return t.plusMinutes(GRADE_MINUTOS - resto).withSecond(0).withNano(0);
    }

    private boolean sobrepoe(LocalDateTime ini, LocalDateTime fim, List<Intervalo> ocupados) {
        for (Intervalo i : ocupados) {
            if (ini.isBefore(i.fim) && fim.isAfter(i.inicio)) {
                return true;
            }
        }
        return false;
    }

    private List<Intervalo> merge(List<Intervalo> lista) {
        if (lista.isEmpty()) return lista;
        lista.sort(Comparator.comparing(i -> i.inicio));
        List<Intervalo> res = new ArrayList<>();
        Intervalo atual = lista.get(0);

        for (int k = 1; k < lista.size(); k++) {
            Intervalo nxt = lista.get(k);
            if (!nxt.inicio.isAfter(atual.fim)) {
                // sobrepõe ou encosta -> junta
                atual.fim = max(atual.fim, nxt.fim);
            } else {
                res.add(atual);
                atual = nxt;
            }
        }
        res.add(atual);
        return res;
    }

    private LocalDateTime max(LocalDateTime a, LocalDateTime b) {
        return a.isAfter(b) ? a : b;
    }

    private static class Intervalo {
        LocalDateTime inicio;
        LocalDateTime fim;
        Intervalo(LocalDateTime i, LocalDateTime f) { this.inicio = i; this.fim = f; }
    }
}
