package ch.romix.ivk.resultarchiver;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Main {

  private static final String BASE_URL = "http://korbball.turnverband.ch/ws/";
  // private static final String BASE_URL = "http://localhost/www.turnverband.ch/korbball/ws/";
  private static final String GROUPS = "groupsV1.php";

  public static void main(String[] args) {
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(BASE_URL + GROUPS);
    Response groupsResponse = target.request(MediaType.APPLICATION_JSON).get();
    List<Group> groups = groupsResponse.readEntity(new GenericType<List<Group>>() {});
    groups.forEach(g -> System.out.println(g));
    groupsResponse.close();
  }

}
