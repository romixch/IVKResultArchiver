package ch.romix.ivk.resultarchiver.report;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Date;

import ch.romix.ivk.resultarchiver.model.Games;
import ch.romix.ivk.resultarchiver.model.Group;
import ch.romix.ivk.resultarchiver.model.Table;
import ch.romix.ivk.resultarchiver.model.TeamOne;
import ch.romix.ivk.resultarchiver.model.TeamTwo;

import com.itextpdf.text.BaseColor;
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
    preface.add(new Paragraph("Resultate der Innerschweizer Korbball Meisterschaft", catFont));
    addEmptyLine(preface, 1);
    document.add(preface);
  }

  private void addTable(Document document) throws DocumentException {
    PdfPTable pdfTable = new PdfPTable(table.getTeams().size() + 2);
    int[] widths = new int[pdfTable.getNumberOfColumns()];
    widths[0] = 2;
    widths[1] = 2;
    for (int i = 2; i < widths.length; i++) {
      widths[i] = 1;
    }
    pdfTable.setWidths(widths);
    pdfTable.setWidthPercentage(100);

    writeHeaderRow(pdfTable);

    writeResults(pdfTable);

    document.add(pdfTable);

  }

  private void writeResults(PdfPTable pdfTable) {
    table.getTeams().forEach(
        teamOne -> {
          PdfPCell teamCell = new PdfPCell(new Phrase(teamOne.getName()));
          teamCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
          teamCell.setRowspan(table.getRounds().size());
          pdfTable.addCell(teamCell);
          table.getRounds().forEach(
              round -> {
                pdfTable.addCell(round.getName());
                table.getTeams().forEach(
                    teamTwo -> {
                      if (teamOne.getId() == teamTwo.getId()) {
                        PdfPCell cell = new PdfPCell(new Phrase(""));
                        cell.setBackgroundColor(BaseColor.DARK_GRAY);
                        pdfTable.addCell(cell);
                      } else {
                        Games games =
                            table.getGames().stream().filter(g -> g.getRoundId() == round.getId())
                                .findAny().get();
                        TeamOne one =
                            games.getTeamOnes().stream()
                                .filter(o -> o.getTeamOneId() == teamOne.getId()).findAny().get();
                        TeamTwo two =
                            one.getTeamTwos().stream()
                                .filter(t -> t.getTeamTwoId() == teamTwo.getId()).findAny().get();
                        String result = two.getResult();
                        PdfPCell cell = new PdfPCell(new Phrase(result));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfTable.addCell(cell);
                      }
                    });
              });
        });
  }

  private void writeHeaderRow(PdfPTable pdfTable) {
    PdfPCell groupCell = new PdfPCell(new Phrase(group.getName(), smallBold));
    groupCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    groupCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    groupCell.setColspan(2);
    pdfTable.addCell(groupCell);

    table.getTeams().forEach(t -> {
      PdfPCell cell = new PdfPCell(new Phrase(t.getName()));
      cell.setRotation(90);
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      pdfTable.addCell(cell);
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
