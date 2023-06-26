/*package com.easyms.security.azuread.ms.config;

import org.springframework.security.oauth2.jwt.JwtDecoder;

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
}*/
