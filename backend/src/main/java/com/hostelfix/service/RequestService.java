package com.hostelfix.service;
import com.hostelfix.model.*;
import com.hostelfix.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
@Service
public class RequestService {
  private final RequestRepository requests; private final StaffRepository staff; private final NotificationClient notifications;
  public RequestService(RequestRepository r, StaffRepository s, NotificationClient n){requests=r;staff=s;notifications=n;}
  public List<MaintenanceRequest> findAll(String search,String status,String category) {
    return requests.findAllByOrderByCreatedAtDesc().stream()
      .filter(r -> search==null || search.isBlank() || (r.getTicketNumber()+" "+r.getReporterName()+" "+r.getRoomNumber()+" "+r.getDescription()).toLowerCase().contains(search.toLowerCase()))
      .filter(r -> status==null || status.isBlank() || r.getStatus().name().equalsIgnoreCase(status))
      .filter(r -> category==null || category.isBlank() || r.getCategory().equalsIgnoreCase(category))
      .collect(Collectors.toList());
  }
  public MaintenanceRequest findById(Long id){return requests.findById(id).orElseThrow(()->new NoSuchElementException("Request not found: "+id));}
  @Transactional public MaintenanceRequest create(MaintenanceRequest r) {
    long next=1000+requests.count()+1; r.setTicketNumber("MR-"+next); r.setStatus(RequestStatus.OPEN);
    return requests.save(r);
  }
  @Transactional public MaintenanceRequest update(Long id,MaintenanceRequest input) {
    MaintenanceRequest r=findById(id); r.setReporterName(input.getReporterName());r.setRoomNumber(input.getRoomNumber());
    r.setCategory(input.getCategory());r.setPriority(input.getPriority());r.setDescription(input.getDescription());
    return requests.save(r);
  }
  @Transactional public MaintenanceRequest updateStatus(Long id,RequestStatus status) {
    MaintenanceRequest r=findById(id);r.setStatus(status);
    if(status==RequestStatus.RESOLVED || status==RequestStatus.CLOSED) r.setResolvedAt(java.time.LocalDateTime.now());
    else r.setResolvedAt(null);
    MaintenanceRequest saved=requests.save(r); notifications.notifyStatus(saved.getTicketNumber(),status.name()); return saved;
  }
  @Transactional public MaintenanceRequest assign(Long id,Long staffId) {
    MaintenanceRequest r=findById(id); StaffMember person=staff.findById(staffId).orElseThrow(()->new NoSuchElementException("Staff member not found"));
    r.setAssignedStaff(person);r.setStatus(RequestStatus.ASSIGNED);return requests.save(r);
  }
  public void delete(Long id){requests.delete(findById(id));}
  public Map<String,Long> stats(){
    long total=requests.count(), open=requests.countByStatus(RequestStatus.OPEN);
    long resolved=requests.countByStatus(RequestStatus.RESOLVED)+requests.countByStatus(RequestStatus.CLOSED);
    long inProgress=requests.countByStatus(RequestStatus.IN_PROGRESS)+requests.countByStatus(RequestStatus.ASSIGNED);
    return Map.of("total",total,"open",open,"inProgress",inProgress,"resolved",resolved);
  }
}
