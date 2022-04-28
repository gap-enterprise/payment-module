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
      <xsl:text>GAP - Paiements - Paiement par lots</xsl:text>
    </title>
    <link rel="stylesheet" href="/css/print.min.css"/>
    <link rel="stylesheet" href="/css/toastr.min.css"/>
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
                  <li class="active breadcrumb-item" aria-current="page">
                    <xsl:text>Paiement du lot </xsl:text>
                    <xsl:value-of select="item/id"/>
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
    <div class="main-card mb-3 card" ng-controller="printCtrl">
      <div class="card">
        <div class="card-header">
          <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
            <xsl:text>Lot de paiement </xsl:text>
            <xsl:value-of select="item/id"/>
          </div>
        </div>
        <div class="card-body">
          <div class="form-row">
            <div class="col-md-4">
              <div class="position-relative form-group">
                <label class="font-weight-bold">
                  <xsl:text>Compte</xsl:text>
                </label>
                <p>
                  <xsl:value-of select="item/account"/>
                </p>
              </div>
            </div>
            <div class="col-md-4">
              <div class="position-relative form-group">
                <label for="date" class="font-weight-bold">
                  <xsl:text>Date d'édition</xsl:text>
                  <span style="color: red"> *</span>
                </label>
                <p>
                  <xsl:value-of select="item/date_view"/>
                </p>
              </div>
            </div>
          </div>
          <div class="form-row">
            <div class="col-md-12">
              <div class="table-responsive">
                <table class="mb-0 table table-hover table-sm">
                  <thead>
                    <tr>
                      <th>N°</th>
                      <th><xsl:value-of select="item/mean_type"/> N°</th>
                      <xsl:if test="not(item/mean_type_id = 'CHEQUE')">
                        <th>Date d'échéance</th>
                      </xsl:if>
                      <th>Bénéficiaire</th>
                      <th>Montant</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <xsl:for-each select="item/notes/note">
                      <tr>
                        <td>
                          <xsl:value-of select="position()"/>
                        </td>
                        <td>
                          <xsl:value-of select="number"/>
                        </td>
                        <xsl:if test="not(mean_type_id = 'CHEQUE')">
                          <td>
                            <xsl:value-of select="duedate_view"/>
                          </td>
                        </xsl:if>
                        <td>
                          <xsl:value-of select="beneficiary"/>
                        </td>
                        <td>
                          <xsl:value-of select="amount_in_human"/>
                        </td>
                        <td>
                          <div role="group" class="btn-group-sm btn-group">
                            <a href="/payment/view?id={id}&amp;{../../../current_page/full}" class="btn-shadow btn btn-primary">Voir</a>
                          </div>
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
            <xsl:if test="sec:hasAccess(.,'EXECUTER_ORDRES_PAIEMENT')">
              <xsl:if test="item/status_id='TO_PRINT'">
              
              </xsl:if>
              <a class="btn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-primary" ng-click="printNotes()" ng-class="{{disabled: loadingData}}" href="javascript:void(0)">
                <xsl:text>Imprimer formules </xsl:text>
                <i class="fa fa-print"/>
              </a>
              <xsl:if test="item/status_id='TO_PRINT'">
              
              </xsl:if>
              <a class="btn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-primary mr-1" ng-class="{{disabled: loadingData}}" ng-click="printPaymentResumes()" href="javascript:void(0)">
                <xsl:text>Imprimer bordereaux </xsl:text>
                <i class="fa fa-print"/>
              </a>
              <xsl:if test="not(item/mean_type_id='CHEQUE') and item/status_id='TO_PRINT'">

              </xsl:if>
              <xsl:if test="not(item/mean_type_id='CHEQUE')">
                <a class="btn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-primary mr-1" ng-click="printLetter()" ng-class="{{disabled: loadingData}}" href="javascript:void(0)">
                  <xsl:text>Imprimer lettre </xsl:text>
                  <i class="fa fa-print"/>
                </a>
              </xsl:if>
            </xsl:if>
            <xsl:if test="sec:hasAccess(.,'EXECUTER_ORDRES_PAIEMENT')">
              <xsl:if test="item/status_id='TO_PRINT'">
                <a class="btn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-success mr-1" href="/batch/finalize?batchid={item/id}&amp;{root_page/full}" onclick="return confirm('Voulez-vous finaliser ce lot ?');">
                  <xsl:text>Finaliser </xsl:text>
                  <i class="fa fa-check"/>
                </a>
              </xsl:if>
              <a class="btn-shadow float-right btn-wide btn-pill mr-1 btn btn-outline-secondary" href="{root_page/uri}">
                <xsl:text>Retourner </xsl:text>
                <i class="fa fa-arrow-left"/>
              </a>
            </xsl:if>
          </div>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="custom-script">
    <script src="/io/surati/gap/web/base/js/print.min.js"/>
    <script type="text/javascript"><![CDATA[
        var app = angular.module("app", []);
				                 
		app.controller("printCtrl", ["$scope", "$rootScope", "$timeout", "$http", function ($scope, $rootScope, $timeout, $http) {
			
	        // Methods
	        $scope.printNotes = printNotes;
	        $scope.printPaymentResumes = printPaymentResumes;
	        $scope.printLetter = printLetter;
	        
	        function printNotes() {
	            render('/api/batch/bank-note/print?batch=]]><xsl:value-of select="item/id"/><![CDATA[', { }, function (url) {
	                printJS({printable:url, type:'pdf', showModal:false})
	                $scope.loadingData = false;
	            });            
	        }
	        
	        function printPaymentResumes() {
	            render('/api/batch/payment-resume/print?batch=]]><xsl:value-of select="item/id"/><![CDATA[', { }, function (url) {
	                printJS({printable:url, type:'pdf', showModal:false})
	                $scope.loadingData = false;
	            });            
	        }
	
			function printLetter() {
	            render('/api/batch/letter/print?batch=]]><xsl:value-of select="item/id"/><![CDATA[', { }, function (url) {
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
