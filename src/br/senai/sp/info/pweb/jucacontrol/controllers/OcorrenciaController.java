package br.senai.sp.info.pweb.jucacontrol.controllers;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sp.info.pweb.jucacontrol.dao.CategoriaOcorrenciaDAO;
import br.senai.sp.info.pweb.jucacontrol.dao.OcorrenciaDAO;
import br.senai.sp.info.pweb.jucacontrol.models.Ocorrencia;
import br.senai.sp.info.pweb.jucacontrol.models.Usuario;

@Controller
@RequestMapping("/app")
public class OcorrenciaController {
		
	@Autowired
	private CategoriaOcorrenciaDAO categoriaOcorrenciaDAO;
	
	@Autowired
	private OcorrenciaDAO ocorrenciaDAO;
	
	@GetMapping({"", "/"})
	public String abrirListaOcorrencia(@RequestParam(name = "id", required = false) Long id, Model model) {
		if (id != null) {
			model.addAttribute("ocorrencia", ocorrenciaDAO.buscar(id));
		} else {
			model.addAttribute("ocorrencia", new Ocorrencia());
		}
		model.addAttribute("ocorrencias", ocorrenciaDAO.buscarTodas());
		return "ocorrencia/lista";
	}
	
	@GetMapping({"/ocorrencia/nova"})
	public String abriFormOcorrencia(Model model) {
		model.addAttribute("ocorrencia", new Ocorrencia());
		model.addAttribute("categorias", categoriaOcorrenciaDAO.buscarTodas());
		return "ocorrencia/form";
	}
	
	@GetMapping("/ocorrencia/editar")
	public String abrirEditarOcorrencia(@RequestParam(required = true) Long id, Model model, Ocorrencia ocorrencia) {
		Ocorrencia ocorrenciaBuscada = ocorrenciaDAO.buscar(id);
		model.addAttribute("ocorrencia", ocorrenciaBuscada);
		model.addAttribute("categorias", categoriaOcorrenciaDAO.buscarTodas());
		return "ocorrencia/form";
	}
	
	@GetMapping("/ocorrencia/assumir")
	public String assumirOcorrencia(@RequestParam(required = true) Long id, RedirectAttributes redirectAttributes) {
		
		return "redirect:/app";
	}
	
	@GetMapping("/ocorrencia/encerrar")
	public String concluirOcorrencia(@RequestParam(required = true) Long id, RedirectAttributes redirectAttributes) {
		return "redirect:/app";
	}
	
	@PostMapping("/ocorrencia/salvar")
	public String salvar(@Valid Ocorrencia ocorrencia, BindingResult brOcorrencia, Model model, HttpSession session) {
		
		// Verificando se possui erros
		if (brOcorrencia.hasErrors()) {
			System.out.println(brOcorrencia);
			System.out.println("-_-_-_-_-_-_-_-_-_-_-");
			
			// Reenvia as categorias para a tela para, caso dê erros de validação
			model.addAttribute("categorias", categoriaOcorrenciaDAO.buscarTodas());
			return "ocorrencia/form";
		}

		ocorrencia.setDataModificacao(new Date());
		// Verificando cadastro
		if (ocorrencia.getId() == null) {
			// Pegando o usuário logado
			Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuarioAutenticado");
			// Determina que o usuário autenticado é o emissor da ocorrencia
			ocorrencia.setEmissor(usuarioAutenticado);
			// Apicando so valroes padrões do sistema
			ocorrencia.setDataCadastro(new Date());
			ocorrenciaDAO.persistir(ocorrencia);
		// Verificando alteração
		}else {
			// Pegando o objeto que será alterado
			Ocorrencia ocorrenciaAlterada = ocorrenciaDAO.buscar(ocorrencia.getId());
			// copyProperties -> passa as propriedades de um objeto para outro (objetoFonteDeDados, objetoQueReceberaOsDados)
			BeanUtils.copyProperties(ocorrencia, ocorrenciaAlterada, "id", "dataCadastro", "emissor");
			ocorrenciaDAO.alterar(ocorrenciaAlterada);
		}
		
		return "redirect:/app";
	}
	

}
