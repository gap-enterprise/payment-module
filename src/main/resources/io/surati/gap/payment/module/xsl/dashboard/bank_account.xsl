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
                    <th class="text-center">N°</th>
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
