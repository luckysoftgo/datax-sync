package com.application.base.admin.config;


import com.application.base.admin.exception.JWTAccessDeniedHandler;
import com.application.base.admin.exception.JWTAuthenticationEntryPoint;
import com.application.base.admin.filter.JWTAuthenticationFilter;
import com.application.base.admin.filter.JWTAuthorizationFilter;
import com.application.base.admin.service.impl.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Created by admin on 2019/11/17
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginService loginService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/user/register").hasRole("ADMIN")
                .antMatchers("/api/auth/login").permitAll()
                .anyRequest().permitAll()
                // 需要验证了的用户才能访问
                //.anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(new JWTAuthenticationEntryPoint())
		        //添加无权限时的处理
		        .accessDeniedHandler(new JWTAccessDeniedHandler());
	    
	    //设置X-Frame-Options 响应头,可以嵌套ifram中去.
	    http.headers()
			    .frameOptions()
			    .disable();
		/*
	    http.headers()
			    .frameOptions()
			    .sameOrigin()
			    .httpStrictTransportSecurity()
			    .disable();
	    */
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
