package com.stashbank.deliverymanagment.rest;
import retrofit2.http.*;
import retrofit2.*;
import java.util.*;

public interface GitHubClient {  
    @GET("/users/{user}/repos")
    Call<List<GitHubRepo>> reposForUser(
        @Path("user") String user
    );
}
