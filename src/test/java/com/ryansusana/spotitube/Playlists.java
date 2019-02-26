package com.ryansusana.spotitube;


import com.ryansusana.spotitube.presentation.Playlist;

import java.util.List;

public class Playlists {

    private List<Playlist> playlists;
    private long length;

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
