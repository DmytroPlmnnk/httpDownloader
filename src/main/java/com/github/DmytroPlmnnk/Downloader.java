package com.github.DmytroPlmnnk;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Downloader {

    private final List<String> fileUrls;

    public Downloader(List<String> fileUrls) {
        this.fileUrls = fileUrls;
    }


    public void downloadAll(String outputPath) {
        ExecutorService executorService = Executors.newFixedThreadPool(fileUrls.size());

        for (String fileUrl : fileUrls) {
            executorService.submit(() -> downloadFile(fileUrl, outputPath));
        }
        executorService.shutdown();


        try {
            if (executorService.awaitTermination(30, TimeUnit.MINUTES)) {
                System.out.println("All files downloaded successfully");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to download files in 30 minutes", e);
        }
    }


    public void downloadFile(String fileUrl, String outputPath) {
        String fileName = UUID.randomUUID().toString();

        try {
            String fileNameWithExtension = fileName + fileUrl.substring(fileUrl.lastIndexOf('.'));

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fileUrl))
                    .GET()
                    .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());


            if (response.statusCode() != 200) {
                System.err.println("Failed to download: " + fileUrl + " (HTTP " + response.statusCode() + ")");
                return;
            } else {
                System.out.println("Download from " + fileUrl + " started (HTTP " + response.statusCode() + ")");
            }

            try (InputStream inputStream = response.body();
                 OutputStream outputStream = new FileOutputStream(outputPath + fileNameWithExtension)) {


                inputStream.transferTo(outputStream);
                System.out.println(fileNameWithExtension + " downloaded to " + outputPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
