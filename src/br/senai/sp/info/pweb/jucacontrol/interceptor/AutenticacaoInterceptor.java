package br.senai.sp.info.pweb.jucacontrol.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AutenticacaoInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// Se a URL contém /app
		boolean necessitaAutenticacao = request.getRequestURI().contains("/app");
		boolean usuarioEstaLogado = request.getSession().getAttribute("usuarioAutenticado") != null;
		
		// Realizando filtro
		if (necessitaAutenticacao && !usuarioEstaLogado) {
			return false;
		}
		
			return true;
	}
	
}
