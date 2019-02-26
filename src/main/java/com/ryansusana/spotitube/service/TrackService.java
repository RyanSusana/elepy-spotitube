package com.ryansusana.spotitube.service;

import com.elepy.annotations.Inject;
import com.elepy.dao.Crud;
import com.elepy.describers.ModelDescription;
import com.elepy.exceptions.ElepyException;
import com.elepy.http.HttpContext;
import com.elepy.routes.DefaultService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryansusana.spotitube.presentation.Playlist;
import com.ryansusana.spotitube.presentation.Track;

import java.util.*;

public class TrackService extends DefaultService<Track> {

    @Inject
    private Crud<Playlist> playlistDao;

    @Override
    //Override how Elepy handles GET /tracks to change the form of the request and implement the 'forPlaylist' feature.
    public void handleFindMany(HttpContext context, Crud<Track> crud, ModelDescription<Track> modelDescription, ObjectMapper objectMapper) throws Exception {

        Optional<String> forPlaylistOptional = Optional.ofNullable(context.request().queryParams("forPlaylist"));
        List<Track> tracks = new ArrayList<>(crud.getAll());
        Map<String, Object> toReturn = new HashMap<>();


        //Removes tracks already in the playlist
        forPlaylistOptional
                .ifPresent(
                        forPlaylist -> tracks.removeAll(playlistDao
                                .getById(Integer.parseInt(forPlaylist))
                                .orElseThrow(() -> new ElepyException("Playlist not found!", 404))
                                .getTracks()
                        )
                );


        toReturn.put("tracks", tracks);
        context.response().result(objectMapper.writeValueAsString(toReturn));
    }
}
