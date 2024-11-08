package com.wibeechat.missa.userlogin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wibeechat.missa.controller.user.LoginController;
import com.wibeechat.missa.dto.login.LoginRequest;
import com.wibeechat.missa.dto.login.LoginResponse;
import com.wibeechat.missa.exception.InvalidPasswordException;
import com.wibeechat.missa.exception.UserNotFoundException;
import com.wibeechat.missa.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@Test
	@DisplayName("로그인 성공 테스트")
	void loginSuccess() throws Exception {
		// given
		LoginRequest request = new LoginRequest("hongminyeong", "1234");
		LoginResponse expectedResponse = LoginResponse.success(
				"ec30ee0d-c663-42aa-bf81-448e2d4f50c2"
		);

		given(userService.login(any(LoginRequest.class)))
				.willReturn(expectedResponse);

		// when & then
		mockMvc.perform(post("/api/users/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(result -> {
					HttpSession session = result.getRequest().getSession();
                    assert session != null;
                    assertNotNull(session.getAttribute("userId"));
					assertEquals("ec30ee0d-c663-42aa-bf81-448e2d4f50c2", session.getAttribute("userId"));
				})
				.andDo(print());
	}

	@Test
	@DisplayName("로그인 실패 시 세션이 생성되지 않아야 한다")
	void loginFail() throws Exception {
		// given
		LoginRequest loginRequest = new LoginRequest("test@email.com", "wrong_password");
		LoginResponse loginResponse = LoginResponse.builder()
				.isSuccess(false)
				.build();

		given(userService.login(any(LoginRequest.class))).willReturn(loginResponse);

		// when & then
		mockMvc.perform(post("/api/users/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(result -> {
					HttpSession session = result.getRequest().getSession(false);
					assertTrue(session == null || session.getAttribute("userId") == null);
				})
				.andDo(print());
	}

	@Test
	@DisplayName("잘못된 비밀번호로 로그인 시도 테스트")
	void loginFailInvalidPassword() throws Exception {
		// given
		LoginRequest request = new LoginRequest("hongminyeong", "wrongpass");
		given(userService.login(any(LoginRequest.class)))
				.willThrow(new InvalidPasswordException());

		// when & then
		mockMvc.perform(post("/api/users/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.isSuccess").value(false))
				.andExpect(jsonPath("$.code").value(401))
				.andExpect(jsonPath("$.message").value("Invalid Password"))
				.andDo(print());
	}

	@Test
	@DisplayName("잘못된 요청 형식으로 로그인 시도 테스트")
	void loginFailInvalidRequest() throws Exception {
		// given
		String invalidJson = "{\"userId\": }"; // 잘못된 JSON 형식

		// when & then
		mockMvc.perform(post("/api/users/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(invalidJson))
				.andExpect(status().isBadRequest())
				.andDo(print());
	}

	@Test
	@DisplayName("세션이 있을 때 세션 체크가 성공해야 한다")
	void checkSessionSuccess() throws Exception {
		// when & then
		mockMvc.perform(get("/api/users/check-session")
						.sessionAttr("userId", "ec30ee0d-c663-42aa-bf81-448e2d4f50c2"))  // 세션 속성 설정
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("현재 로그인된 사용자: ec30ee0d-c663-42aa-bf81-448e2d4f50c2")))
				.andDo(print());
	}

	@Test
	@DisplayName("세션이 없을 때 세션 체크가 실패해야 한다")
	void checkSessionFail() throws Exception {
		// when & then
		mockMvc.perform(get("/api/users/check-session"))
				.andExpect(status().isUnauthorized())
				.andExpect(content().string("세션이 만료되었습니다."))
				.andDo(print());
	}

	@Test
	@DisplayName("로그아웃 시 세션이 무효화되어야 한다")
	void logout() throws Exception {
		// given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("userId", "ec30ee0d-c663-42aa-bf81-448e2d4f50c2");

		// when & then
		mockMvc.perform(post("/api/users/logout")
						.session(session))
				.andExpect(status().isOk())
				.andExpect(content().string("로그아웃 되었습니다."))
				.andExpect(result -> {
					HttpSession resultSession = result.getRequest().getSession(false);
					assertNull(resultSession);  // 세션이 무효화되었는지 확인
				})
				.andDo(print());
	}

}