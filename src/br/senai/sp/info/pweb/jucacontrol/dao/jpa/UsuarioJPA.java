package br.senai.sp.info.pweb.jucacontrol.dao.jpa;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.senai.sp.info.pweb.jucacontrol.dao.UsuarioDAO;
import br.senai.sp.info.pweb.jucacontrol.models.Usuario;

@Repository
public class UsuarioJPA implements UsuarioDAO{

	@Autowired // Vindo do PersistenceConfig
	private SessionFactory sessionFactory;
	
	@Override
	public void alterar(Usuario obj) {
		
	}

	@Override
	public Usuario buscar(Long id) {
		
		return null;
	}

	@Override
	public List<Usuario> buscarTodos() {
		
		return null;
	}

	@Override
	public void deletar(Usuario obj) {
		
	}

	@Transactional
	@Override
	public void persistir(Usuario obj) {
		sessionFactory.getCurrentSession().persist(obj);
	}
	
}