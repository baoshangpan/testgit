package com.byd.glasses.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsUtil {

    private static final String TAG = "-->HttpsUtils";
    
    public HttpsUtil() {
        // TODO Auto-generated constructor stub
    }

    /**
     * send post
     * @param httpsUrl
     * @param params
     * @return
     */
    public static String post(String httpsUrl, String params){
        String result = null;
        try {
            URL url = new URL(httpsUrl);
            
            SSLContext sslctxt = SSLContext.getInstance("TLS");
            sslctxt.init(null,
                    new TrustManager[] { new MyX509TrustManager() },
                    new java.security.SecureRandom());
            
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();

            conn.setSSLSocketFactory(sslctxt.getSocketFactory());
            conn.setHostnameVerifier(new MyHostnameVerifier());
            
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type","application/json");//must hava https head
            conn.connect();

            conn.getOutputStream().write(params.getBytes());
            conn.getOutputStream().flush();
            conn.getOutputStream().close();

            int respCode = conn.getResponseCode();
            Log.d(TAG, "ResponseCode=" + respCode);
            InputStream input = conn.getInputStream();
            result = toString(input);
            
            input.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    
    private static String toString(InputStream input){
        
        String content = null;
        try{
        InputStreamReader ir = new InputStreamReader(input);
        BufferedReader br = new BufferedReader(ir);
        
        StringBuilder sbuff = new StringBuilder();
        while(null != br){
            String temp = br.readLine();
            if(null == temp)break;
            sbuff.append(temp).append(System.getProperty("line.separator"));
        }
        
        content = sbuff.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return content;
    }
    
    static class MyX509TrustManager implements X509TrustManager{

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            if(null != chain){
                for(int k=0; k < chain.length; k++){
                    X509Certificate cer = chain[k];
                    print(cer);
                }
            }
            Log.d(TAG, "check client trusted. authType="+authType);
            
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            if(null != chain){
                for(int k=0; k < chain.length; k++){
                    X509Certificate cer = chain[k];
                    print(cer);
                }
            }
            Log.d(TAG, "check servlet trusted. authType="+authType);
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            
            Log.d(TAG, "get acceptedissuer");
            
            return null;
        }
        
        
        private void print(X509Certificate cer){
            
            int version = cer.getVersion();
            String sinname = cer.getSigAlgName();
            String type = cer.getType();
            String algorname = cer.getPublicKey().getAlgorithm();
            BigInteger serialnum = cer.getSerialNumber();
            Principal principal = cer.getIssuerDN();
            String principalname = principal.getName();
            
            Log.d(TAG, "version="+version+", sinname="+sinname+", type="+type+", algorname="+algorname+", serialnum="+serialnum+", principalname="+principalname);
        }
        
    }
    
    static class MyHostnameVerifier implements HostnameVerifier{

        @Override
        public boolean verify(String hostname, SSLSession session) {
            Log.d(TAG, "hostname="+hostname+",PeerHost= "+session.getPeerHost());
            return true;
        }
        
    }
}
