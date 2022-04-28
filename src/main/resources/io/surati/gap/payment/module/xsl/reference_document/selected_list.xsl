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
    <title><xsl:text>GAP - </xsl:text><xsl:value-of select="root_page/title"/> - <xsl:value-of select="root_page/subtitle"/>
    </title>
  </xsl:template>
  <xsl:template match="page" mode="header">
    <div class="app-page-title app-page-title-simple">
      <div class="page-title-wrapper">
        <div class="page-title-heading">
          <div class="page-title-icon">
            <i class="lnr-database icon-gradient bg-night-fade"/>
          </div>
          <div>
            <xsl:value-of select="root_page/title"/>
            <div class="page-title-subheading opacity-10">
              <nav aria-label="breadcrumb">
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
                  <li class="active breadcrumb-item">
                    Documents de référence sélectionnés
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
          <xsl:text>Documents de référence sélectionnés</xsl:text>
        </div>
        <div class="btn-actions-pane-right">
          <div class="row">
            <xsl:if test="sec:hasAccess(.,'EDITER_DOCUMENT_REF')">
              <xsl:if test="ref_docs[ref_doc]">
                <a href="/reference-document/selected/cancel?{root_page/full}" class="btn-icon btn-wide btn btn-danger btn-sm d-flex mr-1" onclick="return confirm('Souhaitez-vous annuler la sélection ?');">
                  <xsl:text>Annuler la sélection</xsl:text>
                  <span class="pl-2 align-middle opacity-7">
                    <i class="fa fa-minus"/>
                  </span>
                </a>
              </xsl:if>
            </xsl:if>
            <xsl:if test="sec:hasAccess(.,'ENVOYER_DOC_REF_EN_PREPARATION') and ref_docs[ref_doc]">
              <a href="/reference-document/selected/prepare?{root_page/full}" class="btn-icon btn-wide btn-outline-2x btn btn-outline-focus btn-sm d-flex" onclick="return confirm('Souhaitez-vous envoyer la sélection en préparation ?');">
                <xsl:text>Démarrer la préparation</xsl:text>
                <span class="pl-2 align-middle opacity-7">
                  <i class="fa fa-road"/>
                </span>
              </a>
            </xsl:if>
          </div>
        </div>
      </div>
      <xsl:if test="ref_docs[not(ref_doc)]">
        <h6 class="text-center pb-1 pt-1">
          <xsl:text>Il n'y a aucun document de référence sélectionné.</xsl:text>
        </h6>
      </xsl:if>
      <xsl:if test="ref_docs[ref_doc]">
        <div class="table-responsive">
          <table class=" mb-0 table table-hover table-sm">
            <thead>
              <tr>
                <th>N°</th>
                <th>Date</th>
                <th>Document</th>
                <th>Bénéficiaire</th>
                <th>Montant total</th>
                <th>Reste à payer</th>
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <xsl:for-each select="ref_docs/ref_doc">
                <tr>
                  <td>
                    <xsl:value-of select="position()"/>
                  </td>
                  <td>
                    <xsl:value-of select="date_view"/>
                  </td>
                  <td>
                    <xsl:value-of select="name"/>
                  </td>
                  <td>
                    <xsl:value-of select="beneficiary"/>
                  </td>
                  <td>
                    <xsl:value-of select="amount_in_human"/>
                  </td>
                  <td>
                    <xsl:choose>
                      <xsl:when test="amount_left &gt; 0">
                        <div class="badge badge-pill badge-success">
                          <xsl:value-of select="amount_left_in_human"/>
                        </div>
                      </xsl:when>
                      <xsl:otherwise>
                        <div class="badge badge-pill badge-danger">
                          <xsl:value-of select="amount_left_in_human"/>
                        </div>
                      </xsl:otherwise>
                    </xsl:choose>
                  </td>
                  <td>
                    <xsl:value-of select="status"/>
                  </td>
                  <td>
                    <div role="group">
                      <xsl:if test="../../sec:hasAccess(.,'EDITER_DOCUMENT_REF')">
                        <a href="/reference-document/deselect?id={id}&amp;{../../root_page/full}" class="mb-2 mr-2 btn btn-sm btn-danger"><i class="fa fa-minus"/> Retirer
	                    </a>
                      </xsl:if>
                    </div>
                  </td>
                </tr>
              </xsl:for-each>
            </tbody>
          </table>
        </div>
        <div class="d-block p-4 card-footer">
          <xsl:choose>
            <xsl:when test="total_amount &gt; 0">
              <div class="badge badge-pill badge-success">Total : <xsl:value-of select="total_amount_in_human"/></div>
            </xsl:when>
            <xsl:otherwise>
              <div class="badge badge-pill badge-danger">Total : <xsl:value-of select="total_amount_in_human"/></div>
            </xsl:otherwise>
          </xsl:choose>
        </div>
      </xsl:if>
      <div class="divider"/>
      <div class="clearfix">
        <a href="{root_page/uri}" class="btn-shadow float-right btn-wide btn-pill btn btn-outline-secondary">
          <xsl:text>Retourner </xsl:text>
          <i class="fa fa-arrow-left"/>
        </a>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="custom-script"/>
</xsl:stylesheet>
