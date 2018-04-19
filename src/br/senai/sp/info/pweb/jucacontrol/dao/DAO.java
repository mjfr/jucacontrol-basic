package br.senai.sp.info.pweb.jucacontrol.dao;

import java.util.List;

import br.senai.sp.info.pweb.jucacontrol.models.Ocorrencia;

public interface DAO<T> {

public void persistir(T obj);
	
	public T buscar(Long id);
	
	public List<T> buscarTodas();
	
	public void deletar(T obj);
	
	public void alterar(T obj);
	
}
