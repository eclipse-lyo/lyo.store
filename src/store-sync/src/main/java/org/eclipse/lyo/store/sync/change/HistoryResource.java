/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-1.0 OR BSD-3-Clause
 */
package org.eclipse.lyo.store.sync.change;

import org.eclipse.lyo.oslc4j.core.annotation.OslcName;
import org.eclipse.lyo.oslc4j.core.annotation.OslcNamespace;
import org.eclipse.lyo.oslc4j.core.annotation.OslcPropertyDefinition;
import org.eclipse.lyo.oslc4j.core.annotation.OslcResourceShape;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * HistoryResource is a wrapper OSLC Resource around <code>org.eclipse.lyo.oslc4j.trs.provider.HistoryData</code>.
 *
 * @author Andrew Berezovskyi (andriib@kth.se)
 * @version $version-stub$
 * @since 4.0.0
 */
@OslcNamespace(HistoryResource.NS_TRS)
@OslcName(HistoryResource.NAME)
@OslcResourceShape(title = "TRS History Resource Shape", describes = HistoryResource.TYPE)
public class HistoryResource extends AbstractResource {
    public static final String NS_TRS = "http://open-services.net/ns/core/trs#";
    public static final String NAME = "TrsHistoryResource";
    public static final String TYPE = NS_TRS + NAME;
    private ChangeKind changeKind;
    private Date timestamp;
    private URI resourceURI;

    /**
     * Shall be used only by the OSLC Jena Model Helper
     */
    @Deprecated
    public HistoryResource() {
    }

    public HistoryResource(ChangeKind changeKind, Date timestamp, URI resourceURI) {
        this.changeKind = changeKind;
        this.timestamp = timestamp;
        this.resourceURI = resourceURI;
    }

    public HistoryResource(ChangeKind changeKind, ZonedDateTime timestamp, URI resourceURI) {
        this.changeKind = changeKind;
        this.timestamp = Date.from(timestamp.toInstant());
        this.resourceURI = resourceURI;
    }

    @OslcName("change_kind")
    @OslcPropertyDefinition(NS_TRS + "change_kind")
    public String getChangeKind() {
        return changeKind.toString();
    }

    public void setChangeKind(ChangeKind changeKind) {
        this.changeKind = changeKind;
    }

    public ChangeKind getChangeKindEnum() {
        return changeKind;
    }

    public void setChangeKind(String changeKind) {
        this.changeKind = ChangeKind.fromString(changeKind);
    }

    @OslcName("timestamp")
    @OslcPropertyDefinition(NS_TRS + "timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @OslcName("uri")
    @OslcPropertyDefinition(NS_TRS + "uri")
    public URI getResourceURI() {
        return resourceURI;
    }

    public void setResourceURI(URI resourceURI) {
        this.resourceURI = resourceURI;
    }
}
