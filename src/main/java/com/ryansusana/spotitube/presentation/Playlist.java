package com.ryansusana.spotitube.presentation;

import com.elepy.annotations.*;
import com.elepy.models.AccessLevel;
import com.ryansusana.spotitube.service.PlaylistService;

import java.util.ArrayList;
import java.util.List;

@RestModel(slug = "/playlists", name = "Playlists")
@Service(PlaylistService.class)
@Create(accessLevel = AccessLevel.ADMIN)
@Delete(accessLevel = AccessLevel.ADMIN)
@Find(accessLevel = AccessLevel.ADMIN)
@Update(accessLevel = AccessLevel.ADMIN)
public class Playlist {
    private Integer id;
    private String name;
    private boolean owner;

    //Make default an empty list instead of null
    private List<Track> tracks = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}

