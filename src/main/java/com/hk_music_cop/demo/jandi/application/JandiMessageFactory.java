package com.hk_music_cop.demo.jandi.application;

import com.hk_music_cop.demo.jandi.dto.response.JandiWebhookRequest;
import com.hk_music_cop.demo.lottery.dto.request.LotteryRequest;
import com.hk_music_cop.demo.lottery.dto.response.LotteryResponse;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.List;

public interface JandiMessageFactory {
	JSONObject scheduleWeekMessage(LocalDate date);

	JSONObject scheduleDayMessage(LocalDate date);

	JSONObject chooseLotteryMessage(String imgURL);

	JSONObject errorMessage(String message);

	JSONObject infoMessage(JandiWebhookRequest jandiWebhookRequest);

	JSONObject registerLotteryMessage(LotteryRequest lotteryRequest);

	JSONObject deleteLotteryMessage(LotteryRequest lotteryRequest);

	JSONObject updateLotteryMessage(Long targetLotteryId, LotteryRequest lotteryRequest);

	JSONObject lotteryListMessage(List<LotteryResponse> lotteryResponseList);
}
