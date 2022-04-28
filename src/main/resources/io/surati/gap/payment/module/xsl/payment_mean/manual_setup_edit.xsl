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
      <xsl:text>GAP - Moyen de paiement</xsl:text>
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
            <xsl:text>Moyen de paiement</xsl:text>
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
                      <li class="active breadcrumb-item" aria-current="page">
                        <xsl:text>Configurer manuellement</xsl:text>
                      </li>
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
                        <a href="/bank">Banques</a>
                      </li>
                      <li class="breadcrumb-item">
                        <a href="/bank/view?id={item/bank_id}">
                          <xsl:text>Banque </xsl:text>
                          <xsl:value-of select="item/bank"/>
                        </a>
                      </li>
                      <li class="breadcrumb-item">
                        <a href="/bank/payment-mean/view?id={item/id}&amp;bank={item/bank_id}">
                          <xsl:value-of select="item/name"/>
                        </a>
                      </li>
                      <li class="active breadcrumb-item" aria-current="page">
                        <xsl:text>Configurer manuellement</xsl:text>
                      </li>
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
    <div class="main-card mb-3 card">
      <div class="card">
        <div class="card-body">
          <form action="/bank/payment-mean/manual-setup/save" method="post">
            <input name="id" type="hidden" value="{item/id}"/>
            <input name="bank_id" type="hidden" value="{item/bank_id}"/>
            <div class="form-row">
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="width" class="">
                    <xsl:text>Largeur (cm)</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <input name="width" id="width" value="{item/width}" type="number" class="form-control" step="any" min="0" required=""/>
                </div>
              </div>
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="height" class="">
                    <xsl:text>Hauteur (cm)</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <input name="height" id="height" value="{item/height}" type="number" class="form-control" step="any" min="0" required=""/>
                </div>
              </div>
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="height" class="">
                    <xsl:text>DPI</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <input name="dpi" id="dpi" value="{item/dpi}" type="number" class="form-control" min="72" required=""/>
                </div>
              </div>
              <div class="col-md-12">
                <div class="card-header">
                  <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
                    <xsl:text>Champs</xsl:text>
                  </div>
                </div>
                <div class="table-responsive">
                  <table class=" mb-0 table table-hover table-sm">
                    <thead>
                      <tr>
                        <th>N°</th>
                        <th>Libellé</th>
                        <th>X</th>
                        <th>Y</th>
                        <th>Largeur (cm)</th>
                        <th>Visible</th>
                      </tr>
                    </thead>
                    <tbody>
                      <xsl:for-each select="payment_mean_fields/payment_mean_field">
                        <tr>
                          <td>
                            <input name="field_type_id" type="hidden" value="{type_id}"/>
                            <xsl:value-of select="position()"/>
                          </td>
                          <td>
                            <xsl:value-of select="name"/>
                          </td>
                          <td>
                            <input name="x" value="{x}" type="number" class="form-control-xs" step="any" min="0" required=""/>
                          </td>
                          <td>
                            <input name="y" value="{y}" type="number" class="form-control-xs" step="any" min="0" required=""/>
                          </td>
                          <td>
                            <input name="fwidth" value="{width}" type="number" class="form-control-xs" step="any" min="0" required=""/>
                          </td>
                          <td class="text-center">
                            <input name="visible" type="checkbox" class="form-control-xs">
                              <xsl:if test="visible = 'true'">
                                <xsl:attribute name="checked">checked</xsl:attribute>
                              </xsl:if>
                            </input>
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
                <xsl:text>Enregistrer </xsl:text>
                <i class="fa fa-check"/>
              </button>
              <button type="button" class="btn-shadow float-right btn-wide btn-pill mr-1 btn btn-outline-secondary" onclick="location.href='/bank/payment-mean/view?id={item/id}&amp;bank={item/bank_id}'">
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
