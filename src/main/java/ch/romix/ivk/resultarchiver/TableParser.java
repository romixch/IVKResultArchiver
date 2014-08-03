package ch.romix.ivk.resultarchiver;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.romix.ivk.resultarchiver.model.Table;

public class TableParser {

  private Client client;

  public TableParser(Client client) {
    this.client = client;
  }

  public Table getTable(int id) {
    Table table = null;
    WebTarget target = client.target(RESTURI.getTableURI(id));
    Response response = target.request(MediaType.APPLICATION_JSON).get();
    try {
      table = response.readEntity(Table.class);
    } finally {
      response.close();
    }
    return table;
  }
}
