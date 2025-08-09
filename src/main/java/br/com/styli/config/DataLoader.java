package br.com.styli.config;

import br.com.styli.domain.model.*;
import br.com.styli.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.*;
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
            ClienteRepository clienteRepo,
            AgendamentoRepository agendamentoRepo
    ) {
        return args -> {
            // CATEGORIAS
            var catBarbearia = new Categoria(); catBarbearia.setNome("Barbearia");
            var catEstetica   = new Categoria(); catEstetica.setNome("Estética");
            categoriaRepo.saveAll(List.of(catBarbearia, catEstetica));

            // EMPRESAS
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

            // SERVIÇOS DA EMPRESA
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

            servicoRepo.saveAll(List.of(s1, s2, s3));

            // FUNCIONÁRIOS
            var f1 = Funcionario.builder()
                    .nomeCompleto("João Barbeiro")
                    .telefone("1198888-1111")
                    .imagens(List.of("joao.jpg")).build();

            var f2 = Funcionario.builder()
                    .nomeCompleto("Maria Estilo")
                    .telefone("1198888-2222")
                    .imagens(List.of("maria.jpg")).build();

            funcionarioRepo.saveAll(List.of(f1, f2));

            // VÍNCULO FUNCIONÁRIO ↔ EMPRESA
            var fe1 = FuncionarioEmpresa.builder()
                    .funcionario(f1).empresa(emp1).ativo(true).papel("Barbeiro")
                    .dataInicio(LocalDate.now().minusMonths(6)).build();

            var fe2 = FuncionarioEmpresa.builder()
                    .funcionario(f2).empresa(emp1).ativo(true).papel("Barbeiro")
                    .dataInicio(LocalDate.now().minusMonths(3)).build();

            var fe3 = FuncionarioEmpresa.builder()
                    .funcionario(f2).empresa(emp2).ativo(true).papel("Barbeiro/Designer")
                    .dataInicio(LocalDate.now().minusMonths(1)).build();

            funcEmpRepo.saveAll(List.of(fe1, fe2, fe3));

            // HABILITAÇÃO DE SERVIÇOS POR FUNCIONÁRIO
            var fs1 = FuncionarioServicoEmpresa.builder()
                    .funcionario(f1).empresaServico(s1).ativo(true).build();
            var fs2 = FuncionarioServicoEmpresa.builder()
                    .funcionario(f1).empresaServico(s2).ativo(true).build();
            var fs3 = FuncionarioServicoEmpresa.builder()
                    .funcionario(f2).empresaServico(s1).ativo(true).build();
            var fs4 = FuncionarioServicoEmpresa.builder()
                    .funcionario(f2).empresaServico(s3).ativo(true).build();
            funcServRepo.saveAll(List.of(fs1, fs2, fs3, fs4));

            // CLIENTES
            var c1 = Cliente.builder().nome("Roberto").telefone("1197000-0001").build();
            var c2 = Cliente.builder().nome("Ana").telefone("1197000-0002").build();
            var c3 = Cliente.builder().nome("Carlos").telefone("1197000-0003").build();
            clienteRepo.saveAll(List.of(c1, c2, c3));

            // AGENDAMENTOS DE TESTE (sem validar conflito aqui, é só seed)
            var agora = LocalDateTime.now().withSecond(0).withNano(0);
            var a1 = Agendamento.builder()
                    .empresa(emp1).servico(s1).cliente(c1).funcionario(f1)
                    .inicio(agora.plusHours(1))
                    .fim(agora.plusHours(1).plusMinutes(s1.getDuracaoMinutos()))
                    .status(StatusAgendamento.RESERVADO).build();

            var a2 = Agendamento.builder()
                    .empresa(emp1).servico(s2).cliente(c2).funcionario(f1)
                    .inicio(agora.plusHours(2))
                    .fim(agora.plusHours(2).plusMinutes(s2.getDuracaoMinutos()))
                    .status(StatusAgendamento.CONFIRMADO).build();

            var a3 = Agendamento.builder()
                    .empresa(emp2).servico(s3).cliente(c3).funcionario(f2)
                    .inicio(agora.plusDays(1).withHour(11).withMinute(0))
                    .fim(agora.plusDays(1).withHour(11).withMinute(0).plusMinutes(s3.getDuracaoMinutos()))
                    .status(StatusAgendamento.RESERVADO).build();

            agendamentoRepo.saveAll(List.of(a1, a2, a3));

            System.out.println(">>> Seed H2 concluído com sucesso!");
        };
    }
}
