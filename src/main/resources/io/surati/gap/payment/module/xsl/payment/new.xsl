<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (c) 2022 Surati

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to read
the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
merge, publish, distribute, sublicense, and/or sell copies of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <xsl:output method="html" include-content-type="no" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
  <xsl:include href="/io/surati/gap/web/base/xsl/layout.xsl"/>
  <xsl:template match="page" mode="head">
    <title>
      <xsl:text>GAP - Paiements</xsl:text>
    </title>
  </xsl:template>
  <xsl:template match="page" mode="header">
    <div class="app-page-title app-page-title-simple">
      <div class="page-title-wrapper">
        <div class="page-title-heading">
          <div class="page-title-icon">
            <i class="lnr-inbox icon-gradient bg-night-fade"/>
          </div>
          <div>
            <xsl:value-of select="root_page/title"/>
            <div class="page-title-subheading opacity-10">
              <nav class="" aria-label="breadcrumb">
                <ol class="breadcrumb">
                  <li class="breadcrumb-item">
                    <a href="/home">
                      <i aria-hidden="true" class="fa fa-home"/>
                    </a>
                  </li>
                  <li class="breadcrumb-item">
                    <a href="{root_page/uri}">
                      <xsl:value-of select="root_page/subtitle"/>
                    </a>
                  </li>
                  <li class="active breadcrumb-item" aria-current="page">
                    <xsl:text>Nouveau paiement</xsl:text>
                  </li>
                </ol>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="body">
    <div class="main-card mb-3 card">
      <div class="card">
        <div class="card-body">
          <form action="/payment/save?{root_page/full}" method="post">
            <input name="group_id" type="hidden" value="{group_id}"/>
            <div class="form-row">
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label class="font-weight-bold">
                    <xsl:text>Montant en chiffres</xsl:text>
                  </label>
                  <p>
                    <xsl:value-of select="total_amount_to_pay"/>
                  </p>
                </div>
              </div>
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label class="font-weight-bold">
                    <xsl:text>Montant en lettres</xsl:text>
                  </label>
                  <p>
                    <xsl:value-of select="total_amount_to_pay_in_letters"/>
                  </p>
                </div>
              </div>
              <div class="col-md-12">
                <div class="position-relative form-group">
                  <label class="font-weight-bold">
                    <xsl:text>Carnet de formules à utiliser</xsl:text>
                  </label>
                  <p>
                    <xsl:value-of select="bank_note_book/name_with_current_note"/>
                  </p>
                  <input type="hidden" name="book_id" value="{bank_note_book/id}"/>
                </div>
              </div>
              <div class="col-md-4">
                <div class="position-relative form-group">
                  <label for="date">
                    <xsl:text>Date d'édition</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <div class="input-group">
                    <input name="date" id="date" value="{date}" placeholder="Entrez une date ..." type="date" class="form-control" required=""/>
                    <div class="input-group-append">
                      <button onclick="document.getElementById('date').value = ''" class="btn btn-outline-secondary" type="button">X</button>
                    </div>
                  </div>
                </div>
              </div>
              <xsl:if test="echeance_date">
                <div class="col-md-4">
                  <div class="position-relative form-group">
                    <label for="echeance_date">
                      <xsl:text>Date d'échéance</xsl:text>
                      <span style="color: red"> *</span>
                    </label>
                    <div class="input-group">
                      <input name="echeance_date" id="echeance_date" value="{echeance_date}" placeholder="Entrez une date ..." type="date" class="form-control" required=""/>
                      <div class="input-group-append">
                        <button onclick="document.getElementById('echeance_date').value = ''" class="btn btn-outline-secondary" type="button">X</button>
                      </div>
                    </div>
                  </div>
                </div>
              </xsl:if>
              <div class="col-md-12">
                <div class="card-header">
                  <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
                    <xsl:text>Ordres de paiement à exécuter</xsl:text>
                  </div>
                </div>
                <div class="table-responsive">
                  <table class="align-middle text-truncate mb-0 table table-borderless table-hover">
                    <thead>
                      <tr>
                        <th class="text-center">N°</th>
                        <th class="text-center">Date</th>
                        <th class="text-center">Référence</th>
                        <xsl:if test="is_hetero">
                          <th class="text-center">Bénéficiaire</th>
                        </xsl:if>
                        <th class="text-center">Doc de référence</th>
                        <th class="text-center">Montant à payer</th>
                      </tr>
                    </thead>
                    <tbody>
                      <xsl:for-each select="payment_orders/payment_order">
                        <tr>
                          <td class="text-center text-muted" style="width: 80px;">
                            <xsl:value-of select="position()"/>
                          </td>
                          <td class="text-center" style="width: 80px;">
                            <xsl:value-of select="date_view"/>
                          </td>
                          <td class="text-center">
                            <xsl:value-of select="reference"/>
                          </td>
                          <xsl:if test="../../is_hetero">
                            <td class="text-center">
                              <xsl:value-of select="beneficiary"/>
                            </td>
                          </xsl:if>
                          <td class="text-center">
                            <xsl:value-of select="ref_doc_name"/>
                          </td>
                          <td class="text-center">
                            <div class="badge badge-pill badge-success">
                              <xsl:value-of select="amount_to_pay_in_human"/>
                            </div>
                          </td>
                        </tr>
                      </xsl:for-each>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
            <div class="divider"/>
            <div class="clearfix">
              <button type="submit" class="btn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-success">
                <xsl:text>Payer </xsl:text>
                <i class="fa fa-check"/>
              </button>
              <button type="button" class="btn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-primary mr-1" onclick="location.href='/payment/new/book/edit?group={group_id}&amp;{root_page/full}'">
                <xsl:text>Changer de carnet </xsl:text>
                <i class="fa fa-check"/>
              </button>
              <button type="button" class="btn-shadow float-right btn-wide btn-pill mr-1 btn btn-outline-secondary" onclick="location.href='{root_page/uri}'">
                <xsl:text>Annuler </xsl:text>
                <i class="fa fa-undo"/>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="custom-script"/>
</xsl:stylesheet>
