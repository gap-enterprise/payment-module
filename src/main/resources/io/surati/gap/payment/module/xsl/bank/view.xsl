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
      <xsl:text>GAP - Banques</xsl:text>
    </title>
  </xsl:template>
  <xsl:template match="page" mode="header">
    <div class="app-page-title app-page-title-simple">
      <div class="page-title-wrapper">
        <div class="page-title-heading">
          <div class="page-title-icon">
            <i class="lnr-apartment icon-gradient bg-night-fade"/>
          </div>
          <div>
            <xsl:text>Banques</xsl:text>
            <div class="page-title-subheading opacity-10">
              <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                  <li class="breadcrumb-item">
                    <a href="/home">
                      <i aria-hidden="true" class="fa fa-home"/>
                    </a>
                  </li>
                  <li class="breadcrumb-item">
                    <a href="/bank">Banques</a>
                  </li>
                  <li class="active breadcrumb-item" aria-current="page">
                    <xsl:text>Banque </xsl:text>
                    <xsl:value-of select="item/abbreviated"/>
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
            <div class="col-md-6">
              <h5>
                <xsl:text>Code banque</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/code"/>
              </p>
            </div>
            <div class="col-md-6">
              <h5>
                <xsl:text>Abrégé</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/abbreviated"/>
              </p>
            </div>
            <div class="col-md-6">
              <h5>
                <xsl:text>Intitulé</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/name"/>
              </p>
            </div>
            <div class="col-md-6">
              <h5>
                <xsl:text>Siège social</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/headquarters"/>
              </p>
            </div>
            <div class="col-md-6">
              <h5>
                <xsl:text>Représentant</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/representative"/>
              </p>
            </div>
            <div class="col-md-6">
              <h5>
                <xsl:text>Civilité du représentant</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/representative_civility"/>
              </p>
            </div>
            <div class="col-md-6">
              <h5>
                <xsl:text>Poste du représentant</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/representative_position"/>
              </p>
            </div>
            <div class="col-md-12">
              <div class="card-header">
                <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
                  <xsl:text>Moyens de paiement</xsl:text>
                </div>
              </div>
              <div class="table-responsive">
                <table class=" mb-0 table table-hover table-sm">
                  <thead>
                    <tr>
                      <th>N°</th>
                      <th>Libellé</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <xsl:for-each select="payment_means/payment_mean">
                      <tr>
                        <td>
                          <xsl:value-of select="position()"/>
                        </td>
                        <td>
                          <xsl:value-of select="name"/>
                        </td>
                        <td>
                          <div role="group">
                            <a href="/bank/payment-mean/view?id={id}&amp;bank={../../item/id}" class="mb-2 mr-2 btn btn-sm btn-outline-primary">
                              <i class="fa fa-eye"/>
                            </a>
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
            <a href="/bank" class="btn-shadow float-right btn-wide btn-pill btn btn-outline-secondary">
              <xsl:text>Retourner </xsl:text>
              <i class="fa fa-arrow-left"/>
            </a>
            <xsl:if test="sec:hasAccess(.,'CONFIGURER_BANQUES')">
              <a href="/bank/edit" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-success">
                <xsl:text>Nouveau </xsl:text>
                <i class="fa fa-file"/>
              </a>
              <a href="/bank/delete?id={item/id}" class="btn-shadow btn-wide float-right mr-1 btn-pill btn-hover-shine btn btn-danger" onclick="return confirm('Voulez-vous supprimer cette banque ?');">
                <xsl:text>Supprimer </xsl:text>
                <i class="fa fa-trash"/>
              </a>
              <a href="/bank/edit?id={item/id}" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-primary">
                <xsl:text>Modifier </xsl:text>
                <i class="fa fa-edit"/>
              </a>
            </xsl:if>
          </div>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="custom-script"/>
</xsl:stylesheet>
