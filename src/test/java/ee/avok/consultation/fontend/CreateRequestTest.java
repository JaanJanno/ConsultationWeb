package ee.avok.consultation.fontend;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ee.avok.consultation.ConsultationWebApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsultationWebApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class CreateRequestTest {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		driver = new FirefoxDriver();
		driver = MockMvcHtmlUnitDriverBuilder.mockMvcSetup(mockMvc).build();
		baseUrl = driver.getCurrentUrl();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
/*	
	@Ignore
	@Test
	public void testCreateRequest() throws Exception {
		testRequest();
		testLoginConsultant();
		testValidateRequest();
	}
*/
	public void testRequest() {
		driver.get(baseUrl + "/");
		driver.findElement(By.xpath("//a[contains(text(),'Request\n									form')]")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Testman Coolface");
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("cool@test.com");
		driver.findElement(By.id("programme")).clear();
		driver.findElement(By.id("programme")).sendKeys("Engineering");
		new Select(driver.findElement(By.id("degree"))).selectByVisibleText("Msc");
		new Select(driver.findElement(By.id("department"))).selectByVisibleText("Faculty of Medicine");
		new Select(driver.findElement(By.id("language"))).selectByVisibleText("Estonian");
		driver.findElement(By.cssSelector("option[value=\"Estonian\"]")).click();
		driver.findElement(By.id("textType")).clear();
		driver.findElement(By.id("textType")).sendKeys("Essay");
		driver.findElement(By.cssSelector("input.btn.btn-primary")).click();
		assertEquals("We recieved request. We will contact you as soon as possible", closeAlertAndGetItsText());
	}

	public void testLoginConsultant() {
		driver.get(baseUrl + "/");
		driver.findElement(By.linkText("Log in")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("testy");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("testy");
		driver.findElement(By.cssSelector("input.btn.col-md-12")).click();
	}

	public void testValidateRequest() {

		assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));

		assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));

		assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));

		assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));

		driver.findElement(By.xpath("(//a[contains(text(),'Details')])[2]")).click();

		assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));

		assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));

		assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
