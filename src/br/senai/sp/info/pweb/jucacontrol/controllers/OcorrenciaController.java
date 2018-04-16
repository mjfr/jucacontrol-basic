package br.senai.sp.info.pweb.jucacontrol.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sp.info.pweb.jucacontrol.dao.OcorrenciaDAO;
import br.senai.sp.info.pweb.jucacontrol.models.Ocorrencia;

@Controller
@RequestMapping("/app")
public class OcorrenciaController {
		
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
		
		return "ocorrencia/form";
	}
	
	@GetMapping("/ocorrencia/editar")
	public String abrirEditarOcorrencia(@RequestParam(required = true) Long id, Model model) {
		
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
	public String salvar(@Valid Ocorrencia ocorrencia, BindingResult brOcorrencia, Model model) {		
		if (brOcorrencia.hasErrors()) {
			System.out.println(brOcorrencia);
			System.out.println("-_-_-_-_-_-_-_-_-_-_-");
			model.addAttribute("ocorrencia", ocorrenciaDAO.buscarTodas());
			return "ocorrencia/lista";
		}
		
		if (ocorrencia.getId() == null) {
			ocorrenciaDAO.persistir(ocorrencia);
		}
		ocorrenciaDAO.alterar(ocorrencia);
		return "redirect:/app";
	}
	

}
