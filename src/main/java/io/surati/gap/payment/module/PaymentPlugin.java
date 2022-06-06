package io.surati.gap.payment.module;

import io.surati.gap.commons.utils.pf4j.DatabaseSetup;
import io.surati.gap.commons.utils.pf4j.ModuleRegistration;
import io.surati.gap.commons.utils.pf4j.WebFront;
import io.surati.gap.payment.module.server.FkActions;
import io.surati.gap.payment.module.server.FkApi;
import io.surati.gap.payment.module.server.FkPages;
import javax.sql.DataSource;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.takes.facets.auth.Pass;
import org.takes.facets.fork.FkChain;
import org.takes.facets.fork.Fork;

public final class PaymentPlugin extends Plugin {

    public PaymentPlugin(final PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("Starting Payment module...");
    }

    @Override
    public void stop() {
        System.out.println("Stopping Payment module...");
    }

    @Override
    public void delete() {
        System.out.println("Deleting Payment module...");
    }

    @Extension
    public static final class PaymentRegistration implements ModuleRegistration {

        @Override
        public void register() {
            PaymentModule.setup();
        }
    }

    @Extension
    public static final class PaymentDatabaseSetup implements DatabaseSetup {

        @Override
        public void migrate(final DataSource src, final boolean demo) {
            // nothing to do
        }
    }

    @Extension
    public static final class PaymentWebFront implements WebFront {

        @Override
        public Fork pages() {
            return new FkChain();
        }

        @Override
        public Fork pages(final DataSource src) {
            return new FkChain(
                new FkPages(src),
                new FkApi(src),
                new FkActions(src)
            );
        }

        @Override
        public Fork pages(final DataSource src, final Pass pass) {
            return new FkChain();
        }
    }
}
