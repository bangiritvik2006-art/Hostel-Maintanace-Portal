package com.hostelfix.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
@Entity @Table(name="maintenance_requests")
public class MaintenanceRequest {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,unique=true) private String ticketNumber;
  @NotBlank private String reporterName;
  @NotBlank private String roomNumber;
  @NotBlank private String category;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Priority priority=Priority.MEDIUM;
  @NotBlank @Column(length=2000) private String description;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private RequestStatus status=RequestStatus.OPEN;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime resolvedAt;
  @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="staff_id") private StaffMember assignedStaff;
  @PrePersist void onCreate(){createdAt=LocalDateTime.now();updatedAt=createdAt;}
  @PreUpdate void onUpdate(){updatedAt=LocalDateTime.now();}
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getTicketNumber(){return ticketNumber;} public void setTicketNumber(String v){ticketNumber=v;}
  public String getReporterName(){return reporterName;} public void setReporterName(String v){reporterName=v;}
  public String getRoomNumber(){return roomNumber;} public void setRoomNumber(String v){roomNumber=v;}
  public String getCategory(){return category;} public void setCategory(String v){category=v;}
  public Priority getPriority(){return priority;} public void setPriority(Priority v){priority=v;}
  public String getDescription(){return description;} public void setDescription(String v){description=v;}
  public RequestStatus getStatus(){return status;} public void setStatus(RequestStatus v){status=v;}
  public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
  public LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(LocalDateTime v){updatedAt=v;}
  public LocalDateTime getResolvedAt(){return resolvedAt;} public void setResolvedAt(LocalDateTime v){resolvedAt=v;}
  public StaffMember getAssignedStaff(){return assignedStaff;} public void setAssignedStaff(StaffMember v){assignedStaff=v;}
}
