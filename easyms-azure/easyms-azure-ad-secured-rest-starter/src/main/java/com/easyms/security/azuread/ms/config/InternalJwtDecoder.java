package com.easyms.security.azuread.ms.config;

import com.azure.spring.aad.AADAuthorizationServerEndpoints;
import com.azure.spring.autoconfigure.aad.AADAuthenticationProperties;
import com.azure.spring.autoconfigure.aad.UserPrincipal;
import com.azure.spring.autoconfigure.aad.UserPrincipalManager;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Optional;

public abstract class InternalJwtDecoder implements JwtDecoder {

//    private char[] PASSWORD;
//    private static final String ALIAS = "jwt";
//    private final InternalTokenProperties internalTokenProperties;
//    private JWSVerifier jwsVerifier;
//
//
//    public InternalJwtDecoder(InternalTokenProperties internalTokenProperties) {
//        this.internalTokenProperties = internalTokenProperties;
//        if (internalTokenProperties.isEnabled()) {
//            jwsVerifier = new RSASSAVerifier(getInternalPubKey());
//        }
//
//    }
//
//
//
//
//
//
//    @Override
//    public Jwt decode(String idToken) throws JwtException {
//        if (internalTokenProperties.isEnabled()) {
//            SignedJWT signedJWT = null;
//            try {
//                signedJWT = SignedJWT.parse(idToken);
//                if (signedJWT.getJWTClaimsSet().getIssuer().equals(internalTokenProperties.getIssuer())) {
//                    if (signedJWT.verify(jwsVerifier)) {
//                        return Jwt.withTokenValue(idToken).build()
//                        return signedJWT;
//                    } else {
//                        throw new JwtException("Internal token not validated by Internal key");
//                    }
//                }
//            } catch (ParseException | JOSEException e) {
//                throw new JwtException("Internal token not validated by Internal key", e);
//            }
//
//        }
//
//        throw new JwtException("Internal token validation not supported");
//
//    }
}
