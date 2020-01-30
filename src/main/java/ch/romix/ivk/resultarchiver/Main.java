package ch.romix.ivk.resultarchiver;

import ch.romix.ivk.resultarchiver.model.Group;
import ch.romix.ivk.resultarchiver.model.Season;
import ch.romix.ivk.resultarchiver.report.PdfCreator;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class Main {

  public static void main(String[] args) {
    Client client = ClientBuilder.newClient();
    SeasonParser seasonParser = new SeasonParser(client);
    Season season = seasonParser.getSeason();
    List<Group> groups = new GroupParser(client).getGroups();
    groups.forEach(group -> {
      TableParser tableParser = new TableParser(client);
      tableParser.readTableOfGroup(group.getID());
      RankingParser rankingParser = new RankingParser(client);
      GamesParser gamesParser = new GamesParser(client);
      AnnotationParser annotationParser = new AnnotationParser((client));
      printResults(season, group, tableParser, rankingParser, gamesParser, annotationParser);
    });
  }

  private static void printResults(Season season, Group group, TableParser tableParser,
      RankingParser rankingParser, GamesParser gamesParser, AnnotationParser annotationParser) {
    PdfCreator pdfCreator = new PdfCreator();
    pdfCreator.setSeason(season);
    pdfCreator.setGroup(group);
    pdfCreator.setRankings(rankingParser.getRankings(group.getID()));
    pdfCreator.setGames(gamesParser.getGames(group.getID()));
    pdfCreator.setAnnotations(annotationParser.getAnnotations((group.getID())));
    pdfCreator.print();
  }
}
