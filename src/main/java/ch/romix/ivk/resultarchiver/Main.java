package ch.romix.ivk.resultarchiver;

import ch.romix.ivk.resultarchiver.model.Group;
import ch.romix.ivk.resultarchiver.report.PdfFactoryNew;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class Main {

  public static void main(String[] args) {
    Client client = ClientBuilder.newClient();
    List<Group> groups = new GroupParser(client).getGroups();
    groups.forEach(group -> {
      TableParser tableParser = new TableParser(client);
      tableParser.readTableOfGroup(group.getID());
      RankingParser rankingParser = new RankingParser(client);
      GamesParser gamesParser = new GamesParser(client);
      AnnotationParser annotationParser = new AnnotationParser((client));
      printResults(group, tableParser, rankingParser, gamesParser, annotationParser);
    });
  }

  private static void printResults(Group group, TableParser tableParser, RankingParser rankingParser, GamesParser gamesParser, AnnotationParser annotationParser) {
    PdfFactoryNew pdfFactory = new PdfFactoryNew();
    pdfFactory.setGroup(group);
    pdfFactory.setRankings(rankingParser.getRankings(group.getID()));
    pdfFactory.setGames(gamesParser.getGames(group.getID()));
    pdfFactory.setAnnotations(annotationParser.getAnnotations((group.getID())));
    pdfFactory.print();
  }
}
