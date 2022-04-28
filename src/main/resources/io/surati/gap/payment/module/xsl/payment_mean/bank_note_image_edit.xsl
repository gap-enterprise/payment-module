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
      <xsl:text>GAP - Moyen de paiement - Configurer image formule</xsl:text>
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
            <xsl:text>Configurer image formule</xsl:text>
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
                        <a href="/bank">Banques</a>
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
          <form action="/bank/payment-mean/bank-note-image/save" method="post" enctype="multipart/form-data">
            <input name="id" type="hidden" value="{item/id}"/>
            <input name="bank_id" type="hidden" value="{item/bank_id}"/>
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label class="form-label">Image à charger <span style="color: red">*</span></label>
                  <input type="file" id="bank_note_image" name="bank_note_image" class="form-control" accept="image/*" aria-describedby="bank_note_image" required=""/>
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
