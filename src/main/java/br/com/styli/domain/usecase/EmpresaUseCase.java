package br.com.styli.domain.usecase;

import br.com.styli.domain.dto.request.AgendamentoDinamicoRequest;
import br.com.styli.domain.dto.request.ReservarHorarioRequest;
import br.com.styli.domain.dto.response.AgendamentoResponse;
import br.com.styli.domain.exception.BusinessException;
import br.com.styli.domain.exception.ErrorCode;
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

@Slf4j
@Component
public class EmpresaUseCase {

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    AgendamentoRepository agendamentoRepository;

    @Autowired
    HorarioAtendimentoRepository horarioAtendimentoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ServicoRepository servicoRepository;

    public List<Empresa> findAll(){
        List<Empresa> empresaList = empresaRepository.findAll();
        return empresaList ;
    }

    public Empresa findById(Long id){
        Empresa empresa = empresaRepository.findById(id).orElseThrow(() -> {
            log.error("Empresa com ID {} não encontrado",id);
            return new BusinessException(ErrorCode.EMPRESA_NOT_FOUND);
        });
        return empresa;
    }

    public Empresa save(Empresa empresa){
        Empresa empresaSaved = empresaRepository.save(empresa);
        return empresaSaved;
    }

    public Empresa salvarFuncioario(Long empresaid, Funcionario funcionario){
        Empresa empresa = empresaRepository
                .findById(empresaid)
                .orElseThrow(() -> {
                    log.error("Empresa com ID {} não encontrado",empresaid);
                    return new BusinessException(ErrorCode.EMPRESA_NOT_FOUND);
                });

        funcionario.setEmpresa(empresa);
        funcionarioRepository.save(funcionario);

        empresa.getFuncionarios().add(funcionario);

        return empresaRepository.save(empresa);
    }

    public List<Empresa> findAllByCategoria(Long categoriaId) {
        return empresaRepository.findByCategorias_Id(categoriaId);
    }

    public List<LocalTime> buscarHorariosDisponiveis(Long idEmpresa, Long idFuncionario,
                                                     LocalDate data, int duracaoMinutos) {
        Funcionario funcionario = funcionarioRepository
                .findByIdAndEmpresaId(idFuncionario, idEmpresa)
                .orElseThrow(() -> {
                    log.error("Funcionario com ID {} não encontrado para empresa {}", idFuncionario, idEmpresa);
                    return new BusinessException(ErrorCode.FUNCIONARIO_NOT_FOUND);
                });

        List<LocalTime> horariosDisponiveis = new ArrayList<>();

        List<HorarioAtendimentoFuncionario> atendimentos = horarioAtendimentoRepository
                .findByFuncionarioAndDiaSemana(funcionario, data.getDayOfWeek());

        if (atendimentos.isEmpty()) return horariosDisponiveis;

        List<Agendamento> agendamentos = agendamentoRepository
                .findByFuncionarioAndData(funcionario, data);

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
    public AgendamentoResponse reservarHorario(
            Long idEmpresa,
            Long idFuncionario,
            ReservarHorarioRequest request) {

        Long idCliente = request.getIdCliente();
        Long idServico =  request.getIdServico();
        LocalDate data = request.getData();
        LocalTime horaInicio = request.getHoraInicio();

        Funcionario funcionario = funcionarioRepository
                .findByIdAndEmpresaId(idFuncionario, idEmpresa)
                .orElseThrow(() -> new BusinessException(ErrorCode.FUNCIONARIO_NOT_FOUND));

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLIENTE_NOT_FOUND));

        Servico servico = servicoRepository.findById(idServico)
                .orElseThrow(() -> new BusinessException(ErrorCode.SERVICO_NOT_FOUND));

        DayOfWeek diaSemana = data.getDayOfWeek();

        List<HorarioAtendimentoFuncionario> horariosAtendimento = horarioAtendimentoRepository
                .findByFuncionarioAndDiaSemana(funcionario, diaSemana);

        boolean dentroExpediente = horariosAtendimento.stream().anyMatch(h ->
                !horaInicio.isBefore(h.getHoraInicio()) &&
                        !horaInicio.plusMinutes(servico.getDuracaoMinutos()).isAfter(h.getHoraFim())
        );

        if (!dentroExpediente) {
            throw new IllegalArgumentException("Horário fora do expediente do funcionário");
        }

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

        Agendamento agendamento = Agendamento.builder()
                .funcionario(funcionario)
                .cliente(cliente)
                .servico(servico)
                .horario(LocalDateTime.of(data, horaInicio))
                .build();

        Agendamento agendamentoSaved = agendamentoRepository.save(agendamento);


        AgendamentoResponse response = AgendamentoResponse.builder()
                .idAgendamento(agendamentoSaved.getId())
                .idFuncionario(agendamentoSaved.getFuncionario().getId())
                .idCliente(agendamentoSaved.getCliente().getId())
                .idServico(agendamentoSaved.getServico().getId())
                .horario(agendamentoSaved.getHorario())
                .build();

        return response;
    }

    @Transactional
    public AgendamentoResponse agendarAleatoriamente(Long idEmpresa, AgendamentoDinamicoRequest request) {

        Long idCliente = request.getIdCliente();
        Long idServico = request.getIdServico();
        LocalDate data = request.getData();
        LocalTime horaExata = request.getHoraDesejada();

        Servico servico = servicoRepository.findById(idServico)
                .orElseThrow(() -> new BusinessException(ErrorCode.SERVICO_NOT_FOUND));

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLIENTE_NOT_FOUND));

        List<Funcionario> funcionarios = funcionarioRepository
                .findByEmpresaIdAndServicos_Id(idEmpresa, idServico);

        for (Funcionario funcionario : funcionarios) {
            List<HorarioAtendimentoFuncionario> atendimentos = horarioAtendimentoRepository
                    .findByFuncionarioAndDiaSemana(funcionario, data.getDayOfWeek());

            if (atendimentos.isEmpty()) continue;

            boolean atendeNesseHorario = atendimentos.stream().anyMatch(h ->
                    !horaExata.isBefore(h.getHoraInicio()) &&
                            !horaExata.plusMinutes(servico.getDuracaoMinutos()).isAfter(h.getHoraFim())
            );

            if (!atendeNesseHorario) continue;

            List<Agendamento> agendamentos = agendamentoRepository
                    .findByFuncionarioAndData(funcionario, data);

            boolean conflita = agendamentos.stream().anyMatch(ag -> {
                LocalTime inicioAg = ag.getHorario().toLocalTime();
                LocalTime fimAg = inicioAg.plusMinutes(ag.getServico().getDuracaoMinutos());
                LocalTime fimNovo = horaExata.plusMinutes(servico.getDuracaoMinutos());
                return !(fimNovo.isBefore(inicioAg) || horaExata.isAfter(fimAg.minusMinutes(1)));
            });

            if (conflita) continue;

            // Funcionário disponível no horário exato
            Agendamento agendamentoSalvo = agendamentoRepository.save(
                    Agendamento.builder()
                            .cliente(cliente)
                            .funcionario(funcionario)
                            .servico(servico)
                            .horario(LocalDateTime.of(data, horaExata))
                            .build()
            );

            return AgendamentoResponse.builder()
                    .idAgendamento(agendamentoSalvo.getId())
                    .idFuncionario(funcionario.getId())
                    .idCliente(cliente.getId())
                    .idServico(servico.getId())
                    .horario(agendamentoSalvo.getHorario())
                    .build();
        }

        throw new BusinessException(ErrorCode.HORARIO_UNAVAILABLE);
    }

}
