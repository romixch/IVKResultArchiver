package ch.romix.ivk.resultarchiver;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import ch.romix.ivk.resultarchiver.model.Group;
import ch.romix.ivk.resultarchiver.report.PdfFactory;

public class Main {

  public static void main(String[] args) {
    Client client = ClientBuilder.newClient();
    List<Group> groups = new GroupParser(client).getGroups();
    groups.forEach(group -> {
      TableParser parser = new TableParser(client);
      parser.readTableOfGroup(group.getID());
      print(group, parser);
    });
  }

  private static void print(Group group, TableParser parser) {
    PdfFactory pdfFactory = new PdfFactory();
    pdfFactory.setGroup(group);
    pdfFactory.setTable(parser.getTable());
    pdfFactory.setRates(parser.getRates());
    pdfFactory.setPoints(parser.getPoints());
    pdfFactory.setRankings(parser.getRankings());
    pdfFactory.print();
  }
}
