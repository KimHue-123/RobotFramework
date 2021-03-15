package FirstTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.gson.Gson;

public class TestMain {
	private ListTestCaseLogin listTestCaseLogin = new ListTestCaseLogin();
	
	private void loadTestCase() {
		Gson gson  = new Gson();
		try {
			listTestCaseLogin = gson.fromJson(new FileReader(new File("D:/lib/Data/testcaselogin.json")), ListTestCaseLogin.class);
		} catch (Exception e) {
			System.out.println("Loi doc file " + e.getMessage());
		} 
	}
	
	private void writeResult(String result) {
		File file = new File("D:/lib/Data/resultFile.txt");
		
		try {
			PrintWriter printWriter = new PrintWriter(file);
			printWriter.println(result);
			printWriter.close();
		} catch (FileNotFoundException e) {
			System.out.println("Loi ghi file " + e.getMessage());
		}
	}
	
	public void startTest() {
		WebDriver webDriver;
		WebElement webElement;
		loadTestCase();
		
		String resultRow ="";
			for(int i = 0; i < listTestCaseLogin.getList().size(); i++) {
				webDriver = new ChromeDriver();
				String check = "true";
				try {
					webDriver.get("https://webmail.tma.com.vn/");
					Thread.sleep(3000);
					webDriver.manage().window().maximize();
					webDriver.findElement(By.name("username")).sendKeys(listTestCaseLogin.getList().get(i).getUsername());
					Thread.sleep(3000);
					webDriver.findElement(By.name("password")).sendKeys(listTestCaseLogin.getList().get(i).getPassword());
					Thread.sleep(3000);
					webDriver.findElement(By.className("ZLoginButton")).click();
					int result = Integer.parseInt(listTestCaseLogin.getList().get(i).getFlag());
					try {
						webElement = webDriver.findElement(By.id("ZLoginErrorPanel"));
					} catch (Exception e) {
						check = "error";
					}
					
					if((result==1 && check == "error")|| 
							(result==0 &&check == "true")	) {
						resultRow += "TesCase " + (i + 1) + " passed \n";
					}
					else {
						resultRow += "TesCase " + (i + 1) + " failed \n";
					}
					writeResult(resultRow);
					Thread.sleep(3000);
				} catch (Exception e) {
					resultRow += "TesCase " + (i + 1) + " has error " +  e.getMessage() + "\n";
					writeResult(resultRow);
					System.out.println("testcase fail: " + e.getMessage());
				}
				webDriver.close();
			}
		
	}
}
