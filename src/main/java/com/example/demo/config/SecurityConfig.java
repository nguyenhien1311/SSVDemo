package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.AuthTokenFilter;
import com.example.demo.security.CustomAccessDeniedHandler;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserDetailService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;

	@Bean
	public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userDetailService()).passwordEncoder(passwordEncoder());
		return authenticationManagerBuilder.build();
	}

	@Autowired
	private JwtTokenProvider provider;

	@Bean
	public AuthTokenFilter authTokenFilter() {
		return new AuthTokenFilter(provider, userDetailService());
	}

	@Bean
	public UserDetailService userDetailService() {
		return new UserDetailService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(BCryptVersion.$2A);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();
		http.authorizeHttpRequests().antMatchers("/api/v1/login","api/v1/logout","/api/v1/signin").permitAll()
				.antMatchers("/api/v1/class/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPERVISOR")
				.antMatchers("/api/v1/student/**").hasAnyAuthority("ROLE_USER", "ROLE_SUPERVISOR")
				.antMatchers("/api/v1/subject/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPERVISOR").anyRequest()
				.authenticated();
//		http.headers().frameOptions().sameOrigin();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}
}
