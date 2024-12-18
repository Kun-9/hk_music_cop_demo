package com.hk_music_cop.demo.jandi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jandi")
public record JandiProperties(Color color, Title title) {

	// record : Getter, 생성자, 불변성 클래스 포함
	public record Color(String failColor, String successColor) {}

	public record Title(
			String infoTitle,
			String weekScheduleTitle,
			String dayScheduleTitle,
			String lotteryTitle,
			String successTitle,
			String failTitle
	) {}
}


