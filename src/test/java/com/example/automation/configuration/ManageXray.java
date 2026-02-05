package com.example.automation.configuration;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import net.lingala.zip4j.core.ZipFile;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.net.ssl.SSLContext;

public class ManageXray {

    String clientID = "56752DABA0CC4DACB3AD5E720FFD2C7E";
    String clientSecret = "4702b175d5ff4d20c8fcffcf4334c333452588abf12ff8fd451ebc559749c114";
    static String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnQiOiJiNmNhZGQwNS1lMzQxLTNmMTctYjU1Zi00OTM0MTI4MWQ4MmEiLCJhY2NvdW50SWQiOiI3MTIwMjA6MGVkNjY4NzAtM2Y2ZC00NzM3LTljMmYtZDQyMTVmM2MyOWRmIiwiaXNYZWEiOmZhbHNlLCJpYXQiOjE3NzAyODI0ODMsImV4cCI6MTc3MDM2ODg4MywiYXVkIjoiNTY3NTJEQUJBMENDNERBQ0IzQUQ1RTcyMEZGRDJDN0UiLCJpc3MiOiJjb20ueHBhbmRpdC5wbHVnaW5zLnhyYXkiLCJzdWIiOiI1Njc1MkRBQkEwQ0M0REFDQjNBRDVFNzIwRkZEMkM3RSJ9.BX_hO_Eo3si2BPr2sGljeV5P7OykNqav0AJn4neeG5s";

    public String getToken() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        // Méthode correcte pour HttpClient 4.x
        SSLContext sslContext = SSLContexts.custom()
                .useProtocol("TLSv1.2")  // useProtocol, PAS setProtocol
                .build();

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1.2", "TLSv1.3"},
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier()
        );

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build()) {

            HttpPost request = new HttpPost("https://xray.cloud.getxray.app/api/v1/authenticate");
            request.addHeader("Content-Type", "application/json");

            String input = "{ \"client_id\": \"" + clientID + "\", \"client_secret\": \"" + clientSecret + "\"}";
            request.setEntity(new StringEntity(input, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String result = EntityUtils.toString(response.getEntity());
                System.out.println(result);
                return result.replace("\"", "").trim();
            }
        }
    }

    public static void remonterResultatsXray() throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, InterruptedException {
        HttpURLConnection conn;
        URL url;
        String result;
        String  URL ="https://xray.cloud.getxray.app/api/v1/import/execution/cucumber";
        //Call the openConnection method on the URL to create a connection object
        url = new URL(URL);
        conn = (HttpURLConnection) url.openConnection();

        //Various settings of HttpURLConnection
        //Set HTTP method to POST
        conn.setRequestMethod("POST");
        //Allow body submission of request
        conn.setDoInput(true);
        //Allow body reception of response
        conn.setDoOutput(true);
        //Specify Json format
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        // 2.Establish a connection
        conn.connect();
        // 3.Write to request and body
        //Get OutputStream from HttpURLConnection and write json string
        Thread.sleep(10000);
        PrintStream ps = new PrintStream(conn.getOutputStream());

        Path filePath = Path.of(System.getProperty("user.dir")+"\\target\\cucumber.json");
        String content = Files.readString(filePath);
        System.out.println("mon fichier  = "+content);
        ps.print(content);
        ps.close();

        // 4.Receive a response
        //HttpStatusCode 200 is returned at the end of normal operation
        if (conn.getResponseCode() != 200) {
            //Error handling
        }
        //Get InputStream from HttpURLConnection and read
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        result = sb.toString();
        // 5.Disconnect
        conn.disconnect();
        //Return the result to the caller
        System.out.println(result);
    }

    public static void downloadFeatureFiles(String testKeys) {
        try {
            URL url = new URL("https://xray.cloud.getxray.app/api/v2/export/cucumber?keys=" + testKeys);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String zipPath = "features.zip";
            String filesDestination = System.getProperty("user.dir") + "\\src\\test\\resources\\features";

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");

            InputStream inputStream = conn.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(zipPath);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();

            (new ZipFile(zipPath)).extractAll(filesDestination);
            Files.deleteIfExists(Path.of(zipPath));
        } catch (Exception e) {
            System.err.println("Erreur lors du téléchargement des fichiers feature: " + e.getMessage());
        }
    }

    @Test
    public void test() {
        downloadFeatureFiles("POEI2-700");
    }
}
