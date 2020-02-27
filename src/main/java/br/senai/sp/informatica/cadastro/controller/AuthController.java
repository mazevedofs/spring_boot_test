package br.senai.sp.informatica.cadastro.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.informatica.cadastro.component.JwtTokenProvider;
import br.senai.sp.informatica.cadastro.model.valueObject.JwtAuthenticationResponse;
import br.senai.sp.informatica.cadastro.model.valueObject.LoginRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider provider;
	
	private Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@PostMapping("/signin")
	public ResponseEntity<?> autenticaUsuario(
			@Valid @RequestBody LoginRequest login) {
		
		logger.debug("Usuario: " + login.getUsername() +
				" Senha: " + login.getPassword());
		
		Authentication autenticacao = authenticationManager.authenticate(	
				new UsernamePasswordAuthenticationToken(
						login.getUsername(), 
						login.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(autenticacao);
		
		String jwt = provider.criarToken(autenticacao);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}
}
