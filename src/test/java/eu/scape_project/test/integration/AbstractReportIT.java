package eu.scape_project.test.integration;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.runner.RunWith;
import org.openarchives.oai._2.IdentifyType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.ObjectFactory;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;

import static java.lang.Integer.parseInt;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-container.xml")
public abstract class AbstractReportIT {
    protected final CloseableHttpClient client = HttpClients.createDefault();

    protected Unmarshaller unmarshaller;

    protected Marshaller marshaller;

    protected static final String serverAddress = "http://localhost:" + parseInt(System.getProperty("test.port", "8013"));

    public AbstractReportIT() {
        try {
            this.marshaller = JAXBContext.newInstance(IdentifyType.class).createMarshaller();
            this.unmarshaller = JAXBContext.newInstance(OAIPMHtype.class).createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException("Unable to create JAX-B context");
        }
    }

    public HttpResponse getOAIPMHResponse(String verb, String identifier, String metadataPrefix, String from,
                                          String until, String set) throws IOException,
            JAXBException {
        final StringBuilder url = new StringBuilder(serverAddress)
                .append("/oai?verb=")
                .append(verb);

        if (identifier != null && !identifier.isEmpty()) {
            url.append("&identifier=").append(identifier);
        }
        if (metadataPrefix != null && !metadataPrefix.isEmpty()) {
            url.append("&metadataPrefix=").append(metadataPrefix);
        }
        if (from != null && !from.isEmpty()) {
            url.append("&from=").append(from);
        }
        if (until != null && !until.isEmpty()) {
            url.append("&until=").append(until);
        }
        if (set != null && !set.isEmpty()) {
            url.append("&set=").append(set);
        }

        HttpGet get = new HttpGet(url.toString());
        return this.client.execute(get);
    }

    public HttpResponse getOAIPMHResponse(String tokenData) throws IOException, JAXBException {
        final StringBuilder url = new StringBuilder(serverAddress)
                .append("/oai?resumptionToken=")
                .append(tokenData);
        HttpGet get = new HttpGet(url.toString());
        return this.client.execute(get);
    }
}
