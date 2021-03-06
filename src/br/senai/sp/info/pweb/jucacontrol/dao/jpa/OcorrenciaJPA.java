package br.senai.sp.info.pweb.jucacontrol.dao.jpa;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.senai.sp.info.pweb.jucacontrol.dao.OcorrenciaDAO;
import br.senai.sp.info.pweb.jucacontrol.models.BuscarPorSituacaoOcorrencia;
import br.senai.sp.info.pweb.jucacontrol.models.Ocorrencia;

@Transactional
@Repository
public class OcorrenciaJPA implements OcorrenciaDAO{

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void persistir(Ocorrencia obj) {
		sessionFactory.getCurrentSession().persist(obj);
		
	}

	@Override
	public Ocorrencia buscar(Long id) {
		String hql = "FROM Ocorrencia c WHERE c.id = :id";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("id", id);
		
		List<Ocorrencia> resultados = query.list();
		
		if (!resultados.isEmpty()) {
			return resultados.get(0);
		}
		return null;
	}

	@Override
	public List<Ocorrencia> buscarTodas() {
		String hql = "FROM Ocorrencia";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}

	@Override
	public void deletar(Ocorrencia obj) {
		sessionFactory.getCurrentSession().delete(obj);
		
	}

	@Override
	public void alterar(Ocorrencia obj) {
		sessionFactory.getCurrentSession().update(obj);
		
	}

	@Override
	public List<Ocorrencia> buscarPorSituacao(BuscarPorSituacaoOcorrencia situacao) {
		String hql = "FROM Ocorrencia o "; // Colocar um final em branco para depois 'grudar' com os filtros
		
		// Verificando o filtro que ser� aplicado no HQL
		switch (situacao) {
		case AGUARDANDO:
			hql += "WHERE o.tecnico IS NULL";
			break;
		case EM_ATENDIMENTO:
			hql += "WHERE o.tecnico IS NOT NULL AND o.dataConclusao IS NULL";
			break;
		case ENCERRADOS:
			hql += "WHERE o.dataConclusao IS NOT NULL";
			break;
		}
		
		// Executando a query
		return sessionFactory.getCurrentSession().createQuery(hql).list();
	}
}