package br.com.alura.owasp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.alura.owasp.dao.UsuarioDao;
import br.com.alura.owasp.model.Role;
import br.com.alura.owasp.model.Usuario;
import br.com.alura.owasp.retrofit.GoogleWebClient;
import br.com.alura.owasp.validator.ImagemValidator;

@Controller
@Transactional
public class UsuarioController {

	@Autowired
	private UsuarioDao dao;
	
	@Autowired
    private GoogleWebClient cliente;
	
	@Autowired
	private ImagemValidator imagemValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
	    binder.setAllowedFields("nome","email","senha","nomeImagem");
	}

	@RequestMapping("/usuario")
	public String usuario(Model model) {
		Usuario usuario = new Usuario();
		model.addAttribute("usuario", usuario);
		return "usuario";
	}

	@RequestMapping("/usuarioLogado")
	public String usuarioLogado() {
		return "usuarioLogado";
	}

	/*
	 * M�todo registrar usando a associa��o com a classe UsuarioDTO
	 */
//	@RequestMapping(value = "/registrar", method = RequestMethod.POST)
//	public String registrar(MultipartFile imagem, @ModelAttribute("usuarioRegistro") UsuarioDTO usuarioDTO,
//			RedirectAttributes redirect, HttpServletRequest request,
//			Model model, HttpSession session) throws IllegalStateException, IOException {
//
//		/* Feito isso, precisaremos chamar o m�todo montaUsuario presente na classe UsuarioDTO 
//		 * para termos nosso objeto do tipo Usuario, vamos declarar a vari�vel como usuarioRegistro 
//		 * para que a aplica��o continue funcionando
//		 */
//		Usuario usuarioRegistro = usuarioDTO.montaUsuario();
//		tratarImagem(imagem, usuarioRegistro, request);
//		usuarioRegistro.getRoles().add(new Role("ROLE_USER"));
//		dao.salva(usuarioRegistro);
//		session.setAttribute("usuario", usuarioRegistro);
//		model.addAttribute("usuario", usuarioRegistro);
//		return "usuarioLogado";
//	}
	
	/*
	 * Usando o m�todo setAllowedFields ao inv�s do DTO
	 */
	@RequestMapping(value = "/registrar", method = RequestMethod.POST)
	public String registrar(MultipartFile imagem, @ModelAttribute("usuarioRegistro") Usuario usuarioRegistro,
			RedirectAttributes redirect, HttpServletRequest request,
			Model model, HttpSession session) throws IllegalStateException, IOException {

		boolean ehImagem = imagemValidator.tratarImagem(imagem, usuarioRegistro,request);
		
		if (ehImagem) {
	        usuarioRegistro.getRoles().add(new Role("ROLE_USER"));
	        dao.salva(usuarioRegistro);
	        session.setAttribute("usuario", usuarioRegistro);
	        model.addAttribute("usuario", usuarioRegistro);
	        return "usuarioLogado";
	    }
		redirect.addFlashAttribute("mensagem","A imagem passada n�o � v�lida!");
	    return "redirect:/usuario";
	}

	@RequestMapping("/login")
	public String login(@ModelAttribute("usuario") Usuario usuario,
			RedirectAttributes redirect, Model model, HttpSession session, HttpServletRequest request) throws IOException {
		
		// Pega o click do bot�o do recaptcha
		String recaptcha = request.getParameter("g-recaptcha-response");
		
		boolean verificaRecaptcha = cliente.verifica(recaptcha);
		
		if(verificaRecaptcha){
			return procuraUsuario(usuario, redirect, model, session);
		}
		
		redirect.addFlashAttribute("mensagem", "Por favor, comprove que voc� � humano!");
	    return "redirect:/usuario";
	}

	private String procuraUsuario(Usuario usuario, RedirectAttributes redirect, Model model, HttpSession session) {
		Usuario usuarioRetornado = dao.procuraUsuario(usuario);
		model.addAttribute("usuario", usuarioRetornado);
		if (usuarioRetornado == null) {
			redirect.addFlashAttribute("mensagem", "Usu�rio n�o encontrado");
			return "redirect:/usuario";
		}
		session.setAttribute("usuario", usuarioRetornado);
		return "usuarioLogado";
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("usuario");
		return "usuario";
	}

}
