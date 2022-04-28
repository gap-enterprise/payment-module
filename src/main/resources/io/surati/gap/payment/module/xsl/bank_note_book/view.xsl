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
      <xsl:text>GAP - Carnet de formules</xsl:text>
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
            <xsl:text>Carnet de formules</xsl:text>
            <div class="page-title-subheading opacity-10">
              <nav class="" aria-label="breadcrumb">
                <ol class="breadcrumb">
                  <li class="breadcrumb-item">
                    <a href="/home">
                      <i aria-hidden="true" class="fa fa-home"/>
                    </a>
                  </li>
                  <li class="breadcrumb-item">
                    <a href="/bank-note-book">Mes carnets de formules</a>
                  </li>
                  <li class="active breadcrumb-item" aria-current="page">
                      Carnet <xsl:value-of select="item/name"/>
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
          <div class="row">
            <div class="col-md-4">
              <h5>
                <xsl:text>Banque</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/bank"/>
              </p>
            </div>
            <div class="col-md-4">
              <h5>
                <xsl:text>RIB</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/rib"/>
              </p>
            </div>
            <div class="col-md-4">
              <h5>
                <xsl:text>Type de carnet</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/mean_type"/>
              </p>
            </div>
            <div class="col-md-4">
              <h5>
                <xsl:text>Numéro de départ</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/start_number_view"/>
              </p>
            </div>
            <div class="col-md-4">
              <h5>
                <xsl:text>Numéro de fin</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/end_number_view"/>
              </p>
            </div>
            <div class="col-md-4">
              <h5>
                <xsl:text>Numéro courant</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/current_number"/>
              </p>
            </div>
            <div class="col-md-4">
              <h5>
                <xsl:text>Nombre de formules</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/number_of_leaves"/>
              </p>
            </div>
            <div class="col-md-4">
              <h5>
                <xsl:text>Formules tirées</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/number_of_leaves_used"/>
              </p>
            </div>
            <div class="col-md-4">
              <h5>
                <xsl:text>Etat</xsl:text>
              </h5>
              <p class="badge badge-primary">
                <xsl:value-of select="item/status"/>
              </p>
            </div>
          </div>
          <div class="divider"/>
          <div class="clearfix">
            <a href="/bank-note-book" class="btn-shadow float-right btn-wide btn-pill btn btn-outline-secondary">
              <xsl:text>Retourner </xsl:text>
              <i class="fa fa-arrow-left"/>
            </a>
            <xsl:if test="sec:hasAccess(.,'PREPARER_CARNETS')">
              <a href="/bank-note-book/edit" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-success">
                <xsl:text>Nouveau </xsl:text>
                <i class="fa fa-file"/>
              </a>
              <a href="/bank-note-book/delete?id={item/id}" class="btn-shadow btn-wide float-right mr-1 btn-pill btn-hover-shine btn btn-danger" onclick="return confirm('Voulez-vous supprimer ce carnet ?');">
                <xsl:text>Supprimer </xsl:text>
                <i class="fa fa-trash"/>
              </a>
              <a href="/bank-note-book/edit?id={item/id}" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-primary">
                <xsl:text>Modifier </xsl:text>
                <i class="fa fa-edit"/>
              </a>
            </xsl:if>
            <xsl:if test="sec:hasAccess(.,'METTRE_EN_UTILISATION_CARNETS')">
              <xsl:if test="item/status_id = 'REGISTERED' or item/status_id = 'BLOCKED'">
                <a href="/bank-note-book/use?id={item/id}" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-success" onclick="return confirm('Voulez-vous mettre ce carnet en utilisation ?');">
                  <xsl:text>Mettre en utilisation </xsl:text>
                  <i class="fa fa-road"/>
                </a>
              </xsl:if>
            </xsl:if>
            <xsl:if test="sec:hasAccess(.,'BLOQUER_CARNETS')">
              <xsl:if test="item/status_id = 'IN_USE'">
                <a href="/bank-note-book/block?id={item/id}" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-danger" onclick="return confirm('Voulez-vous bloquer ce carnet ?');">
                  <xsl:text>Bloquer </xsl:text>
                  <i class="fa fa-minus-circle"/>
                </a>
              </xsl:if>
            </xsl:if>
          </div>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="custom-script"/>
</xsl:stylesheet>
