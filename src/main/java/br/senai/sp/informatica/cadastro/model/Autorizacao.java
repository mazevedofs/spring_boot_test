package br.senai.sp.informatica.cadastro.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="authorities")
public class Autorizacao {
	@Id
	@Column(name="username", length=15)
	private String nome;
	@Column(name="authority")
	private String perfil;
}
