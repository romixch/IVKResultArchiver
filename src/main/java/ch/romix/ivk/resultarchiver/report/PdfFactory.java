package ch.romix.ivk.resultarchiver.report;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.romix.ivk.resultarchiver.model.Games;
import ch.romix.ivk.resultarchiver.model.Group;
import ch.romix.ivk.resultarchiver.model.Rate;
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

  private static Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
  private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
  private static Font tableFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

  private Group group;
  private Table table;
  private List<Rate> rates;

  public void setTable(Table table) {
    this.table = table;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  public void setRates(List<Rate> rates) {
    this.rates = rates;
  }

  public void print() {
    try {
      Document document = new Document(PageSize.A4.rotate());
      File outputDir = new File(System.getProperties().getProperty("user.dir"), "ResultArchive");
      Files.createDirectories(outputDir.toPath());
      String fileName = group.getName();
      fileName = fileName.replace('/', '_');
      File pdfFile = new File(outputDir, fileName + ".pdf");
      pdfFile.delete();
      PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
      document.open();
      addMetaData(document);
      addTitle(document);
      if (table.hasData()) {
        addTable(document);
      } else {
        addNoDataInformation(document);
      }
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
    preface.add(new Paragraph("Resultate der Innerschweizer Korbball Meisterschaft", titleFont));
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

    writeRates(pdfTable);

    document.add(pdfTable);
  }

  private void addNoDataInformation(Document document) throws DocumentException {
    Paragraph noDataParagraph =
        new Paragraph("Momentan leider keine Daten für diese Gruppe verfügbar.", smallBold);
    addEmptyLine(noDataParagraph, 1);
    document.add(noDataParagraph);
  }

  private void writeHeaderRow(PdfPTable pdfTable) {
    PdfPCell groupCell = new PdfPCell(new Phrase(group.getName(), titleFont));
    groupCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    groupCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    groupCell.setColspan(2);
    pdfTable.addCell(groupCell);

    table.getTeams().forEach(t -> {
      PdfPCell cell = createCenteredCell(t.getName());
      cell.setRotation(90);
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      pdfTable.addCell(cell);
    });
  }

  private void writeResults(PdfPTable pdfTable) {
    table.getTeams().forEach(
        teamOne -> {
          PdfPCell teamCell = createTableCell(teamOne.getName());
          teamCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
          teamCell.setRowspan(table.getRounds().size());
          pdfTable.addCell(teamCell);
          table.getRounds().forEach(
              round -> {
                pdfTable.addCell(createTableCell(round.getName()));
                table.getTeams().forEach(
                    teamTwo -> {
                      if (teamOne.getId().equals(teamTwo.getId())) {
                        PdfPCell cell = createTableCell("");
                        cell.setBackgroundColor(BaseColor.DARK_GRAY);
                        pdfTable.addCell(cell);
                      } else {
                        Games games =
                            table.getGames().stream().filter(g -> g.getRoundId() == round.getId())
                                .findAny().get();
                        TeamOne one =
                            games.getTeamOnes().stream()
                                .filter(o -> o.getTeamOneId().equals(teamOne.getId())).findAny()
                                .get();
                        TeamTwo two =
                            one.getTeamTwos().stream()
                                .filter(t -> t.getTeamTwoId().equals(teamTwo.getId())).findAny()
                                .get();
                        String result = two.getResult();
                        pdfTable.addCell(createCenteredCell(result));
                      }
                    });
              });
        });
  }

  private PdfPCell createCenteredCell(String text) {
    PdfPCell cell = createTableCell(text);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    return cell;
  }

  private PdfPCell createTableCell(String text) {
    return new PdfPCell(new Phrase(text, tableFont));
  }

  private void writeRates(PdfPTable pdfTable) {
    Map<String, Rate> teamIdToRateMap = new HashMap<>();
    rates.stream().forEach(rate -> teamIdToRateMap.put(rate.getTeamId(), rate));

    writeSummaryTitle(pdfTable, "Körbe");
    table.getTeams().forEach(team -> {
      Rate rate = teamIdToRateMap.get(team.getId());
      pdfTable.addCell(createCenteredCell(rate.getBothScores()));
    });

    writeSummaryTitle(pdfTable, "Korbverhältnis");
    table.getTeams().forEach(team -> {
      Rate rate = teamIdToRateMap.get(team.getId());
      pdfTable.addCell(createCenteredCell(String.valueOf(rate.getRate())));
    });
  }

  private void writeSummaryTitle(PdfPTable pdfTable, String summaryTitle) {
    PdfPCell ratesTitle = createTableCell(summaryTitle);
    ratesTitle.setColspan(2);
    pdfTable.addCell(ratesTitle);
  }


  private void addEmptyLine(Paragraph paragraph, int number) {
    for (int i = 0; i < number; i++) {
      paragraph.add(new Paragraph(" "));
    }
  }

  private void addFooter(Document document) throws DocumentException {
    Paragraph footer = new Paragraph();
    addEmptyLine(footer, 1);
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
    String now = LocalDateTime.now().format(formatter);
    footer.add(new Paragraph("Generiert am " + now, smallBold));
    document.add(footer);
  }
}
