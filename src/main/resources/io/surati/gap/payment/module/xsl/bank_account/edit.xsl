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
      <xsl:text>GAP - Compte bancaire</xsl:text>
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
            <xsl:text>Compte bancaire</xsl:text>
            <xsl:choose>
              <xsl:when test="holder_id">
                <div class="page-title-subheading opacity-10">
                  <nav class="" aria-label="breadcrumb">
                    <ol class="breadcrumb">
                      <li class="breadcrumb-item">
                        <a href="/home">
                          <i aria-hidden="true" class="fa fa-home"/>
                        </a>
                      </li>
                      <li class="breadcrumb-item">
                        <a href="/third-party">Tiers</a>
                      </li>
                      <li class="breadcrumb-item">
                        <a href="/third-party/view?id={holder_id}">
                          <xsl:value-of select="holder_name"/>
                        </a>
                      </li>
                      <xsl:choose>
                        <xsl:when test="$is_new">
                          <li class="active breadcrumb-item" aria-current="page">
                            <xsl:text>Créer un compte bancaire</xsl:text>
                          </li>
                        </xsl:when>
                        <xsl:otherwise>
                          <li class="active breadcrumb-item" aria-current="page">
                            <xsl:text>Modifier un compte bancaire</xsl:text>
                          </li>
                        </xsl:otherwise>
                      </xsl:choose>
                    </ol>
                  </nav>
                </div>
              </xsl:when>
              <xsl:otherwise>
                <div class="page-title-subheading opacity-10">
                  <nav class="" aria-label="breadcrumb">
                    <ol class="breadcrumb">
                      <li class="breadcrumb-item">
                        <a href="/home">
                          <i aria-hidden="true" class="fa fa-home"/>
                        </a>
                      </li>
                      <li class="breadcrumb-item">
                        <a href="/bank-account">Mes comptes bancaires</a>
                      </li>
                      <xsl:choose>
                        <xsl:when test="$is_new">
                          <li class="active breadcrumb-item" aria-current="page">
                            <xsl:text>Créer un compte bancaire</xsl:text>
                          </li>
                        </xsl:when>
                        <xsl:otherwise>
                          <li class="active breadcrumb-item" aria-current="page">
                            <xsl:text>Modifier un compte bancaire</xsl:text>
                          </li>
                        </xsl:otherwise>
                      </xsl:choose>
                    </ol>
                  </nav>
                </div>
              </xsl:otherwise>
            </xsl:choose>
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
          <form action="/bank-account/save" method="post">
            <xsl:if test="not($is_new)">
              <input name="id" type="hidden" value="{item/id}"/>
            </xsl:if>
            <div class="form-row">
              <div class="col-md-12">
                <div class="position-relative form-group">
                  <label for="bank_id" class="">
                    <xsl:text>Banque</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <xsl:choose>
                    <xsl:when test="$is_new">
                      <select name="bank_id" class="form-control" required="">
                        <option value="">-- SVP choisir une banque --</option>
                        <xsl:for-each select="banks/bank">
                          <option value="{id}">
                            <xsl:value-of select="name"/>
                          </option>
                        </xsl:for-each>
                      </select>
                    </xsl:when>
                    <xsl:otherwise>
                      <p>
                        <xsl:value-of select="item/bank"/>
                      </p>
                    </xsl:otherwise>
                  </xsl:choose>
                  <xsl:if test="holder_id">
                    <input type="hidden" name="holder_id" value="{holder_id}"/>
                  </xsl:if>
                </div>
              </div>
              <div class="col-md-3">
                <div class="position-relative form-group">
                  <label for="branch_code" class="">
                    <xsl:text>Code guichet (5 caractères)</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <input name="branch_code" id="branch_code" value="{item/branch_code}" placeholder="Entrez un code ..." type="text" class="form-control" minlength="5" maxlength="5" required=""/>
                </div>
              </div>
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="number" class="">
                    <xsl:text>Numéro (12 caractères)</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <input name="number" id="number" value="{item/number}" placeholder="Entrez un numéro ..." type="text" class="form-control" minlength="12" maxlength="12" required=""/>
                </div>
              </div>
              <div class="col-md-3">
                <div class="position-relative form-group">
                  <label for="key" class="">
                    <xsl:text>Clé (2 caractères)</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <input name="key" id="key" value="{item/key}" placeholder="Entrez une clé ..." type="text" class="form-control" minlength="2" maxlength="2" required=""/>
                </div>
              </div>
            </div>
            <div class="divider"/>
            <div class="clearfix">
              <button type="submit" class="btn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-primary">
                <xsl:choose>
                  <xsl:when test="$is_new">
                    <xsl:text>Créer </xsl:text>
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
                    <xsl:choose>
                      <xsl:when test="holder_id">
                        <xsl:attribute name="onclick">
                          <xsl:text>location.href='/third-party/view?id=</xsl:text>
                          <xsl:value-of select="holder_id"/>
                          <xsl:text>'</xsl:text>
                        </xsl:attribute>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="onclick">
                          <xsl:text>location.href='/bank-account'</xsl:text>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:choose>
                      <xsl:when test="holder_id">
                        <xsl:attribute name="onclick">
                          <xsl:text>location.href='/bank-account/view?holder=</xsl:text>
                          <xsl:value-of select="holder_id"/>
                          <xsl:text>&amp;id=</xsl:text>
                          <xsl:value-of select="item/id"/>
                          <xsl:text>'</xsl:text>
                        </xsl:attribute>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="onclick">
                          <xsl:text>location.href='/bank-account/view?id=</xsl:text>
                          <xsl:value-of select="item/id"/>
                          <xsl:text>'</xsl:text>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
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
