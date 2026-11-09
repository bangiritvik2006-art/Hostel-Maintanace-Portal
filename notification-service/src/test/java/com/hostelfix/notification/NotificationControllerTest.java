package com.hostelfix.notification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(NotificationController.class)
class NotificationControllerTest {
 @Autowired MockMvc mvc;
 @Test void healthEndpointIsUp() throws Exception {
   mvc.perform(get("/api/notifications/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP"));
 }
}
