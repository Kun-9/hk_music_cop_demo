package com.hk_music_cop.demo.external.jandi.application;

import com.hk_music_cop.demo.external.jandi.dto.request.JandiWebhookRequest;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;

import java.util.List;


public interface JandiMessageConverter {

	/**
	 * 잔디 request 메시지 엔티티 생성
	 * @param webhookURL 웹훅 URL
	 * @param JandiWebhookRequest 잔디 요청 DTO
	 * @return Request Entity
	 */
	HttpEntity<String> createJandiRequestMessageEntity(String webhookURL, JandiWebhookRequest JandiWebhookRequest);

	/**
	 * 잔디 request 메시지 생성
	 * @param jandiWebhookRequest 잔디 요청 DTO
	 * @return Request JSON
	 */
	JSONObject createJandiSendMessage(JandiWebhookRequest jandiWebhookRequest);


	/**
	 * 잔디 RequestDTO로 파싱
	 * @param title 잔디 메시지 제목
	 * @param color 잔디 메시지 색상
	 * @param result 잔디 응답 메시지 배열
	 * @return JandiWebhookRequest DTO
	 */
	JandiWebhookRequest parseToRequestForm(String title, String color, List<List<String>> result);
}