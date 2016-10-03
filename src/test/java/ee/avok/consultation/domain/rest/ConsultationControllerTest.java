package ee.avok.consultation.domain.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import ee.avok.consultation.ConsultationWebApplication;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsultationWebApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ConsultationControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	private ConsultationRequest conReq1;
	private ConsultationRequest conReq2;
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private ConsultationRequestRepository conReqRepo;

	@Before
	public void setUp() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();

		// Dummy objects
		conReqRepo.deleteAll();
		conReq1 = new ConsultationRequest();
		conReq1.setName("Bla1");
		conReq1.setStatus(ConsultationStatus.RECEIVED);

		// So id is accessible in test
		conReq2 = new ConsultationRequest();
		conReq2.setName("Bla2");
		conReq2.setStatus(ConsultationStatus.ACCEPTED);
		conReqRepo.save(conReq1);
		conReqRepo.save(conReq2);
	}

	@Test
	public void testCreateRequest() throws Exception {
		this.mockMvc.perform(post("/requests").contentType(contentType).content(json()))
				.andExpect(status().isCreated());
	}

	@Test
	public void findAllWithStatusReceived() throws Exception {
		this.mockMvc.perform(get("/requests").param("status", "RECEIVED").accept(contentType))
				.andExpect(status().isOk()).andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].name", is("Bla1")))
				.andExpect(jsonPath("$[0].status", is("RECEIVED")));
	}

	@Test
	public void findAllWithStatusAccepted() throws Exception {
		this.mockMvc.perform(get("/requests").param("status", "ACCEPTED").accept(contentType))
				.andExpect(status().isOk()).andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].name", is("Bla2")))
				.andExpect(jsonPath("$[0].status", is("ACCEPTED")));
	}

	@Test
	public void findAll() throws Exception {
		this.mockMvc.perform(get("/requests").accept(contentType)).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	public void findOne() throws Exception {
		this.mockMvc.perform(get("/requests/{id}", conReq2.getId()).accept(contentType)).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.name", is("Bla2")))
				.andExpect(jsonPath("$.status", is("ACCEPTED")));
	}

	@Test
	public void setAsAccepted() throws Exception {
		assertEquals(ConsultationStatus.RECEIVED, conReqRepo.findOne(conReq1.getId()).getStatus());
		this.mockMvc.perform(post("/requests/{id}", conReq1.getId()).accept(contentType))
				.andExpect(status().isNoContent());

		assertEquals(ConsultationStatus.ACCEPTED, conReqRepo.findOne(conReq1.getId()).getStatus());
	}

	private String json() {
		return "\r\n" + "{\r\n" + "  \"name\": \"Neeru Peeru\",\r\n" + "  \"email\": \"neerupeeru@mail.ee\",\r\n"
				+ "  \"purpose\": \"thesis\",\r\n" + "  \"programme\": \"Software Engineering\",\r\n"
				+ "  \"year\": 2016,\r\n" + "  \"language\": \"Estonian\",\r\n"
				+ "  \"comments\": \"In need of correcting a dangling participle.\",\r\n"
				+ "  \"upload\": \"GHT¤4%¤45Y&#hhg¤6%¤/#eh\"\r\n" + "}";
	}
}
