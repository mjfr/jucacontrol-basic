package br.senai.sp.info.pweb.jucacontrol.dao;

import java.util.List;

import br.senai.sp.info.pweb.jucacontrol.models.BuscarPorSituacaoOcorrencia;
import br.senai.sp.info.pweb.jucacontrol.models.Ocorrencia;

public interface OcorrenciaDAO extends DAO<Ocorrencia>{

//	public void persistir(Ocorrencia obj);
//	
//	public Ocorrencia buscar(Long id);
//	
//	public List<Ocorrencia> buscarTodas();
//	
//	public void deletar(Ocorrencia obj);
//	
//	public void alterar(Ocorrencia obj);
	
	public List<Ocorrencia> buscarPorSituacao(BuscarPorSituacaoOcorrencia situacao);
}
