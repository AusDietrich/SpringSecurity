package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjSecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
		http.csrf().disable()
		.authorizeHttpRequests()
			.requestMatchers("/myAccount","/myBalance","/myLoans","/myCards").authenticated()
			.requestMatchers("/notices","/contact","/register").permitAll()
			.and().formLogin()
			.and().httpBasic();
		/* deny all, useful for temp maintenance periods
		 * http.authorizeHttpRequests().anyRequest().denyAll()
			.and().formLogin()
			.and().httpBasic();
		 */
		/* permit all, useful for easier lower environments testing, local, dev, QA
		 * http.authorizeHttpRequests().anyRequest().permitAll()
			.and().formLogin()
			.and().httpBasic();
		 */
		return http.build();
	}
//	@Bean
	//hardcoded passwords
//	public InMemoryUserDetailsManager userDetailsService() {
//		UserDetails admin = User.withDefaultPasswordEncoder()
//				.username("admin")
//				.password("ad123")
//				.authorities("admin")
//				.build();
//		UserDetails user = User.withDefaultPasswordEncoder()
//				.username("user")
//				.password("ad123")
//				.authorities("read")
//				.build();
//		return new InMemoryUserDetailsManager(admin, user);
//	}
	@Bean
	public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();
	/* useful for dev/local due to no encryption NoOpPasswordEncoder.getInstance();*/ }
}
