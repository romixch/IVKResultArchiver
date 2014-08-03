package ch.romix.ivk.resultarchiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ch.romix.ivk.resultarchiver.model.Group;
import ch.romix.ivk.resultarchiver.model.Table;

public class Main {

  public static void main(String[] args) throws JRException, IOException {
    Client client = ClientBuilder.newClient();
    List<Group> groups = new GroupParser(client).getGroups();
    Table table = new TableParser(client).getTable(groups.get(0).getID());
    print(table);
  }

  private static void print(Table table) throws JRException, IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream jrxmlStream = classLoader.getResourceAsStream("reports/result.jrxml");
    JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);
    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(table.getTeams());
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
    File pdfFile = Files.createTempFile("report", ".pdf").toFile();
    JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdfFile));
    System.out.println("Created report " + pdfFile.getAbsolutePath());
  }
}
