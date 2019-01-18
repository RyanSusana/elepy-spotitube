package com.ryansusana.spotitube;

import com.elepy.Elepy;
import com.elepy.admin.ElepyAdminPanel;
import com.github.fakemongo.Fongo;
import com.ryansusana.spotitube.domain.Playlist;
import com.ryansusana.spotitube.domain.Track;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Main {

    public static void main(String[] args) {

        Logger.getRootLogger().setLevel(Level.INFO);
        org.apache.log4j.BasicConfigurator.configure();

        Fongo spotitube = new Fongo("spotitube");


        Elepy elepy = new Elepy()
                .onPort(1997)
                .connectDB(spotitube.getDB("fongo-db"))
                .addModelPackage("com.ryansusana.spotitube.domain")
                .addExtension(new ElepyAdminPanel());


        elepy.start();

        //DUMMY DATA
        Playlist playlist = new Playlist();
        Track track = new Track();

        track.setId("testTrack");
        track.setTitle("Coming In From The Cold");
        track.setPerformer("Bob Marley");
        track.setAlbum("Uprising");

        playlist.setId("testPlaylist");
        playlist.setName("From the Islands");

        elepy.getCrudFor(Playlist.class).create(playlist);
        elepy.getCrudFor(Track.class).create(track);
    }
}
