package com.ssafy.pit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ssafy.pit.common.auth.JwtAuthenticationFilter;
import com.ssafy.pit.common.auth.PitUserDetailsService;
import com.ssafy.pit.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	PitUserDetailsService pitUserDetailsService;
	
	@Autowired
	private UserService userService;
	
	// Password 인코딩 방식으로 BCrypt 암호화 방식 사용
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// DAO 기반으로 Authentication Provider를 생성
	// BCrypt Password Encoder와 UserDetailService 구현체를 설정
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(this.pitUserDetailsService);
		return daoAuthenticationProvider;
	}
	
	//DAO 기반의 Authentication Provider가 적용되도록 설정
	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 사용 하지않음
        .and()
        .addFilter(new JwtAuthenticationFilter(authenticationManager(), userService)) //HTTP 요청에 JWT 토큰 인증 필터를 거치도록 필터를 추가
        .authorizeRequests()
        .antMatchers("/v1/users/me/**", "/v1/event/{\\d*}", "/v1/class/likes/**", "/v1/class/admin/**", "/v1/ptroom/**", "/v1/class/video/**",
        		"/v1/class/finishedclass/**", "/v1/class/registerclass/**", "/v1/class/enrollment/**", "/v1/class/create/**").authenticated()//인증이 필요한 URL과 필요하지 않은 URL에 대하여 설정
        	    .anyRequest().permitAll()
        .and().cors();
	}
	
	
}
