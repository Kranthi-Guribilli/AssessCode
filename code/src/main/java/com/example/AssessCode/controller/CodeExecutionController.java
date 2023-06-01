package com.example.AssessCode.controller;

import com.example.AssessCode.model.CodeRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@RestController
public class CodeExecutionController {

    @PostMapping("/execute")
    public String executeCode(@RequestBody CodeRequest codeRequest) {
        String code = codeRequest.getCode();
        String output;

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", ".", "YourMainClass");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            OutputStream stdin = process.getOutputStream();
            stdin.write(code.getBytes());
            stdin.flush();
            stdin.close();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = process.getInputStream().read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            output = outputStream.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            output = "Error: " + e.getMessage();
        }

        return output;
    }
}
