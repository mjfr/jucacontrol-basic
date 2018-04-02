package br.senai.sp.info.pweb.jucacontrol.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.senai.sp.info.pweb.jucacontrol.models.CategoriaOcorrencia;

@Controller
@RequestMapping("/app/adm")
public class CategoriaOcorrenciaController {
	
	@GetMapping("/categoria")
	public String abrirMenuCategorias(@RequestParam(name = "id", required = false) Long id,
										Model model) {
		return "categoria/menu";
	}

	@GetMapping("/categoria/deletar")
	public String deletar(@RequestParam(name = "id", required = true) Long id) {
		
		return "redirect:/app/adm/categoria";
	}
	
	
	@PostMapping("/categoria/salvar")
	public String salvar(CategoriaOcorrencia categoriaOcorrencia) {		
		//Redireciona para pagina de categorias
		return "redirect:/app/adm/categoria";
	}

}
