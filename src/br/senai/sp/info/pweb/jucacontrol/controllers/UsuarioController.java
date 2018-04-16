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
		
		// Caso o usuário já esteja logado, redireciona-o para o /app
		if (session.getAttribute("usuarioAutenticado") != null) {
			return "redirect:/app";
		}
		
		model.addAttribute("usuario", new Usuario());
		return "index";
	}
	
	@GetMapping("/app/adm/usuario/editar")
	public String abrirEdicao(Model model, @RequestParam(name = "id", required = true) Long id, HttpServletResponse response) throws IOException {
		// Buscar usuário pelo id
		Usuario usuarioBuscado = usuarioDAO.buscar(id);
		model.addAttribute("usuario", usuarioBuscado); // Para resolver esse erro, é necessário usar o model. : Neither BindingResult nor plain target object for bean name 'usuario' available as request attribute
		return "usuario/form";
	}
	
	@GetMapping("/app/adm/usuario")
	public String abrirLista(Model model) {
		model.addAttribute("usuarios", usuarioDAO.buscarTodos());
		return "usuario/lista";
	}
	
	@GetMapping("/app/adm/usuario/novo")
	public String abrirFormNovoUsuario(Model model) {
		// Passando o usuário vazio para o model attribute
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
			@RequestParam(name = "administrador", required = false) Boolean isAdministrador) { // Para editar, o requerido da confirmação passa a ser false, se não ocorrerá o erro 400
		
		// Verificando se cadastro
		if (usuario.getId() == null) {
			
			// Checando se a confirmação de senha NÃO for igual a senha
			if (!confirmacao.equals(usuario.getSenha())) {
				brUsuario.addError(new FieldError("usuario", "senha", "Passwords doesn't match")); // Nome do objeto, campo trabalhado (tem que ter no model), mensagem mostrada
			}
			
			// Verificando se o email já existe
			if (usuarioDAO.buscarPorEmail(usuario.getEmail()) != null) {
				brUsuario.addError(new FieldError("usuario", "email", "This e-mail '" + usuario.getEmail() + "' is already being used"));
			}
			
			// Verificando se o binding result contém erros (valida o objeto inteiro)
			if (brUsuario.hasErrors()) {
				System.out.println(brUsuario);
				System.out.println("-_-_-_-_-_-_-_-_-_-_-");
				return "usuario/form"; // Reabre a tela de cadastro
			}
			
		}else { // Verificando se é alteração
			if (brUsuario.hasFieldErrors("nome") || brUsuario.hasFieldErrors("sobrenome")) {
				return "usuario/form";
			}
		}	
		
		// Verificar o tipo de usuário
		if (isAdministrador != null) { // O administrador checkbox é OU true OU null
			usuario.setTipo(TiposUsuario.ADMINISTRADOR);
		} else {
			usuario.setTipo(TiposUsuario.COMUM);
		}
		
		
		if (usuario.getId() == null) {
			// Armazena o usuário no banco de dados
			usuario.hashearSenha();
			usuarioDAO.persistir(usuario);
		}else {
			// Buscar o usuári do banco
			Usuario usuarioBanco = usuarioDAO.buscar(usuario.getId());

			//Altera com os dados que recebi do formulário
			usuarioBanco.setNome(usuario.getNome());
			usuarioBanco.setSobrenome(usuario.getSobrenome());
			usuarioBanco.setTipo(usuario.getTipo());
			
			// Altera o usuário
			usuarioDAO.alterar(usuarioBanco);
		}
		
		return "redirect:/app/adm/usuario";
	}
	
	@PostMapping({"/usuario/autenticar"})
	//@Valid - DEtermina que o Spring deve validar o objeto
	//BindingResult - Armazena os possíveis erros de validação que ocorreram no objeto. É uma regra estar a direita e logo após do @Valid
	//Essa parte de validação tem relação com as validações do modelo devido as anotações e.g: @NotNull e @Size
	public String autenticar(@Valid Usuario usuario, BindingResult brUsuario, HttpSession sessao) {
		
		// Explicação de HttpSession(1) e SessionFactory(2)
		// (1) guarda dados em uma sessão (arquivo que fica no servidor, armazenamento de forma rápida), é tipo um mini banco de dados para web. TECNICAMENTE é algo simples que guarda poucas informações, ex: login e senha
		// (2) gera uma sessão com o banco de dados. é um objeto que o hibernate usa

		if (brUsuario.hasFieldErrors("email") || brUsuario.hasFieldErrors("senha")) {
			System.out.println("Capturou os erros");
			System.out.println("-_-_-_-_-_-_-_-_-_-");
			return "index";
		}
		
		// Verificando se o usuário existe, antes de buscar email e senha, devemos hashear a senha do usuário para o login
		usuario.hashearSenha();
		Usuario usuarioAutenticado = usuarioDAO.buscarPorEmailESenha(usuario.getEmail(), usuario.getSenha());
		
		// Verificando se o usuário não existe
		if (usuarioAutenticado == null) {
			brUsuario.addError(new FieldError("usuario", "email", "E-mail ou senha inválidos"));
			return "index";
		}
		
		// Aplica o usuário autenticado na sessão (arquivo que salva os dados do usuário logado no servidor)
		sessao.setAttribute("usuarioAutenticado", usuarioAutenticado); // No caso para o (1) ele criaria um objeto e colocaria dentro de um arquivo do servidor (arquivo cheio de bytes)
		// No mercado de trabalho é muito comum guardar o menor número de informações possível, e.g: o Felipe usa uma nova classe só para guardar os ids e apenas o ID é armazenado na sessão
		
		return "redirect:/app/";
	}
	
	@GetMapping({"/sair"})
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

}
