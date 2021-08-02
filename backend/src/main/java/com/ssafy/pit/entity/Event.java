package com.ssafy.pit.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="event")
@Getter
@Setter
public class Event {
	
	@Id
	@Column(name="event_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int eventNo;
	@Column(name="event_title")
	String eventTitle;
	@Column(name="event_content")
	String eventContent;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="Asia/Seoul")
	@Column(name="event_start_date")
	@Temporal(TemporalType.DATE)
	Date eventStartDate;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="Asia/Seoul")
	@Column(name="event_end_date")
	@Temporal(TemporalType.DATE)
	Date eventEndDate;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="Asia/Seoul")
	@Column(name="event_start_time")
	@Temporal(TemporalType.TIMESTAMP)
	Date eventStartTime;
	@Column(name="event_end_time")
	@Temporal(TemporalType.TIMESTAMP)
	Date eventEndTime;
	@Column(name="event_image")
	String eventImage;
}
