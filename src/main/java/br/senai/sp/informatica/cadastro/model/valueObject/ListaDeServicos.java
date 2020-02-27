package br.senai.sp.informatica.cadastro.model.valueObject;

import br.senai.sp.informatica.cadastro.model.Servico;
import lombok.Data;

@Data
public class ListaDeServicos {
	private int idCliente;
	private Servico[] servicos;
}
