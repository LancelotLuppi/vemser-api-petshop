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
                authz.antMatchers("/", "/auth", "/auth/cadastro").permitAll()

                        .antMatchers("/auth/status/**").hasRole("ADMIN")
                        .antMatchers("/auth/cargos").hasRole("ADMIN")
                        .antMatchers("/cliente/relatorio-dados").hasAnyRole("ATENDENTE", "TOSADOR", "ADMIN")
                        .antMatchers("/cliente/logged-user").hasAnyRole("USER", "ADMIN", "ATENDENTE")
                        .antMatchers("/pet/logged-user").hasAnyRole("USER", "ADMIN")
                        .antMatchers("/contato/logged-user").hasAnyRole("USER", "ADMIN")
                        .antMatchers("/pedido/logged-user").hasAnyRole("USER", "ADMIN")
                        .antMatchers("/pedido/atualizar-status").hasAnyRole("TOSADOR", "ATENDENTE", "ADMIN")

                        .antMatchers(HttpMethod.DELETE, "/auth").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/cliente/**").hasAnyRole("ATENDENTE", "ADMIN")
                        .antMatchers(HttpMethod.POST, "/cliente").hasAnyRole("USER", "ADMIN", "ATENDENTE")
                        .antMatchers(HttpMethod.POST, "/pet/**").hasAnyRole("ATENDENTE", "ADMIN")
                        .antMatchers(HttpMethod.GET, "/pet/**").hasAnyRole("TOSADOR", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/pet/**").hasAnyRole("ADMIN")

                        .antMatchers("/cliente/**").hasRole("ADMIN")
                        .antMatchers("/contato").hasAnyRole("ATENDENTE", "ADMIN")
                        .antMatchers("/pedido").hasAnyRole("ATENDENTE", "ADMIN")

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
