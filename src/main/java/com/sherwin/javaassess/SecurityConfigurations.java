package com.sherwin.javaassess;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sherwin.javaassess.jwt.JwtTokenFilter;
import com.sherwin.javaassess.models.CustomUserDetailsService;
import com.sherwin.javaassess.models.UserRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		prePostEnabled = false, 
		securedEnabled = false, 
		jsr250Enabled = true
		)
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserRepository userRepo;
	
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}		
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService());
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}	
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(authenticationProvider());
//	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(username -> userRepo.findByEmail(username));
	}
	
	
	@Order(1)
	@Configuration
	public static class RestConfiguration extends WebSecurityConfigurerAdapter{
		
		@Autowired
		private JwtTokenFilter jwtTokenFilter;
		
		public void configure(HttpSecurity http) throws Exception{
			
			System.out.println("configuring http1");
			
			http
				.requestMatchers().antMatchers("/api/**").and()
				.csrf().disable()
				.authorizeRequests().anyRequest().authenticated()
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()	
				.exceptionHandling()
				.authenticationEntryPoint((request, response, ex) -> {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, 
						ex.getMessage());
				})
				.and()
				.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    	}
	}
	
	
	@Order(2)
	@Configuration
	public static class AuthConfiguration extends WebSecurityConfigurerAdapter{
		
			
		@Autowired
		private JwtTokenFilter jwtTokenFilter;
		
		public void configure(HttpSecurity http) throws Exception{
			
			System.out.println("configuring http2");
			
			http
				.requestMatchers().antMatchers("/auth/login").and()
				.csrf().disable()
				.authorizeRequests()
				.anyRequest().permitAll()
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()	
				.exceptionHandling()
				.authenticationEntryPoint((request, response, ex) -> {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, 
						ex.getMessage());
				});
				
    	}
	}
	
	
	@Order(3)
	@Configuration
	public static class WebAppConfiguration extends WebSecurityConfigurerAdapter{
		
	
		public void configure(HttpSecurity http) throws Exception{
			
			System.out.println("configuring http4");
			
			http
				.requestMatchers().antMatchers("/**")
				.and()
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/showProfile").authenticated()
				.antMatchers("/register").hasAuthority("ROLE_ADMIN")
				.antMatchers("/deleteUser").hasAuthority("ROLE_ADMIN")
				.antMatchers("/users/edit/**").hasAnyAuthority("ROLE_ADMIN","ROLE_EDITOR")
				.antMatchers("/listUsers").hasAnyAuthority("ROLE_ADMIN","ROLE_EDITOR")
				.antMatchers("/**/favicon.ico", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.xlsx", "/fonts/**", "/logout").permitAll()
                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin().permitAll()
					.loginPage("/login")
					.usernameParameter("email")
					.passwordParameter("pass")
					.defaultSuccessUrl("/")
					.loginProcessingUrl("/doLogin")
					.failureUrl("/login_error")
				.and()
				.logout().permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/403")
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
				;
    	}
	
//		@Override
//	    public void configure(WebSecurity web) throws Exception {
//	        web
//	                .ignoring()
//	                .antMatchers(
//	                		HttpMethod.GET,
//	                		"/",
//                            "/*.html",
//                            "/**/favicon.ico",
//                            "/**/*.html",
//                            "/**/*.css",
//                            "/**/*.js",
//                            "/**/*.xlsx"
//	                )
//	        ;
//	    }
	}
}	
	
//	
//	
//	
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//
//		http.csrf().disable();
//		
//		// For Form Login Security
//		
//		http
//			.authorizeRequests().antMatchers("/").permitAll();
//		
//			
//		http
//			.authorizeRequests()
//			.requestMatchers(matchers -> matchers
//					
//					
//					)
//					
//			.and()
//			.formLogin().permitAll()
//				.loginPage("/login")
//				.usernameParameter("email")
//				.passwordParameter("pass")
//				.loginProcessingUrl("/doLogin")
//				.defaultSuccessUrl("/")
//				.failureUrl("/login_error")
//			.and()
//			.logout().permitAll()
//			.and()
//			.exceptionHandling().accessDeniedPage("/403");
//		
//		
//		 http
//         	.requestMatchers(matchers -> matchers
//        		 .antMatchers("/api/**") // apply JWTSecurityConfig to requests matching "/api/**"
//        		 )	
//         	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//         	.and()
//         	.exceptionHandling()
//         	.authenticationEntryPoint((request, response, ex) -> {
//         		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
//         		})
//         	.and()
//         	.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
//		
//		
		// for JWT config
//		http.csrf().disable().authorizeRequests()
//		.antMatchers("/auth/login").permitAll()
//		.antMatchers("/api/**").authenticated()
//		.anyRequest()
//		.authenticated()
//		.and()
//		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//		.and()
//		.exceptionHandling()
//		.authenticationEntryPoint((request, response, ex) -> {
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
//		})
//		.and()
//		.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		
	
	
	
	




