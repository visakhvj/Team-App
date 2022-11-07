package com.visakh.test.teams.model;
import com.google.gson.Gson;

import java.util.List;
public class TeamDetails {

    public String id = "57994f271ca5dd20847b910c";
    public Members members;
    public Plan plan;

    public static TeamDetails convertToObject(String json) {
        return new Gson().fromJson(json, TeamDetails.class);
    }

    public static String convertToString(TeamDetails input){
        return new Gson().toJson(input);
    }
}
