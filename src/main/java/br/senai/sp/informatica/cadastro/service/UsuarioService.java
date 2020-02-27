package br.senai.sp.informatica.cadastro.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import br.senai.sp.informatica.cadastro.component.SecurityFacade;
import br.senai.sp.informatica.cadastro.model.Autorizacao;
import br.senai.sp.informatica.cadastro.model.Usuario;
import br.senai.sp.informatica.cadastro.repo.AutorizacaoRepository;
import br.senai.sp.informatica.cadastro.repo.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository user;
	@Autowired
	private AutorizacaoRepository auth;
	@Autowired
	private SecurityFacade security;
	
	public List<Usuario> getUsuarios() {
		return user.findAll().stream()
				.filter(usuario -> !usuario.getNome()
						.equals(security.getUserName()) )
				.map(usuario -> atribuirPerfil(usuario))
					.collect(Collectors.toList());
	}
	   
	private Usuario atribuirPerfil(Usuario usuario) {
		Autorizacao autorizacao = 
				auth.findById(usuario.getNome()).orElse(null);
		if(autorizacao != null) {
			usuario.setAdministrador(
				autorizacao.getPerfil().endsWith("ADMIN") );
		} else {
			usuario.setAdministrador(false);
		}
		
		return usuario;
	}
	
	
	public void salvar(Usuario usuario) {
		Usuario old_usuario;
		
		if(!usuario.getOld_nome().equalsIgnoreCase(usuario.getNome())) {
			old_usuario = getUsuario(usuario.getOld_nome());
			removeUsuario(usuario.getOld_nome());
		} else {
			old_usuario = getUsuario(usuario.getNome());
		}
		
		if(usuario.isAdministrador()) {
		  auth.save(
			 new Autorizacao(usuario.getNome(), "ROLE_ADMIN"));
		} else {
		  auth.save(
			new Autorizacao(usuario.getNome(), "ROLE_USER"));
		}
		
		if(old_usuario != null) {
			usuario.setSenha(old_usuario.getSenha());
		}
		user.save(usuario);
	}

	public boolean removeUsuario(String nome) {
		Usuario usuario = user.findById(nome).orElse(null);
		if(usuario != null) {
			Autorizacao autorizacao = auth.findById(nome).orElse(null);
			if(autorizacao != null) {
				auth.delete(autorizacao);
			}
			
			user.delete(usuario);
			
			return true;
		} else {
			return false;
		}
	}
	
	public GrantedAuthority getAutorizacoes(String nome) {
		Autorizacao autorizacao = auth.findById(nome).orElse(null);
		return autorizacao != null ? () -> autorizacao.getPerfil() : null; 
	}
	
	public Usuario getUsuario(String nome) {
		Usuario usuario = user.findById(nome).orElse(null);
		if(usuario != null) {
			atribuirPerfil(usuario);
		}
		return usuario;
	}
}
