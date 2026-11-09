package com.hostelfix.notification;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;
@RestController @RequestMapping("/api/notifications")
public class NotificationController {
 public record NotificationEvent(@NotBlank String ticketNumber,@NotBlank String status){}
 @GetMapping("/health") public Map<String,String> health(){return Map.of("service","notification-service","status","UP");}
 @PostMapping @ResponseStatus(HttpStatus.ACCEPTED)
 public Map<String,String> receive(@Valid @RequestBody NotificationEvent event){
   System.out.println("NOTIFICATION EVENT: "+event.ticketNumber()+" changed to "+event.status());
   return Map.of("message","Notification event accepted","ticketNumber",event.ticketNumber(),"status",event.status(),"receivedAt",Instant.now().toString());
 }
}
