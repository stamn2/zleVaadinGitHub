package com.project1.zle;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.*;

public class Main {
    
	public static void main(String[] args) {
        
        try{
        
        	System.out.println("Create Simple PDF file with Text");
            String fileName = "PdfWithtext.pdf"; // name of our file
            String imageName = "Logo.jpg";
            
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();

            doc.addPage(page);
            
            PDImageXObject pdImage = PDImageXObject.createFromFile("user.dir/Header-graphic03.gif", doc);

            PDPageContentStream content = new PDPageContentStream(doc, page);
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 26);
            content.moveTextPositionByAmount(220, 750);
            content.drawString("Registration Form");
            content.endText();
            
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.moveTextPositionByAmount(80, 700);
            content.drawString("Name : ");
            content.endText();
            
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.moveTextPositionByAmount(80,650);
            content.drawString("Father Name : ");
            content.endText();
            
            content.beginText();
            content.moveTextPositionByAmount(80,600);
            content.drawString("DOB : ");
            content.endText();
            
            
            content.close();
            doc.save(fileName);
            doc.close();
        
        System.out.println("your file created in : "+ System.getProperty("user.dir"));
        
        
        }
        catch(IOException e){ // "| COSVisitorException"
        System.out.println(e.getMessage());
        }
	
	}
}
