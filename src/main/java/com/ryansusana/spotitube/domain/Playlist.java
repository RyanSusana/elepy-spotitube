package com.ryansusana.spotitube.domain;

import com.elepy.annotations.*;
import com.elepy.models.AccessLevel;
import com.ryansusana.spotitube.services.PlaylistService;

import java.util.ArrayList;
import java.util.List;

@RestModel(slug = "/playlists", name = "Playlists")
@Service(PlaylistService.class)
@Create(accessLevel = AccessLevel.PUBLIC)
@Delete(accessLevel = AccessLevel.PUBLIC)
@Find(accessLevel = AccessLevel.PUBLIC)
@Update(accessLevel = AccessLevel.PUBLIC)
public class Playlist {
    private String id;
    private String name;
    private boolean owner;

    //Make default an empty list instead of null
    private List<Track> tracks = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

