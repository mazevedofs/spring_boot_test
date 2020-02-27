package br.senai.sp.informatica.cadastro.filtro;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import br.senai.sp.informatica.cadastro.component.JwtTokenProvider;
import br.senai.sp.informatica.cadastro.model.Usuario;
import br.senai.sp.informatica.cadastro.service.UsuarioService;

public class FiltroAutenticadorJWT extends OncePerRequestFilter {
	@Autowired
	private JwtTokenProvider provedor;
	@Autowired
	private UsuarioService usuarioService;
	
	private static final Logger logger = LoggerFactory.getLogger(FiltroAutenticadorJWT.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
		        HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
					
		try {
			String jwt = getJwtFromRequest(request);

			if (StringUtils.hasText(jwt) && provedor.validaToken(jwt)) {
				String userId = provedor.getUserIdDoJWT(jwt);
				logger.error("UserId: " + userId);

				Usuario userDetails = usuarioService.getUsuario(userId);
			
				logger.error("Usuario: " + userDetails.getNome());
				
				UsernamePasswordAuthenticationToken authentication = 
					new UsernamePasswordAuthenticationToken(
						userDetails,
    						 null, 
						Collections.singletonList(
							usuarioService.getAutorizacoes(userId)) );

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
		}

		filterChain.doFilter(request, response);
	}

	
	private String getJwtFromRequest(HttpServletRequest request) {
		String autorizacao = request.getHeader("Authorization");
		logger.error("auth: " + autorizacao);
		if(StringUtils.hasText(autorizacao) &&
			autorizacao.startsWith("Bearer")) {
			return autorizacao.substring(7, autorizacao.length());
		}
		logger.error("Token nulo");
		return null;
	}
}






















