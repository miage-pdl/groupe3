package org.opencompare;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;


public class CSVToExcelConverter {


	public void converter(String outputFile, String name) throws IOException {
		
        ArrayList arList=null;
        ArrayList al=null;
        String thisLine;
        int count=0;
        FileInputStream fis = new FileInputStream(outputFile);
        DataInputStream myInput = new DataInputStream(fis);
        int i=0;
        arList = new ArrayList();
        while ((thisLine = myInput.readLine()) != null)
        {
            al = new ArrayList();
            String strar[] = thisLine.split("...,");
            for (String aStrar : strar) {
                al.add(aStrar);
            }
            arList.add(al);
            i++;
        }

        try
        {
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("new sheet");
            for(int k=0;k<arList.size();k++)
            {
                ArrayList ardata = (ArrayList)arList.get(k);
                HSSFRow row = sheet.createRow(k);
                for(int p=0;p<ardata.size();p++)
                {
                    HSSFCell cell = row.createCell((short) p);
                    String data = ardata.get(p).toString();
                    if(data.startsWith("=")){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        data=data.replaceAll("\"", "");
                        data=data.replaceAll("=", "");
                        cell.setCellValue(data);
                    }else if(data.startsWith("\"")){
                        data=data.replaceAll("\"", "");
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(data);
                    }else{
                        data=data.replaceAll("\"", "");
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(data);
                    }
                    //*/
                    // cell.setCellValue(ardata.get(p).toString());
                }
                
            }
            String out = "xls/" + name +".xls";
            FileOutputStream fileOut = new FileOutputStream(out);
            hwb.write(fileOut);
            fileOut.close();
            //System.out.println("Your excel file has been generated");
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } 		
		
	}
}
