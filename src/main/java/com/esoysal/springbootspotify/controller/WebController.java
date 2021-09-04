package com.esoysal.springbootspotify.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.esoysal.springbootspotify.constant.Genres;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("genres", Genres.genres);
        return "spotify";
    }
}