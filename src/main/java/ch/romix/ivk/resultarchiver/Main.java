package ch.romix.ivk.resultarchiver;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import ch.romix.ivk.resultarchiver.model.Group;
import ch.romix.ivk.resultarchiver.model.Table;
import ch.romix.ivk.resultarchiver.report.PdfFactory;

public class Main {

  public static void main(String[] args) {
    Client client = ClientBuilder.newClient();
    List<Group> groups = new GroupParser(client).getGroups();
    groups.forEach(group -> {
      Table table = new TableParser(client).getTable(group.getID());
      print(group, table);
    });
  }

  private static void print(Group group, Table table) {
    PdfFactory pdfFactory = new PdfFactory();
    pdfFactory.setGroup(group);
    pdfFactory.setTable(table);
    pdfFactory.print();
  }
}
