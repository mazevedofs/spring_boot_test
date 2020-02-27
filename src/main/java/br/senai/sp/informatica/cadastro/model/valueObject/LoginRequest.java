package br.senai.sp.informatica.cadastro.model.valueObject;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginRequest {
	@NotBlank
	private String username;
	@NotBlank
	private String password;
}
