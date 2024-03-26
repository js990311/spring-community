package com.toyproject.community.config;

import com.toyproject.community.repository.MemberRepository;
import com.toyproject.community.security.authentication.MemberDetailsService;
import com.toyproject.community.security.authorization.manager.CustomAuthorizationManager;
import com.toyproject.community.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final RoleService roleService;
    private final MemberRepository memberRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        List<String> permitAll = new ArrayList<>(List.of(new String[]{
                "/", "/member/regist", "/member/login*", "/b/list", "/css/**","/js/**", "/setting",
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
        http.rememberMe(config->config
                        .rememberMeServices(rememberMeServices())
        );

        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public TokenBasedRememberMeServices rememberMeServices(){
        TokenBasedRememberMeServices rememberMeService = new TokenBasedRememberMeServices("key", userDetailsService());
        rememberMeService.setParameter("rememberMe");
        rememberMeService.setAlwaysRemember(false);
        return rememberMeService;
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
    public UserDetailsService userDetailsService(){
        return new MemberDetailsService(memberRepository);
    }

    @Bean
    public CustomAuthorizationManager authorizationManager(){
        return new CustomAuthorizationManager(roleService, roleHierarchy());
    }

    @Bean
    public RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(
            roleService.getRoleHierarchyInfo()
        );
        return roleHierarchy;
    }

    @Bean
    MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy){
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }
}
