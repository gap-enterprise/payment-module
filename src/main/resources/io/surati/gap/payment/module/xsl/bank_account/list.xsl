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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sec="http://www.surati.io/Security/User/Profile" version="2.0">
  <xsl:output method="html" include-content-type="no" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
  <xsl:include href="/io/surati/gap/web/base/xsl/layout.xsl"/>
  <xsl:template match="page" mode="head">
    <title>
      <xsl:text>GAP - Mes comptes bancaires</xsl:text>
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
            <xsl:text>Mes comptes bancaires</xsl:text>
            <div class="page-title-subheading opacity-10">
              <nav class="" aria-label="breadcrumb">
                <ol class="breadcrumb">
                  <li class="breadcrumb-item">
                    <a href="/home">
                      <i aria-hidden="true" class="fa fa-home"/>
                    </a>
                  </li>
                  <li class="active breadcrumb-item">
                    Mes comptes bancaires
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
    <div class="main-card mb-3 card card-body">
      <div class="card-header">
        <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
          <xsl:text>Liste des comptes bancaires</xsl:text>
        </div>
        <xsl:if test="sec:hasAccess(.,'CONFIGURER_COMPTES_BANCAIRES')">
          <div class="btn-actions-pane-right">
            <a href="/bank-account/edit" type="button" class="btn-icon btn-wide btn-outline-2x btn btn-outline-focus btn-sm d-flex">
              <xsl:text>Nouveau</xsl:text>
              <span class="pl-2 align-middle opacity-7">
                <i class="fa fa-plus"/>
              </span>
            </a>
          </div>
        </xsl:if>
      </div>
      <xsl:if test="bank_accounts[not(bank_account)]">
        <h6 class="text-center pb-1 pt-1">
          <xsl:text>Il n'y a aucun compte bancaire.</xsl:text>
        </h6>
      </xsl:if>
      <xsl:if test="bank_accounts[bank_account]">
        <div class="table-responsive">
          <table class=" mb-0 table table-hover table-sm">
            <thead>
              <tr>
                <th>N°</th>
                <th>Banque</th>
                <th>Code guichet</th>
                <th>Numéro</th>
                <th>Clé</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <xsl:for-each select="bank_accounts/bank_account">
                <tr>
                  <td>
                    <xsl:value-of select="position()"/>
                  </td>
                  <td>
                    <xsl:value-of select="bank"/>
                  </td>
                  <td>
                    <xsl:value-of select="branch_code"/>
                  </td>
                  <td>
                    <xsl:value-of select="number"/>
                  </td>
                  <td>
                    <xsl:value-of select="key"/>
                  </td>
                  <td>
                    <div role="group">
                      <a href="/bank-account/view?id={id}" class="mb-2 mr-2 btn btn-sm btn-outline-primary">
                        <i class="fa fa-eye"/>
                      </a>
                      <xsl:if test="../../sec:hasAccess(.,'CONFIGURER_COMPTES_BANCAIRES')">
                        <a href="/bank-account/edit?id={id}" class="mb-2 mr-2 btn btn-sm btn-outline-success">
                          <i class="fa fa-edit"/>
                        </a>
                        <a href="/bank-account/delete?id={id}" class="mb-2 mr-2 btn btn-sm btn-outline-danger" onclick="return confirm('Voulez-vous supprimer ce compte ?');">
                          <i class="fa fa-trash"/>
                        </a>
                      </xsl:if>
                    </div>
                  </td>
                </tr>
              </xsl:for-each>
            </tbody>
          </table>
        </div>
      </xsl:if>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="custom-script"/>
</xsl:stylesheet>
