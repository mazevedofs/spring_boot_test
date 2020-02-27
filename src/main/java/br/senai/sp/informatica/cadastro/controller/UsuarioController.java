package br.senai.sp.informatica.cadastro.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.informatica.cadastro.component.JsonError;
import br.senai.sp.informatica.cadastro.model.Usuario;
import br.senai.sp.informatica.cadastro.service.UsuarioService;

@RestController
@RequestMapping("/api")
public class UsuarioController {
	@Autowired
	private UsuarioService dao;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/salvaUsuario")
	public ResponseEntity<Object> salvaUsuario(@RequestBody @Valid Usuario usuario, BindingResult result) {
		if(result.hasErrors()) {
			return ResponseEntity.unprocessableEntity()
					.contentType(MediaType.APPLICATION_JSON)
					.body(JsonError.build(result));
		} else {
			usuario.setSenha(encoder.encode(usuario.getSenha()));
			dao.salvar(usuario);
			return ResponseEntity.ok().build();
		}
	}

	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@RequestMapping("/leAutorizacoes/{nome}")
	public ResponseEntity<GrantedAuthority> getAutorizacoes(
			@PathVariable("nome") String nome) {
		return ResponseEntity.ok(dao.getAutorizacoes(nome));		
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/listaUsuario")
	public ResponseEntity<List<Usuario>> listaUsuario() {
		return ResponseEntity.ok(dao.getUsuarios());
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/removeUsuario/{nome}")
	public ResponseEntity<Object> removeUsuario(@PathVariable("nome") String nome) {
		if (dao.removeUsuario(nome)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.unprocessableEntity().build();
		}	
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/editaUsuario/{nome}")
	public ResponseEntity<Object> editaUsuario(@PathVariable(value = "nome") String nome) {
		Usuario usuario = dao.getUsuario(nome);

		if (usuario != null) {
			usuario.setOld_nome(usuario.getNome());
			return ResponseEntity.ok(usuario);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
