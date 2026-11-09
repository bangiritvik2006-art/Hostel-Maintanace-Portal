package com.hostelfix.repository;
import com.hostelfix.model.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
public interface StaffRepository extends JpaRepository<StaffMember,Long> {}
