package com.jtriemstra.bang.api.model;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Profile("aws")
public class KakfaNotificationService implements NotificationService {

	@Override
	public void notify(String s) {
		
	}

}
