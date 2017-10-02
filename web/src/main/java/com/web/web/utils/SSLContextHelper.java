package com.web.web.utils;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * SSL security management.
 */
@Slf4j
public class SSLContextHelper {

    private static final String KEY_STORE_TYPE = "JKS";
    private static final String CLASS_NAME = SSLContextHelper.class.getName();
    private static final String TRANSPORT_SECURITY_PROTOCOL = "TLS";

    /**
     * Enable verification.
     */
    public static void enable() {
        // For the time being, the data is not set.
        String keystoreType = "JKS";
        InputStream keystoreLocation = null;
        char[] keystorePassword = null;
        char[] keyPassword = null;


        try {
            KeyStore keystore = KeyStore.getInstance(keystoreType);
            keystore.load(keystoreLocation, keystorePassword);
            KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmfactory.init(keystore, keyPassword);
            InputStream truststoreLocation = null;
            char[] truststorePassword = null;
            String truststoreType = KEY_STORE_TYPE;

            KeyStore truststore = KeyStore.getInstance(truststoreType);
            truststore.load(truststoreLocation, truststorePassword);
            TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            KeyManager[] keymanagers = kmfactory.getKeyManagers();
            TrustManager[] trustmanagers = tmfactory.getTrustManagers();

            SSLContext sslContext = SSLContext.getInstance(TRANSPORT_SECURITY_PROTOCOL);
            sslContext.init(keymanagers, trustmanagers, new SecureRandom());
            SSLContext.setDefault(sslContext);
        } catch (Exception e) {
            log.error(CLASS_NAME + "Exception in SSL " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Disable verification.
     */
    public static void disable() {
        try {
            SSLContext sslc = SSLContext.getInstance("TLS");
            TrustManager[] trustManagerArray = {(TrustManager) new NullX509TrustManager()};
            sslc.init(null, trustManagerArray, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostnameVerifier());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Instance of this interface manage which X509 certificates may be used to authenticate the remote side of a secure socket.
     * Decisions may be based on trusted certificate authorities, certificate revocation lists, online status checking or other means.
     */
    private static class NullX509TrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            System.out.println();
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            System.out.println();
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

    }

    /**
     * This class is the base interface for hostname verification.
     */
    private static class NullHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}