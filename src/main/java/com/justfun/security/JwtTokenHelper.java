package com.justfun.security;


import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class JwtTokenHelper {

	private static final String AUTHORITIES = "authorities";
	
	private RSAKey rsaJWK;
	
	public JwtTokenHelper(/** @Value("${security.token.key}")**/ String kid) throws JOSEException {
		rsaJWK = new RSAKeyGenerator(2048).keyID(kid).generate();
	}
	
	public String generateToken(Authentication authentication) throws JOSEException {
		JWSSigner signer = new RSASSASigner(rsaJWK);
		
		String authorities = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.subject(authentication.getPrincipal().toString())
				.claim(AUTHORITIES, authorities)
				.issuer("justfunX99")
				.expirationTime(new Date(new Date().getTime() + 3600 * 1000))
				.build();
		
		SignedJWT signedJWT = new SignedJWT(
			new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
			claimsSet
		);
		
		signedJWT.sign(signer);
				
		return signedJWT.serialize();
	}
	
	public Authentication verifyToken(String token) throws JOSEException, ParseException {
		if(token == null) {
			throw new RuntimeException("could not find token for authentication");
		}
		
		SignedJWT signedJWT = SignedJWT.parse(token);
		
		Assert.isTrue(signedJWT.verify(new RSASSAVerifier(rsaJWK.toRSAPublicKey())), "could not verify");
		Assert.isTrue(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime()), "expired token");
		
		JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
		
		return buildAuthenticationObject(token, claims);
	}
	
	private Authentication buildAuthenticationObject(String token, JWTClaimsSet claims) {
		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.getClaim(AUTHORITIES).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		
		return new PreAuthenticatedAuthenticationToken(
			new User(claims.getSubject(), "", authorities),
			token,
			authorities
		);
	}
	
}