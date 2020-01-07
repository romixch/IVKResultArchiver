package ch.romix.ivk.resultarchiver;

import ch.romix.ivk.resultarchiver.model.Group;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class GroupParser {

  private Client jaxrsClient;

  public GroupParser(Client jaxrsClient) {
    this.jaxrsClient = jaxrsClient;
  }

  public List<Group> getGroups() {
    List<Group> groups;
    WebTarget target = jaxrsClient.target(RESTURI.getGroupURI());
    Response groupsResponse = target.request(MediaType.APPLICATION_JSON).get(Response.class);
    try {
      groups = groupsResponse.readEntity(new GenericType<List<Group>>() {});
    } finally {
      groupsResponse.close();
    }
    return groups;
  }
}
