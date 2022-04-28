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
      <xsl:text>GAP - Paiements - Paiement par lots</xsl:text>
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
                    <xsl:text>Paiement du lot</xsl:text>
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
        <div class="card-header">
          <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
            <xsl:text>Payer le lot</xsl:text>
          </div>
        </div>
        <div class="card-body">
          <form action="/payment/batch/save?{root_page/full}" method="post">
            <input name="account_id" type="hidden" value="{account/id}"/>
            <input name="mean_type_id" type="hidden" value="{mean_type/id}"/>
            <div class="form-row">
              <div class="col-md-4">
                <div class="position-relative form-group">
                  <label class="font-weight-bold">
                    <xsl:text>Banque</xsl:text>
                  </label>
                  <p>
                    <xsl:value-of select="account/bank"/>
                  </p>
                </div>
              </div>
              <div class="col-md-4">
                <div class="position-relative form-group">
                  <label for="date" class="font-weight-bold">
                    <xsl:text>Date d'édition</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <div class="input-group">
                    <input name="date" id="date" value="{today}" placeholder="Entrez une date ..." type="date" class="form-control" required=""/>
                    <div class="input-group-append">
                      <button onclick="document.getElementById('date').value = ''" class="btn btn-outline-secondary" type="button">X</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="form-row">
              <div class="col-md-12">
                <div class="table-responsive">
                  <table class="mb-0 table table-hover table-sm">
                    <thead>
                      <tr>
                        <th>N°</th>
                        <th><xsl:value-of select="mean_type/name"/> N°</th>
                        <xsl:if test="not(mean_type/id = 'CHEQUE')">
                          <th>Date d'échéance</th>
                        </xsl:if>
                        <th>Bénéficiaire</th>
                        <th>Montant</th>
                      </tr>
                    </thead>
                    <tbody>
                      <xsl:for-each select="bank_notes/bank_note">
                        <tr>
                          <td>
                            <xsl:value-of select="position()"/>
                            <input name="group_id" hidden="hidden" value="{group_id}"/>
                            <input name="book_id" hidden="hidden" value="{book_id}"/>
                          </td>
                          <td>
                            <xsl:value-of select="number"/>
                            <input name="note_number" hidden="hidden" value="{number}"/>
                          </td>
                          <xsl:if test="not(mean_type_id = 'CHEQUE')">
                            <td>
                              <xsl:value-of select="duedate_view"/>
                            </td>
                          </xsl:if>
                          <td>
                            <xsl:value-of select="beneficiary"/>
                          </td>
                          <td>
                            <xsl:value-of select="amount_in_human"/>
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
              <button type="submit" class="btn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-primary">
                <xsl:text>Payer </xsl:text>
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
