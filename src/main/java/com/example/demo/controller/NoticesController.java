package com.example.demo.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Notice;
import com.example.demo.repo.NoticeRepository;

@RestController
public class NoticesController {

	@GetMapping("/notices")
	public ResponseEntity<List<Notice>> getMyNotices() {
		List<Notice> notices = NoticeRepository.findAllActiveNotices();
		if(notices != null) {
			return ResponseEntity.ok()
					.cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
					.body(notices);
		} else {
			return null;
		}
	}
}
