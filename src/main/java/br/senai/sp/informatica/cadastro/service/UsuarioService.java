package br.senai.sp.informatica.cadastro.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public List<Usuario> getUsuarios() {
		return user.findAll().stream()
					.collect(Collectors.toList());
	}
	   
	public void salvar(Usuario usuario) {
		Usuario old_usuario;
		
		if(!usuario.getOld_nome().equalsIgnoreCase(usuario.getNome())) {
			old_usuario = getUsuario(usuario.getOld_nome());
			removeUsuario(usuario.getOld_nome());
		} else {
			old_usuario = getUsuario(usuario.getNome());
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
	
	
	public Usuario getUsuario(String nome) {
		Usuario usuario = user.findById(nome).orElse(null);
		if(usuario != null) {
			Autorizacao autorizacao = auth.findById(nome).orElse(null);
			if(autorizacao != null) {
				usuario.setAdministrador(autorizacao.getPerfil().endsWith("ADMIN"));
			} else {
				usuario.setAdministrador(false);
			}
		}
		return usuario;
	}
}
