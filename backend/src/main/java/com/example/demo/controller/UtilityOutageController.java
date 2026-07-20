package com.example.demo.controller;

import com.example.demo.entity.UtilityOutage;
import com.example.demo.service.UtilityOutageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/out