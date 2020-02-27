package br.senai.sp.informatica.cadastro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configurable
public class AppConfig {
	@Autowired
	private Environment env;
	
	@Bean(name = "DataSource")
	public DriverManagerDataSource dataSource() {
		String driver = 
			env.getProperty("spring.datasource.driver-class-name");
		String url = env.getProperty("spring.datasource.url");
		String user = env.getProperty("spring.datasource.username");
		String senha = env.getProperty("spring.datasource.password");
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(senha);
		
		return dataSource;
	}
}
