package com.wibeechat.missa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wibeechat.missa.controller.user.LoginController;
import com.wibeechat.missa.dto.login.LoginRequest;
import com.wibeechat.missa.dto.login.LoginResponse;
import com.wibeechat.missa.exception.InvalidPasswordException;
import com.wibeechat.missa.exception.UserNotFoundException;
import com.wibeechat.missa.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
				"6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b"
		);

		given(userService.login(any(LoginRequest.class)))
				.willReturn(expectedResponse);

		// when & then
		mockMvc.perform(post("/api/users/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.isSuccess").value(true))
				.andExpect(jsonPath("$.code").value(200))
				.andExpect(jsonPath("$.userNo").value(expectedResponse.getUserNo()))
				.andDo(print());
	}

	@Test
	@DisplayName("유효하지 않은 사용자로 로그인 시도 테스트")
	void loginFailUserNotFound() throws Exception {
		// given
		LoginRequest request = new LoginRequest("unknown", "1234");
		given(userService.login(any(LoginRequest.class)))
				.willThrow(new UserNotFoundException());

		// when & then
		mockMvc.perform(post("/api/users/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.isSuccess").value(false))
				.andExpect(jsonPath("$.code").value(404))
				.andExpect(jsonPath("$.message").value("User Not Found"))
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
}