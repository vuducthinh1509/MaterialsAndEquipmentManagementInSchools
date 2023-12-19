package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.advice;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.enumm.EStatusMaintenance;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.Device;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.Notification;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.OutgoingGoodsNote;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository.DeviceRepository;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository.NotificationRepository;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository.OutgoingGoodsNoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.javaspringboot.MaterialsAndEquipmentManagementInSchools.enumm.ETypeNotification.ADMIN_TO_SPECIFIC;

@Component
public class ScheduledTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    static int count = 0;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private OutgoingGoodsNoteRepository outgoingGoodsNoteRepository;

    @Autowired
    private NotificationRepository notificationRepository;


    @Scheduled(fixedRate = 24*60*60*1000) // Thời gian giữa các lần chạy là 1 ngày
    public void runTask() {
        List<OutgoingGoodsNote> outgoingGoodsNotes = outgoingGoodsNoteRepository.findAll();
        Date currentDate = new Date();
        for(OutgoingGoodsNote item : outgoingGoodsNotes){
            Instant export_date = item.getCreatedAt();
            long timestamp1 = currentDate.getTime();
            long timestamp2 = Timestamp.from(export_date).getTime();
            Set<Device> devices = item.getDevices();
            for(Device device : devices){
                long differenceInSeconds = (timestamp1 - timestamp2) / 1000 % (device.getMaintenanceTime()/12*365*24*60*60);
                if(differenceInSeconds < 24*60*60){
                    device.setMaintenanceStatus(EStatusMaintenance.CAN_BAO_TRI);
                    deviceRepository.save(device);
                }
                if(device.getMaintenanceStatus()!=null && device.getMaintenanceStatus().ordinal()!=0){
                    String message = String.format("Thiết bị %s cần được bảo trì",device.getSerial());
                    Notification notification = new Notification(message,ADMIN_TO_SPECIFIC,device.getOutgoingGoodsNote().getReceiver());
                    notificationRepository.save(notification);
                }
            }
        }
        logger.info(String.format("Scheduled task is running... %d ",++count));
    }
}
