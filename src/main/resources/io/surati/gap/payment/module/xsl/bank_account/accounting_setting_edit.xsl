<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <xsl:output method="html" include-content-type="no" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
  <xsl:include href="/io/surati/gap/web/base/xsl/layout.xsl"/>
  <xsl:template match="page" mode="head">
    <title>
      <xsl:text>GAP - Mes comptes bancaires - Param&#xE8;tre comptable</xsl:text>
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
            <xsl:text>Param&#xE8;tre comptable</xsl:text>
            <div class="page-title-subheading opacity-10">
              <nav class="" aria-label="breadcrumb">
                <ol class="breadcrumb">
                  <li class="breadcrumb-item">
                    <a href="/home">
                      <i aria-hidden="true" class="fa fa-home"/>
                    </a>
                  </li>
                  <li class="breadcrumb-item">
                    <a href="/bank-account">Mes comptes comptables</a>
                  </li>
                  <li class="breadcrumb-item">
                    <a href="/bank-account/view?id={item/account_id}">
                      <xsl:value-of select="item/account"/>
                    </a>
                  </li>
                  <li class="active breadcrumb-item" aria-current="page">
                    <xsl:text>Modifier un param&#xE8;tre comptable</xsl:text>
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
          <form action="/bank-account/accounting-setting/save" method="post">
            <div class="form-row">
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="mean_type_id" class="">
                    <xsl:text>Mode de paiement</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <p>
                    <xsl:value-of select="item/mean_type"/>
                  </p>
                  <input name="mean_type_id" type="hidden" value="{item/mean_type_id}"/>
                  <input type="hidden" name="account_id" value="{item/account_id}"/>
                </div>
              </div>
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="journal_code" class="">
                    <xsl:text>Code journal</xsl:text>
                  </label>
                  <input name="journal_code" id="journal_code" value="{item/journal_code}" placeholder="Entrez un num&#xE9;ro de journal ..." type="text" class="form-control"/>
                </div>
              </div>
            </div>
            <div class="divider"/>
            <div class="clearfix">
              <button type="submit" class="btn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-primary">
                <xsl:text>Modifier </xsl:text>
                <i class="fa fa-check"/>
              </button>
              <button type="button" class="btn-shadow float-right btn-wide btn-pill mr-1 btn btn-outline-secondary">
                <xsl:attribute name="onclick">
                  <xsl:text>location.href='/bank-account/accounting-setting/view?meantype=</xsl:text>
                  <xsl:value-of select="item/mean_type_id"/>
                  <xsl:text>&amp;account=</xsl:text>
                  <xsl:value-of select="item/account_id"/>
                  <xsl:text>'</xsl:text>
                </xsl:attribute>
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
