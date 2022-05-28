package io.surati.gap.payment.module.server;

import io.surati.gap.payment.module.PaymentModule;
import io.surati.gap.web.base.InClasspath;
import java.io.IOException;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Scalar;
import org.takes.rs.RsWrap;
import org.takes.rs.xe.XeSource;

public final class RsPage extends RsWrap {

    /**
     * Ctor.
     *
     * @param res Original response
     */
    public RsPage(final String xsl, final Request req, final DataSource source, final Scalar<Iterable<XeSource>> src) throws IOException {
        super(
            new io.surati.gap.web.base.RsPage(xsl, new InClasspath(PaymentModule.class), req, source, src)
        );
    }
}
