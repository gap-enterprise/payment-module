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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sec="http://www.surati.io/Security/User/Profile" version="2.0">
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
                  <li class="active breadcrumb-item" aria-current="page">
                    <xsl:value-of select="item/name"/>
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
        <div class="card-body" ng-controller="printCtrl">
          <div class="row">
            <div class="col-md-12">
              <h5>
                <xsl:text>Banque</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/bank"/>
              </p>
            </div>
            <div class="col-md-4">
              <h5>
                <xsl:text>Libellé</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/name"/>
              </p>
            </div>
            <div class="col-md-3">
              <h5>
                <xsl:text>Largeur</xsl:text>
              </h5>
              <p><xsl:value-of select="item/width"/> cm</p>
            </div>
            <div class="col-md-3">
              <h5>
                <xsl:text>Hauteur</xsl:text>
              </h5>
              <p><xsl:value-of select="item/height"/> cm</p>
            </div>
            <div class="col-md-2">
              <h5>
                <xsl:text>DPI</xsl:text>
              </h5>
              <p>
                <xsl:value-of select="item/dpi"/>
              </p>
            </div>
            <div class="col-md-12">
              <div class="card-header">
                <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
                  <xsl:text>Champs</xsl:text>
                </div>
                <xsl:if test="sec:hasAccess(.,'CONFIGURER_BANQUES')">
                  <div class="btn-actions-pane-right">
                    <a href="/bank/payment-mean/bank-note-image/edit?id={item/id}&amp;bank={item/bank_id}" type="button" class="btn-icon btn-wide btn-outline-2x btn btn-outline-focus btn-sm d-flex">
                      <xsl:text>Configurer image</xsl:text>
                      <span class="pl-2 align-middle opacity-7">
                        <i class="fa fa-cog"/>
                      </span>
                    </a>
                  </div>
                </xsl:if>
              </div>
              <div class="table-responsive">
                <table class=" mb-0 table table-hover table-sm">
                  <thead>
                    <tr>
                      <th>N°</th>
                      <th>Libellé</th>
                      <th>X</th>
                      <th>Y</th>
                      <th>Largeur</th>
                    </tr>
                  </thead>
                  <tbody>
                    <xsl:for-each select="payment_mean_fields/payment_mean_field">
                      <tr>
                        <xsl:if test="visible = 'false'">
                          <xsl:attribute name="class">opacity-3</xsl:attribute>
                        </xsl:if>
                        <td>
                          <xsl:value-of select="position()"/>
                        </td>
                        <td>
                          <xsl:value-of select="name"/>
                        </td>
                        <td>
                          <xsl:value-of select="x"/>
                        </td>
                        <td>
                          <xsl:value-of select="y"/>
                        </td>
                        <td><xsl:value-of select="width"/> cm
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
            <a href="/bank/view?id={item/bank_id}" class="btn-shadow float-right btn-wide btn-pill btn btn-outline-secondary">
              <xsl:text>Retourner </xsl:text>
              <i class="fa fa-arrow-left"/>
            </a>
            <xsl:if test="sec:hasAccess(.,'CONFIGURER_BANQUES')">
              <a href="/bank/payment-mean/manual-setup/edit?id={item/id}&amp;bank={item/bank_id}" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-primary">
                <xsl:text>Configurer manuellement </xsl:text>
                <i class="fa fa-edit"/>
              </a>
              <a href="/bank/payment-mean/graphical-setup/edit?id={item/id}&amp;bank={item/bank_id}" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-primary">
                <xsl:text>Configurer graphiquement </xsl:text>
                <i class="fa fa-edit"/>
              </a>
              <a ng-click="printSample()" ng-class="{{disabled: loadingData}}" href="javascript:void(0)" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-primary">
                <xsl:text>Tester impression </xsl:text>
                <i class="fa fa-print"/>
              </a>
              <a href="/bank/payment-mean/bank-note-image/view?id={item/id}&amp;bank={item/bank_id}" class="btn-shadow btn-wide float-right btn-pill mr-1 btn-hover-shine btn btn-primary">
                <xsl:text>Tester rendu </xsl:text>
                <i class="fa fa-eye"/>
              </a>
            </xsl:if>
          </div>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="custom-script">
    <script src="/js/print.min.js"/>
    <script type="text/javascript"><![CDATA[
        var app = angular.module("app", []);
				                 
		app.controller("printCtrl", ["$scope", "$rootScope", "$timeout", "$http", function ($scope, $rootScope, $timeout, $http) {
			
	        // Methods
	        $scope.printSample = printSample;
	        
	        function printSample() {
	            render('/bank/payment-mean/print/test?mean=]]><xsl:value-of select="item/id"/><![CDATA[&bank=]]><xsl:value-of select="item/bank_id"/><![CDATA[', { }, function (url) {
	                printJS({printable:url, type:'pdf', showModal:false})
	                $scope.loadingData = false;
	            });            
	        }
	        
			function buildReport(url, data, success, failure) {
			
			    var config = {
	                headers : {
	                    'Content-Type': 'application/json;charset=utf-8;'
	                },
	                responseType: "arraybuffer"
	            };
	            
	            return $http.post(url, data, config).
                      then(function (response) {
                          if(success)
                            success(response.data);
                      }, function (error, status) {
                          if (error.status == -1) {
                              toastr.error('Connexion au serveur momentanément interrompue.');
                          } else if (error.status == '401') {
                              toastr.error(error);
                          } else if(error.status == '400') {
                          	toastr.error(error);
                          } else if (error.status == '500') {
                              toastr.error("An error occured during generation of report. Please contact administrator.");
                          } 
                          
                          if(failure)
                              failure(error);
                      });
	        }
        
	        function render(url, config, callback) {
	            $scope.loadingData = true;
								
	            buildReport(url, config, function (arraybuffer) {	                	
                    var currentBlob = new Blob([arraybuffer], { type: 'application/pdf' });                    
                    var url = URL.createObjectURL(currentBlob);	                   

                    if (callback)
                        callback(url);	                     	                    
                }, function (response) {
                    $scope.loadingData = false;
                    toastr.error("Error during generating report : " + response.statusText);
                });           
	        }
	        
	        this.$onInit = function () {
   			
	        }
		}]);
		
		angular.bootstrap(document, ['app']);
        ]]></script>
  </xsl:template>
</xsl:stylesheet>
