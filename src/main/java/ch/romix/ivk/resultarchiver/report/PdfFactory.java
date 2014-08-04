package ch.romix.ivk.resultarchiver.report;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Date;

import ch.romix.ivk.resultarchiver.model.Group;
import ch.romix.ivk.resultarchiver.model.Table;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class PdfFactory {

  private static Font catFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
  private static Font subFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
  private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
  private static Font tableFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

  private Group group;
  private Table table;

  public void setTable(Table table) {
    this.table = table;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  public void print() {
    try {
      Document document = new Document(PageSize.A4.rotate());
      File outputDir = new File(System.getProperties().getProperty("user.dir"), "ResultArchive");
      Files.createDirectories(outputDir.toPath());
      File pdfFile = new File(outputDir, group.getName() + ".pdf");
      pdfFile.delete();
      PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
      document.open();
      addMetaData(document);
      addTitle(document);
      addTable(document);
      addFooter(document);
      document.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addMetaData(Document document) {
    document.addTitle("Resultat der Innerschweizer Korbball Meitserschaft");
    document.addSubject(group.getName());
    document.addKeywords("IVK, Korbball " + group.getName());
    document.addAuthor("Roman Schaller");
    document.addCreator("Roman Schaller");
  }

  private void addTitle(Document document) throws DocumentException {
    Paragraph preface = new Paragraph();
    addEmptyLine(preface, 1);
    preface.add(new Paragraph("Innerschweizer Korbball Meisterschaft", catFont));
    addEmptyLine(preface, 1);
    preface.add(new Paragraph("Resultate der Gruppe " + group.getName(), catFont));
    addEmptyLine(preface, 3);
    document.add(preface);
  }

  private void addTable(Document document) throws DocumentException {
    // PdfPTable table = new PdfPTable(this.table.getTeams().size() + 1);
    PdfPTable table = new PdfPTable(this.table.getTeams().size() + 1);
    int[] widths = new int[table.getNumberOfColumns()];
    widths[0] = 3;
    for (int i = 1; i < widths.length; i++) {
      widths[i] = 1;
    }
    table.setWidths(widths);
    table.setWidthPercentage(100);

    writeFirstLine(table);


    document.add(table);

  }

  private void writeFirstLine(PdfPTable table) {
    PdfPCell groupCell = new PdfPCell(new Phrase(group.getName(), smallBold));
    groupCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    groupCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    table.addCell(groupCell);

    this.table.getTeams().forEach(t -> {
      PdfPCell cell = new PdfPCell(new Phrase(t.getName()));
      cell.setRotation(90);
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      table.addCell(cell);
    });
  }

  private void addEmptyLine(Paragraph paragraph, int number) {
    for (int i = 0; i < number; i++) {
      paragraph.add(new Paragraph(" "));
    }
  }

  private void addFooter(Document document) throws DocumentException {
    Paragraph footer = new Paragraph();
    addEmptyLine(footer, 1);
    footer.add(new Paragraph("Generiert am " + new Date(), smallBold));
    document.add(footer);
  }
}
