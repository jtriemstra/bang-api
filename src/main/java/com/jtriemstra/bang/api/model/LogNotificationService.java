package com.jtriemstra.bang.api.model;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LogNotificationService implements NotificationService {

	@Override
	public void notify(String s) {
		log.info("** " + s);
	}
}
