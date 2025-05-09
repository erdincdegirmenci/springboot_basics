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

    public UserAudit AddUserAudit(UserAudit userAudit) {
        String deviceInfo = httpServletRequest.getHeader("User-Agent");
        String clientIp = httpServletRequest.getRemoteAddr();
        userAudit.setDevice(deviceInfo);
        userAudit.setIpaddress(clientIp);
        userAudit.setCreatedate(new Date());
        return userAuditRepository.save(userAudit);
    }

    public List<UserAudit> GetUserAuditsByUserId(Long userId) {
        return userAuditRepository.findByUserid(userId);
    }

    public List<UserAudit> GetAllUserAudits() {
        return userAuditRepository.findAll();
    }
}
