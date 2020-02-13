package br.senai.sp.informatica.cadastro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.senai.sp.informatica.cadastro.component.JsonError;
import br.senai.sp.informatica.cadastro.model.Cliente;
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

	@PostMapping("/cadastra")
	public ResponseEntity<Object> cadastrar(@RequestBody Cliente cliente, 
			BindingResult result) {
		if (result.hasErrors()) {
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

	@RequestMapping("/editaCliente/{clienteId}")
	public ResponseEntity<Object> 
	editaCliente(@PathVariable("clienteId") int id) {
		var cliente = clienteDao.getCliente(id);

		if (cliente != null) {
			return ResponseEntity.ok(cliente);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping("/removeCliente")
	public ResponseEntity<Object> removeCliente (@RequestBody int[] lista){
		clienteDao.removeCliente(lista);
		return ResponseEntity.ok().build();
	}

	@RequestMapping("/carregaServicos/{clienteid}")
	public ResponseEntity<Object> carregaServicos(@PathVariable("clienteId") int id){
		var cliente = clienteDao.getCliente(id);
		if(cliente != null) {
			return ResponseEntity.ok(servicoDao.getServicos(cliente));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
