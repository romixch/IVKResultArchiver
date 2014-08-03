package ch.romix.ivk.resultarchiver;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.junit.Before;
import org.junit.Test;

import ch.romix.ivk.resultarchiver.GroupParser;
import ch.romix.ivk.resultarchiver.model.Group;


public class TestGroupParser {

  private GroupParser parser;

  @Before
  public void before() {
    Client client = ClientBuilder.newClient();
    parser = new GroupParser(client);
  }

  @Test
  public void getGroups() {
    List<Group> groups = parser.getGroups();
    assertNotNull(groups);
  }

}
