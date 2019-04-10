package com.ryansusana.spotitube;

import com.elepy.Elepy;
import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.ryansusana.spotitube.playlists.Playlist;
import com.ryansusana.spotitube.tracks.Track;
import com.ryansusana.spotitube.users.User;
import com.ryansusana.spotitube.users.Authentication;

public class Main {

    public static void main(String[] args) {

        String databaseServer = System.getenv("DATABASE_SERVER") != null ? System.getenv("DATABASE_SERVER") : "localhost";
        String databasePort = System.getenv("DATABASE_PORT") != null ? System.getenv("DATABASE_PORT") : "27017";


        MongoClient mongoClient = new MongoClient(new ServerAddress(databaseServer, Integer.parseInt(databasePort)));

        DB database = (System.getenv("testing") == null) ? mongoClient.getDB("spotitube-db")
                : new Fongo("spotitube").getDB("spotitube-db");


        Elepy elepy = new Elepy()
                .onPort(1997)
                .connectDB(database)
                .addModel(Playlist.class)
                .addModel(Track.class)
                .addModel(User.class)
                .addAdminFilter(Authentication.class);


        elepy.start();


        //DUMMY DATA
        Track track = new Track();
        track.setId(192);
        track.setTitle("Coming In From The Cold");
        track.setPerformer("Bob Marley");
        track.setAlbum("Uprising");
        track.setDuration(500);

        Track track2 = new Track();
        track2.setId(195);
        track2.setTitle("Pimper's Paradise");
        track2.setPerformer("Bob Marley");
        track2.setAlbum("Uprising");
        track2.setDuration(400);

        User user = new User();
        user.setName("Ryan Susana");
        user.setUsername("ryan");
        user.setPassword("susana");

        elepy.getCrudFor(Track.class).create(track, track2);
        elepy.getCrudFor(User.class).create(user);
    }
}
