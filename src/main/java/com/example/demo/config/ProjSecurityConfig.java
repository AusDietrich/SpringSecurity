package com.example.demo.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.demo.filter.CsrfCookieFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class ProjSecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
		CsrfTokenRequestAttributeHandler csrfToken = new CsrfTokenRequestAttributeHandler();
		csrfToken.setCsrfRequestAttributeName("_csrf");
		
		http.securityContext().requireExplicitSave(false)
			.and().sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
			.cors().configurationSource(new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
				config.setAllowedMethods(Collections.singletonList("*"));
				config.setAllowCredentials(true);
				config.setAllowedHeaders(Collections.singletonList("*"));
				config.setMaxAge(3600L);
				return config;
			}
			//cookie name: XSRF-TOKEN 
			//header: X-XRSF-Token
		}).and().csrf((csrf) -> csrf.csrfTokenRequestHandler(csrfToken).ignoringRequestMatchers("/contact","/register")
			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
			.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
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
