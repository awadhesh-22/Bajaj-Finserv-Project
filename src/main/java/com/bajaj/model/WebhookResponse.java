package com.bajaj.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookResponse {
    // handles both "webhook " and "webhook"
    @JsonProperty("webhook ")
    @JsonAlias("webhook")
    private String webhook;

    // handles both "accessToken " and "accessToken"
    @JsonProperty("accessToken ")
    @JsonAlias("accessToken")
    private String accessToken;

    @JsonProperty("data")
    private Data data;

    public String getWebhook() { return webhook; }
    public void setWebhook(String webhook) { this.webhook = webhook; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("n")
        private int n;

        // handles both "findId " and "findId"
        @JsonProperty("findId ")
        @JsonAlias("findId")
        private int findId;

        @JsonProperty("users")
        private List<User> users;

        public int getN() { return n; }
        public void setN(int n) { this.n = n; }

        public int getFindId() { return findId; }
        public void setFindId(int findId) { this.findId = findId; }

        public List<User> getUsers() { return users; }
        public void setUsers(List<User> users) { this.users = users; }
    }
}
