package com.example.herbfinder.service;

import com.example.herbfinder.dtos.ChatCompletionRequest;
import com.example.herbfinder.dtos.ChatCompletionResponse;
import com.example.herbfinder.dtos.MyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@Service
public class OpenAIService {
    public static final Logger logger = LoggerFactory.getLogger(OpenAIService.class);

    @Value("${app.api-key}")
    private String API_KEY;

    public final static String URL ="https://api.openai.com/v1/chat/completions";
    public final static String MODEL = "gpt-4";
    public final static double TEMPERATURE = 1;
    public final static int MAX_TOKENS = 4000;
    public final static double FREQUENCY_PENALTY = 0.0;
    public final static double PRESENCE_PENALTY = 0.0;
    public final static double TOP_P = 1.0;

    WebClient client = WebClient.create();

    public OpenAIService() {
        this.client = WebClient.create();
    }

    public OpenAIService(WebClient client) {
        this.client = client;
    }

    public MyResponse makeRequest(String userPrompt, String _systemMessage){

        ChatCompletionRequest requestDto = new ChatCompletionRequest();
        requestDto.setModel(MODEL);
        requestDto.setTemperature(TEMPERATURE);
        requestDto.setMax_tokens(MAX_TOKENS);
        requestDto.setTop_p(TOP_P);
        requestDto.setFrequency_penalty(FREQUENCY_PENALTY);
        requestDto.setPresence_penalty(PRESENCE_PENALTY);
        requestDto.getMessages().add(new ChatCompletionRequest.Message("system",_systemMessage));
        requestDto.getMessages().add(new ChatCompletionRequest.Message("user", userPrompt));

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        String err = null;
        try {
            json = mapper.writeValueAsString(requestDto);
            System.out.println(json);
            ChatCompletionResponse response = client.post()
                    .uri(new URI(URL))
                    .header("Authorization","Bearer" + API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(json))
                    .retrieve()
                    .bodyToMono(ChatCompletionResponse.class)
                    .block();
            String responseMsg = response.getChoices().get(0).getMessage().getContent();
            int tokenUsed = response.getUsage().getTotal_tokens();
            System.out.print("Token used: " + tokenUsed);
            System.out.print(". Cost ($0.0015 / 1K tokens) : $" + String.format("%6f",(tokenUsed * 0.0015 / 1000)));
            System.out.print(". For 1$, this is the amount of the similar requests you can make" + Math.round(1/(tokenUsed * 0.0015 / 1000)));
            return new MyResponse(responseMsg);
        }
        catch (WebClientResponseException e) {
            logger.error("Error response status code" + e.getRawStatusCode());
            logger.error("Error response body: " + e.getResponseBodyAsString());
            logger.error("WebClientResponseException", e);
            err = "Internal Server Error, due to a fail request to external service. You could try again";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
        }
        catch (Exception e) {
            logger.error("Exception", e);
            err = "Internal Server Error - you should try again";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,err);
        }
    }

}
