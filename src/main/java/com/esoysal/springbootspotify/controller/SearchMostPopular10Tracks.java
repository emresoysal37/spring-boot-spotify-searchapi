package com.esoysal.springbootspotify.controller;

import com.esoysal.springbootspotify.constant.Genres;
import com.esoysal.springbootspotify.model.Song;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

@Controller
public class SearchMostPopular10Tracks {

    private final WebClient webClient;

    public SearchMostPopular10Tracks(WebClient webClient) {
        this.webClient = webClient;
    }

    @PostMapping("/tracks/")
    public String post(@RequestParam(name = "myid") String myid, Model model) throws JsonProcessingException {
    	String id=Genres.genres.get(myid);
        String json = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/artists/"+id+"/toptracks")
                        .queryParam("market", "TR")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<Song> songs = parseJsonToSongs(json);

        model.addAttribute("genres", Genres.genres);
        model.addAttribute("json", json);
        model.addAttribute("songs", songs);
        return "result";
    }

    private List<Song> parseJsonToSongs(String body) throws JsonProcessingException {
        List<Song> songs = new ArrayList<>();
        JsonNode json = new ObjectMapper().readTree(body);
        JsonNode tracks = json.get("tracks");
        for (int i = 0; i < tracks.size(); i++) {
            JsonNode track = tracks.get(i);
            Song song = new Song();
            song.setSongName(track.get("name").textValue());
            song.setPreviewUrl(track.get("preview_url").textValue());

            JsonNode artists = track.get("artists");
            if(!artists.isEmpty()) {
                song.setArtistName(artists.get(0).get("name").textValue());
            }
            
            JsonNode albums = track.get("album");
            if(!albums.isEmpty()) {
            	song.setImageUrl(albums.get("images").get(2).get("url").textValue());
            }
                 
            songs.add(song);
        }
        return songs;
    }
}
