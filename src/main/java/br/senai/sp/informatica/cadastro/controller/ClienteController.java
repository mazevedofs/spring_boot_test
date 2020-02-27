package br.senai.sp.informatica.cadastro.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.senai.sp.informatica.cadastro.component.JsonError;
import br.senai.sp.informatica.cadastro.model.Cliente;
import br.senai.sp.informatica.cadastro.model.Servico;
import br.senai.sp.informatica.cadastro.model.valueObject.ListaDeServicos;
import br.senai.sp.informatica.cadastro.service.ClienteService;
import br.senai.sp.informatica.cadastro.service.ServicoService;
import lombok.var;

@Controller
@RequestMapping("/api")
public class ClienteController {
	@Autowired
	private ClienteService clienteDao;
	@Autowired
	private ServicoService servicoDao;
	
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping("/cadastra")
	public ResponseEntity<Object> cadastra(@RequestBody @Valid Cliente cliente, BindingResult result) {
		if(result.hasErrors()) {
			return ResponseEntity.unprocessableEntity()
					.contentType(MediaType.APPLICATION_JSON)
					.body(JsonError.build(result));
		} else {
			clienteDao.salvar(cliente);
			return ResponseEntity.ok().build();
		}
	}
	
	@RequestMapping("/listaCliente")
	public ResponseEntity<List<Cliente>> listaCliente() {
		return ResponseEntity.ok(clienteDao.getClientes());
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@RequestMapping("/editaCliente/{clienteId}")
	public ResponseEntity<Object> editaCliente(@PathVariable("clienteId") int id) {
		var cliente = clienteDao.getCliente(id);
		
		if(cliente != null) {
			return ResponseEntity.ok(cliente);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@RequestMapping("/removeCliente")
	public ResponseEntity<Object> removeCliente(@RequestBody int[] lista) {
		if(clienteDao.removeCliente(lista)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.unprocessableEntity().build();
		}
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@RequestMapping("/carregaServicos/{clienteId}")
	public ResponseEntity<Object> carregaServicos(@PathVariable("clienteId") int id) {
		var cliente = clienteDao.getCliente(id);
		
		if(cliente != null) {
			return ResponseEntity.ok(servicoDao.getServicos(cliente));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping("/selecionaServico")
	public ResponseEntity<Object> selecionaServico(@RequestBody ListaDeServicos lista) {
		// Localizar o Cliente pelo id
		var cliente = clienteDao.getCliente(lista.getIdCliente());
		
		// Se o cliente foi encontrado
		if(cliente != null) {
			// Garanta que a lista de serviços não é nula
			if(cliente.getServicos() == null)
				cliente.setServicos(new ArrayList<>());
			
			// Cria a lista com os IDs de Serviços que serão excluidos do cliente
			var aExcluir = cliente.getServicos().stream()
					.filter(servico -> !Arrays.stream(lista.getServicos())
						.filter(srv -> srv.getIdServico() == servico.getIdServico())
						.findFirst().get().isSelecionado())
					.collect(Collectors.toList());
					
			// Excluir os servicos do cliente que constam na lista
		    aExcluir.stream().forEach(servico -> 
		    		cliente.getServicos()
		    			.removeIf(srv -> srv.getIdServico() == servico.getIdServico())
		    		);
			
		    // Cria a lista com os IDs de Srviço que serão incluidos para o cliente
		    var aIncluir = Arrays.stream(lista.getServicos())
		    		.filter(Servico::isSelecionado)
		    		.filter(srv -> !cliente.getServicos().contains(srv))
		    		.collect(Collectors.toList());
		    
		    // Inclui os servicos na lista do cliente
		    aIncluir.stream().forEach(srv -> cliente.getServicos().add(srv));
		    
		    clienteDao.salvar(cliente);
		    
		    return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.unprocessableEntity().build();
		}
	}
}























