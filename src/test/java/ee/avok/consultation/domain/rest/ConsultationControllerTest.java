package ee.avok.consultation.domain.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import ee.avok.consultation.ConsultationWebApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsultationWebApplication.class)
@WebAppConfiguration
public class ConsultationControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testCreateRequest() throws Exception {
		this.mockMvc.perform(post("/requests").contentType(contentType).content(json()))
				.andExpect(status().isCreated());
	}

	private String json() {
		return "\r\n" + "{\r\n" + "  \"name\": \"Neeru Peeru\",\r\n" + "  \"email\": \"neerupeeru@mail.ee\",\r\n"
				+ "  \"purpose\": \"thesis\",\r\n" + "  \"programme\": \"Software Engineering\",\r\n"
				+ "  \"year\": 2016,\r\n" + "  \"language\": \"Estonian\",\r\n"
				+ "  \"comments\": \"In need of correcting a dangling participle.\",\r\n"
				+ "  \"upload\": \"GHT¤4%¤45Y&#hhg¤6%¤/#eh\"\r\n" + "}";
	}
}
