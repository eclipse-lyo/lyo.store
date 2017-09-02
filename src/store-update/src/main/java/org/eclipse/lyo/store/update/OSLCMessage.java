package org.eclipse.lyo.store.update;

import java.net.URI;

/**
 * OSLCMessage is .
 *
 * @author Andrew Berezovskyi (andriib@kth.se)
 * @version $version-stub$
 * @since 0.0.0
 */
public class OSLCMessage implements ServiceProviderMessage {
    private final URI serviceProviderId;

    public OSLCMessage(URI serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    @Override
    public URI getServiceProviderId() {
        return serviceProviderId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OSLCMessage{");
        sb.append("serviceProviderId='").append(serviceProviderId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
