package com.project1.zle;

import java.awt.Desktop;
import java.io.File;
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
            String imageName = "Header-graphic03.gif";
            
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            PDPage page2 = new PDPage();

            doc.addPage(page);
            
            PDImageXObject pdImage = PDImageXObject.createFromFile(imageName, doc);

            PDPageContentStream content = new PDPageContentStream(doc, page);
            PDPageContentStream content2 = new PDPageContentStream(doc, page2);
            
            content.drawImage(pdImage, 180, 700);
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 30);
            content.newLineAtOffset(240, 760);
            content.showText("Company XYZ");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.setLeading(14.5f);
            content.newLineAtOffset(40, 650);
            content.showText("Company XYZ");
            content.newLine();
            content.showText("Bielstrasse 1");
            content.newLine();
            content.showText("2502 Biel");
            content.newLine();
            content.showText("Schweiz");
            content.newLine();
            content.showText("company@mail.com");
            content.newLine();
            content.showText("+41 32 123 45 67");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 26);
            content.newLineAtOffset(100, 500);
            content.showText("Invoice of Project X");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.newLineAtOffset(100, 450);
            content.showText("Client");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.setLeading(14.5f);
            content.newLineAtOffset(100, 430);
            content.showText("Name:");
            content.newLine();
            content.showText("Address:");
            content.newLine();
            content.showText("Email:");
            content.newLine();
            content.showText("Phone:");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.newLineAtOffset(100, 350);
            content.showText("Project");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.setLeading(14.5f);
            content.newLineAtOffset(100, 330);
            content.showText("Project-Name:");
            content.newLine();
            content.showText("Description:");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.newLineAtOffset(100, 280);
            content.showText("Period");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.setLeading(14.5f);
            content.newLineAtOffset(100, 260);
            content.showText("Begin-Date:");
            content.newLine();
            content.showText("End-Date:");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 18);
            content.newLineAtOffset(100, 200);
            content.showText("Total Costs:");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.setLeading(15f);
            content.newLineAtOffset(100, 120);
            content.showText("Max Muster");
            content.newLine();
            content.showText("CEO");
            content.endText();
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.setLeading(15f);
            content.newLineAtOffset(200, 120);
            content.showText("Nana Beispiel");
            content.newLine();
            content.showText("CFO");
            content.endText();
            
            content.close();
            
            doc.addPage(page2);
            
            content2.drawImage(pdImage, 180, 700);
            
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 30);
            content2.newLineAtOffset(240, 760);
            content2.showText("Company XYZ");
            content2.endText();
            
            //loops for all employees, that worked on the project
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 16);
            content2.newLineAtOffset(100, 600);
            content2.showText("Employee");
            content2.endText();
            
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 16);
            content2.setLeading(14.5f);
            content2.newLineAtOffset(100, 580);
            for(int i=0; i<5;i++){ 
                content2.showText("Emp XYZ");
                content2.newLine();
            }
            content2.endText();
            
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 16);
            content2.newLineAtOffset(200, 600);
            content2.showText("Cost/H");
            content2.endText();
            
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 16);
            content2.setLeading(14.5f);
            content2.newLineAtOffset(200, 580);
            for(int i=0; i<5;i++){
                content2.showText("29.50");
                content2.newLine();
            }
            content2.endText();
            
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 16);
            content2.newLineAtOffset(300, 600);
            content2.showText("Time [h]");
            content2.endText();
            
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 16);
            content2.setLeading(14.5f);
            content2.newLineAtOffset(300, 580);
            for(int i=0; i<5;i++){
                content2.showText("48");
                content2.newLine();
            }
            content2.endText();
            
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 16);
            content2.newLineAtOffset(400, 600);
            content2.showText("Cost [CHF]");
            content2.endText();
            
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 16);
            content2.setLeading(14.5f);
            content2.newLineAtOffset(400, 580);
            for(int i=0; i<5;i++){
                content2.showText("1'416");
                content2.newLine();
            }
            content2.endText();
            
            //loops for all material-costs of the project
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 16);
            content2.newLineAtOffset(100, 400);
            content2.showText("Description");
            content2.endText();
            
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 16);
            content2.setLeading(14.5f);
            content2.newLineAtOffset(100, 380);
            for(int i=0; i<5;i++){ 
                content2.showText("Material XYZ");
                content2.newLine();
            }
            content2.endText();
            
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 16);
            content2.newLineAtOffset(250, 400);
            content2.showText("Comment");
            content2.endText();
            
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 16);
            content2.setLeading(14.5f);
            content2.newLineAtOffset(250, 380);
            for(int i=0; i<5;i++){
                content2.showText("comment");
                content2.newLine();
            }
            content2.endText();            
            
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 16);
            content2.newLineAtOffset(400, 400);
            content2.showText("Cost [CHF]");
            content2.endText();
            
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 16);
            content2.setLeading(14.5f);
            content2.newLineAtOffset(400, 380);
            for(int i=0; i<5;i++){
                content2.showText("1'999");
                content2.newLine();
            }
            content2.endText();
            
            //Total Costs
            content2.beginText();
            content2.setFont(PDType1Font.HELVETICA, 20);
            content2.newLineAtOffset(100, 200);
            content2.showText("Total Cost: 26'900 CHF");
            content2.endText();
            
            content2.close();
            doc.save(fileName);
            doc.close();
        
        System.out.println("your file created in : "+ System.getProperty("user.dir"));
        
        
        }
        catch(IOException e){ // "| COSVisitorException"
        System.out.println(e.getMessage());
        }
	
		try {

			File pdfFile = new File("PdfWithtext.pdf");
			if (pdfFile.exists()) {

				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(pdfFile);
				} else {
					System.out.println("Awt Desktop is not supported!");
				}

			} else {
				System.out.println("File is not exists!");
			}

			System.out.println("Done");

		  } catch (Exception ex) {
			ex.printStackTrace();
		  }
        
	}
}
