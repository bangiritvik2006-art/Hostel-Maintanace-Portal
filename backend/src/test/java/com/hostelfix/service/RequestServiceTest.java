package com.hostelfix.service;

import com.hostelfix.model.*;
import com.hostelfix.repository.*;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestServiceTest {
  @Test void resolvingRequestSetsResolvedTimestampAndNotifies() {
    RequestRepository requests = mock(RequestRepository.class);
    StaffRepository staff = mock(StaffRepository.class);
    NotificationClient notifications = mock(NotificationClient.class);
    MaintenanceRequest request = new MaintenanceRequest();
    request.setTicketNumber("MR-1001");
    request.setStatus(RequestStatus.IN_PROGRESS);
    when(requests.findById(1L)).thenReturn(Optional.of(request));
    when(requests.save(request)).thenReturn(request);

    RequestService service = new RequestService(requests, staff, notifications);
    MaintenanceRequest result = service.updateStatus(1L, RequestStatus.RESOLVED);

    assertEquals(RequestStatus.RESOLVED, result.getStatus());
    assertNotNull(result.getResolvedAt());
    verify(notifications).notifyStatus("MR-1001", "RESOLVED");
  }
}
