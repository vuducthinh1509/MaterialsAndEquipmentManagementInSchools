package com.javaspringboot.DevicesManagementSystemBackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;

@SpringBootTest
class DevicesManagementSystemBackendApplicationTests {

	@Test
	void contextLoads() {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(2023, Calendar.JANUARY, 1); // Đặt ngày 1/1/2023
		Date date1 = calendar1.getTime();

		// Tạo đối tượng Date 2
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(2022, Calendar.JANUARY, 1); // Đặt ngày 1/1/2022
		Date date2 = calendar2.getTime();

		// Trừ hai đối tượng Date
		long differenceInMillis = date1.getTime() - date2.getTime();
		System.out.println(differenceInMillis);

		// Thực hiện phép chia phần dư cho 12
		long remainder = differenceInMillis % 31536000000L;

		System.out.println("Phần dư: " + remainder);
	}

}
