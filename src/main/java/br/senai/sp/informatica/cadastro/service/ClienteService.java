package br.senai.sp.informatica.cadastro.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.senai.sp.informatica.cadastro.model.Cliente;
import br.senai.sp.informatica.cadastro.repo.ClienteRepo;

@Service
public class ClienteService {
	@Autowired
	private ClienteRepo repo;
	
	public void salvar(Cliente cliente) {
		repo.save(cliente);
	}
	
	public List<Cliente> getClientes() {
		return repo.findAll().stream()
				.filter(cliente -> !cliente.isDesativado())
				.collect(Collectors.toList());
	}
	
	private boolean ok;
	
	public boolean removeCliente(int[] lista) {
		ok = true;
		
		Arrays.stream(lista).forEach(id -> {
			Cliente cliente = getCliente(id);
			if(cliente != null) {
				cliente.setDesativado(true);
				salvar(cliente);
			} else {
				ok = false;
			}
		});
		
		return ok;
	}
	
	public Cliente getCliente(int id) {
		return repo.findById(id).orElse(null);
	}
}
