package br.com.alura.owasp.retrofit;

import java.io.IOException;

import org.springframework.stereotype.Component;

import retrofit2.Call;

@Component
public class GoogleWebClient {
	
	private static final String SECRET = "6LdngvAUAAAAAOQD2s41Wzw8qZPVQ7ZAXWZQQA6D";

	public boolean verifica(String recaptcha) throws IOException {
		
		Call<Resposta> token = new RetrofitInicializador().getGoogleService().enviaToken(SECRET, recaptcha);
		return token.execute().body().isSuccess();
    }

}
