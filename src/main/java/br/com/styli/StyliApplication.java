package br.com.styli;

import br.com.styli.domain.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class StyliApplication {

	public static void main(String[] args) {
		SpringApplication.run(StyliApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(
			ClienteRepository clienteRepository,
			FormaPagamentoRepository formaPagamentoRepository,
			OrdemDeServicoRepository ordemDeServicoRepository,
			ItemRepository itemRepository,
			AgendamentoRepository adicionalRepository) {

		return args -> {

			// Criando Clientes
			Cliente cliente1 = clienteRepository.save(Cliente.builder().nome("João Silva").build());
			Cliente cliente2 = clienteRepository.save(Cliente.builder().nome("Maria Oliveira").build());

			// Criando Formas de Pagamento
			FormaPagamento cartaoCredito = formaPagamentoRepository.save(
					FormaPagamento.builder().nome("Cartão Nubank").tipo("Crédito").diaVencimento(10).build());

			FormaPagamento dinheiro = formaPagamentoRepository.save(
					FormaPagamento.builder().nome("Dinheiro").tipo("Dinheiro").diaVencimento(null).build());

			// Criando Ordens de Serviço
			for (int i = 1; i <= 5; i++) {
				Cliente cliente = (i % 2 == 0) ? cliente2 : cliente1;

				OrdemDeServico os = ordemDeServicoRepository.save(
						OrdemDeServico.builder()
								.cliente(cliente)
								.descricao("Ordem de Serviço " + i)
								.titulo("Titulo " + i)
								.valorTotal(BigDecimal.valueOf(500 + i * 100))
								.dataAtualizacao(LocalDateTime.now())
								.build()
				);

				// Criando Itens
				List<Item> itens = Arrays.asList(
						Item.builder()
								.ordemDeServico(os)
								.descricao("Produto A" + i)
								.valor(BigDecimal.valueOf(100 + i * 10))
								.fornecedor("Fornecedor X")
								.formaPagamento(cartaoCredito)
								.dataPagamento(LocalDate.now().minusDays(i))
								.build(),

						Item.builder()
								.ordemDeServico(os)
								.descricao("Produto B" + i)
								.valor(BigDecimal.valueOf(150 + i * 10))
								.fornecedor("Fornecedor Y")
								.formaPagamento(dinheiro)
								.dataPagamento(LocalDate.now().minusDays(i + 2))
								.build()
				);

				itemRepository.saveAll(itens);

				// Criando Adicionais
				List<Adicional> adicionais = Arrays.asList(
						Adicional.builder()
								.ordemDeServico(os)
								.descricao("Serviço extra " + i)
								.valor(BigDecimal.valueOf(50 + i * 5))
								.dataAtualizacao(LocalDateTime.now())
								.build(),

						Adicional.builder()
								.ordemDeServico(os)
								.descricao("Garantia extendida " + i)
								.valor(BigDecimal.valueOf(30 + i * 2))
								.dataAtualizacao(LocalDateTime.now())
								.build()
				);

				adicionalRepository.saveAll(adicionais);
			}

			System.out.println("Banco de dados populado com 5 ordens de serviço!");
		};
	}
}
