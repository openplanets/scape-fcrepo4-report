package eu.scape_project.test.integration;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.VerbType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReportIT extends AbstractReportIT {
    @Test
    public void testGetMetadataFormats() throws Exception{
        HttpResponse resp =this.getOAIPMHResponse(VerbType.LIST_METADATA_FORMATS.value(), null, null, null, null, null);
        assertEquals(EntityUtils.toString(resp.getEntity()), 200, resp.getStatusLine().getStatusCode());
        OAIPMHtype oai = (OAIPMHtype) this.unmarshaller.unmarshal(resp.getEntity().getContent());
        assertNotNull(oai);
        assertEquals(3, oai.getListMetadataFormats().getMetadataFormat().size());
        assertEquals(0, oai.getError().size());
    }
}
