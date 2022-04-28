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
      <xsl:text>GAP - Import Ordres de paiement</xsl:text>
    </title>
  </xsl:template>
  <xsl:template match="page" mode="header">
    <div class="app-page-title app-page-title-simple">
      <div class="page-title-wrapper">
        <div class="page-title-heading">
          <div class="page-title-icon">
            <i class="lnr-pointer-left icon-gradient bg-night-fade"/>
          </div>
          <div>
            <xsl:value-of select="root_page/title"/>
            <div class="page-title-subheading opacity-10">
              <nav class="" aria-label="breadcrumb">
                <ol class="breadcrumb">
                  <li class="breadcrumb-item">
                    <a href="/home">
                      <i aria-hidden="true" class="fa fa-home"/>
                    </a>
                  </li>
                  <li class="breadcrumb-item">
                    <a href="{root_page/uri}">
                      <xsl:value-of select="root_page/subtitle"/>
                    </a>
                  </li>
                  <li class="active breadcrumb-item">
                    Importer des ordres de paiement
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
        <form class="card" action="/reference-document/import/save?{root_page/full}" method="post" enctype="multipart/form-data">
          <div class="card-body">
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label class="form-label">Charger ici le fichier des documents de référence à payer <span style="color: red">*</span></label>
                  <input name="file" type="file" class="form-control" accept=".csv,.xls,.xlsx" placeholder="Choisir un fichier CSV, XLS ou XLSX" required=""/>
                </div>
              </div>
              <div class="col-md-12">
                <div class="alert alert-primary">
              		Nous acceptons les formats suivants :
              		<ul><li><b>XLS</b> et <b>XLSX</b> (Télécharger ce <a href="/io/surati/gap/payment/module/xls/modele_fichier_import_document_reference.xlsx" class="alert-link">fichier modèle</a>)</li><li><b>CSV</b> avec ; comme délimiteur (Télécharger ce <a href="/io/surati/gap/payment/module/csv/modele_fichier_import_document_reference.csv" class="alert-link">fichier modèle</a>)</li></ul>
              		</div>
              </div>
            </div>
            <div class="divider"/>
            <div class="clearfix">
              <a href="{root_page/uri}" class="btn-shadow float-right btn-wide btn-pill btn btn-outline-secondary">
                <xsl:text>Retourner </xsl:text>
                <i class="fa fa-arrow-left"/>
              </a>
              <button type="submit" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-primary">
                <xsl:text>Exécuter </xsl:text>
                <i class="fa fa-cog"/>
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="custom-script"/>
</xsl:stylesheet>
