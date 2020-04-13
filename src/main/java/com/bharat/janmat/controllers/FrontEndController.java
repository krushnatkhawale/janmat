package com.bharat.janmat.controllers;

import com.bharat.janmat.model.Mat;
import com.bharat.janmat.model.Place;
import com.bharat.janmat.services.MatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FrontEndController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontEndController.class);
    private final MatService matService;

    public FrontEndController(MatService matService) {
        this.matService = matService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Mat> mats = matService.findAll();
        model.addAttribute("mats", mats);
        return "home";
    }

    @GetMapping("/postmat")
    public String postmat(Model model) throws IOException {
        model.addAttribute("mat", new Mat());
        List<Place> collect = Files.lines(Paths.get("indian_cities.csv"))
                .skip(1)
                .map(line -> {
                    String[] split = line.split(",");
                    return new Place(split[0], split[1], split[2]);
                }).collect(Collectors.toList());
        model.addAttribute("status", collect);

        return "postmat";
    }

    @PostMapping("/postMat")
    public String addUser(@Valid Mat mat, BindingResult result, Model model) throws JsonProcessingException {
        LOGGER.debug("Saving new mat");
        matService.save(mat);
        LOGGER.info("New mat saved successfully");

        List<Mat> mats = matService.findAll();
        model.addAttribute("mats", mats);
        return "home";
    }
}