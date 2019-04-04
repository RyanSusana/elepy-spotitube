package com.ryansusana.spotitube.playlists;

import com.elepy.dao.Crud;
import com.elepy.exceptions.ElepyException;
import com.elepy.http.HttpContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryansusana.spotitube.Base;
import com.ryansusana.spotitube.tracks.Tracks;
import com.ryansusana.spotitube.tracks.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PlaylistServiceTest extends Base {

    @InjectMocks
    private PlaylistService playlistService;

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
        int supposedLength = playlistDao.getAll().size() * TRACK_LENGTH;

        playlistService.handleFindMany(mockedContext(), playlistDao, null, objectMapper);
        Playlists playlists = objectMapper.readValue(lastReturnedResult(), Playlists.class);

        assertEquals(supposedLength, playlists.getLength());
    }

    @Test
    void testCreate() throws Exception {

        int startLength = playlistDao.getAll().size();

        String request = "{ \"id\" : -1, \"name\" : \"Progressive Rock\", \"owner\" : false, \"tracks\": [] }";
        HttpContext mockedContext = mockedContext();

        when(mockedContext.request().body()).thenReturn(request);
        playlistService.handleCreate(mockedContext, playlistDao, null, objectMapper);
        Playlists playlists = objectMapper.readValue(lastReturnedResult(), Playlists.class);

        assertEquals(startLength + 1, playlists.getPlaylists().size());
    }

    @Test
    void testGetPlaylistTracks() throws Exception {
        int supposedTrackSize = playlistDao.getById(PLAYLIST_ID).orElseThrow(() -> new RuntimeException("Should not throw")).getTracks().size();

        HttpContext mockedContext = mockedContext();
        when(mockedContext.request().params("playlistId")).thenReturn("" + PLAYLIST_ID);
        playlistService.getTracksOnPlaylist(mockedContext.request(), mockedContext.response());

        assertEquals(supposedTrackSize, objectMapper.readValue(lastReturnedResult(), Tracks.class).getTracks().size());
    }

    @Test
    void testRemoveTrackFromPlaylist() {
        HttpContext mockedContext = mockedContext();

        when(mockedContext.request().params("playlistId")).thenReturn("" + PLAYLIST_ID);
        when(mockedContext.request().params("trackId")).thenReturn("" + TRACK_IDS[0]);

        assertDoesNotThrow(() -> playlistService.removeTrackFromPlaylist(mockedContext.request(), mockedContext.response()));
        assertEquals(0, playlistDao.getById(PLAYLIST_ID).orElseThrow(() -> new RuntimeException("Should not throw")).getTracks().size());

    }

    @Test
    void testRemoveTrackFromPlaylistThatsNotInPlaylist() {
        HttpContext mockedContext = mockedContext();

        when(mockedContext.request().params("playlistId")).thenReturn("" + PLAYLIST_ID);
        when(mockedContext.request().params("trackId")).thenReturn("" + TRACK_IDS[1]);

        ElepyException elepyException = assertThrows(ElepyException.class, () -> playlistService.removeTrackFromPlaylist(mockedContext.request(), mockedContext.response()));
        assertEquals(404, elepyException.getStatus());
        assertEquals(1, playlistDao.getById(PLAYLIST_ID).orElseThrow(() -> new RuntimeException("Should not throw")).getTracks().size());

    }

    @Test
    void testRemoveTrackFromPlaylistThatDoesntExist() {

        final int startSize = playlistDao.getById(PLAYLIST_ID).orElseThrow(() -> new RuntimeException("Should not throw")).getTracks().size();

        HttpContext mockedContext = mockedContext();
        when(mockedContext.request().params("playlistId")).thenReturn("" + PLAYLIST_ID + 1);
        when(mockedContext.request().params("trackId")).thenReturn("" + TRACK_IDS[0]);

        ElepyException elepyException = assertThrows(ElepyException.class, () -> playlistService.removeTrackFromPlaylist(mockedContext.request(), mockedContext.response()));
        assertEquals(404, elepyException.getStatus());
        assertEquals(startSize, playlistDao.getById(PLAYLIST_ID).orElseThrow(() -> new RuntimeException("Should not throw")).getTracks().size());

    }

    @Test
    void testAddTrackToPlaylistThatDoesntExist() throws JsonProcessingException {
        final int startSize = playlistDao.getById(PLAYLIST_ID).orElseThrow(() -> new RuntimeException("Should not throw")).getTracks().size();

        HttpContext mockedContext = mockedContext();

        when(mockedContext.request().params("playlistId")).thenReturn("" + PLAYLIST_ID + 1);
        String trackJson = objectMapper.writeValueAsString(trackDao.getById(TRACK_IDS[0]).orElseThrow(() -> new RuntimeException("Should not throw")));
        when(mockedContext.request().body()).thenReturn(trackJson);


        ElepyException elepyException = assertThrows(ElepyException.class, () -> playlistService.addTrackToPlaylist(mockedContext.request(), mockedContext.response()));
        assertEquals(404, elepyException.getStatus());
        assertEquals(startSize, playlistDao.getById(PLAYLIST_ID).orElseThrow(() -> new RuntimeException("Should not throw")).getTracks().size());

    }

    @Test
    void testReAddTrackToPlaylist() throws JsonProcessingException {
        final int startSize = playlistDao.getById(PLAYLIST_ID).orElseThrow(() -> new RuntimeException("Should not throw")).getTracks().size();

        HttpContext mockedContext = mockedContext();
        when(mockedContext.request().params("playlistId")).thenReturn("" + PLAYLIST_ID);
        String trackJson = objectMapper.writeValueAsString(trackDao.getById(TRACK_IDS[0]).orElseThrow(() -> new RuntimeException("Should not throw")));
        when(mockedContext.request().body()).thenReturn(trackJson);


        ElepyException elepyException = assertThrows(ElepyException.class, () -> playlistService.addTrackToPlaylist(mockedContext.request(), mockedContext.response()));
        assertEquals(400, elepyException.getStatus());
        assertEquals(startSize, playlistDao.getById(PLAYLIST_ID).orElseThrow(() -> new RuntimeException("Should not throw")).getTracks().size());

    }

    @Test
    void testAddTrackToPlaylist() throws JsonProcessingException {
        final int startSize = playlistDao.getById(PLAYLIST_ID).orElseThrow(() -> new RuntimeException("Should not throw")).getTracks().size();

        HttpContext mockedContext = mockedContext();
        when(mockedContext.request().params("playlistId")).thenReturn("" + PLAYLIST_ID);
        String trackJson = objectMapper.writeValueAsString(trackDao.getById(TRACK_IDS[1]).orElseThrow(() -> new RuntimeException("Should not throw")));
        when(mockedContext.request().body()).thenReturn(trackJson);


        assertDoesNotThrow(() -> playlistService.addTrackToPlaylist(mockedContext.request(), mockedContext.response()));
        assertEquals(startSize + 1, playlistDao.getById(PLAYLIST_ID).orElseThrow(() -> new RuntimeException("Should not throw")).getTracks().size());

    }
}
