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
      <xsl:text>GAP - Paiements - Choix de carnets</xsl:text>
    </title>
  </xsl:template>
  <xsl:template match="page" mode="header">
    <xsl:variable name="is_new" select="not(item/id)"/>
    <div class="app-page-title app-page-title-simple">
      <div class="page-title-wrapper">
        <div class="page-title-heading">
          <div class="page-title-icon">
            <i class="lnr-license icon-gradient bg-night-fade"/>
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
                  <li class="active breadcrumb-item" aria-current="page">
                    Choisir un carnet de formules
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
        <div class="card-header">
          <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
            <xsl:text>Choisir un carnet de formules à utiliser</xsl:text>
          </div>
        </div>
        <div class="card-body">
          <form action="/payment/batch/new/book/choose?{root_page/full}" method="post">
            <div class="position-relative form-group">
              <input type="hidden" name="account_id" value="{account/id}"/>
              <input type="hidden" name="mean_type_id" value="{mean_type/id}"/>
              <div class="p-2">
                <ul class="todo-list-wrapper list-group list-group-flush">
                  <xsl:for-each select="bank_note_books/bank_note_book">
                    <li class="list-group-item">
                      <div class="todo-indicator bg-warning"/>
                      <div class="widget-content p-0">
                        <div class="widget-content-wrapper">
                          <div class="widget-content-left mr-2">
                            <div class="custom-checkbox custom-control">
                              <input type="checkbox" id="book-{id}" name="book-{id}" class="custom-control-input">
                                <xsl:if test="position() = 1">
                                  <xsl:attribute name="checked">true</xsl:attribute>
                                </xsl:if>
                              </input>
                              <label class="custom-control-label" for="book-{id}"/>
                            </div>
                          </div>
                          <div class="widget-content-left">
                            <div class="widget-heading">
                              <xsl:value-of select="name_with_current_note"/>
                              <div class="badge badge-pill badge-success"><xsl:value-of select="number_of_leaves_left"/> formules</div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </li>
                  </xsl:for-each>
                </ul>
              </div>
            </div>
            <div class="divider"/>
            <div class="clearfix">
              <button type="submit" class="btn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-primary">
                <xsl:text>Choisir </xsl:text>
                <i class="fa fa-check"/>
              </button>
              <button type="button" onclick="location.href='{root_page/uri}'" class="btn-shadow float-right btn-wide btn-pill mr-1 btn btn-outline-secondary">
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
