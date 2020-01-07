package ch.romix.ivk.resultarchiver;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.romix.ivk.resultarchiver.model.ComposedModel;
import ch.romix.ivk.resultarchiver.model.Points;
import ch.romix.ivk.resultarchiver.model.TableRanking;
import ch.romix.ivk.resultarchiver.model.Rate;
import ch.romix.ivk.resultarchiver.model.Table;

public class TableParser {

  private Client client;
  private ComposedModel model;

  public TableParser(Client client) {
    this.client = client;
  }

  public void readTableOfGroup(int id) {
    WebTarget target = client.target(RESTURI.getTableURI(id));
    Response response = target.request(MediaType.APPLICATION_JSON).get();
    try {
      model = response.readEntity(ComposedModel.class);
    } finally {
      response.close();
    }
  }

  public Table getTable() {
    return model.getTable();
  }

  public List<Rate> getRates() {
    return model.getRates();
  }

  public List<Points> getPoints() {
    return model.getPoints();
  }

  public List<TableRanking> getRankings() {
    return model.getRankings();
  }
}
