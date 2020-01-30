package ch.romix.ivk.resultarchiver.report;

import ch.romix.ivk.resultarchiver.model.Game;
import ch.romix.ivk.resultarchiver.model.Group;
import ch.romix.ivk.resultarchiver.model.RankingModel;
import ch.romix.ivk.resultarchiver.model.Season;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


public class PdfCreator {

  private static Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
  private static Font subtitleFont = new Font(FontFamily.HELVETICA, 16, Font.NORMAL);
  private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
  private static Font tableFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

  private Season season;
  private Group group;
  private PdfPTable pdfTable;
  private Document document;
  private RankingModel rankings;
  private List<Game> games;
  private List<String> annotations;

  public void setSeason(Season season) {
    this.season = season;
  }

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
    Paragraph titlePara = new Paragraph("Resultate der Innerschweizer Korbball Meisterschaft",
        titleFont);
    titlePara.setSpacingAfter(10);
    document.add(titlePara);
    Paragraph seasonPara = new Paragraph("Saison " + season, titleFont);
    seasonPara.setSpacingAfter(40);
    document.add(seasonPara);
    Paragraph subtitle = new Paragraph("Rangliste " + group.getName(), subtitleFont);
    subtitle.setSpacingAfter(20);
    document.add(subtitle);
  }

  private void addRankingTable() throws DocumentException {
    float[] widths = new float[]{0.5f, 3f, 1f, 1f, 1f, 1f, 2f, 1f, 1f};
    pdfTable = new PdfPTable(widths.length);
    pdfTable.setWidthPercentage(90);
    pdfTable.setWidths(widths);
    pdfTable.setHorizontalAlignment(Element.ALIGN_LEFT);

    writeRankingHeader();

    writeRankings();

    formatTable();

    document.add(pdfTable);
  }

  private void addAnnotations() throws DocumentException {
    if (!annotations.isEmpty()) {
      Paragraph annotationsTitle = new Paragraph("Bemerkungen:", subtitleFont);
      annotationsTitle.setSpacingAfter(20);
      document.add(annotationsTitle);
      for (String annotation : annotations) {
        Paragraph para = new Paragraph(annotation, tableFont);
        document.add(para);
      }
    }
  }

  private void addResultTable() throws DocumentException {
    Paragraph title = new Paragraph("Resultate " + group.getName(), subtitleFont);
    title.setSpacingBefore(20);
    document.add(title);
    float[] widths = new float[]{1f, 3f, 3f, 1f};

    Map<String, List<Game>> gamesByDay = games.stream().filter(Game::isPlayed)
        .collect(Collectors.groupingBy(Game::getTag, Collectors.toList()));

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    List<Entry<String, List<Game>>> sortedGames = gamesByDay.entrySet().stream().sorted(
        (a, b) -> {
          TemporalAccessor timeA = dateTimeFormatter.parse(a.getKey());
          TemporalAccessor timeB = dateTimeFormatter.parse(b.getKey());
          return LocalDate.from(timeA).compareTo(LocalDate.from(timeB));
        }
    ).collect(Collectors.toList());

    for (Entry<String, List<Game>> gamesOfOneDay : sortedGames) {
      Paragraph dayTitle = new Paragraph(gamesOfOneDay.getKey() + ":");
      dayTitle.setSpacingBefore(20.0f);
      dayTitle.setSpacingAfter(10.0f);
      document.add(dayTitle);
      pdfTable = new PdfPTable(widths.length);
      pdfTable.setWidthPercentage(90);
      pdfTable.setWidths(widths);
      pdfTable.setHorizontalAlignment(Element.ALIGN_LEFT);

      for (Game game : gamesOfOneDay.getValue()) {
        writeResultCell(game.getZeit());
        writeResultCell(game.getTxtTeamA());
        writeResultCell(game.getTxtTeamB());
        writeResultCell(game.getResultatA() + " : " + game.getResultatB());
        pdfTable.completeRow();
      }

      formatTable();
      document.add(pdfTable);
    }


  }

  private void formatTable() {
    boolean even = false;
    for (PdfPRow row : pdfTable.getRows()) {
      for (PdfPCell cell : row.getCells()) {
        if (cell != null) {
          cell.setBackgroundColor(even ? new BaseColor(245, 245, 245) : BaseColor.WHITE);
        }
      }
      even = !even;
    }
  }

  private void addNoDataInformation() throws DocumentException {
    Paragraph noDataParagraph =
        new Paragraph("Momentan leider keine Daten für diese Gruppe verfügbar.", tableFont);
    noDataParagraph.setSpacingAfter(20);
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

  private void addFooter() throws DocumentException {
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
    String now = LocalDateTime.now().format(formatter);
    Paragraph footer = new Paragraph("Generiert am " + now, smallBold);
    footer.setSpacingBefore(30);
    document.add(footer);
  }
}
