package com.hk_music_cop.demo.lottery.dto.request;

import lombok.*;

@Getter @ToString @AllArgsConstructor
public class LotteryRequest {
	private Long memberId;
	private String name;
	private String position;

}