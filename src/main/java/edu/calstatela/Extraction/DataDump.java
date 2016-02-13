package edu.calstatela.Extraction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DataDump {

	public static void main(String[] args) {
		String file = "extraction.json";
		// String path = file+"/coffeeshop.txt";
		BufferedReader buf = null;
		try {
			buf = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String text = null;

		while (true) {
			try {
				text = buf.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (text == null) {
				break;
			} else {
				System.out.println(text);
			}

		}

	}

}
