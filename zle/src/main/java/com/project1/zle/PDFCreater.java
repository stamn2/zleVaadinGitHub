package com.project1.zle;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.project1.controller.ProjectController;
import com.project1.domain.Client;
import com.project1.domain.Cost;
import com.project1.domain.Project;

/**
 * @author Rosalie Truong & Nils Stampfli
 */
public class PDFCreater {

	private static DecimalFormat numberFormat = new DecimalFormat("#.00");

	public static void createPdf(List<BillingEmployeeItem> billingList, List<Cost> matCostList, double totalCosts,
			int month, int year, Project project){
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
	            content.showText("Invoice of Project");
	            content.endText();
	            
	            content.beginText();
	            content.setFont(PDType1Font.HELVETICA, 16);
	            content.newLineAtOffset(100, 450);
	            content.showText("Client");
	            content.endText();
	            
	            Client client = project.getClient();
	            
	            content.beginText();
	            content.setFont(PDType1Font.HELVETICA, 12);
	            content.setLeading(14.5f);
	            content.newLineAtOffset(100, 430);
	            content.showText("Name: "+client.getCompanyName());
	            content.newLine();
	            content.showText("Address: "+client.getStreet()+" "+client.getPlz());
	            content.newLine();
	            content.showText("Email: "+ client.getEmail());
	            content.newLine();
	            content.showText("Phone: "+ client.getTel());
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
	            content.showText("Project-Name: "+project.getName());
	            content.endText();
	            
	            content.beginText();
	            content.setFont(PDType1Font.HELVETICA, 16);
	            content.newLineAtOffset(100, 290);
	            content.showText("Period");
	            content.endText();
	            
	            content.beginText();
	            content.setFont(PDType1Font.HELVETICA, 12);
	            content.setLeading(14.5f);
	            content.newLineAtOffset(100, 270);
	            content.showText("Month\\Year: "+String.valueOf(month)+" \\ "+String.valueOf(year));
	            content.endText();
	            
	            content.beginText();
	            content.setFont(PDType1Font.HELVETICA, 18);
	            content.newLineAtOffset(100, 200);
	            content.showText("Total Costs: "+numberFormat.format(totalCosts)+"[CHF]");
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
	            content2.setFont(PDType1Font.HELVETICA, 18);
	            content2.newLineAtOffset(100, 620);
	            content2.showText("[Activities]");
	            content2.endText();
	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 16);
	            content2.newLineAtOffset(100, 600);
	            content2.showText("Employee");
	            content2.endText();
	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 14);
	            content2.setLeading(14.5f);
	            content2.newLineAtOffset(100, 580);
	            for(int i=0; i<billingList.size();i++){ 
	                content2.showText(billingList.get(i).getPc().getEmployee().getLastname());
	                content2.newLine();
	            }
	            content2.endText();
	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 16);
	            content2.newLineAtOffset(200, 600);
	            content2.showText("Cost/H");
	            content2.endText();
	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 14);
	            content2.setLeading(14.5f);
	            content2.newLineAtOffset(200, 580);
	            for(int i=0; i<billingList.size();i++){
	                content2.showText(numberFormat.format(billingList.get(i).getPc().getHourlyRate()));
	                content2.newLine();
	            }
	            content2.endText();
	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 16);
	            content2.newLineAtOffset(300, 600);
	            content2.showText("Time [h]");
	            content2.endText();
	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 14);
	            content2.setLeading(14.5f);
	            content2.newLineAtOffset(300, 580);
	            for(int i=0; i<billingList.size();i++){
	                content2.showText(numberFormat.format(billingList.get(i).getHours()));
	                content2.newLine();
	            }
	            content2.endText();
	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 16);
	            content2.newLineAtOffset(400, 600);
	            content2.showText("Cost [CHF]");
	            content2.endText();
	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 14);
	            content2.setLeading(14.5f);
	            content2.newLineAtOffset(400, 580);
	            for(int i=0; i<billingList.size();i++){
	                content2.showText(numberFormat.format(billingList.get(i).getCost()));
	                content2.newLine();
	            }
	            content2.newLine();
	            content2.showText(String.valueOf(ProjectController.getSumBillingEmployeeItems(billingList)));
	            content2.endText();
	            
	            //loops for all material-costs of the project
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 18);
	            content2.newLineAtOffset(100, 420);
	            content2.showText("[Other Expense]");
	            content2.endText();
	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 16);
	            content2.newLineAtOffset(100, 400);
	            content2.showText("Cost-Type");
	            content2.endText();
	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 14);
	            content2.setLeading(14.5f);
	            content2.newLineAtOffset(100, 380);
	            for(int i=0; i<matCostList.size();i++){ 
	                content2.showText(matCostList.get(i).getName());
	                content2.newLine();
	            }
	            content2.endText();
	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 16);
	            content2.newLineAtOffset(200, 400);
	            content2.showText("Description");
	            content2.endText();
	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 14);
	            content2.setLeading(14.5f);
	            content2.newLineAtOffset(200, 380);
	            for(int i=0; i<matCostList.size();i++){
	                content2.showText(matCostList.get(i).getDescription());
	                content2.newLine();
	            }
	            content2.endText();            
	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 16);
	            content2.newLineAtOffset(400, 400);
	            content2.showText("Cost [CHF]");
	            content2.endText();
	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 14);
	            content2.setLeading(14.5f);
	            content2.newLineAtOffset(400, 380);
	            for(int i=0; i<matCostList.size();i++){
	                content2.showText(numberFormat.format(matCostList.get(i).getPrice()));
	                content2.newLine();
	            }
	            content2.newLine();
	            content2.showText(String.valueOf(ProjectController.getSumCosts(matCostList)));
	            content2.endText();
	            
	            //Total Costs	            
	            content2.beginText();
	            content2.setFont(PDType1Font.HELVETICA, 20);
	            content2.newLineAtOffset(100, 200);
	            content2.showText("Total Cost: "+totalCosts+" [CHF]");
	            content2.endText();
	            
	            content2.close();
	            doc.save(fileName);
	            doc.close();
	        
	        System.out.println("your file created in : "+ System.getProperty("user.dir"));
	        
	        
	        }
	        catch(IOException e){ // "| COSVisitorException"
	        System.out.println(e.getMessage());
	        }
	}
	
	public static void openPDF(){
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
