package com.itinerar.itinerar;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItinerarApplicationTests {

	@Test
	public void getFiles() {
		File folder = new File("E:\\Bogdan\\Facultate\\Gramatovici\\Un nou inceput\\itinerar\\randomImages");
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        System.out.println(file.getName());
		    }
		}
	}
}

