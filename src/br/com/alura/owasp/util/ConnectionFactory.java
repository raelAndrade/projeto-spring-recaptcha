package br.com.alura.owasp.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

	public Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:mysql://localhost/owasp?useTimezone=true&serverTimezone=UTC",
					"root", "senha123");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
