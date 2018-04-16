package br.senai.sp.info.pweb.jucacontrol.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
	public String abrirLogin(Model model, HttpSession session) {
		
		// Caso o usu�rio j� esteja logado, redireciona-o para o /app
		if (session.getAttribute("usuarioAutenticado") != null) {
			return "redirect:/app";
		}
		
		model.addAttribute("usuario", new Usuario());
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
		usuarioDAO.deletar(usuarioDAO.buscar(id));
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
	public String autenticar(@Valid Usuario usuario, BindingResult brUsuario, HttpSession sessao) {
		
		// Explica��o de HttpSession(1) e SessionFactory(2)
		// (1) guarda dados em uma sess�o (arquivo que fica no servidor, armazenamento de forma r�pida), � tipo um mini banco de dados para web. TECNICAMENTE � algo simples que guarda poucas informa��es, ex: login e senha
		// (2) gera uma sess�o com o banco de dados. � um objeto que o hibernate usa

		if (brUsuario.hasFieldErrors("email") || brUsuario.hasFieldErrors("senha")) {
			System.out.println("Capturou os erros");
			System.out.println("-_-_-_-_-_-_-_-_-_-");
			return "index";
		}
		
		// Verificando se o usu�rio existe, antes de buscar email e senha, devemos hashear a senha do usu�rio para o login
		usuario.hashearSenha();
		Usuario usuarioAutenticado = usuarioDAO.buscarPorEmailESenha(usuario.getEmail(), usuario.getSenha());
		
		// Verificando se o usu�rio n�o existe
		if (usuarioAutenticado == null) {
			brUsuario.addError(new FieldError("usuario", "email", "E-mail ou senha inv�lidos"));
			return "index";
		}
		
		// Aplica o usu�rio autenticado na sess�o (arquivo que salva os dados do usu�rio logado no servidor)
		sessao.setAttribute("usuarioAutenticado", usuarioAutenticado); // No caso para o (1) ele criaria um objeto e colocaria dentro de um arquivo do servidor (arquivo cheio de bytes)
		// No mercado de trabalho � muito comum guardar o menor n�mero de informa��es poss�vel, e.g: o Felipe usa uma nova classe s� para guardar os ids e apenas o ID � armazenado na sess�o
		
		return "redirect:/app/";
	}
	
	@GetMapping({"/sair"})
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

}
