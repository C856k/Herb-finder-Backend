package com.example.herbfinder.api;

import com.example.herbfinder.dtos.MyResponse;
import com.example.herbfinder.service.OpenAIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/herbs")
@CrossOrigin(origins = "*")
public class Herbcontroller {
    private OpenAIService service;
    final static String SYSTEM_MESSAGE = "Act like a high level herbalist and alchemist in world of warcraft, that tells the locations of the required ingredients";

    public Herbcontroller(OpenAIService service) {
        this.service = service;
    }
    @GetMapping
    public MyResponse getHerb(@RequestParam String about) {

        return service.makeRequest(about,SYSTEM_MESSAGE);
    }

}
