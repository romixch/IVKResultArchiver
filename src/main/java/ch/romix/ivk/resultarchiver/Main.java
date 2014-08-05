package ch.romix.ivk.resultarchiver;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import ch.romix.ivk.resultarchiver.model.Group;
import ch.romix.ivk.resultarchiver.model.Rate;
import ch.romix.ivk.resultarchiver.model.Table;
import ch.romix.ivk.resultarchiver.report.PdfFactory;

public class Main {

  public static void main(String[] args) {
    Client client = ClientBuilder.newClient();
    List<Group> groups = new GroupParser(client).getGroups();
    groups.forEach(group -> {
      TableParser table = new TableParser(client);
      table.readTableOfGroup(group.getID());
      print(group, table.getTable(), table.getRates());
    });
  }

  private static void print(Group group, Table table, List<Rate> rates) {
    PdfFactory pdfFactory = new PdfFactory();
    pdfFactory.setGroup(group);
    pdfFactory.setTable(table);
    pdfFactory.setRates(rates);
    pdfFactory.print();
  }
}
