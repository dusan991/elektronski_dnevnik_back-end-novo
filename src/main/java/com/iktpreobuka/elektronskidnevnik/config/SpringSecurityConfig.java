package com.iktpreobuka.elektronskidnevnik.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public AuthenticationEntryPoint authEntryPoint;

	@Autowired
	private DataSource dataSource;

	@Value("${spring.queries.ucenici-query}")
	private String uceniciQuery;

	@Value("${spring.queries.rolesUcenici-query}")
	private String rolesUceniciQuery;

	@Value("${spring.queries.nastavnici-query}")
	private String nastavniciQuery;

	@Value("${spring.queries.rolesNastavnici-query}")
	private String rolesNastavniciQuery;

	@Value("${spring.queries.roditelji-query}")
	private String roditeljiQuery;

	@Value("${spring.queries.rolesRoditelji-query}")
	private String rolesRoditeljiQuery;

	@Value("${spring.queries.admini-query}")
	private String adminiQuery;

	@Value("${spring.queries.rolesAdmini-query}")
	private String rolesAdminiQuery;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().and().authorizeRequests().anyRequest().authenticated().and().httpBasic()
				.authenticationEntryPoint(authEntryPoint);
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().usersByUsernameQuery(uceniciQuery).authoritiesByUsernameQuery(rolesUceniciQuery)
				.passwordEncoder(passwordEncoder()).dataSource(dataSource).and().jdbcAuthentication()
				.usersByUsernameQuery(nastavniciQuery).authoritiesByUsernameQuery(rolesNastavniciQuery)
				.passwordEncoder(passwordEncoder()).dataSource(dataSource).and().jdbcAuthentication()
				.usersByUsernameQuery(roditeljiQuery).authoritiesByUsernameQuery(rolesRoditeljiQuery)
				.passwordEncoder(passwordEncoder()).dataSource(dataSource).and().jdbcAuthentication()
				.usersByUsernameQuery(adminiQuery).authoritiesByUsernameQuery(rolesAdminiQuery)
				.passwordEncoder(passwordEncoder()).dataSource(dataSource);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}
