package com.bajaj.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StartupService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    // 🔧 YOUR DETAILS
    private final String name   = "Your Name";
    private final String regNo  = "RA2211003011520";
    private final String email  = "your@email.com";

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        try {
            // 1) Fetch raw JSON from generateWebhook
            String raw = callGenerateWebhook();
            System.out.println("📥 Received raw JSON:\n" + raw);

            // 2) Parse
            JsonNode root = mapper.readTree(raw);
            String webhookUrl  = firstText(root, "webhook ", "webhook");
            String accessToken = firstText(root, "accessToken ", "accessToken");
            JsonNode dataNode  = root.get("data");
            JsonNode usersNode = dataNode.get("users");

            // 3) Decide Q1 vs Q2
            Object outcome;
            if (usersNode.isArray()) {
                // QUESTION 1: mutual follows
                outcome = solveQ1(usersNode);
            } else if (usersNode.isObject()) {
                // QUESTION 2: nth-level
                int n      = usersNode.path("n").asInt();
                int findId = firstInt(usersNode, "findId ", "findId");
                JsonNode arr = usersNode.get("users");
                outcome = solveQ2(arr, findId, n);
            } else {
                throw new IllegalStateException("Unknown data.users format");
            }

            // 4) Send it
            sendToWebhook(webhookUrl, accessToken, outcome);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String callGenerateWebhook() {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook";
        Map<String,String> body = Map.of(
            "name",  name,
            "regNo", regNo,
            "email", email
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String,String>> req = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(url, req, String.class).getBody();
    }

    // Q1: find all 2-node cycles [min,max]
    private List<List<Integer>> solveQ1(JsonNode arr) {
        Map<Integer,List<Integer>> graph = new HashMap<>();
        for (JsonNode u : arr) {
            int id = u.path("id").asInt();
            JsonNode f = u.has("follows ") ? u.path("follows ") : u.path("follows");
            List<Integer> fl = new ArrayList<>();
            for (JsonNode x : f) fl.add(x.asInt());
            graph.put(id, fl);
        }
        Set<List<Integer>> pairs = new LinkedHashSet<>();
        for (int u : graph.keySet()) {
            for (int v : graph.get(u)) {
                if (graph.containsKey(v) && graph.get(v).contains(u)) {
                    int a = Math.min(u, v), b = Math.max(u, v);
                    pairs.add(Arrays.asList(a, b));
                }
            }
        }
        return new ArrayList<>(pairs);
    }

    // Q2: BFS to nth-level followers
    private List<Integer> solveQ2(JsonNode arr, int startId, int level) {
        Map<Integer,List<Integer>> graph = new HashMap<>();
        for (JsonNode u : arr) {
            int id = u.path("id").asInt();
            JsonNode f = u.has("follows ") ? u.path("follows ") : u.path("follows");
            List<Integer> fl = new ArrayList<>();
            for (JsonNode x : f) fl.add(x.asInt());
            graph.put(id, fl);
        }
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startId);
        visited.add(startId);
        int lvl = 0;
        while (!queue.isEmpty() && lvl < level) {
            int sz = queue.size();
            for (int i = 0; i < sz; i++) {
                int cur = queue.poll();
                for (int v : graph.getOrDefault(cur, Collections.emptyList())) {
                    if (visited.add(v)) queue.add(v);
                }
            }
            lvl++;
        }
        return new ArrayList<>(queue);
    }

    // Universal sender for either List<List<Integer>> or List<Integer>
    private void sendToWebhook(String url, String token, Object outcome) {
        try {
            String payload = mapper.writeValueAsString(Map.of("outcome", outcome));
            System.out.println("📦 Sending JSON to webhook:\n" + payload);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token);
            HttpEntity<String> req = new HttpEntity<>(payload, headers);

            for (int i = 1; i <= 4; i++) {
                try {
                    ResponseEntity<String> resp = restTemplate.postForEntity(url, req, String.class);
                    if (resp.getStatusCode().is2xxSuccessful()) {
                        System.out.println("✅ Webhook sent successfully!");
                        return;
                    } else {
                        System.out.printf("❌ Attempt %d failed: %s – %s%n", i, resp.getStatusCode(), resp.getBody());
                    }
                } catch (Exception e) {
                    System.out.printf("❌ Exception on attempt %d: %s%n", i, e.getMessage());
                }
            }
            System.out.println("❌ All 4 attempts failed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helpers to handle keys with or without trailing space
    private String firstText(JsonNode node, String... names) {
        for (String n : names) {
            if (node.has(n)) return node.get(n).asText();
        }
        return "";
    }
    private int firstInt(JsonNode node, String... names) {
        for (String n : names) {
            if (node.has(n)) return node.get(n).asInt();
        }
        return 0;
    }
}
