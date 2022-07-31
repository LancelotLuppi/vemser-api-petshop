package br.com.vemser.petshop.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final TokenService tokenService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity https) throws Exception {
        https.headers().frameOptions().disable().and()
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests(permissoes());
        https.addFilterBefore(new TokenAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
        return https.build();
    }

    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> permissoes() {
        return (authz) ->
                authz.antMatchers("/", "/auth", "/auth/cadastro", "/auth/logged", "/auth/new-password").permitAll()
                        .antMatchers(HttpMethod.POST, "/cliente").hasAnyRole("USER", "ADMIN","ATENDENTE")
                        .antMatchers(HttpMethod.PUT, "/cliente").hasAnyRole("USER","ADMIN","ATENDENTE")
                        .antMatchers(HttpMethod.POST, "/contato/create-id-logado").hasAnyRole("USER","ADMIN")
                        .antMatchers(HttpMethod.GET, "/contato/contatos-id-logado").hasAnyRole("USER","ADMIN")
                        .antMatchers(HttpMethod.POST, "/pedido").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.PUT, "/pedido").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.POST, "/pet/logged-user").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.GET, "/pet/logged-user").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.PUT, "/pet").hasRole("USER")

                        .antMatchers(HttpMethod.GET, "/pet/page-pets").hasAnyRole("TOSADOR", "ADMIN")
                        .antMatchers(HttpMethod.GET,"/cliente/relatorio-dados").hasAnyRole("TOSADOR", "ADMIN", "ATENDENTE")

                        .antMatchers(HttpMethod.GET, "/contato").hasAnyRole("ATENDENTE", "ADMIN")
                        .antMatchers(HttpMethod.POST,"/contato").hasAnyRole("ATENDENTE", "ADMIN")
                        .antMatchers(HttpMethod.PUT, "/contato").hasAnyRole("ATENDENTE", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/contato").hasAnyRole("ATENDENTE", "ADMIN")
                        .antMatchers(HttpMethod.POST, "/pedido").hasAnyRole("ATENDENTE", "ADMIN")
                        .antMatchers(HttpMethod.PUT, "/pedido/atualizar-status").hasAnyRole("ATENDENTE", "ADMIN")
                        .antMatchers(HttpMethod.GET, "/pedido/relatorio-status").hasAnyRole("ATENDENTE", "ADMIN")
                        .antMatchers(HttpMethod.PUT, "/pedido").hasAnyRole("ATENDENTE", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/pedido").hasAnyRole("ATENDENTE", "ADMIN")
                        .antMatchers(HttpMethod.GET, "/pet/page-pets").hasAnyRole("ATENDENTE", "ADMIN")
                        .antMatchers(HttpMethod.PUT, "/pet").hasAnyRole("ATENDENTE", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/pet").hasAnyRole("ATENDENTE", "ADMIN")

                        .antMatchers("/**").hasRole("ADMIN")
                        .anyRequest().authenticated();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().antMatchers("/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .exposedHeaders("Authorization");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new Argon2PasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
}
