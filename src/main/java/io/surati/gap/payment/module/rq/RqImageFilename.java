package io.surati.gap.payment.module.rq;

import java.io.IOException;
import org.takes.Request;
import org.takes.rq.RqHeaders;

public final class RqImageFilename {

    private final Request req;
    
    public RqImageFilename(final Request req) {
        this.req = req;
    }
    
    public String value() throws IOException {
        final RqHeaders.Smart imgrq = new RqHeaders.Smart(req);
        final String header = String.format("images/header/%s", imgrq.single("Content-Disposition"));
        for (String content : header.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        throw new IllegalArgumentException("Request not contained filename property !");
    }
}
