package com.hostelfix.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
@Entity @Table(name="staff_members")
public class StaffMember {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @NotBlank private String name;
  @NotBlank private String department;
  private String phone;
  private boolean active=true;
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getName(){return name;} public void setName(String name){this.name=name;}
  public String getDepartment(){return department;} public void setDepartment(String department){this.department=department;}
  public String getPhone(){return phone;} public void setPhone(String phone){this.phone=phone;}
  public boolean isActive(){return active;} public void setActive(boolean active){this.active=active;}
}
