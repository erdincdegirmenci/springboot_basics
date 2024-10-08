package com.ornek.springbootproje.service;

import com.ornek.springbootproje.entities.UserAudit;
import com.ornek.springbootproje.repository.UserAuditRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuditService {

    private final UserAuditRepository userAuditRepository;

    private final HttpServletRequest httpServletRequest;

    @Autowired
    public AuditService(UserAuditRepository userAuditRepository,HttpServletRequest httpServletRequest) {
        this.userAuditRepository = userAuditRepository;
        this.httpServletRequest = httpServletRequest;
    }

    public UserAudit addUserAudit(UserAudit userAudit) {
        String deviceInfo = httpServletRequest.getHeader("User-Agent"); // Cihaz bilgisi
        String clientIp = httpServletRequest.getRemoteAddr(); // İstemci IP adresi
        userAudit.setDevice(deviceInfo);
        userAudit.setIpAddress(clientIp);
        userAudit.setCreateDate(new Date());
        return userAuditRepository.save(userAudit);
    }

    public List<UserAudit> getUserAuditsByUserId(Long userId) {
        return userAuditRepository.findByUserId(userId);
    }

    public List<UserAudit> getAllUserAudits() {
        return userAuditRepository.findAll();
    }
}
