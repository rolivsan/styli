package br.com.styli.domain.usecase;

import br.com.styli.domain.exception.BusinessException;
import br.com.styli.domain.model.*;
import br.com.styli.domain.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static br.com.styli.domain.exception.ErrorCode.*;

@Component
@Slf4j
public class FuncionarioUseCase {

    @Autowired
    AgendamentoRepository agendamentoRepository;

    @Autowired
    HorarioAtendimentoRepository horarioAtendimentoRepository;

    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ServicoRepository servicoRepository;

    public List<LocalTime> buscarHorariosDisponiveis(
            Long idFuncionario,
            LocalDate data,
            int duracaoMinutos
    ) {

        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> {
                    log.error("Funcionario com ID {} não encontrado", idFuncionario);
                    return new BusinessException(FUNCIONARIO_NOT_FOUND);
                });

        List<LocalTime> horariosDisponiveis = new ArrayList<>();

        // 1. Buscar horário de atendimento do funcionário
        List<HorarioAtendimentoFuncionario> atendimentos = horarioAtendimentoRepository
                .findByFuncionarioAndDiaSemana(funcionario, data.getDayOfWeek());

        if (atendimentos.isEmpty()) return horariosDisponiveis;

        // 2. Buscar agendamentos existentes no dia
        List<Agendamento> agendamentos = agendamentoRepository
                .findByFuncionarioAndData(funcionario, data);

        // 3. Gerar slots de 30 em 30 minutos dentro do(s) atendimento(s)
        for (HorarioAtendimentoFuncionario atendimento : atendimentos) {
            LocalTime hora = atendimento.getHoraInicio();
            LocalTime fim = atendimento.getHoraFim();

            while (!hora.plusMinutes(duracaoMinutos).isAfter(fim)) {
                LocalTime horaFinal = hora.plusMinutes(duracaoMinutos);
                boolean conflita = false;

                for (Agendamento ag : agendamentos) {
                    LocalTime inicioAg = ag.getHorario().toLocalTime();
                    LocalTime fimAg = inicioAg.plusMinutes(ag.getServico().getDuracaoMinutos());

                    if (!(horaFinal.isBefore(inicioAg) || hora.isAfter(fimAg.minusMinutes(1)))) {
                        conflita = true;
                        break;
                    }
                }

                if (!conflita) {
                    horariosDisponiveis.add(hora);
                }

                hora = hora.plusMinutes(30);
            }
        }

        return horariosDisponiveis;
    }

    @Transactional
    public Agendamento reservarHorario(
            Long idFuncionario,
            Long idCliente,
            Long idServico,
            LocalDate data,
            LocalTime horaInicio
    ) {
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new BusinessException(FUNCIONARIO_NOT_FOUND));

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new BusinessException(CLIENTE_NOT_FOUND));

        Servico servico = servicoRepository.findById(idServico)
                .orElseThrow(() -> new BusinessException(SERVICO_NOT_FOUND));

        DayOfWeek diaSemana = data.getDayOfWeek();

        // Verifica se horário está dentro do expediente do funcionário
        List<HorarioAtendimentoFuncionario> horariosAtendimento = horarioAtendimentoRepository
                .findByFuncionarioAndDiaSemana(funcionario, diaSemana);

        boolean dentroExpediente = horariosAtendimento.stream().anyMatch(h ->
                !horaInicio.isBefore(h.getHoraInicio()) &&
                        !horaInicio.plusMinutes(servico.getDuracaoMinutos()).isAfter(h.getHoraFim())
        );

        if (!dentroExpediente) {
            throw new IllegalArgumentException("Horário fora do expediente do funcionário");
        }

        // Verifica conflitos com agendamentos existentes
        List<Agendamento> agendamentos = agendamentoRepository
                .findByFuncionarioAndData(funcionario, data);

        boolean conflito = agendamentos.stream().anyMatch(ag -> {
            LocalTime inicioAg = ag.getHorario().toLocalTime();
            LocalTime fimAg = inicioAg.plusMinutes(ag.getServico().getDuracaoMinutos());
            LocalTime fimServico = horaInicio.plusMinutes(servico.getDuracaoMinutos());

            return !(fimServico.isBefore(inicioAg) || horaInicio.isAfter(fimAg.minusMinutes(1)));
        });

        if (conflito) {
            throw new IllegalStateException("Horário já está reservado");
        }

        // Criar e salvar agendamento
        Agendamento agendamento =Agendamento.builder().funcionario(funcionario).cliente(cliente).servico(servico)
                .horario(LocalDateTime.of(data, horaInicio)).build();

        return agendamentoRepository.save(agendamento);
    }
}
