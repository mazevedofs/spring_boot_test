package br.senai.sp.informatica.cadastro.model.validacao;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LogradouroValidator 
	implements ConstraintValidator<Logradouro, String>{
	private Logradouro anotacao;
	
	@Override
	public void initialize(Logradouro constraintAnnotation) {
		anotacao = constraintAnnotation;
	}

	
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		int max = anotacao.max();
		
		if(value == null || value.length() > max) {
			return false;
		} else {
			String[] logradouro = value.split(" ");
			
			if( (logradouro[0].equalsIgnoreCase("rua") ||
			     logradouro[0].equalsIgnoreCase("av.") ||
			     logradouro[0].equalsIgnoreCase("praça") ||
			     logradouro[0].equalsIgnoreCase("al.") ||
			     logradouro[0].equalsIgnoreCase("alameda") ||
			     logradouro[0].equalsIgnoreCase("estr.") ||
			     logradouro[0].equalsIgnoreCase("estrada") ) &&
				 logradouro.length > 1 ) {
				return true;
			} else {
				return false;
			}
		}
	}

}
