<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <xsl:output method="html" include-content-type="no" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
  <xsl:include href="/xsl/dashboard/layout.xsl"/>
  <xsl:template match="page" mode="dashboard">
    <div class="row">
      <div class="col-md-12">
        <div class="main-card mb-3 card">
          <div class="card-header">Vos comptes bancaires</div>
          <xsl:if test="bank_accounts[not(bank_account)]">
            <h6 class="text-center pb-1 pt-1">
              <xsl:text>Il n'y a aucun compte bancaire.</xsl:text>
            </h6>
          </xsl:if>
          <xsl:if test="bank_accounts[bank_account]">
            <div class="table-responsive">
              <table class="align-middle mb-0 table table-borderless table-striped table-hover">
                <thead>
                  <tr>
                    <th class="text-center">N&#xB0;</th>
                    <th>Banque</th>
                    <th>RIB</th>
                  </tr>
                </thead>
                <tbody>
                  <xsl:for-each select="bank_accounts/bank_account">
                    <tr>
                      <td class="text-center text-muted">
                        <xsl:value-of select="position()"/>
                      </td>
                      <td>
                        <xsl:value-of select="bank"/>
                      </td>
                      <td>
                        <xsl:value-of select="rib"/>
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
