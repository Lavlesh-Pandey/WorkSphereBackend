//package com.WorkSphere.WorkSphereBackend.config;
//
//import java.net.PasswordAuthentication;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import lombok.RequiredArgsConstructor;
//
//@Configuration
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//	private final PasswordEncoder passwordEncoder;
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//
//		httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers("/").permitAll())
//				.formLogin(Customizer.withDefaults());
//
//		return httpSecurity.build();
//	}
//
//	@Bean
//	UserDetailsService userDetailsService() {
//		UserDetails user1 = User.withUsername("admin").password(passwordEncoder.encode("admin")).roles("ADMIN").build();
//
//		UserDetails user2 = User.withUsername("patient").password(passwordEncoder.encode("admin")).roles("Employee")
//				.build();
//
//		return new InMemoryUserDetailsManager(user1, user2);
//	}
//
//}
