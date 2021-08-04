package com.projectForum.Security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	
	
	// Creating the bean - spring framework automatically inject instance for autowired view
	@Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }
	
	// Password encoder
	@Bean 
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	// TODO GET RID OF THIS
	// Video 1:09:14 - watch again
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	// This is the most important method.
	// TODO Watch video 1:11:00
	// Also explains again in 1:15:25
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	 http.authorizeRequests()
         .antMatchers(AUTHORIZED_ACCESS).authenticated()
         .antMatchers(ADMIN_ACCESS).hasAnyAuthority(Roles.AMDIN.toString())
         .antMatchers(ALL_ACCESS).permitAll()
         .and()
         .formLogin()
         .usernameParameter("email")
         .defaultSuccessUrl("/")
         .permitAll()
         .and()
         .logout().logoutSuccessUrl("/").permitAll();
    	 configureEncodingFilter(http);
    	 
    	 // TODO Delete after testing
    	 //Old configursion
        /*http.authorizeRequests()
            .antMatchers("/list_users").authenticated()
            .anyRequest().permitAll()
            .and()
            .formLogin()
            .usernameParameter("email")
            .defaultSuccessUrl("/list_users")
            .permitAll()
            .and()
            .logout().logoutSuccessUrl("/").permitAll();*/
    }
    
    //Trying to encode the web
    private void configureEncodingFilter(HttpSecurity http) {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        http.addFilterBefore(filter, CsrfFilter.class);
    }
    
    /** In this area we defines all of Access Rules for all Roles*/
    private static final String[] ALL_ACCESS = {
    		"/",
    		"/index",
    		"/topic/*",
    		"/forum/**",
    		"/css/**"
    };

    
    private static final String[] AUTHORIZED_ACCESS = {
    		"/post/**",
    		"/topic/newTopic"
    };
    private static final String[] MODERATE_ACCESS = {};
    
    private static final String[] ADMIN_ACCESS = {
    		"/list_users",
    		"/forum/newForum"
    };
    
}