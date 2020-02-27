package br.senai.sp.informatica.cadastro.component;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class JwtTokenProvider {
	@Value("${app.jwtSecret}")
	private String segredo;
	
	@Value("${app.jwtExpirationInMs}")
	private int limite;
	
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    public String criarToken(Authentication autenticacao) {

    	User userPrincipal = (User) autenticacao.getPrincipal();

        Date now = new Date();
        Date dataDeExpiracao = new Date(now.getTime() + limite);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(dataDeExpiracao)
                .signWith(SignatureAlgorithm.HS512, segredo)
                .compact();
    }

    public String getUserIdDoJWT(String token) {
       return Jwts.parser()
                .setSigningKey(segredo)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validaToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(segredo).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error(" JWT token Invalido");
        } catch (ExpiredJwtException ex) {
            logger.error("JWT token Expirado");
        } catch (UnsupportedJwtException ex) {
            logger.error("JWT token nao Suportado");
        } catch (IllegalArgumentException ex) {
            logger.error("A solicitação no JWT esta vazia.");
        }
        return false;
    }
}
