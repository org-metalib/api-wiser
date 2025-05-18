package org.metalib.market.data.ibkr.http.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.metalib.market.data.ibkr.model.Trade;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IserverHttpClientTest {
    @Test
    void test() throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        final var cert = "/Users/igor/.ssh/igor-ibkr-self-cert.jks";
        final var pwd = "mywebapi";

        final var keyStoreType = KeyStore.getDefaultType();
        final var trustStorePath = Paths.get(cert);
        final var trustStoreStream = Files.newInputStream(trustStorePath, StandardOpenOption.READ);
        final var trustStore = KeyStore.getInstance(keyStoreType);
        trustStore.load(trustStoreStream, pwd.toCharArray());
        final var trustManagerFactoryAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        final var trustManagerFactory = TrustManagerFactory.getInstance(trustManagerFactoryAlgorithm);
        trustManagerFactory.init(trustStore);
        trustStoreStream.close();

        final var sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null,
                trustManagerFactory.getTrustManagers(),
                null
        );

        final var http = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();
        final var iserver = new IserverHttpClient("https://localhost:5051/v1/api", http, new ObjectMapper());
        final List<Trade> trades = iserver.iserverAccountTradesGet();
        return;
    }


}