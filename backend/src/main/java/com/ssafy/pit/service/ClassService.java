package com.ssafy.pit.service;

import java.util.List;

import com.ssafy.pit.entity.Classes;
import com.ssafy.pit.request.ClassSearchGetReq;
import com.ssafy.pit.response.ClassDetailGetRes;
import com.ssafy.pit.response.ClassListGetRes;

public interface ClassService {

	List<ClassListGetRes> getClassList(ClassSearchGetReq searchInfo);
	List<ClassListGetRes> getClassList();
	ClassDetailGetRes getClassDetail(int classNo);
	List<ClassListGetRes> getClassLikesList(int userNo);
	int registerClassLikes(int userNo, int classNo);
	int deleteClassLikes(int userNo, int classNo);
	
}
