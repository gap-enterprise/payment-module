<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sec="http://www.surati.io/Security/User/Profile" version="2.0">
  <xsl:output method="html" include-content-type="no" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
  <xsl:include href="/io/surati/gap/web/base/xsl/layout.xsl"/>
  <xsl:template match="page" mode="head">
    <title>
      <xsl:text>GAP - Compte bancaire</xsl:text>
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
            <xsl:text>Compte bancaire</xsl:text>
            <div class="page-title-subheading opacity-10">
              <nav class="" aria-label="breadcrumb">
                <ol class="breadcrumb">
                  <li class="breadcrumb-item">
                    <a href="/home">
                      <i aria-hidden="true" class="fa fa-home"/>
                    </a>
                  </li>
                  <xsl:choose>
                    <xsl:when test="holder">
                      <li class="breadcrumb-item">
                        <a href="/third-party">Tiers</a>
                      </li>
                      <li class="breadcrumb-item">
                        <a href="/third-party/view?id={holder_id}">
                          <xsl:value-of select="holder_name"/>
                        </a>
                      </li>
                    </xsl:when>
                    <xsl:otherwise>
                      <li class="breadcrumb-item">
                        <a href="/bank-account">Mes comptes bancaires</a>
                      </li>
                    </xsl:otherwise>
                  </xsl:choose>
                  <li class="active breadcrumb-item" aria-current="page">
                      Compte N&#xB0;<xsl:value-of select="item/rib"/>
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
            <div class="col-md-12">
              <h5>
                <xsl:text>Banque</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/bank"/>
              </p>
            </div>
            <div class="col-md-2">
              <h5>
                <xsl:text>Code guichet</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/branch_code"/>
              </p>
            </div>
            <div class="col-md-8">
              <h5>
                <xsl:text>Num&#xE9;ro</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/number"/>
              </p>
            </div>
            <div class="col-md-2">
              <h5>
                <xsl:text>Cl&#xE9;</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/key"/>
              </p>
            </div>
            <xsl:if test="not(holder)">
              <div class="col-md-12">
                <div class="card-header">
                  <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
                    <xsl:text>Param&#xE8;tres comptables</xsl:text>
                  </div>
                </div>
                <div class="table-responsive">
                  <table class=" mb-0 table table-hover table-sm">
                    <thead>
                      <tr>
                        <th>N&#xB0;</th>
                        <th>Mode de paiement</th>
                        <th>Code journal</th>
                        <th>Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      <xsl:for-each select="bank_account_accounting_settings/bank_account_accounting_setting">
                        <tr>
                          <td>
                            <xsl:value-of select="position()"/>
                          </td>
                          <td>
                            <xsl:value-of select="mean_type"/>
                          </td>
                          <td>
                            <xsl:value-of select="journal_code"/>
                          </td>
                          <td>
                            <div role="group">
                              <a href="/bank-account/accounting-setting/view?account={../../item/id}&amp;meantype={mean_type_id}" class="mb-2 mr-2 btn btn-sm btn-outline-primary">
                                <i class="fa fa-eye"/>
                              </a>
                              <xsl:if test="../../sec:hasAccess(.,'CONFIGURER_COMPTES_BANCAIRES')">
                                <a href="/bank-account/accounting-setting/edit?account={../../item/id}&amp;meantype={mean_type_id}" class="mb-2 mr-2 btn btn-sm btn-outline-success">
                                  <i class="fa fa-edit"/>
                                </a>
                              </xsl:if>
                            </div>
                          </td>
                        </tr>
                      </xsl:for-each>
                    </tbody>
                  </table>
                </div>
              </div>
            </xsl:if>
          </div>
          <div class="divider"/>
          <div class="clearfix">
            <xsl:choose>
              <xsl:when test="holder">
                <a href="/third-party/view?id={holder}" class="btn-shadow float-right btn-wide btn-pill btn btn-outline-secondary">
                  <xsl:text>Retourner </xsl:text>
                  <i class="fa fa-arrow-left"/>
                </a>
                <xsl:if test="sec:hasAccess(.,'CONFIGURER_TIERS')">
                  <a href="/bank-account/edit?holder={holder}" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-success">
                    <xsl:text>Nouveau </xsl:text>
                    <i class="fa fa-file"/>
                  </a>
                  <a href="/bank-account/delete?id={item/id}&amp;holder={holder}" class="btn-shadow btn-wide float-right mr-1 btn-pill btn-hover-shine btn btn-danger" onclick="return confirm('Voulez-vous supprimer ce tiers ?');">
                    <xsl:text>Supprimer </xsl:text>
                    <i class="fa fa-trash"/>
                  </a>
                  <a href="/bank-account/edit?id={item/id}&amp;holder={holder}" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-primary">
                    <xsl:text>Modifier </xsl:text>
                    <i class="fa fa-edit"/>
                  </a>
                </xsl:if>
              </xsl:when>
              <xsl:otherwise>
                <a href="/bank-account" class="btn-shadow float-right btn-wide btn-pill btn btn-outline-secondary">
                  <xsl:text>Retourner </xsl:text>
                  <i class="fa fa-arrow-left"/>
                </a>
                <xsl:if test="sec:hasAccess(.,'CONFIGURER_COMPTES_BANCAIRES')">
                  <a href="/bank-account/edit" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-success">
                    <xsl:text>Nouveau </xsl:text>
                    <i class="fa fa-file"/>
                  </a>
                  <a href="/bank-account/delete?id={item/id}" class="btn-shadow btn-wide float-right mr-1 btn-pill btn-hover-shine btn btn-danger" onclick="return confirm('Voulez-vous supprimer ce tiers ?');">
                    <xsl:text>Supprimer </xsl:text>
                    <i class="fa fa-trash"/>
                  </a>
                  <a href="/bank-account/edit?id={item/id}" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-primary">
                    <xsl:text>Modifier </xsl:text>
                    <i class="fa fa-edit"/>
                  </a>
                </xsl:if>
              </xsl:otherwise>
            </xsl:choose>
          </div>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="custom-script"/>
</xsl:stylesheet>
