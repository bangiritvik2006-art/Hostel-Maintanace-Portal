package com.hostelfix.dto;
import com.hostelfix.model.RequestStatus;
import jakarta.validation.constraints.NotNull;
public record StatusUpdate(@NotNull RequestStatus status) {}
