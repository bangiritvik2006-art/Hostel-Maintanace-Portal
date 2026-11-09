package com.hostelfix.repository;
import com.hostelfix.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface RequestRepository extends JpaRepository<MaintenanceRequest,Long> {
  List<MaintenanceRequest> findAllByOrderByCreatedAtDesc();
  List<MaintenanceRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);
  long countByStatus(RequestStatus status);
}
