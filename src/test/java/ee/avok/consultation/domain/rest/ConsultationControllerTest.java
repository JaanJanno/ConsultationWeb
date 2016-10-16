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
import java.util.Base64;

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
		this.mockMvc.perform(post("/api/requests").contentType(contentType).content(json()))
				.andExpect(status().isCreated());
	}

	@Test
	public void testCreateRequestUpload() throws Exception {
		this.mockMvc.perform(post("/api/requests").contentType(contentType).content(json()))
				.andExpect(status().isCreated());
		ConsultationRequest request = null;
		for (ConsultationRequest req : conReqRepo.findAll()) {
			request = req;
		}

		byte[] expected = Base64.getDecoder().decode("SW1la290dGU");
		byte[] received = request.getUpload().getUpload();

		assertEquals(expected.length, received.length);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], received[i]);
		}

	}

	@Test
	public void findAllWithStatusReceived() throws Exception {
		this.mockMvc.perform(get("/api/requests").param("status", "RECEIVED").accept(contentType))
				.andExpect(status().isOk()).andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].name", is("Bla1")))
				.andExpect(jsonPath("$[0].status", is("RECEIVED")));
	}

	@Test
	public void findAllWithStatusAccepted() throws Exception {
		this.mockMvc.perform(get("/api/requests").param("status", "ACCEPTED").accept(contentType))
				.andExpect(status().isOk()).andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].name", is("Bla2")))
				.andExpect(jsonPath("$[0].status", is("ACCEPTED")));
	}

	@Test
	public void findAll() throws Exception {
		this.mockMvc.perform(get("/api/requests").accept(contentType)).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	public void findOne() throws Exception {
		this.mockMvc.perform(get("/api/requests/{id}", conReq2.getId()).accept(contentType)).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.name", is("Bla2")))
				.andExpect(jsonPath("$.status", is("ACCEPTED")));
	}

	private String json() {
		return "{\"name\": \"Neeru Peeru\",\"email\": \"neerupeeru@mail.ee\",\"purpose\": \"thesis\",\"programme\": \"Software Engineering\",\"year\": 2016,\"language\": \"Estonian\",\"comments\": \"In need of correcting a dangling participle.\",\"upload\": {\"upload\":\"SW1la290dGU=\",\"filename\":\"peer.txt\"}}";
	}
}
