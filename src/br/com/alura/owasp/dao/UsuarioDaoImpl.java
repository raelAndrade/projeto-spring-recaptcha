package br.com.alura.owasp.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import br.com.alura.owasp.model.Usuario;

@Repository
public class UsuarioDaoImpl implements UsuarioDao {

	@PersistenceContext
	EntityManager manager;

	public void salva(Usuario usuario) {
		transformaASenhaUsuarioEmHash(usuario);
		manager.persist(usuario);
	}

	private void transformaASenhaUsuarioEmHash(Usuario usuario) {
		String salto = BCrypt.gensalt();
		String senhaHashed = BCrypt.hashpw(usuario.getSenha(), salto);
		usuario.setSenha(senhaHashed);
	}

	public Usuario procuraUsuario(Usuario usuario) {
		TypedQuery<Usuario> query = manager.createQuery("select u from Usuario u where u.email=:email", Usuario.class);
		query.setParameter("email", usuario.getEmail());
		Usuario usuarioRetornado = query.getResultList().stream().findFirst().orElse(null);
		
		if(validaSenhaDoUsuarioComOHashDoBanco(usuario, usuarioRetornado)) {
			return usuarioRetornado;
		}
		return null;
	}

	private boolean validaSenhaDoUsuarioComOHashDoBanco(Usuario usuario, Usuario usuarioRetornado) {
		if(usuarioRetornado == null) {
			return false;
		}
		return BCrypt.checkpw(usuario.getSenha(), usuarioRetornado.getSenha());
	}
}
