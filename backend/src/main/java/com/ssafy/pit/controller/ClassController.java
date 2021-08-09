package com.ssafy.pit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pit.common.auth.PitUserDetails;
import com.ssafy.pit.common.response.BaseResponseBody;
import com.ssafy.pit.entity.User;
import com.ssafy.pit.request.ClassSearchGetReq;
import com.ssafy.pit.request.CreateClassPostReq;
import com.ssafy.pit.response.ClassDetailGetRes;
import com.ssafy.pit.response.ClassListGetRes;
import com.ssafy.pit.response.RegisterClassGetRes;
import com.ssafy.pit.service.ClassService;
import com.ssafy.pit.service.UserService;

@RequestMapping("/v1/class")
@RestController
public class ClassController {
	
	@Autowired
	ClassService classService;
	
	@Autowired
	UserService userService;
	
	// 클래스 상세값 가져오기
	@GetMapping("/{classNo}")
	public ResponseEntity<ClassDetailGetRes> getClassDetail(@PathVariable int classNo){
		ClassDetailGetRes classDetail = classService.getClassDetail(classNo);
		return ResponseEntity.status(200).body(classDetail);
	}
	
	// 클래스 리스트 가져오기
	@GetMapping()
	public ResponseEntity<List<ClassListGetRes>> getClassList(@RequestParam ClassSearchGetReq searchInfo) {
		List<ClassListGetRes> classList = null;
		System.out.println(searchInfo.toString());
		
		if(searchInfo.getClassDay() == null && searchInfo.getClassEndTime() == null && searchInfo.getClassLevel() == null
				&& searchInfo.getClassType() == null && searchInfo.getClassStartTime() == null 
				&& searchInfo.getClassEndTime() == null && searchInfo.getSearchKeyword() == null) {
			System.out.println("홈 전체 클래스 리스트 조회");
			classList = classService.getClassList();		
		}
		else {
			classList = classService.getClassList(searchInfo);
		}
		return ResponseEntity.status(200).body(classList);
	}
	
	// 찜클 목록 가져오기
	@GetMapping("/likes")
	public ResponseEntity<List<ClassListGetRes>> getClassLikesList(Authentication authentication) {
		List<ClassListGetRes> classLikesList = null;
		PitUserDetails userDetails = (PitUserDetails) authentication.getDetails();
		String userEmail = userDetails.getUsername();
		int userNo = userDetails.getUser().getUserNo();
		if(userService.validateUserType(userEmail) == 3) {
			classLikesList = classService.getClassLikesList(userNo);
			return ResponseEntity.status(200).body(classLikesList);
		}
		else {
			return ResponseEntity.status(404).body(classLikesList);
		}
	}
	
	// 찜클 등록
	@PostMapping("/likes/{classNo}")
	public ResponseEntity<BaseResponseBody> registerClassLikes(Authentication authentication, @PathVariable int classNo){
		PitUserDetails userDetails = (PitUserDetails) authentication.getDetails();
		String userEmail = userDetails.getUsername();
		int userNo = userDetails.getUser().getUserNo();
		if(userService.validateUserType(userEmail) == 3) {
			if(classService.registerClassLikes(userNo, classNo) == 1) {
				return ResponseEntity.status(200).body(BaseResponseBody.of(200, "찜한클래스로 등록되었습니다."));
			}
			else {				
				return ResponseEntity.status(500).body(BaseResponseBody.of(500, "등록과정에 문제가 생겼습니다."));
			}
		}
		else {
			return ResponseEntity.status(404).body(BaseResponseBody.of(404, "접근할 수 없는 페이지입니다."));
		}
	}
	
	// 찜클 삭제
	@DeleteMapping("/likes/{classNo}")
	public ResponseEntity<BaseResponseBody> deleteClassLikes(Authentication authentication, @PathVariable int classNo){
		PitUserDetails userDetails = (PitUserDetails) authentication.getDetails();
		String userEmail = userDetails.getUsername();
		int userNo = userDetails.getUser().getUserNo();
		if(userService.validateUserType(userEmail) == 3) {
			if(classService.deleteClassLikes(userNo, classNo) == 1) {
				return ResponseEntity.status(200).body(BaseResponseBody.of(200, "찜한클래스가 해제되었습니다."));
			}
			else {				
				return ResponseEntity.status(500).body(BaseResponseBody.of(500, "찜한 클래스 삭제과정에 문제가 생겼습니다."));
			}
		}
		else {
			return ResponseEntity.status(404).body(BaseResponseBody.of(404, "접근할 수 없는 페이지입니다."));
		}
	}
	
