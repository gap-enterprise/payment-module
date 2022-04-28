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
      <xsl:text>GAP - Paiements</xsl:text>
    </title>
  </xsl:template>
  <xsl:template match="page" mode="header">
    <div class="app-page-title app-page-title-simple">
      <div class="page-title-wrapper">
        <div class="page-title-heading">
          <div class="page-title-icon">
            <i class="lnr-diamond icon-gradient bg-night-fade"/>
          </div>
          <div>
            <xsl:text>Paiements</xsl:text>
            <div class="page-title-subheading opacity-10">
              <nav class="" aria-label="breadcrumb">
                <ol class="breadcrumb">
                  <li class="breadcrumb-item">
                    <a href="/home">
                      <i aria-hidden="true" class="fa fa-home"/>
                    </a>
                  </li>
                  <li class="active breadcrumb-item" aria-current="page">
                    Ordres de paiement à exécuter
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
    <div class="main-card mb-3 card-body" style="padding: 0" app="app" ng-controller="AppCtrl as vm">
      <div class="row bg-white" ng-if="vm.loadingData">
        <div class="col-sm-12 text-center">
          <h4 class="text-muted">Chargement des données... <small>Veuillez patienter</small></h4>
          <img src="/io/surati/gap/web/base/img/loader.gif" width="250"/>
        </div>
      </div>
      <div ng-if="!vm.loadingData">
        <div class="main-card mb-3 card mt-3" ng-if="vm.batches.length == 0">
          <div class="d-block card-body">
            <h6 class="text-center pb-1 pt-1">
              <xsl:text>Il n'y a aucun ordre de paiement à exécuter.</xsl:text>
            </h6>
          </div>
        </div>
        <div class="row pt-3" ng-if="vm.batches.length &gt; 0" ng-repeat="batch in vm.batches">
          <div class="col-sm-12 col-md-12">
            <div class="card">
              <div class="card-header">
                <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
					        Lot {{batch.id}} - 
					        <div class="badge badge-pill badge-success">{{batch.account}}</div>
					        <div class="badge badge-pill badge-success">{{batch.mean_type}} (<span class="text-lowercase">{{batch.groups.length}} à imprimer</span>)</div>
					        </div>
                <xsl:if test="sec:hasAccess(.,'EXECUTER_ORDRES_PAIEMENT')">
                  <div class="btn-actions-pane-right">
                    <a class="btn-icon btn-wide btn-outline-2x btn btn-outline-focus btn-sm d-flex" href="/payment/batch/new?account={{{{batch.account_id}}}}&amp;meantype={{{{batch.mean_type_id}}}}&amp;{root_page/full}">
					              Payer le lot
					          </a>
                  </div>
                </xsl:if>
              </div>
              <div class="card-body">
                <div class="main-card mb-3 card" ng-repeat="group in batch.groups">
                  <div class="card-header">
                    <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
					        {{group.beneficiary}} - 
					        <div class="badge badge-pill badge-success">{{group.account}}</div>
					        </div>
                    <xsl:if test="sec:hasAccess(.,'EXECUTER_ORDRES_PAIEMENT')">
                      <div class="btn-actions-pane-right">
                        <a class="btn-icon btn-wide btn-outline-2x btn btn-outline-focus btn-sm d-flex" href="/payment/new?group={{{{group.id}}}}&amp;{root_page/full}">
					              Payer
					          </a>
                      </div>
                    </xsl:if>
                  </div>
                  <div class="table-responsive">
                    <table class="align-middle text-truncate mb-0 table table-borderless table-hover">
                      <thead>
                        <tr>
                          <th class="text-center">N°</th>
                          <th class="text-center">Date</th>
                          <th class="text-center">Référence</th>
                          <th class="text-center" ng-if="group.is_hetero">Bénéficiaire</th>
                          <th class="text-center">Document de référence</th>
                          <th class="text-center">Date du document</th>
                          <th class="text-center">Montant à payer</th>
                          <th class="text-center">Actions</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr ng-repeat="order in group.orders">
                          <td class="text-center text-muted">
					                    {{ $index + 1 }}
					                  </td>
                          <td class="text-center text-muted">
					                    {{ order.date_view }}
					                  </td>
                          <td class="text-center text-muted">
					                    {{ order.reference }}
					                  </td>
                          <td class="text-center text-muted" ng-if="group.is_hetero">
					                    {{ order.beneficiary }}
					                  </td>
                          <td class="text-center text-muted">
					                    {{ order.ref_doc_name }}
					                  </td>
                          <td class="text-center text-muted">
					                    {{ order.ref_doc_date_view }}
					                  </td>
                          <td class="text-center text-muted">
                            <div ng-if="order.amount_to_pay &gt; 0" class="badge badge-pill badge-success">{{order.amount_to_pay_in_human}}</div>
                            <div ng-if="!(order.amount_to_pay &gt; 0)" class="badge badge-pill badge-danger">{{order.amount_to_pay_in_human}}</div>
                          </td>
                          <td class="text-center">
                            <div role="group" class="btn-group-sm btn-group">
                              <xsl:if test="sec:hasAccess(.,'VISUALISER_ORDRES_PAIEMENT') or sec:hasAccess(.,'PREPARER_ORDRES_PAIEMENT') or sec:hasAccess(.,'AUTORISER_ORDRES_PAIEMENT')">
                                <a class="btn-shadow btn btn-primary" href="/payment-order/view?id={{{{order.id}}}}&amp;{root_page/full}">Voir</a>
                              </xsl:if>
                            </div>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <div class="p-4 card-footer">
                    <div class="badge badge-pill badge-success mr-1">Total : {{ group.total_amount_in_human }}</div>
                    <div class="badge badge-pill badge-success mr-1">{{group.mean_type}}</div>
                    <div class="badge badge-pill badge-success" ng-if="!(group.mean_type_id == 'CHEQUE')">Date d'échéance: {{group.due_date_view}}</div>
                    <div class="btn-actions-pane-right">
                      <a class="btn-icon btn-wide btn btn-warning btn-sm d-flex" href="/payment-order-group/send-back-in-preparation?id={{{{group.id}}}}&amp;{root_page/full}" onclick="return confirm('Souhaitez-vous renvoyer ces ordres de paiement en préparation ?');">
					              Renvoyer en préparation
					          </a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="d-block p-4">
          <div class="badge badge-pill badge-success">Total : {{vm.total_amount_in_human}}</div>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="custom-script">
    <script type="text/javascript"><![CDATA[		
   
				var app = angular.module("app", ['ui.bootstrap']);			
	
				app.controller("AppCtrl", ["$scope", "$http", function ($scope, $http) {
					   var vm = this;
					   
		               vm.load = function() {							
				            var config = {
				                params: { }
				            };
				
				            vm.loadingData = true;
				            return $http.get('/api/payment-order-group/to-execute', config).then(
						            function(response){
						            	vm.loadingData = false;						            
							            vm.batches = response.data.items;
							            vm.total_amount_in_human = response.data.total_amount_in_human;
						            },
						            function(error){
						            	vm.loadingData = false;
						            }
				            );
				        }	
		               		               
					   this.$onInit = function(){
					   	    vm.load();
					   };
			    }]);	
				
				angular.bootstrap(document, ['app']);			
        ]]></script>
  </xsl:template>
</xsl:stylesheet>
