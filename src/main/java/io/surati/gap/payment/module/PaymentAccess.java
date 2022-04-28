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

import io.surati.gap.admin.base.api.Access;
import io.surati.gap.admin.base.api.Module;

/**
 * Access right for a user.
 *
 * @since 0.1
 */
public enum PaymentAccess implements Access {

	TRAVAILLER_DANS_SON_PROPRE_ESPACE_DE_TRAVAIL("Travailler dans son propre espace de travail", ""),
	IMPORTER_ORDRES_PAIEMENT("Importer les ordres de paiement", ""),
	ENVOYER_DOC_REF_EN_PREPARATION("Envoyer les documents de référence en préparation", ""),
	PREPARER_ORDRES_PAIEMENT("Préparer les ordres de paiement", ""),
	AUTORISER_ORDRES_PAIEMENT("Autoriser les ordres de paiement", ""),
	EXECUTER_ORDRES_PAIEMENT("Exécuter les ordres de paiement", ""),
	ANNULER_PAIEMENTS("Annuler les paiements", ""),
	VISUALISER_BANQUES("Visualiser les banques", ""),
	CONFIGURER_BANQUES("Configurer les banques", ""),
	VISUALISER_COMPTES_BANCAIRES("Visualiser les comptes bancaires", ""),
	CONFIGURER_COMPTES_BANCAIRES("Configurer les comptes bancaires", ""),
	VISUALISER_CARNETS("Visualiser les carnets de formules", ""),
	PREPARER_CARNETS("Préparer les carnets de formules", ""),
	METTRE_EN_UTILISATION_CARNETS("Mettre en utilisation les carnets de formules", ""),
	BLOQUER_CARNETS("Bloquer les carnets de formules", ""),
	VISUALISER_UTILISATEURS("Visualiser les utilisateurs", ""),
	CONFIGURER_UTILISATEURS("Configurer les utilisateurs", ""),
	BLOQUER_UTILISATEURS("Bloquer les utilisateurs", ""),
	CHANGER_MOT_DE_PASSE_UTILISATEURS("Changer le mot de passe d'un utilisateur", ""),
	VISUALISER_PROFILS("Visualiser les profils d'utilisateurs", ""),
	CONFIGURER_PROFILS("Configurer les profils d'utilisateurs", ""),
	VISUALISER_INFO_ENTREPRISE("Visualiser les informations de l'entreprise", ""),
	CONFIGURER_INFO_ENTREPRISE("Configurer les informations de l'entreprise", ""),
	VISUALISER_TABLEAU_BORD_PAIEMENTS("Visualiser le tableau de bord des paiements", ""),
	VISUALISER_TABLEAU_BORD_CARNETS("Visualiser le tableau de bord des carnets de formules", ""),
	VISUALISER_TABLEAU_BORD_COMPTES_BANCAIRES("Visualiser le tableau de bord des comptes bancaires", ""),
	VISUALISER_LA_JOURNALISATION("Visualiser la journalisation", ""),
	EXPORTER_PAIEMENTS("Exporter les paiements", "");

	static {
		for (Access acs : PaymentAccess.values()) {
			Access.VALUES.add(acs);
			Access.BY_CODE.put(acs.code(), acs);
		}
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
	PaymentAccess(final String title, final String description) {
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
	 * Get Code.
	 * @return Code
	 */
	public String code() {
		return this.name();
	}

	/**
	 * Get description.
	 * @return Code
	 */
	public String description() {
		return this.description;
	}

	/**
	 * Get module.
	 * @return Module name
	 */
	public Module module() {
		return PaymentModule.PAYMENT;
	}
	
	@Override
	public String toString() {
		return this.title;
	}
}
