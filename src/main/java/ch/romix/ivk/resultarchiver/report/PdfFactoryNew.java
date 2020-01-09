package ch.romix.ivk.resultarchiver.report;

import ch.romix.ivk.resultarchiver.model.Game;
import ch.romix.ivk.resultarchiver.model.Group;
import ch.romix.ivk.resultarchiver.model.RankingModel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;


public class PdfFactoryNew {

  private static Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
  private static Font subtitleFont = new Font(FontFamily.HELVETICA, 16, Font.NORMAL);
  private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
  private static Font tableFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

  private Group group;
  private PdfPTable pdfTable;
  private Document document;
  private RankingModel rankings;
  private List<Game> games;
  private List<String> annotations;

  public void setGroup(Group group) {
    this.group = group;
  }

  public void setRankings(RankingModel rankings) {
    this.rankings = rankings;
  }

  public void setGames(List<Game> games) {
    this.games = games;
  }

  public void setAnnotations(List<String> annotations) {
    this.annotations = annotations;
  }

  public void print() {
    System.out.println(group.getName());
    try {
      document = new Document(PageSize.A4);
      File outputDir = new File(System.getProperties().getProperty("user.dir"), "ResultArchive");
      Files.createDirectories(outputDir.toPath());
      String fileName = "Resultate " + group.getName();
      fileName = fileName.replace('/', '_');
      File pdfFile = new File(outputDir, fileName + ".pdf");
      pdfFile.delete();
      PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
      document.open();
      addMetaData();
      addTitle();
      if (rankings.hasData()) {
        addRankingTable();
        addAnnotations();
        addResultTable();
      } else {
        addNoDataInformation();
      }
      addFooter();
      document.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addMetaData() {
    document.addTitle("Resultat der Innerschweizer Korbball Meitserschaft");
    document.addSubject(group.getName());
    document.addKeywords("IVK, Korbball " + group.getName());
    document.addAuthor("Roman Schaller");
    document.addCreator("Roman Schaller");
  }

  private void addTitle() throws DocumentException {
    Paragraph titlePara = new Paragraph();
    titlePara.add(new Paragraph("Resultate der Innerschweizer Korbball Meisterschaft", titleFont));
    addEmptyLine(titlePara, 1);
    titlePara.add(new Paragraph("Rangliste " + group.getName(), subtitleFont));
    addEmptyLine(titlePara, 1);
    document.add(titlePara);
  }

  private void addRankingTable() throws DocumentException {
    float[] widths = new float[]{1f, 3f, 1f, 1f, 1f, 1f, 2f, 1f, 1f};
    pdfTable = new PdfPTable(widths.length);
    pdfTable.setWidths(widths);
    pdfTable.setHorizontalAlignment(Element.ALIGN_LEFT);

    writeRankingHeader();

    writeRankings();

    formatTable();

    document.add(pdfTable);
  }

  private void addAnnotations() throws DocumentException {
    if (!annotations.isEmpty()) {
      Paragraph para = new Paragraph();
      addEmptyLine(para, 1);
      para.add(new Paragraph("Bemerkungen:", subtitleFont));
      annotations.forEach(annotation -> {
        para.add(new Paragraph(annotation, tableFont));
      });
      document.add(para);
    }
  }

  private void addResultTable() throws DocumentException {
    Paragraph title = new Paragraph();
    addEmptyLine(title, 1);
    title.add(new Paragraph("Resultate " +group.getName(), subtitleFont));
    addEmptyLine(title, 1);
    document.add(title);
    float[] widths = new float[]{1f, 3f, 3f, 1f};
    pdfTable = new PdfPTable(widths.length);
    pdfTable.setWidths(widths);
    pdfTable.setHorizontalAlignment(Element.ALIGN_LEFT);

    writeResults();
    formatTable();

    document.add(pdfTable);
  }

  private void formatTable() {
    boolean even = false;
    for (PdfPRow row : pdfTable.getRows()) {
      for (PdfPCell cell : row.getCells()) {
        cell.setBackgroundColor(even ? new BaseColor(245,245,245) : BaseColor.WHITE);
      }
      even = !even;
    }
  }
  private void addNoDataInformation() throws DocumentException {
    Paragraph noDataParagraph =
        new Paragraph("Momentan leider keine Daten für diese Gruppe verfügbar.", tableFont);
    addEmptyLine(noDataParagraph, 1);
    document.add(noDataParagraph);
  }

  private void writeRankingHeader() {
    pdfTable.addCell(createRankingHeader(""));
    pdfTable.addCell(createRankingHeader(""));
    pdfTable.addCell(createRankingHeader("gespielt"));
    pdfTable.addCell(createRankingHeader("gewonnen"));
    pdfTable.addCell(createRankingHeader("verloren"));
    pdfTable.addCell(createRankingHeader("unentschieden"));
    pdfTable.addCell(createRankingHeader("geschossen : erhalten"));
    pdfTable.addCell(createRankingHeader("Torverhältnis"));
    pdfTable.addCell(createRankingHeader("Punkte"));
  }

  private PdfPCell createRankingHeader(String text) {
    PdfPCell cell = createTableCell(text);
    cell.setRotation(90);
    cell.setHorizontalAlignment(Element.ALIGN_BASELINE);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setPadding(10);
    return cell;
  }

  private void writeRankings() {

    rankings.getRankings().forEach(
        ranking -> {

          PdfPCell rankingCell = createTableCell(ranking.getRank() + ".");
          rankingCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
          rankingCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

          pdfTable.addCell(rankingCell);

          PdfPCell teamCell = createTableCell(ranking.getTeam());
          teamCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
          teamCell.setHorizontalAlignment(Element.ALIGN_LEFT);
          pdfTable.addCell(teamCell);

          writeResultRankingCell(String.valueOf(ranking.getPlayed()));
          writeResultRankingCell(String.valueOf(ranking.getWon()));
          writeResultRankingCell(String.valueOf(ranking.getLost()));
          writeResultRankingCell(String.valueOf(ranking.getTie()));
          writeResultRankingCell(ranking.getRate().get(0) + " : " + ranking.getRate().get(1));
          int rankingRate = ranking.getRate().get(0) - ranking.getRate().get(1);
          String rankingRateString =
              rankingRate < 0 ? String.valueOf(rankingRate) : "+" + rankingRate;
          writeResultRankingCell(rankingRateString);
          writeResultRankingCell(String.valueOf(ranking.getPoints()));
        });
  }

  private void writeResultRankingCell(String text) {
    PdfPCell cell = createTableCell(text);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    pdfTable.addCell(cell);
  }

  private void writeResults() {
    games.stream().filter(Game::isPlayed).forEach(game -> {
      writeResultCell(game.getZeit());
      writeResultCell(game.getTxtTeamA());
      writeResultCell(game.getTxtTeamB());
      writeResultCell(game.getResultatA() + " : " + game.getResultatB());
    });
  }

  private void writeResultCell(String text) {
    PdfPCell cell = createTableCell(text);
    pdfTable.addCell(cell);
  }

  private PdfPCell createTableCell(String text) {
    PdfPCell cell = new PdfPCell(new Phrase(text, tableFont));
    cell.setBorder(0);
    cell.setBorderWidthBottom(0.5f);
    cell.setBorderColorBottom(BaseColor.LIGHT_GRAY);
    cell.setPaddingTop(3.5f);
    cell.setPaddingBottom(4f);
    return cell;
  }


  private void addEmptyLine(Paragraph paragraph, int number) {
    for (int i = 0; i < number; i++) {
      paragraph.add(new Paragraph(" "));
    }
  }

  private void addFooter() throws DocumentException {
    Paragraph footer = new Paragraph();
    addEmptyLine(footer, 1);
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
    String now = LocalDateTime.now().format(formatter);
    footer.add(new Paragraph("Generiert am " + now, smallBold));
    document.add(footer);
  }


}
