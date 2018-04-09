package br.senai.sp.info.pweb.jucacontrol.dao;

import java.util.List;

import br.senai.sp.info.pweb.jucacontrol.models.CategoriaOcorrencia;

public interface CategoriaOcorrenciaDAO {

	public void persistir(CategoriaOcorrencia obj);
	
	public CategoriaOcorrencia buscar(Long id);
	
	public List<CategoriaOcorrencia> buscarTodas();
	
	public void deletar(CategoriaOcorrencia obj);
	
	public void alterar(CategoriaOcorrencia obj);

	public CategoriaOcorrencia buscarPorNome(String nome);
	
}
