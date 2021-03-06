package com.ssafy.pit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pit.common.auth.PitUserDetails;
import com.ssafy.pit.common.response.BaseResponseBody;
import com.ssafy.pit.repository.PtroomRepositorySupport;
import com.ssafy.pit.service.ClassService;
import com.ssafy.pit.service.PtroomService;
import com.ssafy.pit.service.UserService;

@CrossOrigin(
        origins = {"https://i5a204.p.ssafy.io:5000", "https://i5a204.p.ssafy.io:8083", "https://localhost:8083"}, 
        allowCredentials = "true", 
        allowedHeaders = "*", 
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS}
)
@RequestMapping("/v1/ptroom/")
@RestController
public class PtroomController {

	@Autowired
	UserService userService;
	
	@Autowired
	ClassService classService;
	
	@Autowired
	PtroomService ptroomService;
	
	@Autowired
	PtroomRepositorySupport ptroomRepositorySupport;
	
	// PTroom ์ธ์ ์กฐํ
	@GetMapping("/{classNo}")
	public ResponseEntity<Integer> getUserCnt(Authentication authentication, @PathVariable int classNo){
		PitUserDetails userDetails = (PitUserDetails) authentication.getDetails();
		String userEmail = userDetails.getUsername();
		if(userService.validateUserType(userEmail) == 1) {
			return ResponseEntity.status(403).body(null);
		}
		else {
			try {
				int userCnt = ptroomService.getUserCnt(classNo);
				return ResponseEntity.status(200).body(userCnt);				
			}
			catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(500).body(null);
			}
		}
	}
	
	
	// PTroom ์ธ์ ๋ค์ ๋ฐํ
	@GetMapping("/enter/{classNo}")
	public ResponseEntity<String> getSessionName(Authentication authentication, @PathVariable int classNo) {
		PitUserDetails userDetails = (PitUserDetails) authentication.getDetails();
		String userEmail = userDetails.getUsername();
		if(userService.validateUserType(userEmail) == 1) {
			return ResponseEntity.status(403).body(null);
		}
		else {
			try {
				String sessionName = ptroomRepositorySupport.getPtroomByClassNo(classNo).getPtroomSessionName();
				return ResponseEntity.status(200).body(sessionName);
			}
			catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(500).body(null);
			}
		}
	}
	
	// PTroom ์์ฅ
	@PutMapping("/enter/{classNo}")
	public ResponseEntity<String> enterPtroom(Authentication authentication, @PathVariable int classNo) {
		PitUserDetails userDetails = (PitUserDetails) authentication.getDetails();
		String userEmail = userDetails.getUsername();
		if(userService.validateUserType(userEmail) == 1) {
			return ResponseEntity.status(403).body(null);
		}
		else {
			try {
				Integer userCnt = ptroomService.enterPtroom(classNo);
				if(userCnt == null) {
					return ResponseEntity.status(200).body("ptroom์ ์?์์ด  ์ด๊ณผ๋์์ต๋๋ค.");
				}
				else {					
					return ResponseEntity.status(200).body("ptroom ์์ฅ์ ์ฑ๊ณตํ์์ต๋๋ค.");
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(500).body(null);
			}
		}
	}
	
	// PTroom ์ข๋ฃํ๊ธฐ
	@PutMapping("/leave/{classNo}")
	public ResponseEntity<BaseResponseBody> leavePtroom(Authentication authentication, @PathVariable int classNo) {
		PitUserDetails userDetails = (PitUserDetails) authentication.getDetails();
		String userEmail = userDetails.getUsername();
		if(userService.validateUserType(userEmail) == 2) {
			try {
				ptroomService.leavePtroom(classNo);
				return ResponseEntity.status(200).body(BaseResponseBody.of(200, "ptroom์ด ์ข๋ฃ๋์์ต๋๋ค."));
			}
			catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(500).body(BaseResponseBody.of(500, "์ข๋ฃ์ ์ค๋ฅ๊ฐ ์๊ฒผ์ต๋๋ค."));
			}
			
		} else {
			return ResponseEntity.status(403).body(BaseResponseBody.of(403, "์?๊ทผํ? ์ ์๋ ํ์ด์ง์๋๋ค."));
		}
	}

}
