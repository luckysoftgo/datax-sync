package com.application.base.executor.test;

import com.application.base.executor.DataXExecutorApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataXExecutorApplication.class)
public class DataXExecutorExampleBootApplicationTests {

	@Test
	public void test() {
		// command process
		String dataXPyPath = "D:\\installer\\dataX\\datax\\bin\\datax.py";
		String tmpFilePath = "D:\\installer\\dataX\\json\\job.conf";
		String command = "--jvm=\"-Xms1g -Xmx1g\" -p \"-DcreateTime=2019-12-16\"";
		//String command = "--jvm=\"-Xms1g -Xmx1g\"";
		//String command = "--jvm=\"-Xms1g\"";
		//command = String.format("--jvm=\"%s/\b%s\"","-Xms1g","-Xmx1g");
		//String param = " -p \"-DcreateTime=2019-12-16\"";
		Process process = null;
		try {
			//process = Runtime.getRuntime().exec(new String[]{"python", dataXPyPath,param, tmpFilePath},new String[]{command});
			process = Runtime.getRuntime().exec(new String[]{"cmd","/C","python "+dataXPyPath+" "+command+" "+tmpFilePath});
			InputStream is1 = process.getInputStream();
			InputStream is2 = process.getErrorStream();
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(is1));
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(is2));
			String tmp = null;
			while((tmp=reader1.readLine())!=null){
				System.out.println("return :"+tmp);
			}
			while((tmp=reader2.readLine())!=null){
				System.out.println("error:"+tmp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}