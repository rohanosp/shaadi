package sd;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;	
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class interview {
	public WebDriver driver = null;
	public WebDriverWait wait;
	String csv_file = "./TestData/CsvFile.csv";

	@BeforeMethod //Invoking Browser
	public void Invoke_browser() throws InterruptedException, IOException 
	{
		System.setProperty("webdriver.chrome.driver", "./chromedriver");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}
	@Test
	public void Demo() throws CsvValidationException, IOException, InterruptedException
		{
		CSVReader reader = new CSVReader(new FileReader(csv_file));	
		String[] cell;
		while((cell=reader.readNext())!=null)
		{
			for (int i=0;i<cell.length;i++)											 //Reading data from csv file
			{
				String keyword = cell[i];
				driver.get(keyword);												 //Getting url on browser from csv file
				String community = getcommunity(keyword);
				System.out.println("Base commuity is : "+community);
				Thread.sleep(500L);
				driver.findElement(By.xpath("//*[@data-testid='lets_begin']")).click();
				Thread.sleep(500L);
				String email = getemail();
				driver.findElement(By.xpath("//*[@data-testid='email']")).sendKeys(email);
				String password = getpassword();
				driver.findElement(By.xpath("//*[@data-testid='password1']")).sendKeys(password);
				Thread.sleep(500L);
				driver.findElement(By.xpath("//*[@class='Dropdown-placeholder']")).click();
				driver.findElement(By.xpath("//*[@class='Dropdown-menu postedby_options']/div")).click();
				driver.findElement(By.xpath("//*[@data-testid='gender_male']")).click();
				driver.findElement(By.xpath("//*[@data-testid='next_button']")).click();
				String mt = driver.findElement(By.xpath("//*[@class='Dropdown-placeholder is-selected']")).getText();		////Panel 2, Getting Mother tongue
				String language = mt.toLowerCase();
				System.out.println("Pre Selected Mother toungue is : "+language);
				Assert.assertEquals(community, language);							//Validating community name and mother tongue is the same
				System.out.println("Test case passed");
				System.out.println("\n");
			}
		}
		
	}
	// Method to split url to get community domain
	public static String getcommunity(String keyword) 
	{
		String[] arrOfStr = keyword.split("\\.", 3);							
		String main = arrOfStr[1];											
		return main.substring(0, main.length() - 6);			
	}
	
	// Method to generate unique email with random name
	public static String getemail()   
	{
		return RandomStringUtils.randomAlphabetic(6) + "@gmail.com";
	}
	
	// Method to generate random alphanumeric password
	public static String getpassword()   
	{
		return RandomStringUtils.randomAlphanumeric(6);					
	}
	
	@AfterMethod //Closing browser
	public void tear()
	{
	driver.quit();
	}
}
