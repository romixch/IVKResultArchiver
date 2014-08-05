package ch.romix.ivk.resultarchiver.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ComposedModel {
  private Table table;
  private List<Rate> rates;
  private List<Points> points;
  private List<Ranking> rankings;

  public Table getTable() {
    return table;
  }

  public List<Rate> getRates() {
    return rates;
  }

  public List<Points> getPoints() {
    return points;
  }

  public List<Ranking> getRankings() {
    return rankings;
  }
}
