<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <xsl:output method="html" include-content-type="no" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
  <xsl:include href="/io/surati/gap/web/base/xsl/layout.xsl"/>
  <xsl:template match="page" mode="head">
    <title>
      <xsl:text>GAP - Banques</xsl:text>
    </title>
  </xsl:template>
  <xsl:template match="page" mode="header">
    <xsl:variable name="is_new" select="not(item/id)"/>
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
                    <xsl:choose>
                      <xsl:when test="$is_new">
                        <xsl:text>Cr&#xE9;er une Banque</xsl:text>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:text>Modifier Banque </xsl:text>
                        <xsl:value-of select="item/abbreviated"/>
                      </xsl:otherwise>
                    </xsl:choose>
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
    <xsl:variable name="is_new" select="not(item/id)"/>
    <div class="main-card mb-3 card">
      <div class="card">
        <div class="card-body">
          <form action="/bank/save" method="post">
            <xsl:if test="not($is_new)">
              <input name="id" type="hidden" value="{item/id}"/>
            </xsl:if>
            <div class="form-row">
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="code" class="">
                    <xsl:text>Code banque (5 caract&#xE8;res)</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <input name="code" id="code" value="{item/code}" placeholder="Entrez un code ..." type="text" class="form-control" minlength="5" maxlength="5" required=""/>
                </div>
              </div>
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="abbreviated" class="">
                    <xsl:text>Abr&#xE9;g&#xE9;</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <input name="abbreviated" id="abbreviated" value="{item/abbreviated}" placeholder="Entrez l'abr&#xE9;g&#xE9; ..." type="text" class="form-control" required=""/>
                </div>
              </div>
            </div>
            <div class="position-relative form-group">
              <label for="name" class="">
                <xsl:text>Intitul&#xE9;</xsl:text>
                <span style="color: red"> *</span>
              </label>
              <input name="name" id="name" value="{item/name}" placeholder="Entrez un nom ..." type="text" class="form-control" required=""/>
            </div>
            <div class="form-row">
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="headquarters">
                    <xsl:text>Si&#xE8;ge social</xsl:text>
                  </label>
                  <input name="headquarters" id="headquarters" value="{item/headquarters}" placeholder="Entrez un lieu ..." type="text" class="form-control"/>
                </div>
              </div>
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="representative">
                    <xsl:text>Repr&#xE9;sentant</xsl:text>
                  </label>
                  <input name="representative" id="representative" value="{item/representative}" placeholder="Entrez le repr&#xE9;sentant ..." type="text" class="form-control"/>
                </div>
              </div>
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="representative_civility">
                    <xsl:text>Civilit&#xE9; du repr&#xE9;sentant</xsl:text>
                  </label>
                  <input name="representative_civility" id="representative_civility" value="{item/representative_civility}" placeholder="Entrez une civilit&#xE9; ..." type="text" class="form-control"/>
                </div>
              </div>
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="representative_position">
                    <xsl:text>Poste du repr&#xE9;sentant</xsl:text>
                  </label>
                  <input name="representative_position" id="representative_position" value="{item/representative_position}" placeholder="Entrez un poste ..." type="text" class="form-control"/>
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
              <button type="button" onclick="location.href='/bank'" class="btn-shadow float-right btn-wide btn-pill mr-1 btn btn-outline-secondary">
                <xsl:if test="not($is_new)">
                  <xsl:attribute name="onclick">
                    <xsl:text>location.href='/bank/view?id=</xsl:text>
                    <xsl:value-of select="item/id"/>
                    <xsl:text>'</xsl:text>
                  </xsl:attribute>
                </xsl:if>
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
