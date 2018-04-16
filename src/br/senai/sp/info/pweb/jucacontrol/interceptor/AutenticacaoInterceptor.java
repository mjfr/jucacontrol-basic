package br.senai.sp.info.pweb.jucacontrol.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import br.senai.sp.info.pweb.jucacontrol.models.TiposUsuario;
import br.senai.sp.info.pweb.jucacontrol.models.Usuario;

@Component
public class AutenticacaoInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// Se a URL contém /app
		boolean necessitaAutenticacao = request.getRequestURI().contains("/app");
		boolean necessitaSerAdm = request.getRequestURI().contains("/adm");
		Usuario usuarioAutenticado = (Usuario) request.getSession().getAttribute("usuarioAutenticado");
		boolean usuarioEstaLogado = usuarioAutenticado != null;
		// Variável booleana recebe um termo chamado de flag, (verdadeiro = bandeira levantada, falso = bandeira abaixada) but why??
		
		// Realizando filtro
		if (necessitaAutenticacao && !usuarioEstaLogado) {
			return false;
//		}else if(necessitaSerAdm && usuarioAutenticado.getAdministrador()) {
		}else if(necessitaSerAdm && usuarioAutenticado.getTipo() != TiposUsuario.ADMINISTRADOR) {
			return false;
		}
			return true;
	}
	
}
