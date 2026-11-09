package com.hostelfix.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
@Component
public class NotificationClient {
  private final RestClient client;
  public NotificationClient(@Value("${notification.service.url}") String url) { this.client=RestClient.builder().baseUrl(url).build(); }
  public void notifyStatus(String ticket, String status) {
    try { client.post().uri("/api/notifications").body(new Event(ticket,status)).retrieve().toBodilessEntity(); }
    catch (Exception ignored) { /* Main request workflow must continue if notification service is offline. */ }
  }
  public record Event(String ticketNumber,String status) {}
}
