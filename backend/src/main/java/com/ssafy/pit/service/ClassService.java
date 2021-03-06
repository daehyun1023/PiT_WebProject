package com.ssafy.pit.service;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ssafy.pit.entity.User;
import com.ssafy.pit.request.ClassSearchGetReq;
import com.ssafy.pit.request.CreateClassPostReq;
import com.ssafy.pit.request.SetVideoUrlsPostReq;
import com.ssafy.pit.response.ClassDetailGetRes;
import com.ssafy.pit.response.ClassListGetRes;
import com.ssafy.pit.response.RegisterClassGetRes;

public interface ClassService {

	List<ClassListGetRes> getClassList(ClassSearchGetReq searchInfo, String permission);
	List<ClassListGetRes> getClassList(String permission);
	ClassDetailGetRes getClassDetail(int classNo, String permission);
	List<ClassListGetRes> getClassLikesList(int userNo);
	int registerClassLikes(int userNo, int classNo);
	int deleteClassLikes(int userNo, int classNo);
	List<ClassListGetRes> getFinishedClassList(int userNo);
	List<RegisterClassGetRes> getRegisterClassList(int userNo);
	void createClass(CreateClassPostReq createClassInfo, User user) throws Exception;
	int getLatestClassNo() throws Exception;
	void createClassPhoto(MultipartHttpServletRequest request, int classNo) throws Exception;
	int enrollClass(User user, int classNo) throws Exception;
	void updateClassPermission(int classNo, String permission) throws Exception;
	List<String> getVideoUrls(int userNo, int classNo) throws Exception;
	void setVideoUrls(int userNo, int classNo, SetVideoUrlsPostReq setVideoUrlsInfo) throws Exception;
	List<RegisterClassGetRes> getTeachClassList(int userNo);
	List<ClassListGetRes> getFinishedTeachClassList(int userNo);
	void addClassCnt(int classNo);
	List<Date> getSaveTimtes(int userNo, int classNo) throws Exception;
	int createSubPhotos(MultipartHttpServletRequest request, int classNo);
	
}
