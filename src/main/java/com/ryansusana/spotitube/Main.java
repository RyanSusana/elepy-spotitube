package com.ryansusana.spotitube;

import com.elepy.Elepy;
import com.github.fakemongo.Fongo;
import com.ryansusana.spotitube.presentation.Track;
import com.ryansusana.spotitube.presentation.User;
import com.ryansusana.spotitube.service.Authentication;

public class Main {

    public static void main(String[] args) {

        Fongo spotitube = new Fongo("spotitube");

        Elepy elepy = new Elepy()
                .onPort(1997)
                .connectDB(spotitube.getDB("fongo-db"))
                .addModelPackage("com.ryansusana.spotitube.presentation")
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
