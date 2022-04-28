<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <xsl:output method="html" include-content-type="no" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
  <xsl:include href="/io/surati/gap/web/base/xsl/layout.xsl"/>
  <xsl:template match="page" mode="head">
    <title>
      <xsl:text>GAP - Paiements</xsl:text>
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
                    <a href="/root_page/uri">
                      <xsl:value-of select="root_page/subtitle"/>
                    </a>
                  </li>
                  <li class="breadcrumb-item">
                    <a href="/payment/view?id={item/id}&amp;{root_page/full}">
                      <xsl:value-of select="item/note"/>
                    </a>
                  </li>
                  <li class="active breadcrumb-item" aria-current="page">
                    <xsl:text>Annuler un paiement</xsl:text>
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
          <form action="/payment/cancel/save?{root_page/full}" method="post">
            <input name="id" type="hidden" value="{item/id}"/>
            <div class="form-row">
              <div class="col-md-8">
                <div class="position-relative form-group">
                  <label for="reason_id" class="">
                    <xsl:text>Motif</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <select name="reason_id" class="form-control" required="">
                    <option value="">-- SVP choisir un motif --</option>
                    <xsl:for-each select="bank_note_cancel_reasons/bank_note_cancel_reason">
                      <option value="{id}">
                        <xsl:value-of select="name"/>
                      </option>
                    </xsl:for-each>
                  </select>
                </div>
              </div>
              <div class="col-md-3">
                <div class="position-relative form-group">
                  <label for="cancel_date">
                    <xsl:text>Date d'annulation</xsl:text>
                    <span style="color: red"> *</span>
                  </label>
                  <div class="input-group">
                    <input name="cancel_date" id="cancel_date" value="{today}" type="date" class="form-control" required=""/>
                    <div class="input-group-append">
                      <button onclick="document.getElementById('cancel_date').value = ''" class="btn btn-outline-secondary" type="button">X</button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-md-8">
                <div class="position-relative form-group">
                  <label for="description">
                    <xsl:text>Description</xsl:text>
                  </label>
                  <textarea name="description" id="description" rows="4" class="form-control" placeholder="Saisir ici une description de l'annulation..."/>
                </div>
              </div>
            </div>
            <div class="p-2">
              <ul class="todo-list-wrapper list-group list-group-flush">
                <li class="list-group-item">
                  <div class="todo-indicator bg-warning"/>
                  <div class="widget-content p-0">
                    <div class="widget-content-wrapper">
                      <div class="widget-content-left mr-2">
                        <div class="custom-checkbox custom-control">
                          <input type="checkbox" id="sendbackinpayment" name="sendbackinpayment" class="custom-control-input" checked=""/>
                          <label class="custom-control-label" for="sendbackinpayment"/>
                        </div>
                      </div>
                      <div class="widget-content-left">
                        <div class="widget-heading">Reprendre le paiement</div>
                      </div>
                    </div>
                  </div>
                </li>
              </ul>
            </div>
            <div class="divider"/>
            <div class="clearfix">
              <button type="submit" class="btn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-primary">
                <xsl:text>Continuer </xsl:text>
                <i class="fa fa-check"/>
              </button>
              <button type="button" class="btn-shadow float-right btn-wide btn-pill mr-1 btn btn-outline-secondary" onclick="location.href='/payment/view?id={item/id}&amp;{root_page/full}'">
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
