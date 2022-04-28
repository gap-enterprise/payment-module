<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <xsl:output method="html" include-content-type="no" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
  <xsl:include href="/xsl/dashboard/layout.xsl"/>
  <xsl:template match="page" mode="dashboard">
    <div class="row">
      <div class="col-md-6 col-lg-3">
        <div class="widget-chart widget-chart2 text-left mb-3 card-btm-border card-shadow-primary border-primary card">
          <div class="widget-chat-wrapper-outer">
            <div class="widget-chart-content">
              <div class="widget-title opacity-5 text-uppercase">Factures en attente</div>
              <div class="widget-numbers mt-2 fsize-4 mb-0 w-100">
                <div class="widget-chart-flex align-items-center">
                  <div>
                    <span class="opacity-10 text-success pr-5">
                      <i/>
                    </span>
                    <xsl:value-of select="nb_orders_to_authorize"/>
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
              <div class="widget-title opacity-5 text-uppercase">Montant &#xE0; r&#xE9;gler</div>
              <div class="widget-numbers mt-2 fsize-4 mb-0 w-100">
                <div class="widget-chart-flex align-items-center">
                  <div>
                    <xsl:value-of select="amount_to_authorize"/>
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
              <div class="widget-title opacity-5 text-uppercase">Ordres &#xE0; ex&#xE9;cuter</div>
              <div class="widget-numbers mt-2 fsize-4 mb-0 w-100">
                <div class="widget-chart-flex align-items-center">
                  <div>
                    <span class="opacity-10 text-danger pr-5">
                      <i class=""/>
                    </span>
                    <xsl:value-of select="nb_orders_to_execute"/>
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
              <div class="widget-title opacity-5 text-uppercase">Montant &#xE0; ex&#xE9;cuter</div>
              <div class="widget-numbers mt-2 fsize-4 mb-0 w-100">
                <div class="widget-chart-flex align-items-center">
                  <div>
                    <small class="text-success pr-1"/>
                    <xsl:value-of select="amount_to_execute"/>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="custom-script"/>
</xsl:stylesheet>
