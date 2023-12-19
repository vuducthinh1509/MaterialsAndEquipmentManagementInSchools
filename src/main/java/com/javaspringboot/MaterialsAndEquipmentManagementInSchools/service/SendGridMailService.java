package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.service;

import com.sendgrid.Content;

public interface SendGridMailService {
    void sendMail(String subject, Content content, String sendTo);
}
