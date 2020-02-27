package br.senai.sp.informatica.cadastro.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.senai.sp.informatica.cadastro.model.Cliente;
import br.senai.sp.informatica.cadastro.model.Servico;
import br.senai.sp.informatica.cadastro.repo.ServicoRepo;
import lombok.var;

@Service
public class ServicoService {
	@Autowired
	private ServicoRepo repo;
	
	// Retorna a lista de todos os serviços existentes
	public List<Servico> getServicos() {
		return repo.findAll().stream()
				.filter(servico -> !servico.isDesativado())
				.collect(Collectors.toList());
	}

	// Retorna a lista dos serviços de um determinado cliente
	public List<Servico> getServicos(Cliente cliente) {
		var servicosDoCliente = cliente.getServicos();
		return repo.findAll().stream()
				.filter(servico -> !servico.isDesativado())
				.map(servico -> {
					servico.setSelecionado(
							servicosDoCliente.contains(servico));
					return servico;
				}).collect(Collectors.toList());
	}
	
	public void salvar(Servico servico) {
		repo.save(servico);
	}
	
	public boolean removeServico(int id) {
		var servico = getServico(id);
		if(servico != null) {
			servico.setDesativado(true);
			salvar(servico);
			return true;
		} else {
			return false;
		}
	}
	
	public Servico getServico(int id) {
		return repo.findById(id).orElse(null);
	}
}
