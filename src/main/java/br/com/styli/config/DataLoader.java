package br.com.styli.config;

import br.com.styli.domain.model.*;
import br.com.styli.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    @Bean
    CommandLineRunner loadData(
            CategoriaRepository categoriaRepo,
            EmpresaRepository empresaRepo,
            EmpresaServicoRepository servicoRepo,
            FuncionarioRepository funcionarioRepo,
            FuncionarioEmpresaRepository funcEmpRepo,
            FuncionarioServicoEmpresaRepository funcServRepo,
            HorarioAtendimentoFuncionarioRepository horarioRepo,
            BloqueioAgendaRepository bloqueioRepo,
            ClienteRepository clienteRepo,
            AgendamentoRepository agendamentoRepo
    ) {
        return args -> {
            // ========= CATEGORIAS =========
            var catBarbearia = new Categoria(); catBarbearia.setNome("Barbearia");
            var catEstetica = new Categoria();  catEstetica.setNome("Estética");
            categoriaRepo.saveAll(List.of(catBarbearia, catEstetica));

            // ========= EMPRESAS =========
            var emp1 = Empresa.builder()
                    .nome("Barbearia Central")
                    .telefone("1199999-0001")
                    .email("contato@central.com")
                    .endereco("Av. Principal, 100")
                    .cidade("São Paulo")
                    .uf("SP")
                    .latitude(-23.5505)
                    .longitude(-46.6333)
                    .horarioFuncionamento("09:00 - 19:00")
                    .destaque(true)
                    .imagens(List.of("img1.jpg", "img2.jpg"))
                    .categorias(List.of(catBarbearia))
                    .build();

            var emp2 = Empresa.builder()
                    .nome("Studio Beleza")
                    .telefone("1199999-0002")
                    .email("contato@studio.com")
                    .endereco("Rua Secundária, 200")
                    .cidade("São Paulo")
                    .uf("SP")
                    .latitude(-23.5590)
                    .longitude(-46.6350)
                    .horarioFuncionamento("10:00 - 18:00")
                    .destaque(false)
                    .imagens(List.of("img3.jpg"))
                    .categorias(List.of(catBarbearia, catEstetica))
                    .build();

            empresaRepo.saveAll(List.of(emp1, emp2));

            // ========= SERVIÇOS =========
            var s1 = EmpresaServico.builder()
                    .empresa(emp1).nome("Corte Masculino")
                    .duracaoMinutos(30).preco(new BigDecimal("40.00"))
                    .ativo(true).categoria(catBarbearia).build();

            var s2 = EmpresaServico.builder()
                    .empresa(emp1).nome("Barba")
                    .duracaoMinutos(20).preco(new BigDecimal("25.00"))
                    .ativo(true).categoria(catBarbearia).build();

            var s3 = EmpresaServico.builder()
                    .empresa(emp2).nome("Corte + Barba")
                    .duracaoMinutos(50).preco(new BigDecimal("60.00"))
                    .ativo(true).categoria(catBarbearia).build();

            var s4 = EmpresaServico.builder()
                    .empresa(emp2).nome("Alisamento")
                    .duracaoMinutos(90).preco(new BigDecimal("150.00"))
                    .ativo(true).categoria(catEstetica).build();

            servicoRepo.saveAll(List.of(s1, s2, s3, s4));

            // ========= FUNCIONÁRIOS =========
            var f1 = Funcionario.builder()
                    .nomeCompleto("João Barbeiro")
                    .telefone("1198888-1111")
                    .imagens(List.of("joao.jpg")).build();

            var f2 = Funcionario.builder()
                    .nomeCompleto("Maria Estilo")
                    .telefone("1198888-2222")
                    .imagens(List.of("maria.jpg")).build();

            var f3 = Funcionario.builder()
                    .nomeCompleto("Pedro Multi")
                    .telefone("1198888-3333")
                    .imagens(List.of("pedro.jpg")).build();

            funcionarioRepo.saveAll(List.of(f1, f2, f3));

            // ========= VÍNCULOS FUNCIONÁRIO ↔ EMPRESA =========
            var fe1 = FuncionarioEmpresa.builder()
                    .funcionario(f1).empresa(emp1).ativo(true).papel("Barbeiro")
                    .dataInicio(LocalDate.now().minusMonths(6)).build();

            var fe2 = FuncionarioEmpresa.builder()
                    .funcionario(f2).empresa(emp1).ativo(true).papel("Barbeiro")
                    .dataInicio(LocalDate.now().minusMonths(3)).build();

            var fe3 = FuncionarioEmpresa.builder()
                    .funcionario(f2).empresa(emp2).ativo(true).papel("Designer")
                    .dataInicio(LocalDate.now().minusMonths(1)).build();

            var fe4 = FuncionarioEmpresa.builder()
                    .funcionario(f3).empresa(emp1).ativo(true).papel("Barbeiro")
                    .dataInicio(LocalDate.now().minusMonths(2)).build();

            var fe5 = FuncionarioEmpresa.builder()
                    .funcionario(f3).empresa(emp2).ativo(true).papel("Barbeiro/Colorista")
                    .dataInicio(LocalDate.now().minusMonths(2)).build();

            funcEmpRepo.saveAll(List.of(fe1, fe2, fe3, fe4, fe5));

            // ========= HABILITAÇÕES (FUNCIONÁRIO x SERVIÇO) =========
            var fs1 = FuncionarioServicoEmpresa.builder()
                    .funcionario(f1).empresaServico(s1).ativo(true).build(); // João faz Corte
            var fs2 = FuncionarioServicoEmpresa.builder()
                    .funcionario(f1).empresaServico(s2).ativo(true).build(); // João faz Barba
            var fs3 = FuncionarioServicoEmpresa.builder()
                    .funcionario(f2).empresaServico(s1).ativo(true).build(); // Maria faz Corte
            var fs4 = FuncionarioServicoEmpresa.builder()
                    .funcionario(f2).empresaServico(s3).ativo(true)
                    .duracaoMinutosOverride(55).precoOverride(new BigDecimal("65.00")).build(); // override
            var fs5 = FuncionarioServicoEmpresa.builder()
                    .funcionario(f3).empresaServico(s1).ativo(true).build(); // Pedro faz Corte
            var fs6 = FuncionarioServicoEmpresa.builder()
                    .funcionario(f3).empresaServico(s3).ativo(true).build(); // Pedro faz Corte+Barba
            var fs7 = FuncionarioServicoEmpresa.builder()
                    .funcionario(f3).empresaServico(s4).ativo(true).build(); // Pedro faz Alisamento (emp2)

            funcServRepo.saveAll(List.of(fs1, fs2, fs3, fs4, fs5, fs6, fs7));

            // ========= HORÁRIOS SEMANAIS =========
            // Regras simples: seg-sex
            var seg = DayOfWeek.MONDAY;
            var ter = DayOfWeek.TUESDAY;
            var qua = DayOfWeek.WEDNESDAY;
            var qui = DayOfWeek.THURSDAY;
            var sex = DayOfWeek.FRIDAY;

            // João (emp1): 09:00-13:00 e 14:00-18:00 de seg a sex
            for (var d : List.of(seg, ter, qua, qui, sex)) {
                horarioRepo.save(HorarioAtendimentoFuncionario.builder()
                        .funcionario(f1).diaSemana(d)
                        .horaInicio(LocalTime.of(9,0)).horaFim(LocalTime.of(13,0))
                        .ativo(true).build());
                horarioRepo.save(HorarioAtendimentoFuncionario.builder()
                        .funcionario(f1).diaSemana(d)
                        .horaInicio(LocalTime.of(14,0)).horaFim(LocalTime.of(18,0))
                        .ativo(true).build());
            }

            // Maria (emp1/emp2): 10:00-16:00 seg, qua, sex
            for (var d : List.of(seg, qua, sex)) {
                horarioRepo.save(HorarioAtendimentoFuncionario.builder()
                        .funcionario(f2).diaSemana(d)
                        .horaInicio(LocalTime.of(10,0)).horaFim(LocalTime.of(16,0))
                        .ativo(true).build());
            }

            // Pedro (emp1/emp2): 12:00-20:00 ter a sex
            for (var d : List.of(ter, qua, qui, sex)) {
                horarioRepo.save(HorarioAtendimentoFuncionario.builder()
                        .funcionario(f3).diaSemana(d)
                        .horaInicio(LocalTime.of(12,0)).horaFim(LocalTime.of(20,0))
                        .ativo(true).build());
            }

            // ========= BLOQUEIOS =========
            // Ex.: João (f1) bloqueado na quarta de manhã (09:00-12:00)
            var proxQua = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
            bloqueioRepo.save(BloqueioAgenda.builder()
                    .funcionario(f1)
                    .inicio(LocalDateTime.of(proxQua, LocalTime.of(9,0)))
                    .fim(LocalDateTime.of(proxQua, LocalTime.of(12,0)))
                    .diaTodo(false)
                    .motivo("Consulta médica")
                    .build());

            // Pedro (f3) bloqueado amanhã à tarde (14:00-16:00)
            var amanha = LocalDate.now().plusDays(1);
            bloqueioRepo.save(BloqueioAgenda.builder()
                    .funcionario(f3)
                    .inicio(LocalDateTime.of(amanha, LocalTime.of(14,0)))
                    .fim(LocalDateTime.of(amanha, LocalTime.of(16,0)))
                    .diaTodo(false)
                    .motivo("Treinamento")
                    .build());

            // ========= CLIENTES =========
            var c1 = Cliente.builder().nome("Roberto").telefone("1197000-0001").build();
            var c2 = Cliente.builder().nome("Ana").telefone("1197000-0002").build();
            clienteRepo.saveAll(List.of(c1, c2));

            // ========= AGENDAMENTOS =========
            var agora = LocalDateTime.now().withSecond(0).withNano(0);

            // Hoje emp1: João tem 2 horários ocupados
            var a1 = Agendamento.builder()
                    .empresa(emp1).servico(s1).cliente(c1).funcionario(f1)
                    .inicio(agora.plusHours(1))
                    .fim(agora.plusHours(1).plusMinutes(s1.getDuracaoMinutos()))
                    .status(StatusAgendamento.RESERVADO).build();

            var a2 = Agendamento.builder()
                    .empresa(emp1).servico(s2).cliente(c2).funcionario(f1)
                    .inicio(agora.plusHours(2))
                    .fim(agora.plusHours(2).plusMinutes(s2.getDuracaoMinutos()))
                    .status(StatusAgendamento.RESERVADO).build();

            // Amanhã emp2: Maria com Corte+Barba às 11:00 (override 55 min)
            var durMaria = fs4.getDuracaoMinutosOverride() != null ? fs4.getDuracaoMinutosOverride() : s3.getDuracaoMinutos();
            var a3 = Agendamento.builder()
                    .empresa(emp2).servico(s3).cliente(c1).funcionario(f2)
                    .inicio(LocalDateTime.of(amanha, LocalTime.of(11,0)))
                    .fim(LocalDateTime.of(amanha, LocalTime.of(11,0)).plusMinutes(durMaria))
                    .status(StatusAgendamento.RESERVADO).build();

            // Depois de amanhã emp2: Pedro com Alisamento às 13:00 (90 min)
            var depoisAmanha = LocalDate.now().plusDays(2);
            var a4 = Agendamento.builder()
                    .empresa(emp2).servico(s4).cliente(c2).funcionario(f3)
                    .inicio(LocalDateTime.of(depoisAmanha, LocalTime.of(13,0)))
                    .fim(LocalDateTime.of(depoisAmanha, LocalTime.of(13,0)).plusMinutes(s4.getDuracaoMinutos()))
                    .status(StatusAgendamento.RESERVADO).build();

            agendamentoRepo.saveAll(List.of(a1, a2, a3, a4));

            System.out.println(">>> Seed H2 concluído com horários, bloqueios e agendamentos.");
        };
    }
}
