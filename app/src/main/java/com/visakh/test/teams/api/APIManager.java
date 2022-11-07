package com.visakh.test.teams.api;

import com.visakh.test.teams.model.TeamDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class APIManager {

    private static String BASE_URL="http://localhost/teams/";

    String[]  TEAM_DETAILS = {"{\n" +
            "  \"id\": \"12233333\",\n" +
            "  \"members\": {\n" +
            "    \"total\": 89,\n" +
            "    \"administrators\": 1,\n" +
            "    \"managers\": 18,\n" +
            "    \"editors\": 6,\n" +
            "    \"members\": 58,\n" +
            "    \"supporters\": 6\n" +
            "}, \"plan\": {\n" +
            "    \"memberLimit\": 100,\n" +
            "    \"supporterLimit\": 20\n" +
            "  }\n" +
            "}","{\n" +
            "  \"id\": \"57994f27111ca5dd20847b910c\",\n" +
            "  \"members\": {\n" +
            "    \"total\": 89,\n" +
            "    \"administrators\": 1,\n" +
            "    \"managers\": 18,\n" +
            "    \"editors\": 6,\n" +
            "    \"members\": 58,\n" +
            "    \"supporters\": 6\n" +
            "}, \"plan\": {\n" +
            "    \"memberLimit\": 200,\n" +
            "    \"supporterLimit\": 30\n" +
            "  }\n" +
            "}",
            "{\n" +
                    "  \"id\": \"57994f27111ca5dd20847b910c\",\n" +
                    "  \"members\": {\n" +
                    "    \"total\": 89,\n" +
                    "    \"administrators\": 1,\n" +
                    "    \"managers\": 18,\n" +
                    "    \"editors\": 6,\n" +
                    "    \"members\": 58,\n" +
                    "    \"supporters\": 0\n" +
                    "}, \"plan\": {\n" +
                    "    \"memberLimit\": 200,\n" +
                    "    \"supporterLimit\": 0\n" +
                    "  }\n" +
                    "}"};

    public TeamDetails getTeamDetails(String teamID){
        String url = BASE_URL+teamID;
        Random generator = new Random();
        int randomIndex = generator.nextInt(TEAM_DETAILS.length);
        String apiResponseDummy= TEAM_DETAILS[randomIndex];
        TeamDetails teamDetails = TeamDetails.convertToObject(apiResponseDummy);
        return teamDetails;
    }

    public String createInvitationURL(String teamID,String role){
        String url = BASE_URL+teamID+"/invites";
        //Create POST request
        //Add Body
        JSONObject body = new JSONObject();
        try {
            body.put("role",role);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String dummyURL = "https://example.com/ti/eyJpbnZpdGVJZ";
        return dummyURL;
    }
}
