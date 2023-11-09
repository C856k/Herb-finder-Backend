package com.example.herbfinder.api;

import com.example.herbfinder.dtos.MyResponse;
import com.example.herbfinder.service.OpenAiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/herbs")
@CrossOrigin(origins = "*")
public class Herbcontroller {
    private OpenAiService service;
    final static String SYSTEM_MESSAGE = "You are a high level player with herbalism and alchemy in world of warcraft dragonflight that can provide an answer to where i can find the ingredients, given a user input";

    public Herbcontroller(OpenAiService service) {
        this.service = service;
    }
    @GetMapping
    public MyResponse getHerb(@RequestParam String about) {
        return service.makeRequest(about,SYSTEM_MESSAGE);

    }



}
