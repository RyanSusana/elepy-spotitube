package com.ryansusana.spotitube.services;

import com.elepy.annotations.Inject;
import com.elepy.annotations.Route;
import com.elepy.dao.Crud;
import com.elepy.exceptions.ElepyException;
import com.elepy.routes.DefaultService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryansusana.spotitube.domain.Playlist;
import com.ryansusana.spotitube.domain.Track;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;

public class PlaylistService extends DefaultService<Playlist> {

    @Inject(tag = "/playlists")
    private Crud<Playlist> playlistDao;

    @Inject(tag = "/tracks")
    private Crud<Track> trackDao;

    @Inject
    private ObjectMapper objectMapper;

    @Route(path = "/add/:trackId/to/:playlistId", requestMethod = HttpMethod.post)
    public String addTrackToPlaylist(Request request, Response response) throws JsonProcessingException {
        Playlist playlist = playlistDao.getById(request.params("playlistId")).orElseThrow(() -> new ElepyException("Playlist not found!", 404));
        Track track = trackDao.getById(request.params("trackId")).orElseThrow(() -> new ElepyException("Track not found!", 404));
        if (!playlist.getTracks().contains(track)) {
            playlist.getTracks().add(track);
            playlistDao.update(playlist);
            response.type("application/json");
            return objectMapper.writeValueAsString(playlist);
        } else {
            throw new ElepyException(String.format("'%s' already exist in the playlist '%s'.", track.getTitle(), playlist.getName()), 404);
        }
    }


    @Route(path = "/remove/:trackId/from/:playlistId", requestMethod = HttpMethod.delete)
    public String removeTrackFromPlaylist(Request request, Response response) throws JsonProcessingException {
        Track track = trackDao.getById(request.params("trackId")).orElseThrow(() -> new ElepyException("Track not found!", 404));
        Playlist playlist = playlistDao.getById(request.params("playlistId")).orElseThrow(() -> new ElepyException("Playlist not found!", 404));

        if (playlist.getTracks().contains(track)) {
            playlist.getTracks().remove(track);
            playlistDao.update(playlist);
            response.type("application/json");
            return objectMapper.writeValueAsString(playlist);
        } else {
            throw new ElepyException(String.format("'%s' does not exist in the playlist '%s'.", track.getTitle(), playlist.getName()), 404);
        }
    }

}
