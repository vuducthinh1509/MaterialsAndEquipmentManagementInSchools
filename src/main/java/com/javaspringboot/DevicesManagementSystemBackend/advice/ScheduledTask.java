package com.javaspringboot.DevicesManagementSystemBackend.advice;

import com.javaspringboot.DevicesManagementSystemBackend.enumm.EStatusMaintenance;
import com.javaspringboot.DevicesManagementSystemBackend.model.Device;
import com.javaspringboot.DevicesManagementSystemBackend.model.OutgoingGoodsNote;
import com.javaspringboot.DevicesManagementSystemBackend.repository.DeviceRepository;
import com.javaspringboot.DevicesManagementSystemBackend.repository.OutgoingGoodsNoteRepository;
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

@Component
public class ScheduledTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    static int count = 0;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private OutgoingGoodsNoteRepository outgoingGoodsNoteRepository;


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
            }
        }
        logger.info(String.format("Scheduled task is running... %d ",++count));
    }
}
