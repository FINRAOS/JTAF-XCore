/*
 * (C) Copyright 2014 Java Test Automation Framework Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.finra.jtaf.core.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.commons.codec.binary.Base64;

//Apache Commons Codec Base64 static Encoder/Decoder

/**
 * Utility class to encode or decode string using base64.
 */
public class Base64EncoderDecoder {
	private static String fileLocation = "C:\\TEMP\\base64\\test.properties";

	/*
	 * main method is used to decode a property file only. It can take one incoming parameter - the location of 
	 * the file to be encoded. If there is no incoming parameter (or more than one provided), then the default 'propertyFileLocation' value used.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 1) {
			setPropertyFileLocation(args[0]); 
		}	
	
		String fileName = fileLocation.split("\\\\")[fileLocation.split("\\\\").length - 1];
		String encodedFileName = fileName.split("\\.")[0] + "_Encoded." + fileName.split("\\.")[1];
		String encodedFileLocation = fileLocation.replace(fileName, encodedFileName);
				
		BufferedReader in = new BufferedReader(new FileReader(fileLocation));
		BufferedWriter out = new BufferedWriter(new FileWriter(encodedFileLocation));
	
		String line;
		String key;
		String value;
		
		while ((line = in.readLine()) != null) {
			if (!line.startsWith("#")) {
				if (line.contains("=")) {
					key = line.substring(0, line.indexOf("="));
					value =  line.substring(line.indexOf("=") + 1);
					if (key.endsWith(".password")) {
						value = encodeString(value);
						line = key + "=" + value;
					}
				}
			}
			
			System.out.println(line);
			out.write(line + System.getProperty("line.separator"));
		}
		
		in.close();
		out.close();
	}
	
	
	public static String encodeString(String stringToEncode) {
		byte[] encodedBytes = Base64.encodeBase64(stringToEncode.getBytes());
		return new String(encodedBytes);
	}
	
	public static String decodeString(String stringToDecode) {
		 byte[] decodedBytes = Base64.decodeBase64(stringToDecode.getBytes());
		 return new String(decodedBytes);
	}
	
	public static String getPropertyFileLocation() {
		return fileLocation;
	}

	public static void setPropertyFileLocation(String newPropertyFileLocation) {
		fileLocation = newPropertyFileLocation;
	}
	
}