package com.maloef.cdnd.capstone.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

@Service
@Slf4j
public class JwtVerifier {

    private JwtParser jwtParser = Jwts.parser().setSigningKey(readAuth0PublicKey());

    public void verify(String jwt) {
        Jws<Claims> jws = jwtParser.parseClaimsJws(jwt);
        log.info("jws: {}", jws);
    }

    @SneakyThrows
    private PublicKey readAuth0PublicKey() {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream inputStream = getClass().getResourceAsStream("/auth0Certificate.pem");
        Certificate cert = cf.generateCertificate(inputStream);
        return cert.getPublicKey();
    }
}
