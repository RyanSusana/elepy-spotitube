package com.ryansusana.spotitube.service;

import com.elepy.dao.Crud;
import com.elepy.exceptions.ElepyException;
import com.elepy.http.HttpContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryansusana.spotitube.Base;
import com.ryansusana.spotitube.Tracks;
import com.ryansusana.spotitube.presentation.Playlist;
import com.ryansusana.spotitube.presentation.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TrackServiceTest extends Base {
    @InjectMocks
    private TrackService trackService;

    @Mock
    private Crud<Playlist> playlistDao;

    @Mock
    private Crud<Track> trackDao;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        initMocks(this);
        setupMockDaos(trackDao, playlistDao);
    }

    @Test
    void testFindMany() throws Exception {
        int supposedLength = trackDao.getAll().size();

        trackService.handleFindMany(mockedContext(), trackDao, null, objectMapper);
        Tracks tracks = objectMapper.readValue(lastReturnedResult(), Tracks.class);

        assertEquals(supposedLength, tracks.getTracks().size());
    }

    @Test
    void testFindManyForPlaylist() throws Exception {
        int supposedLength = trackDao.getAll().size();

        HttpContext mockedContext = mockedContext();

        when(mockedContext.request().queryParams("forPlaylist")).thenReturn("" + PLAYLIST_ID);
        trackService.handleFindMany(mockedContext, trackDao, null, objectMapper);
        Tracks tracks = objectMapper.readValue(lastReturnedResult(), Tracks.class);

        assertEquals(supposedLength - 1, tracks.getTracks().size());
    }

    @Test
    void testFindManyForPlaylistThatDoesntExist() {
        int supposedLength = trackDao.getAll().size();

        HttpContext mockedContext = mockedContext();

        when(mockedContext.request().queryParams("forPlaylist")).thenReturn("" + PLAYLIST_ID + 1);
        ElepyException elepyException = assertThrows(ElepyException.class, () -> trackService.handleFindMany(mockedContext, trackDao, null, objectMapper));


        assertEquals(404, elepyException.getStatus());
        assertEquals(supposedLength, trackDao.getAll().size());
    }

}
