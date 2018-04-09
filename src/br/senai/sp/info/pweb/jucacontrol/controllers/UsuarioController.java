package br.senai.sp.info.pweb.jucacontrol.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.senai.sp.info.pweb.jucacontrol.dao.UsuarioDAO;
import br.senai.sp.info.pweb.jucacontrol.models.TiposUsuario;
import br.senai.sp.info.pweb.jucacontrol.models.Usuario;

@Controller
public class UsuarioController {
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@GetMapping(value = {"/", ""})
	public String abrirLogin(Model model) {
		
		return "index";
	}
	
	@GetMapping("/app/adm/usuario/editar")
	public String abrirEdicao(Model model, @RequestParam(name = "id", required = true) Long id, HttpServletResponse response) throws IOException {
		// Buscar usu�rio pelo id
		Usuario usuarioBuscado = usuarioDAO.buscar(id);
		model.addAttribute("usuario", usuarioBuscado); // Para resolver esse erro, � necess�rio usar o model. : Neither BindingResult nor plain target object for bean name 'usuario' available as request attribute
		return "usuario/form";
	}
	
	@GetMapping("/app/adm/usuario")
	public String abrirLista(Model model) {
		model.addAttribute("usuarios", usuarioDAO.buscarTodos());
		return "usuario/lista";
	}
	
	@GetMapping("/app/adm/usuario/novo")
	public String abrirFormNovoUsuario(Model model) {
		// Passando o usu�rio vazio para o model attribute
		model.addAttribute("usuario", new Usuario());
		return "usuario/form";
	}
	
	@GetMapping("/app/perfil")
	public String abrirFormEditarUsuarioLogado(Model model) {
		
		return "usuario/form";
	}
	
	@GetMapping("/app/alterarSenha")
	public String abrirFormAlterarSenha(Model model) {
		
		return "usuario/alterarSenha";
	}
	
	@GetMapping("/app/adm/usuario/deletar")
	public String deletar(@RequestParam(required = true) Long id, HttpServletResponse response) throws IOException {
		
		return "redirect:/app/adm/usuario";
	}
	
	@PostMapping( value = {"/app/adm/usuario/salvar"})
	public String salvar(@Valid Usuario usuario, BindingResult brUsuario, @RequestParam(name = "confirmacaoSenha", required = false) String confirmacao,
			@RequestParam(name = "administrador", required = false) Boolean isAdministrador) { // Para editar, o requerido da confirma��o passa a ser false, se n�o ocorrer� o erro 400
		
		// Verificando se cadastro
		if (usuario.getId() == null) {
			
			// Checando se a confirma��o de senha N�O for igual a senha
			if (!confirmacao.equals(usuario.getSenha())) {
				brUsuario.addError(new FieldError("usuario", "senha", "Passwords doesn't match")); // Nome do objeto, campo trabalhado (tem que ter no model), mensagem mostrada
			}
			
			// Verificando se o email j� existe
			if (usuarioDAO.buscarPorEmail(usuario.getEmail()) != null) {
				brUsuario.addError(new FieldError("usuario", "email", "This e-mail '" + usuario.getEmail() + "' is already being used"));
			}
			
			// Verificando se o binding result cont�m erros (valida o objeto inteiro)
			if (brUsuario.hasErrors()) {
				System.out.println(brUsuario);
				System.out.println("-_-_-_-_-_-_-_-_-_-_-");
				return "usuario/form"; // Reabre a tela de cadastro
			}
			
		}else { // Verificando se � altera��o
			if (brUsuario.hasFieldErrors("nome") || brUsuario.hasFieldErrors("sobrenome")) {
				return "usuario/form";
			}
		}	
		
		// Verificar o tipo de usu�rio
		if (isAdministrador != null) { // O administrador checkbox � OU true OU null
			usuario.setTipo(TiposUsuario.ADMINISTRADOR);
		} else {
			usuario.setTipo(TiposUsuario.COMUM);
		}
		
		
		if (usuario.getId() == null) {
			// Armazena o usu�rio no banco de dados
			usuario.hashearSenha();
			usuarioDAO.persistir(usuario);
		}else {
			// Buscar o usu�ri do banco
			Usuario usuarioBanco = usuarioDAO.buscar(usuario.getId());

			//Altera com os dados que recebi do formul�rio
			usuarioBanco.setNome(usuario.getNome());
			usuarioBanco.setSobrenome(usuario.getSobrenome());
			usuarioBanco.setTipo(usuario.getTipo());
			
			// Altera o usu�rio
			usuarioDAO.alterar(usuarioBanco);
		}
		
		return "redirect:/app/adm/usuario";
	}
	
	@PostMapping({"/usuario/autenticar"})
	//@Valid - DEtermina que o Spring deve validar o objeto
	//BindingResult - Armazena os poss�veis erros de valida��o que ocorreram no objeto. � uma regra estar a direita e logo ap�s do @Valid
	//Essa parte de valida��o tem rela��o com as valida��es do modelo devido as anota��es e.g: @NotNull e @Size
	public String autenticar(@Valid Usuario usuario, BindingResult brUsuario) {

		if (brUsuario.hasFieldErrors("email") || brUsuario.hasFieldErrors("senha")) {
			System.out.println("Capturou os erros");
			System.out.println("-------------------");
			return "index";
		}
		
		return "redirect:/app/";
	}
	
	@GetMapping({"/sair"})
	public String logout() {
		
		return "redirect:/";
	}

}
