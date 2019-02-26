package com.ryansusana.spotitube;

import com.elepy.dao.Crud;
import com.elepy.http.HttpContext;
import com.elepy.http.Request;
import com.elepy.http.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryansusana.spotitube.presentation.Playlist;
import com.ryansusana.spotitube.presentation.Track;

import java.io.IOException;
import java.util.*;

import static org.mockito.AdditionalMatchers.and;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class Base {
    private String returnedResult = "";

    protected static final int TRACK_LENGTH = 500;

    protected static final int PLAYLIST_ID = 3333;

    protected static final int[] TRACK_IDS = {192, 195};

    private List<Playlist> playlists = new ArrayList<>();

    protected String lastReturnedResult() {
        return returnedResult;
    }

    //Helper method to transform a login response to a token
    protected String loginStringToToken(String login) throws IOException {
        Map<String, String> requestMap = new ObjectMapper().readValue(login, new TypeReference<HashMap<String, String>>() {
        });
        return requestMap.get("token");
    }

    //Helper method  to create a mocked HttpContext
    protected HttpContext mockedContext() {
        HttpContext context = mock(HttpContext.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        doAnswer(invocationOnMock -> {
            returnedResult = invocationOnMock.getArgument(0);
            return null;
        }).when(response).result(anyString());

        when(context.response()).thenReturn(response);
        when(context.request()).thenReturn(request);


        return context;
    }


    //Setups mocks justwith default data
    protected void setupMockDaos(Crud<Track> trackDao, Crud<Playlist> playlistDao) {


        Track track = new Track();
        track.setId(TRACK_IDS[0]);
        track.setTitle("Coming In From The Cold");
        track.setPerformer("Bob Marley");
        track.setAlbum("Uprising");
        track.setDuration(TRACK_LENGTH);

        Track track2 = new Track();
        track2.setId(TRACK_IDS[1]);
        track2.setTitle("Pimper's Paradise");
        track2.setPerformer("Bob Marley");
        track2.setAlbum("Uprising");
        track2.setDuration(TRACK_LENGTH);

        Playlist playlist = new Playlist();
        playlist.setId(3333);
        playlist.setName("From the Islands");
        playlist.setOwner(true);
        playlist.getTracks().add(track);

        playlists.add(playlist);
        when(trackDao.getAll()).thenReturn(Arrays.asList(track, track2));
        when(trackDao.getById(TRACK_IDS[0])).thenReturn(Optional.of(track));
        when(trackDao.getById(TRACK_IDS[1])).thenReturn(Optional.of(track2));
        when(trackDao.getById(and(not(eq(TRACK_IDS[1])), not(eq(TRACK_IDS[0]))))).thenReturn(Optional.empty());

        when(playlistDao.getById(PLAYLIST_ID)).thenReturn(Optional.of(playlist));
        when(playlistDao.getType()).thenReturn(Playlist.class);
        when(playlistDao.getById(not(eq(PLAYLIST_ID)))).thenReturn(Optional.empty());
        when(playlistDao.getAll()).thenReturn(playlists);

        doAnswer(invocationOnMock -> {
            playlists.add(invocationOnMock.getArgument(0));
            return null;
        }).when(playlistDao).create(any(Playlist.class));
    }
}
