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
  <xsl:include href="/xsl/dashboard/layout.xsl"/>
  <xsl:template match="page" mode="dashboard">
    <div class="row">
      <div class="col-md-6 col-lg-3">
        <div class="widget-chart widget-chart2 text-left mb-3 card-btm-border card-shadow-primary border-primary card">
          <div class="widget-chat-wrapper-outer">
            <div class="widget-chart-content">
              <div class="widget-title opacity-5 text-uppercase">Répertorié</div>
              <div class="widget-numbers mt-2 fsize-4 mb-0 w-100">
                <div class="widget-chart-flex align-items-center">
                  <div>
                    <span class="opacity-10 text-success pr-5">
                      <i/>
                    </span>
                    <xsl:value-of select="nb_registered"/>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-6 col-lg-3">
        <div class="widget-chart widget-chart2 text-left mb-3 card-btm-border card-shadow-warning border-warning card">
          <div class="widget-chat-wrapper-outer">
            <div class="widget-chart-content">
              <div class="widget-title opacity-5 text-uppercase">En utilisation</div>
              <div class="widget-numbers mt-2 fsize-4 mb-0 w-100">
                <div class="widget-chart-flex align-items-center">
                  <div>
                    <xsl:value-of select="nb_in_use"/>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-6 col-lg-3">
        <div class="widget-chart widget-chart2 text-left mb-3 card-btm-border card-shadow-danger border-danger card">
          <div class="widget-chat-wrapper-outer">
            <div class="widget-chart-content">
              <div class="widget-title opacity-5 text-uppercase">Bloqué</div>
              <div class="widget-numbers mt-2 fsize-4 mb-0 w-100">
                <div class="widget-chart-flex align-items-center">
                  <div>
                    <span class="opacity-10 text-danger pr-5">
                      <i class=""/>
                    </span>
                    <xsl:value-of select="nb_blocked"/>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-6 col-lg-3">
        <div class="widget-chart widget-chart2 text-left mb-3 card-btm-border card-shadow-success border-success card">
          <div class="widget-chat-wrapper-outer">
            <div class="widget-chart-content">
              <div class="widget-title opacity-5 text-uppercase">Terminé</div>
              <div class="widget-numbers mt-2 fsize-4 mb-0 w-100">
                <div class="widget-chart-flex align-items-center">
                  <div>
                    <small class="text-success pr-1"/>
                    <xsl:value-of select="nb_finished"/>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <div class="main-card mb-3 card">
          <div class="card-header">Carnets en utilisation</div>
          <xsl:if test="bank_note_books[not(bank_note_book)]">
            <h6 class="text-center pb-1 pt-1">
              <xsl:text>Il n'y a aucun carnet en utilisation.</xsl:text>
            </h6>
          </xsl:if>
          <xsl:if test="bank_note_books[bank_note_book]">
            <div class="table-responsive">
              <table class="align-middle mb-0 table table-borderless table-striped table-hover">
                <thead>
                  <tr>
                    <th class="text-center">N°</th>
                    <th>Carnet</th>
                    <th>Type</th>
                    <th class="text-center">Total</th>
                    <th class="text-center">Restantes</th>
                    <th class="text-center">Courante</th>
                  </tr>
                </thead>
                <tbody>
                  <xsl:for-each select="bank_note_books/bank_note_book">
                    <tr>
                      <td class="text-center text-muted">
                        <xsl:value-of select="position()"/>
                      </td>
                      <td>
                        <xsl:value-of select="name"/>
                      </td>
                      <td>
                        <xsl:value-of select="mean_type"/>
                      </td>
                      <td class="text-center"><xsl:value-of select="number_of_leaves"/> formules</td>
                      <td class="text-center">
                        <div class="badge badge-success"><xsl:value-of select="number_of_leaves_left"/> formules</div>
                      </td>
                      <td class="text-center">
                        <xsl:value-of select="current_number"/>
                      </td>
                    </tr>
                  </xsl:for-each>
                </tbody>
              </table>
            </div>
          </xsl:if>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="custom-script"/>
</xsl:stylesheet>
