package com.toyproject.community.config;

import com.toyproject.community.security.authorization.CustomAuthorizationManager;
import com.toyproject.community.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final RoleService roleService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        List<String> permitAll = new ArrayList<>(List.of(new String[]{
                "/", "/member/regist", "/member/login*", "/b/list", "/css/**","/js/**", "/setting"
        }));

        http.authorizeHttpRequests((request)->{
            for (String permitAllRequest : permitAll) {
                request.requestMatchers(permitAllRequest).permitAll();
            }
            request.anyRequest().access(authorizationManager());
        });
        http.formLogin((formLogin)->
            formLogin.loginPage("/member/login")
                    .loginProcessingUrl("/member/login_process")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", true)
                    .failureHandler(failureHandler())
        );

        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler failureHandler(){
        return new ForwardAuthenticationFailureHandler("/member/login");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler(){
        return new SecurityContextLogoutHandler();
    }

    @Bean
    public CustomAuthorizationManager authorizationManager(){
        return new CustomAuthorizationManager(roleService);
    }
}
