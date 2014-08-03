package ch.romix.ivk.resultarchiver;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import ch.romix.ivk.resultarchiver.model.Group;

public class Main {

  public static void main(String[] args) {
    Client client = ClientBuilder.newClient();
    List<Group> groups = new GroupParser(client).getGroups();
    groups.forEach(g -> System.out.println(g));
  }

}
