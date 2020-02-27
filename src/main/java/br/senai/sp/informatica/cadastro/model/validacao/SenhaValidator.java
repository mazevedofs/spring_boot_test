package br.senai.sp.informatica.cadastro.model.validacao;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SenhaValidator implements ConstraintValidator<Senha, String> {
	private BiFunction<String, Predicate<Integer>, Boolean> regra = (texto, condicao) -> texto.chars()
			.filter(c -> condicao.test(c))
			.findAny()
			.isPresent();

	private static final Logger logger = LoggerFactory.getLogger(SenhaValidator.class);
	
	/*
	 * Valida a senha utilizando as seguintes regras:
	 * 
	 * - pelo menos um caracter especial
	 * - pelo menos uma letra maiuscula
	 * - conter numeros
	 * - ter o tamanho minimo de 8 caracteres
	 */

	@Override
	public boolean isValid(String senha, ConstraintValidatorContext context) {
		logger.info("Senha: " + senha);
		
		return !(senha.length() < 8) &&
				regra.apply(senha, c -> c == '#' || c == '&' || c == '$' || c == '%') &&
				regra.apply(senha, Character::isUpperCase) &&
				regra.apply(senha, Character::isDigit);
	}
}