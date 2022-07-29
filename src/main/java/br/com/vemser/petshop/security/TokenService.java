package br.com.vemser.petshop.security;

import br.com.vemser.petshop.entity.CargoEntity;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.service.UsuarioService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UsuarioService usuarioService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private static final String KEY_CARGOS = "cargos";


    public String generateToken(Authentication authentication) {
        UsuarioEntity usuarioEntity = (UsuarioEntity) authentication.getPrincipal();

        Date now = new Date();
        Date exp = new Date(now.getTime() + Long.parseLong(expiration));

        List<String> listCargos = usuarioEntity.getCargos().stream()
                .map(CargoEntity::getNome)
                .toList();

        String token = Jwts.builder()
                .setIssuer("petshop-api")
                .claim(Claims.ID, usuarioEntity.getIdUsuario())
                .claim(KEY_CARGOS, listCargos)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return TokenAuthenticationFilter.BEARER + token;
    }


    public UsernamePasswordAuthenticationToken isValid(String token) {
        if(token == null) {
            return null;
        }

        Claims body = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJwt(token)
                .getBody();

        Integer idUsuario = body.get(Claims.ID, Integer.class);

        if(idUsuario != null) {
            List<String> cargos = body.get(KEY_CARGOS, List.class);

            List<SimpleGrantedAuthority> cargosAuthority = cargos.stream()
                    .map(SimpleGrantedAuthority::new).toList();

            return new UsernamePasswordAuthenticationToken(idUsuario, null, cargosAuthority);
        }
        return null;
    }
}
