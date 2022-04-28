package io.surati.gap.payment.module;

import io.surati.gap.admin.base.api.Access;
import io.surati.gap.web.base.menu.DashboardMenu;

/**
 * Gtp base dashboard menu.
 *
 * @since 0.1
 */
public enum PaymentDashboardMenu implements DashboardMenu {
    ;

    private final int order;

    private final String code;

    private final String title;

    private final String link;

    private final Iterable<Access> accesses;

    PaymentDashboardMenu(
        final int order, final String code, final String title,
        final String link, final Iterable<Access> accesses
    ) {
        this.order = order;
        this.code = code;
        this.title = title;
        this.link = link;
        this.accesses = accesses;
    }

    @Override
    public int order() {
        return this.order;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public String link() {
        return this.link;
    }

    @Override
    public Iterable<? extends Access> accesses() {
        return this.accesses;
    }
}
