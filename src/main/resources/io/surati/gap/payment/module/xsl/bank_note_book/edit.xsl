<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <xsl:output method="html" include-content-type="no" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
  <xsl:include href="/io/surati/gap/web/base/xsl/layout.xsl"/>
  <xsl:template match="page" mode="head">
    <title>
      <xsl:text>GAP - Carnet de formules</xsl:text>
    </title>
  </xsl:template>
  <xsl:template match="page" mode="header">
    <xsl:variable name="is_new" select="not(item/id)"/>
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
                  <xsl:choose>
                    <xsl:when test="$is_new">
                      <li class="active breadcrumb-item" aria-current="page">
                        <xsl:text>Cr&#xE9;er un carnet de formules</xsl:text>
                      </li>
                    </xsl:when>
                    <xsl:otherwise>
                      <li class="active breadcrumb-item" aria-current="page">
                        <xsl:text>Modifier un carnet de formules</xsl:text>
                      </li>
                    </xsl:otherwise>
                  </xsl:choose>
                </ol>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="body">
    <xsl:variable name="is_new" select="not(item/id)"/>
    <div class="main-card mb-3 card">
      <div class="card">
        <div class="card-body">
          <form action="/bank-note-book/save" method="post">
            <xsl:if test="not($is_new)">
              <input name="id" type="hidden" value="{item/id}"/>
            </xsl:if>
            <div class="form-row">
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="account_id" class="">
                    <xsl:text>Compte bancaire</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <xsl:choose>
                    <xsl:when test="$is_new">
                      <select name="account_id" class="form-control" required="">
                        <option value="">-- SVP choisir un compte --</option>
                        <xsl:for-each select="bank_accounts/bank_account">
                          <option value="{id}">
                            <xsl:value-of select="full_name"/>
                          </option>
                        </xsl:for-each>
                      </select>
                    </xsl:when>
                    <xsl:otherwise>
                      <p>
                        <xsl:value-of select="item/rib"/>
                      </p>
                    </xsl:otherwise>
                  </xsl:choose>
                </div>
              </div>
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="mean_type_id" class="">
                    <xsl:text>Type de moyen de paiement</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <xsl:choose>
                    <xsl:when test="$is_new">
                      <select name="mean_type_id" class="form-control" required="">
                        <option value="">-- SVP choisir un type --</option>
                        <xsl:for-each select="payment_mean_types/payment_mean_type">
                          <option value="{id}">
                            <xsl:value-of select="name"/>
                          </option>
                        </xsl:for-each>
                      </select>
                    </xsl:when>
                    <xsl:otherwise>
                      <p>
                        <xsl:value-of select="item/mean_type"/>
                      </p>
                    </xsl:otherwise>
                  </xsl:choose>
                </div>
              </div>
              <div class="col-md-4">
                <div class="position-relative form-group">
                  <label for="prefix_number">
                    <xsl:text>Pr&#xE9;fixe des num&#xE9;ros</xsl:text>
                  </label>
                  <input name="prefix_number" id="prefix_number" value="{item/prefix_number}" placeholder="Entrez un texte ..." type="text" class="form-control"/>
                </div>
              </div>
              <div class="col-md-4">
                <div class="position-relative form-group">
                  <label for="start_number">
                    <xsl:text>Num&#xE9;ro de d&#xE9;part</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <input name="start_number" id="start_number" value="{item/start_number}" placeholder="Entrez un num&#xE9;ro ..." type="text" class="form-control" required=""/>
                </div>
              </div>
              <div class="col-md-4">
                <div class="position-relative form-group">
                  <label for="end_number">
                    <xsl:text>Num&#xE9;ro de fin</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <input name="end_number" id="end_number" value="{item/end_number}" placeholder="Entrez un num&#xE9;ro ..." type="text" class="form-control" required=""/>
                </div>
              </div>
            </div>
            <div class="divider"/>
            <div class="clearfix">
              <button type="submit" class="btn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-primary">
                <xsl:choose>
                  <xsl:when test="$is_new">
                    <xsl:text>Cr&#xE9;er </xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:text>Modifier </xsl:text>
                  </xsl:otherwise>
                </xsl:choose>
                <i class="fa fa-check"/>
              </button>
              <button type="button" class="btn-shadow float-right btn-wide btn-pill mr-1 btn btn-outline-secondary">
                <xsl:choose>
                  <xsl:when test="$is_new">
                    <xsl:attribute name="onclick">
                      <xsl:text>location.href='/bank-note-book'</xsl:text>
                    </xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="onclick">
                      <xsl:text>location.href='/bank-note-book/view?id=</xsl:text>
                      <xsl:value-of select="item/id"/>
                      <xsl:text>'</xsl:text>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
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
