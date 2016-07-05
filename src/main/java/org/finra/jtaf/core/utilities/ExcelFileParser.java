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

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Parser for testdata excel files.
 */
public class ExcelFileParser {

	private static Logger logger = Logger.getLogger(ExcelFileParser.class.getPackage().getName());
	
	// For xlsx type
	private XSSFWorkbook workBookXlsx;
	private XSSFSheet workBookSheetXlsx;
	
	// For xls type
	private HSSFWorkbook workBookXls;
	private HSSFSheet workBookSheetXls;

	public ExcelFileParser(String fileName, boolean isXlsx) throws Exception {
		if (isXlsx) {
			workBookXlsx = new XSSFWorkbook(new FileInputStream(fileName));
			workBookSheetXlsx = workBookXlsx.getSheetAt(0);
		} else {
			workBookXls = new HSSFWorkbook(new FileInputStream(fileName));
			workBookSheetXls = workBookXls.getSheetAt(0);
		}
	}
	
	public ExcelFileParser(String fileName, String sheetName, boolean isXlsx) throws Exception {
		if (isXlsx) {
			workBookXlsx = new XSSFWorkbook(new FileInputStream(fileName));
			workBookSheetXlsx = workBookXlsx.getSheet(sheetName);
		} else {
			workBookXls = new HSSFWorkbook(new FileInputStream(fileName));
			workBookSheetXls = workBookXls.getSheet(sheetName);
		}
	}

	public List<List<String>> parseExcelFile(boolean isXlsx) throws Exception {
		List<List<String>> parsedExcelFile = new ArrayList<List<String>>();
		if (isXlsx) {
			for (int i = 0, numberOfRows = workBookSheetXlsx.getPhysicalNumberOfRows(); i < numberOfRows + 1; i++) {
				XSSFRow row = workBookSheetXlsx.getRow(i);
				if (row != null) {
					List<String> parsedExcelRow = new ArrayList<String>(); 
					for (int j = 0, numberOfColumns = row.getLastCellNum(); j < numberOfColumns; j++) {
						XSSFCell cell = row.getCell(j);
						if (cell != null) {
							try {
								if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
									parsedExcelRow.add(cell.getStringCellValue());	
								} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
									parsedExcelRow.add("");
								} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
									parsedExcelRow.add(String.valueOf(cell.getBooleanCellValue()));
								} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
									parsedExcelRow.add(String.valueOf(cell.getNumericCellValue()));
								} else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
									parsedExcelRow.add("");
								} else {
									parsedExcelRow.add(cell.getStringCellValue());
								}
							} catch (Exception e) {
								logger.fatal("Oops! Can't read cell (row = " + i + ", column = " + j + ") in the excel file! Change cell format to 'Text', please!");
								return null;
							}
						} else {
							parsedExcelRow.add("");
						}
					}
					parsedExcelFile.add(parsedExcelRow);
				}
			}	
		} else {
			for (int i = 0, numberOfRows = workBookSheetXls.getPhysicalNumberOfRows(); i < numberOfRows + 1; i++) {
				HSSFRow row = workBookSheetXls.getRow(i);
				if (row != null) {
					List<String> parsedExcelRow = new ArrayList<String>(); 
					for (int j = 0, numberOfColumns = row.getLastCellNum(); j < numberOfColumns; j++) {
						HSSFCell cell = row.getCell(j);
						if (cell != null) {
							try {
								if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
									parsedExcelRow.add(cell.getStringCellValue());	
								} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
									parsedExcelRow.add("");
								} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
									parsedExcelRow.add(String.valueOf(cell.getBooleanCellValue()));
								} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
									parsedExcelRow.add(String.valueOf(cell.getNumericCellValue()));
								} else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
									parsedExcelRow.add(String.valueOf(""));
								} else {
									parsedExcelRow.add(cell.getStringCellValue());
								}
							} catch (Exception e) {
								logger.fatal("Oops! Can't read cell (row = " + i + ", column = " + j + ") in the excel file! Change cell format to 'Text', please!");
								return null;
							}
						} else {
							parsedExcelRow.add("");
						}
					}
					parsedExcelFile.add(parsedExcelRow);
				}
			}	
		}
		
		return parsedExcelFile;
	}

}
