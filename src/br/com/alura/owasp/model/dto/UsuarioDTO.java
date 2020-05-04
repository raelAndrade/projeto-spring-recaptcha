package br.com.alura.owasp.model.dto;

import br.com.alura.owasp.model.Usuario;

/**
 * Essa classe será utilizada para transferir os valores vindos do formulário para a classe Usuario, 
 * criando assim o nosso objeto Usuario, não precisaremos dos getters, somente dos setters.
 * 
 * @author israel
 *
 */
public class UsuarioDTO {
	
	private String email;
	
	private String senha;
	
	private String nome;
	
	private String nomeImagem;

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setNomeImagem(String nomeImagem) {
		this.nomeImagem = nomeImagem;
	}

	/**
	 * Criando o método montaUsuario, onde devemos instanciar a classe Usuario e passamos esses dados no construtor da classe.
	 */
	public Usuario montaUsuario(){
	    Usuario usuario = new Usuario(email,senha,nome,nomeImagem);
	    return usuario;
	}
}
