package com.hostelfix.controller;
import com.hostelfix.model.StaffMember;
import com.hostelfix.repository.StaffRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/staff")
public class StaffController {
  private final StaffRepository repo;
  public StaffController(StaffRepository repo){this.repo=repo;}
  @GetMapping public List<StaffMember> all(){return repo.findAll();}
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public StaffMember create(@Valid @RequestBody StaffMember s){return repo.save(s);}
}
