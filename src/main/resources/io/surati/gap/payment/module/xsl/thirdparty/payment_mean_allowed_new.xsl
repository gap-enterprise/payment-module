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
  <xsl:include href="/io/surati/gap/web/base/xsl/dashboard/layout.xsl"/>
  <xsl:template match="page" mode="head">
    <title>
      <xsl:text>GAP - Tiers - Autoriser un moyen de paiement</xsl:text>
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
            <xsl:text>Tiers</xsl:text>
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
                    <a href="/third-party/view?id={tp/id}">
                      <xsl:value-of select="tp/name"/>
                    </a>
                  </li>
                  <li class="active breadcrumb-item" aria-current="page">
                    <xsl:text>Autoriser un moyen de paiement</xsl:text>
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
          <form action="/third-party/payment-mean-allowed/add" method="post">
            <div class="form-row">
              <div class="col-md-6">
                <div class="position-relative form-group">
                  <label for="mean_type_id" class="">
                    <xsl:text>Moyen de paiement à autoriser</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <select id="mean_type_id" name="mean_type_id" class="form-control" required="">
                    <option value="">-- SVP choisir un moyen de paiement --</option>
                    <xsl:for-each select="payment_mean_types/payment_mean_type">
                      <option value="{id}">
                        <xsl:value-of select="name"/>
                      </option>
                    </xsl:for-each>
                  </select>
                  <input type="hidden" name="tp_id" value="{tp/id}"/>
                </div>
              </div>
            </div>
            <div class="divider"/>
            <div class="clearfix">
              <button type="submit" class="btn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-primary">
                <xsl:text>Autoriser </xsl:text>
                <i class="fa fa-plus"/>
              </button>
              <button type="button" class="btn-shadow float-right btn-wide btn-pill mr-1 btn btn-outline-secondary" onclick="location.href='/third-party/view?id={tp/id}'">
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
