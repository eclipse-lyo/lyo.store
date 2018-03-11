package org.eclipse.lyo.store.sync;

import org.apache.jena.rdf.model.Model;
import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.eclipse.lyo.oslc4j.provider.jena.JenaModelHelper;
import org.eclipse.lyo.store.Store;
import org.eclipse.lyo.store.StoreFactory;
import org.eclipse.lyo.store.sync.change.ChangeKind;
import org.eclipse.lyo.store.sync.change.HistoryResource;
import org.junit.Test;

/**
 * TestTrsHistoryResource is .
 *
 * @author Andrew Berezovskyi (andriib@kth.se)
 * @version $version-stub$
 * @since 0.0.0
 */
public class TestHistoryResource {
    public static final URI RESOURCE_URI = URI.create("test:test");
    private final Store store = StoreFactory.inMemory();

    @Test
    public void testResourceIsMarshalled() throws Exception {
        HistoryResource resource = new HistoryResource(ChangeKind.CREATION,
                Date.from(Instant.now()), TestHistoryResource.RESOURCE_URI);

        Model model = JenaModelHelper.createJenaModel(new Object[] { resource });
        assertThat(model.size()).isGreaterThan(0);
    }

    @Test
    public void testResourceContainsStatements() throws Exception {
        HistoryResource resource = new HistoryResource(ChangeKind.CREATION,
                Date.from(Instant.now()), TestHistoryResource.RESOURCE_URI);
        resource.setAbout(TestHistoryResource.RESOURCE_URI);

        Model model = JenaModelHelper.createJenaModel(new Object[] { resource });
        assertThat(model.size()).isGreaterThan(1);
    }

    @Test
    public void testResourceIsPersisted() throws Exception {
        HistoryResource resource = new HistoryResource(ChangeKind.CREATION,
                Date.from(Instant.now()), TestHistoryResource.RESOURCE_URI);
        resource.setAbout(TestHistoryResource.RESOURCE_URI);

        assertThat(store.keySet()).hasSize(0);
        store.putResources(resource.getAbout(), Collections.singletonList(resource));
        assertThat(store.keySet()).hasSize(1);
    }

    @Test
    public void testResourceIsRestored() throws Exception {
        HistoryResource resource = new HistoryResource(ChangeKind.CREATION,
                Date.from(Instant.now()), TestHistoryResource.RESOURCE_URI);
        resource.setAbout(TestHistoryResource.RESOURCE_URI);

        store.putResources(resource.getAbout(), Collections.singletonList(resource));
        List<HistoryResource> resources = store.getResources(TestHistoryResource.RESOURCE_URI, HistoryResource.class);
        assertThat(resources).hasSize(1);
    }

    @Test
    public void testResourceIsRestoredWithProperties() throws Exception {
        String testResourceURI = "lyo:testtest";
        Date timestamp = Date.from(Instant.now());
        HistoryResource resource = new HistoryResource(ChangeKind.CREATION, timestamp,
                URI.create(testResourceURI));
        resource.setAbout(TestHistoryResource.RESOURCE_URI);

        store.putResources(resource.getAbout(), Collections.singletonList(resource));

        List<HistoryResource> resources = store.getResources(TestHistoryResource.RESOURCE_URI, HistoryResource.class);
        HistoryResource storeResource = resources.get(0);

        assertThat(storeResource.getChangeKind()).isEqualToIgnoringCase(String.valueOf(ChangeKind.CREATION));
        assertThat(storeResource.getTimestamp()).isEqualTo(timestamp);
        assertThat(storeResource.getResourceURI().toASCIIString()).isEqualTo(testResourceURI);
    }
}
