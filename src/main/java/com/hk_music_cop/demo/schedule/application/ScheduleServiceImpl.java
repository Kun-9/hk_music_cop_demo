package com.hk_music_cop.demo.schedule.application;

import com.hk_music_cop.demo.external.google_cloud.google_sheet.GoogleSheetAPIImpl;
import com.hk_music_cop.demo.external.google_cloud.google_sheet.GoogleSheetProperties;
import com.hk_music_cop.demo.external.jandi.application.JandiMessageConverter;
import com.hk_music_cop.demo.external.jandi.dto.request.JandiWebhookRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

	private final GoogleSheetProperties googleSheetProperties;
	private final GoogleSheetAPIImpl googleSheetAPIImpl;
	private final JandiMessageConverter jandiMessageConverter;


	@Override
	public JSONObject registTodo(String name, String todo) {
		return null;
	}

	private int getNthWeek(LocalDate today) {

		// WeekFields 인스턴스 생성 (주의 시작을 일요일로 설정)
		WeekFields weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1);

		return today.get(weekFields.weekOfMonth());
	}

	@Override
	public List<List<String>> getWeekTodoData(String title, String color) {
		LocalDate now = LocalDate.now();
		int year = now.getYear();
		int month = now.getMonthValue();

		String sheetName = generateSheetName(year, month).toString();

		// 오늘이 몇번째 주인지 구하기
		int nthWeek = getNthWeek(now) - 1;

		Integer sheetNum = googleSheetProperties.getCalendar().getSheetNumbers().get(nthWeek);

		// 해당 주의 월요일 코드
		String startCode = googleSheetProperties.getCalendar().getDayCode().get(0) + sheetNum;

		// 해당 주의 금요일 코드
		String endCode = googleSheetProperties.getCalendar().getDayCode().get(4) + sheetNum;

		return googleSheetAPIImpl.getSheetDataParse(sheetName, startCode, endCode, true);
	}

	@Override
	public JSONObject getWeekTodo(String title, String color) {
		List<List<String>> weekTodoData = getWeekTodoData(title, color);
		JandiWebhookRequest jandiWebhookRequest = jandiMessageConverter.parseToRequestForm(title, color, weekTodoData);

		return jandiMessageConverter.createJandiSendMessage(jandiWebhookRequest);
	}


	@Override
	public List<List<String>> getTodayTodoData(String title, String color) {

		LocalDate now = LocalDate.now();
		int year = now.getYear();
		int month = now.getMonthValue();
		int day = now.getDayOfWeek().getValue();

		String sheetName = generateSheetName(year, month).toString();

		// 오늘이 몇번째 주인지 구하기
		int nthWeek = getNthWeek(now) - 1;

		String code = googleSheetProperties.getCalendar().getDayCode().get(day - 1) + googleSheetProperties.getCalendar().getSheetNumbers().get(nthWeek);
		return googleSheetAPIImpl.getSheetDataParse(sheetName, code, code, true);
	}

	@Override
	public JSONObject getTodayTodo(String title, String color) {
		List<List<String>> todayTodoData = getTodayTodoData(title, color);
		JandiWebhookRequest jandiWebhookRequest = jandiMessageConverter.parseToRequestForm(title, color, todayTodoData);

		return jandiMessageConverter.createJandiSendMessage(jandiWebhookRequest);
	}

	private static StringBuilder generateSheetName(int year, int month) {
		StringBuilder sheetName = new StringBuilder();
		sheetName.append(year).append(".").append(month).append(" ").append("월간 캘린더");
		return sheetName;
	}

	private LocalDate getMonday(LocalDate now) {
		return now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
	}
}