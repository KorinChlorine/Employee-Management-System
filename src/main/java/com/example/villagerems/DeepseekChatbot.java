package com.example.villagerems;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Desktop;
import javafx.scene.control.TextArea;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;





public class DeepseekChatbot {
    private static final String MODEL = "deepseek-r1:1.5b";
    private static final String BASE_PROMPT = """
        You're a tiny virtual assistant that lives inside a PC. start every sentence with *HELP*
        """;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean ensureReady() {
        if (!isOllamaInstalled()) {
            promptInstallOllama();
            return false;
        }

        if (!isOllamaRunning()) {
            promptStartOllama(); // This now ensures Ollama is started only
            return false;
        }

        if (!isModelAvailable()) {
            promptDownloadModel();
            return false;
        }

        return true;
    }

    private boolean isModelAvailable() {
        try {
            ProcessBuilder listBuilder = new ProcessBuilder("ollama", "list");
            listBuilder.redirectErrorStream(true);
            Process listProcess = listBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(listProcess.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(MODEL.split(":")[0])) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to check model availability: " + e.getMessage());
        }
        return false;
    }

    private void promptDownloadModel() {
        Platform.runLater(() -> {
            Alert downloadPrompt = new Alert(Alert.AlertType.CONFIRMATION);
            downloadPrompt.setTitle("Model Not Found");
            downloadPrompt.setHeaderText("The model \"" + MODEL + "\" is not available.");
            downloadPrompt.setContentText("Would you like to download and run it now?");
            Optional<ButtonType> result = downloadPrompt.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                downloadAndRunModel();
            } else {
                Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                cancelAlert.setTitle("Cancelled");
                cancelAlert.setHeaderText("Model not started.");
                cancelAlert.setContentText("Support cannot proceed without the model.");
                cancelAlert.showAndWait();
            }
        });
    }


    public String sendMessage(String userInput) {
        StringBuilder responseText = new StringBuilder();
        try {
            String fullPrompt = BASE_PROMPT + "\n" + userInput;

            Map<String, String> json = new HashMap<>();
            json.put("model", MODEL);
            json.put("prompt", fullPrompt);

            String requestBody = objectMapper.writeValueAsString(json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<java.util.stream.Stream<String>> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofLines());

            Pattern pattern = Pattern.compile("\"response\"\\s*:\\s*\"(.*?)\"");

            response.body().forEach(line -> {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String token = matcher.group(1)
                            .replace("\\n", "\n")
                            .replace("\\t", "\t")
                            .replace("\\\"", "\"")
                            .replace("\\u003c", "<")
                            .replace("\\u003e", ">");
                    responseText.append(token);
                }
            });

            String result = responseText.toString()
                    .replaceAll("(?s)<think>.*?</think>", "")
                    .trim();

            return result.isBlank() ? "[MicroManny stares blankly...]" : result;
        } catch (Exception e) {
            return "Oh crumbs! MicroManny hit a snag: " + e.getMessage();
        }
    }

    private boolean isOllamaInstalled() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder builder;

            if (os.contains("win")) {
                // Use 'where' on Windows
                builder = new ProcessBuilder("cmd", "/c", "where ollama");
            } else {
                // Use 'which' on Unix/macOS
                builder = new ProcessBuilder("which", "ollama");
            }

            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String path = reader.readLine();

            if (path == null || path.isEmpty()) {
                return false;
            }

            // Now verify it runs
            Process versionProcess = new ProcessBuilder(path, "--version").start();
            return versionProcess.waitFor() == 0;

        } catch (IOException | InterruptedException e) {
            return false;
        }
    }


    private boolean isOllamaRunning() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:11434").openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(1000);
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }

    private void promptInstallOllama() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Ollama is not installed. Would you like to download and install it now?",
                    ButtonType.YES, ButtonType.NO);
            alert.setTitle("Ollama Required");
            alert.setHeaderText("Ollama is missing");
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(button -> {
                if (button == ButtonType.YES) {
                    try {
                        String os = System.getProperty("os.name").toLowerCase();
                        if (os.contains("win")) {
                            Desktop.getDesktop().browse(new URI("https://ollama.com/download/OllamaSetup.exe"));
                        } else if (os.contains("mac")) {
                            Desktop.getDesktop().browse(new URI("https://ollama.com/download/Ollama.pkg"));
                        } else {
                            Desktop.getDesktop().browse(new URI("https://ollama.com"));
                        }
                    } catch (Exception ex) {
                        showError("Failed to open download link: " + ex.getMessage());
                    }
                }
            });
        });
    }

    private void promptStartOllama() {
        Platform.runLater(() -> {
            Alert loadingAlert = new Alert(Alert.AlertType.INFORMATION);
            loadingAlert.setTitle("Starting Ollama");
            loadingAlert.setHeaderText("Launching Ollama daemon...");
            loadingAlert.setContentText("Please wait while Support starts the Ollama service.");
            loadingAlert.show();

            new Thread(() -> {
                StringBuilder outputLog = new StringBuilder();

                try {
                    ProcessBuilder serveBuilder = new ProcessBuilder("ollama", "serve");
                    serveBuilder.redirectErrorStream(true);
                    Process process = serveBuilder.start();

                    // Optionally capture output
                    new Thread(() -> {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                outputLog.append(line).append("\n");
                            }
                        } catch (IOException ignored) {}
                    }).start();

                    // Wait to see if it's responding after a few seconds
                    Thread.sleep(3000);

                    if (isOllamaRunning()) {
                        Platform.runLater(loadingAlert::close);
                    } else {
                        Platform.runLater(() -> {
                            loadingAlert.close();
                            showErrorWithOutput("Ollama did not start correctly", outputLog.toString());
                        });
                    }

                } catch (Exception e) {
                    outputLog.append("Error starting Ollama daemon:\n").append(e.getMessage());
                    Platform.runLater(() -> {
                        loadingAlert.close();
                        showErrorWithOutput("Error launching Ollama", outputLog.toString());
                    });
                }
            }).start();
        });
    }





    private void runModel() {
        Platform.runLater(() -> {
            Alert runningAlert = new Alert(Alert.AlertType.INFORMATION);
            runningAlert.setTitle("Starting Model");
            runningAlert.setHeaderText("Launching Support...");
            runningAlert.setContentText("Running " + MODEL + ", please wait...");
            runningAlert.show();

            new Thread(() -> {
                boolean started = false;
                StringBuilder outputLog = new StringBuilder();

                try {
                    ProcessBuilder builder = new ProcessBuilder("ollama", "run", MODEL);
                    builder.redirectErrorStream(true);
                    Process process = builder.start();

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            outputLog.append(line).append("\n");
                            if (line.toLowerCase().contains("model loaded") || line.toLowerCase().contains("listening")) {
                                started = true;
                            }
                        }
                    }

                    // Optional short delay to let Ollama settle
                    Thread.sleep(3000);
                    started = started || isOllamaRunning();

                } catch (IOException | InterruptedException e) {
                    outputLog.append("Exception:\n").append(e.getMessage());
                }

                final boolean finalStarted = started;
                Platform.runLater(() -> {
                    runningAlert.close();

                    if (finalStarted) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Ready");
                        successAlert.setHeaderText("Support is running.");
                        successAlert.setContentText("You're good to go!");
                        successAlert.showAndWait();
                    } else {
                        showErrorWithOutput("Support failed to start.", outputLog.toString());
                    }
                });

            }).start();
        });
    }


    private void showErrorWithOutput(String header, String output) {
        Platform.runLater(() -> {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(header);
            errorAlert.setContentText("Details are available below.");

            TextArea textArea = new TextArea(output);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);

            errorAlert.getDialogPane().setExpandableContent(textArea);
            errorAlert.showAndWait();
        });
    }



    private void downloadAndRunModel() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("Downloading " + MODEL);

            Label statusLabel = new Label("Initializing download...");
            VBox root = new VBox(10, new Label("Downloading " + MODEL + "...\nThis may take a few minutes."), statusLabel);
            root.setPadding(new Insets(10));
            Scene scene = new Scene(root, 400, 150);
            stage.setScene(scene);
            stage.show();

            // Start the actual download in a background thread
            new Thread(() -> {
                StringBuilder outputLog = new StringBuilder();

                try {
                    ProcessBuilder pullBuilder = new ProcessBuilder("ollama", "pull", MODEL);
                    pullBuilder.redirectErrorStream(true);
                    Process process = pullBuilder.start();

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String trimmedLine = line.trim();
                            outputLog.append(trimmedLine).append("\n");

                            // Update UI with latest line
                            Platform.runLater(() -> statusLabel.setText(trimmedLine));
                        }
                    }

                } catch (IOException e) {
                    outputLog.append("Error during pull:\n").append(e.getMessage());
                    Platform.runLater(() -> {
                        stage.close();
                        showErrorWithOutput("Download Failed", outputLog.toString());
                    });
                    return;
                }

                Platform.runLater(() -> {
                    stage.close();
                    runModel(); // continue after successful download
                });
            }).start();
        });
    }




    private void showError(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
            alert.setTitle("Error");
            alert.setHeaderText("Oops!");
            alert.showAndWait();
        });
    }
}
