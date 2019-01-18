# Elepy Spotitube

This repository is dedicated to show how easy Elepy is to use.
I made this to demonstrate how to create the Spotitube api from OOSE - DEA

You can go to https://localhost:1997/admin to create Tracks and Playlists

You can add a track to a playlist by using:
`POST /add/:trackId/to/:playlistId`

You can remove a track from a playlist by using:
`DELETE /remove/:trackId/from/:playlistId`

This repository further displays how Elepy's Dependency Injection(`@Inject`) works, along with `@Route` and `@Service`.
