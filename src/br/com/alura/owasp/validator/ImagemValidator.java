package br.com.alura.owasp.validator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.com.alura.owasp.model.Usuario;

@Component
public class ImagemValidator {
	
	public boolean tratarImagem(MultipartFile imagem, Usuario usuario, HttpServletRequest request) throws IllegalStateException, IOException {
        ByteArrayInputStream bytesImagem = new ByteArrayInputStream(imagem.getBytes());
        
        /**
         * O m�todo guessContentTypeFromStream ir� analisar os bytes que comp�em o arquivo, conforme especificado na resposta. 
         * Dessa forma, n�o importa se o usu�rio alterar a extens�o do arquivo que a valida��o continuar� a ser feita do conte�do.
         */
        String mime = URLConnection.guessContentTypeFromStream(bytesImagem);
        
        if(mime == null){
            return false;
        }
        usuario.setNomeImagem(imagem.getOriginalFilename());
        File arquivo = new File(request.getServletContext().getRealPath("/image"), usuario.getNomeImagem());
        imagem.transferTo(arquivo);
        return true;
    }

}
