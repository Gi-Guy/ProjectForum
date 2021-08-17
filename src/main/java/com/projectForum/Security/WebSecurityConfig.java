package com.projectForum.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.projectForum.Exceptions.CustomAccessDeniedHandler;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler(){
	    return new CustomAccessDeniedHandler();
	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	 http.authorizeRequests()
    	 .antMatchers(Path.getAuthorizedAccess()).hasAnyAuthority(Path.getAuthorityRoles())
         .antMatchers(Path.getAdminAccess()).hasAnyAuthority(Path.getAdminRole())
         .antMatchers(Path.getAllAccess()).permitAll()
         .and()
         .formLogin()
         .loginPage("/login")
         .usernameParameter("email")
         .passwordParameter("password")	
         .defaultSuccessUrl("/login_success", true)
         .failureUrl("/loginError")
         .usernameParameter("email")
         .permitAll()
         .and()
         .rememberMe().tokenValiditySeconds(7 * 24 * 60 * 60) // will remember user for 7 days
         .key("123456789")
         .and()
         .logout().logoutSuccessUrl("/").permitAll()
         .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler())
         .and().csrf().disable();
         
    	 configureEncodingFilter(http);
    	 
    }
    
    // Trying to encode the web
    private void configureEncodingFilter(HttpSecurity http) {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        http.addFilterBefore(filter, CsrfFilter.class);
    }
}