/*
 * Copyright (c) 2022 Surati

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
	
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.surati.gap.payment.module;

import io.surati.gap.admin.base.api.Module;
import io.surati.gap.web.base.menu.DashboardMenu;
import io.surati.gap.web.base.menu.Menu;
import io.surati.gap.web.base.menu.SimpleMenu;
import io.surati.gap.web.base.menu.SimpleSubmenu;
import org.cactoos.iterable.IterableOf;

/**
 * Gtp base module.
 *
 * @since 0.1
 */
public enum PaymentModule implements Module {

	PAYMENT(
		"Payment module",
		"This module helps to perform payments."
	);

	public static void setup() {
		Module.VALUES.add(PaymentModule.PAYMENT);
		Module.BY_CODE.put(PaymentModule.PAYMENT.code(), PaymentModule.PAYMENT);
		for (final DashboardMenu dmenu : PaymentDashboardMenu.values()) {
			DashboardMenu.VALUES.add(dmenu);
		}
		Menu.VALUES.add(
			new SimpleMenu(
				700,
				"payment",
				"lnr-diamond",
				"Paiement",
				"bg-success",
				"Effectuer vos paiements",
				new IterableOf<>(
					new SimpleSubmenu(
						1, "doc-to-treat", "lnr-pointer-left", "Documents de référence à traiter", "/reference-document/list",
						new IterableOf<>(
							PaymentAccess.IMPORTER_ORDRES_PAIEMENT
						),
						false
					),
					new SimpleSubmenu(
						2, "order-to-prepare", "lnr-pointer-left", "Ordres de paiement à préparer", "/payment-order/to-prepare",
						new IterableOf<>(
							PaymentAccess.PREPARER_ORDRES_PAIEMENT,
							PaymentAccess.AUTORISER_ORDRES_PAIEMENT
						),
						false
					),
					new SimpleSubmenu(
						3, "payment-to-execute", "lnr-diamond", "Paiements à exécuter", "/payment/home",
						new IterableOf<>(
							PaymentAccess.EXECUTER_ORDRES_PAIEMENT
						),
						false
					),
					new SimpleSubmenu(
						4, "payment-to-export", "fa fa-upload", "Paiements à exporter", "/payment/export/list",
						new IterableOf<>(
							PaymentAccess.EXPORTER_PAIEMENTS
						),
						false
					),
					new SimpleSubmenu(
						5, "bank-note-book", "lnr-book", "Mes carnets de formule", "/bank-note-book",
						new IterableOf<>(
							PaymentAccess.VISUALISER_CARNETS,
							PaymentAccess.PREPARER_CARNETS,
							PaymentAccess.METTRE_EN_UTILISATION_CARNETS,
							PaymentAccess.BLOQUER_CARNETS
						),
						true
					)
				)
			)
		);
		Menu.insertSubmenu(
			"settings",
			new SimpleSubmenu(
				3, "bank", "lnr-apartment", "Banques", "/bank",
				new IterableOf<>(
					PaymentAccess.VISUALISER_BANQUES,
					PaymentAccess.CONFIGURER_BANQUES
				),
				false
			)
		);
		Menu.insertSubmenu(
			"settings",
			new SimpleSubmenu(
				4, "bank-account", "lnr-inbox", "Mes comptes bancaires", "/bank-account",
				new IterableOf<>(
					PaymentAccess.VISUALISER_COMPTES_BANCAIRES,
					PaymentAccess.CONFIGURER_COMPTES_BANCAIRES
				),
				false
			)
		);
	}

	/**
	 * Title of access.
	 */
	private String title;

	/**
	 * Description.
	 */
	private String description;

	/**
	 * Ctor.
	 * @param title Title
	 * @param description Description
	 */
	PaymentModule(final String title, final String description) {
		this.title = title;
		this.description = description;
	}
	
	/**
	 * Get title.
	 * @return Title
	 */
	@Override
	public String title() {
		return this.title;
	}

	/**
	 * Get description.
	 * @return Description
	 */
	@Override
	public String description() {
		return this.description;
	}

	/**
	 * Get Code.
	 * @return Code
	 */
	public String code() {
		return this.name();
	}
	
	@Override
	public String toString() {
		return this.title;
	}
}
