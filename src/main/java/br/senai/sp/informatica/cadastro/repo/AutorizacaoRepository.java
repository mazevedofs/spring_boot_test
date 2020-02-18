package br.senai.sp.informatica.cadastro.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.sp.informatica.cadastro.model.Autorizacao;

public interface AutorizacaoRepository extends JpaRepository<Autorizacao, String>{
}
