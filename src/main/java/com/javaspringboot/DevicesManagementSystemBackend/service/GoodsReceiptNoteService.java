package com.javaspringboot.DevicesManagementSystemBackend.service;

import com.javaspringboot.DevicesManagementSystemBackend.repository.GoodsReceiptNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsReceiptNoteService {
    @Autowired
    private GoodsReceiptNoteRepository goodsReceiptNoteRepository;

//    public Set<Device> getGoodsReceiptNoteWithDevices(Long id) {
//        GoodsReceiptNote goodsReceiptNote = goodsReceiptNoteRepository.findByIdAndDevicesIsNotNull(id);
//        if (goodsReceiptNote != null) {
//            Set<Device> devices = goodsReceiptNote.getDevices();
//        }
//    }
}
