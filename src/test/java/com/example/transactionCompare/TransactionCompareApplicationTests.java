package com.example.transactionCompare;

import org.apache.commons.io.FilenameUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionCompareController.class)
public class TransactionCompareApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void uploadBothFiles() throws Exception {
		InputStream uploadStream = TransactionCompareApplicationTests.class.getClassLoader().getResourceAsStream("exceldocument.csv");
		MockMultipartFile file = new MockMultipartFile("csvFile", uploadStream);
		InputStream uploadStream2 = TransactionCompareApplicationTests.class.getClassLoader().getResourceAsStream("exceldocument.csv");
		MockMultipartFile file2 = new MockMultipartFile("csvFile2", uploadStream2);

		this.mockMvc
				.perform(multipart("/api/uploadFiles")
						.file(file)
						.file(file2))
				.andExpect(status().isOk());

	}

	@Test
	public void uploadFirstFileOnly() throws Exception {
		InputStream uploadStream = TransactionCompareApplicationTests.class.getClassLoader().getResourceAsStream("exceldocument.csv");
		MockMultipartFile file = new MockMultipartFile("csvFile", uploadStream);

		this.mockMvc
				.perform(multipart("/api/uploadFiles")
						.file(file))
				.andExpect(status().isBadRequest());

	}

	@Test
	public void uploadSecondFileOnly() throws Exception {
		InputStream uploadStream = TransactionCompareApplicationTests.class.getClassLoader().getResourceAsStream("exceldocument.csv");
		MockMultipartFile file = new MockMultipartFile("csvFile2", uploadStream);

		this.mockMvc
				.perform(multipart("/api/uploadFiles")
						.file(file))
				.andExpect(status().isBadRequest());

	}

	@Test
	public void wrongFormatFile1() throws Exception {
		MockMultipartFile file = new MockMultipartFile("csvFile", "csvFile.txt", null, "csvFile".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("csvFile2", "csvFile2.csv", null, "csvFile2".getBytes());

		this.mockMvc
				.perform(multipart("/api/uploadFiles")
						.file(file)
						.file(file2))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage").value("File 1 wrong Format"));
	}

	@Test
	public void wrongFormatFile2() throws Exception {
		MockMultipartFile file = new MockMultipartFile("csvFile", "csvFile.csv", null, "csvFile".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("csvFile2", "csvFile2.txt", null, "csvFile2".getBytes());

		this.mockMvc
				.perform(multipart("/api/uploadFiles")
						.file(file)
						.file(file2))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage").value("File 2 wrong Format"));
	}

	@Test
	public void exceptionHandling() throws Exception {
		MockMultipartFile file = new MockMultipartFile("csvFile", "csvFile.csv", null, "csvFile".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("csvFile2", "csvFile2.csv", null, "csvFile2".getBytes());

		this.mockMvc
				.perform(multipart("/api/uploadFiles")
						.file(file)
						.file(file2))
				.andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.andReturn();
	}
}
