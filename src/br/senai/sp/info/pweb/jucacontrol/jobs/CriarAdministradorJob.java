package br.senai.sp.info.pweb.jucacontrol.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import br.senai.sp.info.pweb.jucacontrol.dao.UsuarioDAO;
import br.senai.sp.info.pweb.jucacontrol.models.TiposUsuario;
import br.senai.sp.info.pweb.jucacontrol.models.Usuario;

@Component
public class CriarAdministradorJob implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent e) {
		//Tudo que acontecer aqui � o evento disparando
		System.out.println("[JOB] - Cria��o de Administrador");
		// Criando objeto do usu�rio administrador padr�o
		Usuario admin = new Usuario();
		admin.setEmail("admni@email.com");
		admin.setNome("Administrador");
		admin.setSenha("admin");
		admin.setSobrenome("do Sistema");
		admin.setTipo(TiposUsuario.ADMINISTRADOR);
		admin.hashearSenha();
		
		System.out.println("[JOB] - Criando usu�rio administrador...");
		usuarioDAO.persistir(admin);
		System.out.println("[JOB] - Usu�rio administrador pronto para uso");
	}

}