	// 수강완료 클래스
	@GetMapping("/finishedclass")
	public ResponseEntity<List<ClassListGetRes>> getFinishedClassList(Authentication authentication) {
		List<ClassListGetRes> finishedClassList = null;
		PitUserDetails userDetails = (PitUserDetails) authentication.getDetails();
		String userEmail = userDetails.getUsername();
		int userNo = userDetails.getUser().getUserNo();
		if(userService.validateUserType(userEmail) == 3 || userService.validateUserType(userEmail) == 2) {
			finishedClassList = classService.getFinishedClassList(userNo);
			return ResponseEntity.status(200).body(finishedClassList);
		}
		else {
			return ResponseEntity.status(404).body(finishedClassList);
		}
	}
	
	// 수강중 클래스
	@GetMapping("/registerclass")
	public ResponseEntity<List<RegisterClassGetRes>> getRegisterClassList(Authentication authentication) {
		List<RegisterClassGetRes> registerClassList = null;
		PitUserDetails userDetails = (PitUserDetails) authentication.getDetails();
		String userEmail = userDetails.getUsername();
		int userNo = userDetails.getUser().getUserNo();
		if(userService.validateUserType(userEmail) == 3 || userService.validateUserType(userEmail) == 2) {
			registerClassList = classService.getRegisterClassList(userNo);
			return ResponseEntity.status(200).body(registerClassList);
		}
		else {
			return ResponseEntity.status(404).body(registerClassList);
		}
	}
	
	// 클래스 생성 (관리자에게 승인신청)
	@PostMapping("/create")
	public ResponseEntity<BaseResponseBody> createClass(Authentication authentication, @RequestBody CreateClassPostReq createClassInfo) {
		PitUserDetails userDetails = (PitUserDetails) authentication.getDetails();
		User user = userDetails.getUser();
		String userEmail = userDetails.getUsername();
		if(userService.validateUserType(userEmail) == 2) {
			try {
				// 클래스에 정보 추가
				classService.createClass(createClassInfo, user);
				System.out.println("클래스 생성 성공!");
				// class_photo 테이블에 넣을 classNo 값 구하기
				int classNo = classService.getLatestClassNo();
				System.out.println("추가된 클래스의 최신 classNo: " + classNo );
				String thumbnailPhoto = createClassInfo.getClassThumbnail();
				// 썸네일 이미지 넣기
				classService.createClassPhoto(thumbnailPhoto, classNo, true);
				System.out.println("섬네일 등록 성공!");
				// 서브이미지들 넣기
				List<String> subPhotos = createClassInfo.getClassSubPhotos();
				
				for(String subPhoto: subPhotos) {
					classService.createClassPhoto(subPhoto, classNo, false);
				}
				System.out.println("서브 사진 등록 성공!");
				
				return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 클래스가 승인요청 처리되었습니다."));				
			}
			catch (Exception e) {
				return ResponseEntity.status(500).body(BaseResponseBody.of(500, "클래스 승인요청 과정에 문제가 발생하였습니다."));
			}			
		}
		else {
			return ResponseEntity.status(404).body(BaseResponseBody.of(404, "접근할 수 없는 페이지입니다."));
		}
	}
	
	// 클래스 등록하기 (트레이너가 클래스 신청하기)
	@PutMapping("/enrollment/{classNo}")
	public ResponseEntity<BaseResponseBody> enrollClass(Authentication authentication, @PathVariable int classNo) {
		PitUserDetails userDetails = (PitUserDetails) authentication.getDetails();
		User user = userDetails.getUser();
		String userEmail = userDetails.getUsername();
		
		if(userService.validateUserType(userEmail) == 3) {
			try {
				if(classService.enrollClass(user, classNo) == 1) {
					System.out.println("클래스 신청등록 완료!");
					return ResponseEntity.status(200).body(BaseResponseBody.of(200, "해당 클래스가 등록처리되었습니다."));	
				}
				else {
					return ResponseEntity.status(500).body(BaseResponseBody.of(500, "클래스 등록신청 과정에 문제가 발생하였습니다."));
				}
			}
			catch (Exception e) {
				return ResponseEntity.status(500).body(BaseResponseBody.of(500, "클래스 등록신청 과정에 문제가 발생하였습니다."));
			}
		}
		else {
			return ResponseEntity.status(404).body(BaseResponseBody.of(404, "접근할 수 없는 페이지입니다."));
		}
		
	}
	
}
