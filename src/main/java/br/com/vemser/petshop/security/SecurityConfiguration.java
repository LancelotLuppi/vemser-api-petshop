package br.com.vemser.petshop.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
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
                authz.antMatchers(HttpMethod.POST, "/cliente").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.POST, "/pet").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.POST, "/contato").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.POST, "/pedido").hasAnyRole("USER", "ATENDENTE", "TOSADOR")
                        .antMatchers(HttpMethod.GET, "/cliente").hasAnyRole("ADMIN", "ATENDENTE")
                        .antMatchers(HttpMethod.GET, "/pet").hasAnyRole("ADMIN", "TOSADOR")
                        .antMatchers(HttpMethod.GET, "/contato").hasAnyRole("ADMIN", "ATENDENTE")
                        .antMatchers(HttpMethod.PUT, "/cliente").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.PUT, "/pet").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.PUT, "/contato").hasAnyRole("USER", "ADMIN", "ATENDENTE")
                        .antMatchers(HttpMethod.PUT, "/pedido").hasAnyRole("USER", "ADMIN", "ATENDENTE")
                        .antMatchers(HttpMethod.DELETE, "/cliente").hasRole("ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/pet").hasAnyRole("ADMIN", "USER")
                        .antMatchers(HttpMethod.DELETE, "/contato").hasAnyRole("ADMIN", "USER", "ATENDENTE")
                        .antMatchers(HttpMethod.DELETE, "/pedido").hasRole("ADMIN")
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
}
