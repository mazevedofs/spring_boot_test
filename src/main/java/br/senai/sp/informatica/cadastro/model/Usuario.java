package br.senai.sp.informatica.cadastro.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import br.senai.sp.informatica.cadastro.model.validacao.Senha;
import lombok.Data;

@Data
@Entity
@Table(name="users")
public class Usuario {
	@Id
	@Column(name="username")
	@Size(min = 5, max = 15,
			message = "O nome deve ter entre 5 e 15 caracteres")
	private String nome;
	@Transient
	private String old_nome = "";
	@Column(name="password")
	@Senha(message="A senha nao tem a complexidade exigida,"
			+ " os Nºs , Maiuscula e #&$%")
	private String senha;
	@Column(name="enabled")
	private boolean habilitado = true;
	@Transient
	private boolean administrador;
	@Transient
	private boolean editar;
}
