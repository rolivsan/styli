package br.com.styli;

import br.com.styli.domain.model.Categoria;
import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.model.Servico;
import br.com.styli.domain.repository.CategoriaRepository;
import br.com.styli.domain.repository.EmpresaRepository;
import br.com.styli.domain.repository.FuncionarioRepository;
import br.com.styli.domain.repository.ServicoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class StyliApplication {

	public static void main(String[] args) {
		SpringApplication.run(StyliApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(
			CategoriaRepository categoriaRepository,
			EmpresaRepository empresaRepository,
			FuncionarioRepository funcionarioRepository,
			ServicoRepository servicoRepository
	) {
		return args -> {
			System.out.println("Iniciando carga de dados...");

			// Criar categorias
			Categoria cabelo = new Categoria();
			cabelo.setNome("Cabelo");
			categoriaRepository.save(cabelo);

			Categoria unha = new Categoria();
			unha.setNome("Unha");
			categoriaRepository.save(unha);

			// Criar empresa
			Empresa empresa = Empresa.builder()
					.nome("Styli Beleza")
					.telefone("11999999999")
					.email("contato@styli.com")
					.senha("123456")
					.endereco("Rua Exemplo, 123")
					.horarioFuncionamento("08:00 - 18:00")
					.logoUrl("https://example.com/logo.png")
					.instagram("@styli")
					.destaque(true)
					.categorias(List.of(cabelo, unha))
					.build();
			empresaRepository.save(empresa);

			// Criar serviços
			Servico corte = Servico.builder()
					.nome("Corte Masculino")
					.duracaoMinutos(30)
					.build();
			servicoRepository.save(corte);

			Servico manicure = Servico.builder()
					.nome("Manicure Simples")
					.duracaoMinutos(45)
					.build();
			servicoRepository.save(manicure);

			// Criar funcionário
			Funcionario funcionario = Funcionario.builder()
					.nomeCompleto("João Silva")
					.nomeUsuario("joao")
					.senha("123456")
					.telefone("11988887777")
					.empresa(empresa)
					.servicos(List.of(corte, manicure))
					.build();
			funcionarioRepository.save(funcionario);

			// Relacionar funcionário ao serviço
			corte.setFuncionario(List.of(funcionario));
			manicure.setFuncionario(List.of(funcionario));
			servicoRepository.saveAll(List.of(corte, manicure));

			System.out.println("Carga de dados finalizada!");
		};
	}

}
