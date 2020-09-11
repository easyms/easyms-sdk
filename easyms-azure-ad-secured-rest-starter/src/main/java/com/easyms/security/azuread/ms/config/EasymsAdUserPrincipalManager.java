package com.easyms.security.azuread.ms.config;

import com.microsoft.azure.spring.autoconfigure.aad.AADAuthenticationProperties;
import com.microsoft.azure.spring.autoconfigure.aad.ServiceEndpointsProperties;
import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipal;
import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipalManager;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Objects;
import java.util.Optional;

public class EasymsAdUserPrincipalManager extends UserPrincipalManager {

    private char[] PASSWORD;
    private static final String ALIAS = "jwt";
    private final InternalTokenProperties internalTokenProperties;
    private JWSVerifier jwsVerifier;


    public EasymsAdUserPrincipalManager(ServiceEndpointsProperties serviceEndpointsProps,
                                        AADAuthenticationProperties aadAuthProps,
                                        ResourceRetriever resourceRetriever,
                                        boolean explicitAudienceCheck, InternalTokenProperties internalTokenProperties) {
        super(serviceEndpointsProps, aadAuthProps, resourceRetriever, explicitAudienceCheck);
        this.internalTokenProperties = internalTokenProperties;
        if(internalTokenProperties.isEnabled()) {
            jwsVerifier = new RSASSAVerifier(getInternalPubKey());
        }

    }

    @Override
    public UserPrincipal buildUserPrincipal(String idToken) throws ParseException, JOSEException, BadJOSEException {

        Optional<UserPrincipal> userPrincipalOptional = tryToBuildUserPrincipalFromInternallyValidatedIdToken(idToken);
        //We don't use OrElseGet here because we don't want to loose initial Exceptions BadJWTException, ...,
        //since Lambda can only throw RuntimeException
        UserPrincipal userPrincipal;
        if(userPrincipalOptional.isEmpty()) {
            userPrincipal = super.buildUserPrincipal(idToken);
        } else {
            userPrincipal = userPrincipalOptional.get();
        }
        return new EasymsAdUserPrincipal(idToken, userPrincipal);
    }

    private Optional<UserPrincipal> tryToBuildUserPrincipalFromInternallyValidatedIdToken(String idToken) throws ParseException, JOSEException, BadJWTException {

        if (internalTokenProperties.isEnabled()) {
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            if (signedJWT.getJWTClaimsSet().getIssuer().equals(internalTokenProperties.getIssuer())) {
                if (signedJWT.verify(jwsVerifier)) {
                    JWSObject jwsObject = JWSObject.parse(idToken);
                    return Optional.of(new EasymsAdUserPrincipal(idToken, new UserPrincipal(jwsObject, signedJWT.getJWTClaimsSet())));
                } else {
                    throw new BadJWTException("Internal token not validated by Internal key");
                }
            }
        }

        return Optional.empty();
    }

    private RSAPublicKey getInternalPubKey()  {

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            Reader pubKeyReader;
            if(! StringUtils.isEmpty(internalTokenProperties.getPubKeyPath())) {
                File secretsFolder = ResourceUtils.getFile(internalTokenProperties.getPubKeyPath());
                pubKeyReader = new InputStreamReader(new FileInputStream(secretsFolder));
            } else if(! StringUtils.isEmpty(internalTokenProperties.getPubKeyValue())) {
                pubKeyReader = new StringReader(internalTokenProperties.getPubKeyValue());
            } else {
                throw new IllegalStateException("Either pubKey path or pubKey value should be provided");
            }


            byte[] publicKeyBytes = new PemReader(pubKeyReader).readPemObject().getContent();
            return  (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            throw new RuntimeException("Unable to load Internal PubKey", e);
        }

    }

}
