package com.ryansusana.spotitube.playlists;

import com.elepy.annotations.Inject;
import com.elepy.annotations.Route;
import com.elepy.dao.Crud;
import com.elepy.describers.ModelDescription;
import com.elepy.exceptions.ElepyException;
import com.elepy.http.HttpContext;
import com.elepy.http.HttpMethod;
import com.elepy.http.Request;
import com.elepy.http.Response;
import com.elepy.id.NumberIdentityProvider;
import com.elepy.routes.DefaultService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryansusana.spotitube.tracks.Track;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistService extends DefaultService<Playlist> {

    @Inject
    private Crud<Playlist> playlistDao;

    @Inject
    private Crud<Track> trackDao;

    @Inject
    private ObjectMapper objectMapper;

    //This is a class from Elepy that generates ID's for a model with an Integer for a ID.
    private NumberIdentityProvider<Playlist> numberIdentityProvider = new NumberIdentityProvider<>();


    @Override
    //Override how Elepy finds playlists
    public void handleFindMany(HttpContext context, Crud<Playlist> crud, ModelDescription<Playlist> modelDescription, ObjectMapper objectMapper) throws Exception {
        Map<String, Object> toReturn = new HashMap<>();

        List<Playlist> playlists = playlistDao.getAll();

        toReturn.put("playlists", playlists);

        //Use the Stream API to add up the durations of the found playlists. A DBA would probably hate watching this :D.
        toReturn.put("length", playlists
                .stream()
                .mapToLong(playlist -> playlist
                        .getTracks()
                        .stream()
                        .mapToLong(Track::getDuration)
                        .sum())
                .sum()
        );
        context.response().result(objectMapper.writeValueAsString(toReturn));
    }

    @Override
    //Will not test this as the first call is from Elepy, and the second is already tested
    public void handleDelete(HttpContext context, Crud<Playlist> dao, ModelDescription<Playlist> modelDescription, ObjectMapper objectMapper) throws Exception {
        super.handleDelete(context, dao, modelDescription, objectMapper);
        handleFindMany(context, dao, modelDescription, objectMapper);
    }

    @Override
    public void handleCreate(HttpContext context, Crud<Playlist> dao, ModelDescription<Playlist> modelDescription, ObjectMapper objectMapper) throws Exception {
        Playlist playlist = objectMapper.readValue(context.request().body(), Playlist.class);

        //Set the owner, generate the ID and add the playlist to the database
        playlist.setOwner(true);
        numberIdentityProvider.provideId(playlist, dao);
        dao.create(playlist);

        //Execute a find after creation
        handleFindMany(context, dao, modelDescription, objectMapper);
    }


    @Route(path = "/playlists/:playlistId/tracks", method = HttpMethod.GET)
    public void getTracksOnPlaylist(Request request, Response response) throws JsonProcessingException {
        Playlist playlist = getPlaylist(request);

        Map<String, Object> toReturn = new HashMap<>();

        toReturn.put("tracks", playlist.getTracks());

        response.result(objectMapper.writeValueAsString(toReturn));
    }

    @Route(path = "/playlists/:playlistId/tracks", method = HttpMethod.POST)
    public String addTrackToPlaylist(Request request, Response response) throws IOException {

        Playlist playlist = getPlaylist(request);
        Track trackInRequest = getTrack(request);

        Track track = trackDao.getById(trackInRequest.getId()).orElseThrow(() -> new ElepyException("Track not found!", 404));
        track.setOfflineAvailable(trackInRequest.isOfflineAvailable());

        if (!playlist.getTracks().contains(track)) {
            playlist.getTracks().add(track);
            playlistDao.update(playlist);
            response.type("application/json");
            return objectMapper.writeValueAsString(playlist);
        } else {
            throw new ElepyException(String.format("'%s' already exist in the playlist '%s'.", track.getTitle(), playlist.getName()), 400);
        }
    }


    @Route(path = "/playlists/:playlistId/tracks/:trackId", method = HttpMethod.DELETE)
    public String removeTrackFromPlaylist(Request request, Response response) throws IOException {
        Track track = getTrack(request);
        Playlist playlist = getPlaylist(request);


        if (playlist.getTracks().contains(track)) {
            playlist.getTracks().remove(track);
            playlistDao.update(playlist);
            response.type("application/json");
            return objectMapper.writeValueAsString(playlist);
        } else {
            throw new ElepyException(String.format("'%s' does not exist in the playlist '%s'.", track.getTitle(), playlist.getName()), 404);
        }
    }


    private Playlist getPlaylist(Request request) {
        return playlistDao.getById(Integer.parseInt(request.params("playlistId"))).orElseThrow(() -> new ElepyException("Playlist not found!", 404));
    }


    private Track getTrack(Request request) throws IOException {
        if (request.params("trackId") != null) {
            return trackDao.getById(Integer.parseInt(request.params("trackId"))).orElseThrow(() -> new ElepyException("Track not found!", 404));
        }
        return objectMapper.readValue(request.body(), Track.class);
    }
}
