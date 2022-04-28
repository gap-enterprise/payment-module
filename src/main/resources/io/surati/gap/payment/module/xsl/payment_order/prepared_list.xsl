<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sec="http://www.surati.io/Security/User/Profile" version="2.0">
  <xsl:output method="html" include-content-type="no" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
  <xsl:include href="/io/surati/gap/web/base/xsl/layout.xsl"/>
  <xsl:template match="page" mode="head">
    <title><xsl:text>GAP - </xsl:text><xsl:value-of select="root_page/title"/> - <xsl:value-of select="root_page/subtitle"/>
      <link rel="stylesheet" href="/css/select2.css"/>
      <link rel="stylesheet" href="/css/select2-bootstrap4.css"/>
    </title>
  </xsl:template>
  <xsl:template match="page" mode="header">
    <div class="app-page-title app-page-title-simple">
      <div class="page-title-wrapper">
        <div class="page-title-heading">
          <div class="page-title-icon">
            <i class="lnr-database icon-gradient bg-night-fade"/>
          </div>
          <div>
            <xsl:value-of select="root_page/title"/>
            <div class="page-title-subheading opacity-10">
              <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                  <li class="breadcrumb-item">
                    <a href="/home">
                      <i aria-hidden="true" class="fa fa-home"/>
                    </a>
                  </li>
                  <li class="active breadcrumb-item">
                    <xsl:value-of select="root_page/subtitle"/>
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
    <div class="main-card mb-3 card card-body" app="app" ng-controller="AppCtrl as vm" style="position:relative">
      <div id="toolbar" class="card fixed-top d-flex align-items-center" style="top: 60px;visibility:hidden;">
        <div class="card-header">
          <div ng-if="vm.amountSelected() &gt; 0" class="badge badge-pill badge-success mr-2">Montant s&#xE9;lectionn&#xE9; : {{vm.amountSelected() | number}} FCFA</div>
          <div ng-if="!(vm.amountSelected() &gt;= 0)" class="badge badge-pill badge-danger mr-2">Montant s&#xE9;lectionn&#xE9; : {{vm.amountSelected() | number}} FCFA</div>
          <div role="group" class="btn-group btn-group btn-group-toggle mr-1">
            <button type="button" class="btn btn-alternate" ng-click="vm.mergeInNewGroup()" ng-disabled="vm.loadingMerge"><i class="fa fa-spinner fa-spin" ng-if="vm.loadingMerge"/> Grouper
	             </button>
            <button type="button" class="btn btn-primary" ng-click="vm.openOtherBeneficiaryDialog()">Grouper avec un tiers diff&#xE9;rent</button>
          </div>
        </div>
      </div>
      <div ng-if="vm.loadingData">
        <div class="col-sm-12 text-center">
          <h4 class="text-muted">Chargement des donn&#xE9;es... <small>Veuillez patienter</small></h4>
          <img src="/io/surati/gap/web/base/img/loader.gif" width="250"/>
        </div>
      </div>
      <div ng-if="!vm.loadingData">
        <div class="card-header">
          <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
            <xsl:text>Ordres de paiement &#xE0; pr&#xE9;parer</xsl:text>
          </div>
          <div class="btn-actions-pane-right">
            <div class="row">
              <div ng-if="vm.amountSelected() &gt; 0" class="badge badge-pill badge-success text-center my-auto mr-2">Montant s&#xE9;lectionn&#xE9; : {{vm.amountSelected() | number}} FCFA</div>
              <div ng-if="!(vm.amountSelected() &gt;= 0)" class="badge badge-pill badge-danger text-center my-auto mr-2">Montant s&#xE9;lectionn&#xE9; : {{vm.amountSelected() | number }} FCFA</div>
              <xsl:if test="sec:hasAccess(.,'PREPARER_ORDRES_PAIEMENT')">
                <div role="group" class="btn-group btn-group btn-group-toggle mr-1">
                  <button type="button" class="btn btn-alternate" ng-click="vm.mergeInNewGroup()" ng-disabled="vm.loadingMerge"><i class="fa fa-spinner fa-spin" ng-if="vm.loadingMerge"/> Grouper
		             </button>
                  <button type="button" class="btn btn-primary" ng-click="vm.openOtherBeneficiaryDialog()">Grouper avec un tiers diff&#xE9;rent</button>
                </div>
                <a href="/payment-order/edit?{root_page/full}" class="btn-icon btn-wide btn-outline-2x btn btn-outline-focus btn-sm d-flex mr-1">
                  <xsl:text>Nouveau</xsl:text>
                  <span class="pl-2 align-middle opacity-7">
                    <i class="fa fa-plus"/>
                  </span>
                </a>
              </xsl:if>
              <xsl:if test="sec:hasAccess(.,'AUTORISER_ORDRES_PAIEMENT')">
                <button type="button" ng-if="vm.groups.length &gt; 0" ng-click="vm.validateMultiple()" class="btn-icon btn-wide btn btn-success btn-sm d-flex" ng-disabled="vm.loading">
                  <i class="fa fa-spinner fa-spin" ng-if="vm.loading"/>
                  <xsl:text> Valider tout</xsl:text>
                  <span class="pl-2 align-middle opacity-7">
                    <i class="fa fa-road"/>
                  </span>
                </button>
              </xsl:if>
            </div>
          </div>
        </div>
        <h6 class="text-center pb-1 pt-1" ng-if="vm.groups.length == 0">
          <xsl:text>Il n'y a aucun ordre de paiement &#xE0; pr&#xE9;parer.</xsl:text>
        </h6>
        <div class="col-sm-12 col-md-12 mt-4" ng-repeat="group in vm.groups">
          <div id="{{{{group.id}}}}" class="main-card mb-3 card">
            <div class="card-header">
              <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
		        {{group.beneficiary}} - 
		        <div class="badge badge-pill badge-warning" ng-if="group.account_id == 0">{{group.account}}</div>
		        <div class="badge badge-pill badge-success" ng-if="group.account_id &gt; 0">{{group.account}}</div>
		        </div>
              <xsl:if test="sec:hasAccess(.,'EXECUTER_ORDRES_PAIEMENT')">
                <div class="btn-actions-pane-right">
                  <div class="row">
                    <xsl:if test="sec:hasAccess(.,'PREPARER_ORDRES_PAIEMENT')">
                      <div role="group" class="btn-group btn-group btn-group-toggle mr-1">
                        <button type="button" class="btn btn-alternate" ng-click="$parent.vm.mergeSelectedOrders(group)" ng-disabled="group.loading"><i class="fa fa-spinner fa-spin" ng-if="group.loading"/> Ajouter au groupe
		             </button>
                        <button type="button" class="btn btn-dark" ng-click="vm.openOtherBeneficiaryDialog(group)" ng-disabled="group.loading"><i class="fa fa-spinner fa-spin" ng-if="group.loading"/> Changer b&#xE9;n&#xE9;ficiaire
		             </button>
                      </div>
                      <a class="btn-icon btn-wide btn-outline-2x btn btn-outline-focus btn-sm d-flex mr-1" ng-click="$parent.vm.openAccountDialog(group)">
		              Choisir mode de r&#xE8;glement
		          </a>
                      <button type="button" class="btn-icon btn-wide btn-outline-2x btn btn-success btn-sm d-flex mr-3" ng-click="vm.validateSingle(group)" ng-disabled="group.loadingValidate"><i class="fa fa-spinner fa-spin" ng-if="group.loadingValidate"/> Valider
		          </button>
                    </xsl:if>
                  </div>
                </div>
              </xsl:if>
            </div>
            <div class="table-responsive">
              <table class="align-middle text-truncate mb-0 table table-striped table-bordered table-hover">
                <thead>
                  <tr>
                    <th>
                      <input type="checkbox" ng-model="group.selectAll" ng-change="vm.selectAllChanged(group.orders, group.selectAll)"/>
                    </th>
                    <th class="text-center">N&#xB0;</th>
                    <th class="text-center">Date</th>
                    <th class="text-center">R&#xE9;f&#xE9;rence</th>
                    <th class="text-center" ng-if="group.is_hetero">B&#xE9;n&#xE9;ficiaire</th>
                    <th class="text-center">Document de r&#xE9;f&#xE9;rence</th>
                    <th class="text-center">Date du document</th>
                    <th class="text-center">Montant &#xE0; payer</th>
                    <th class="text-center">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  <tr ng-repeat="item in group.orders">
                    <td>
                      <input type="checkbox" ng-model="item.selected" ng-change="vm.poSelectChanged(group, item.selected)"/>
                    </td>
                    <td class="text-center text-muted">
		                    {{ $index + 1 }}
		                  </td>
                    <td class="text-center text-muted">
		                    {{ item.date_view }}
		                  </td>
                    <td class="text-center text-muted">
		                    {{ item.reference }}
		                  </td>
                    <td class="text-center text-muted" ng-if="group.is_hetero">
		                    {{ item.beneficiary }}
		                  </td>
                    <td class="text-center text-muted">
		                    {{ item.ref_doc_name }}
		                  </td>
                    <td class="text-center text-muted">
		                    {{ item.ref_doc_date_view }}
		                  </td>
                    <td class="text-center text-muted">
                      <div ng-if="item.amount_to_pay &gt; 0" class="badge badge-pill badge-success">{{item.amount_to_pay_in_human}}</div>
                      <div ng-if="!(item.amount_to_pay &gt; 0)" class="badge badge-pill badge-danger">{{item.amount_to_pay_in_human}}</div>
                    </td>
                    <td class="text-center">
                      <div role="group">
                        <a href="javascript:void(0)" ng-click="vm.openScinderDialog(group, item)" class="mr-2 btn btn-xs btn-primary" data-toggle="tooltip" title="Scinder l'ordre de paiement">
                          <i class="fa fa-cut"/>
                        </a>
                        <a href="/payment-order/view?id={{{{item.id}}}}&amp;{root_page/full}" class="mr-2 btn btn-xs btn-outline-primary">
                          <i class="fa fa-eye"/>
                        </a>
                        <xsl:if test="sec:hasAccess(.,'PREPARER_ORDRES_PAIEMENT')">
                          <a href="/payment-order/edit?id={{{{item.id}}}}&amp;{root_page/full}" class="mr-2 btn btn-xs btn-outline-success">
                            <i class="fa fa-edit"/>
                          </a>
                          <a href="/payment-order/delete?id={{{{item.id}}}}&amp;{root_page/full}" class="btn btn-xs btn-outline-danger" onclick="return confirm('Voulez-vous supprimer cet ordre de paiement ?');">
                            <i class="fa fa-trash"/>
                          </a>
                        </xsl:if>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="d-block p-4 card-footer">
              <div ng-if="group.total_amount &gt; 0" class="badge badge-pill badge-success">Total : {{group.total_amount_in_human}}</div>
              <div ng-if="!(group.total_amount &gt; 0)" class="badge badge-pill badge-danger">Total : {{group.total_amount_in_human}}</div>
              <div class="badge badge-pill badge-warning" ng-if="group.mean_type_id == 'NONE'">Aucun mode de paiement choisi</div>
              <div class="badge badge-pill badge-success" ng-if="!(group.mean_type_id == 'NONE')">{{group.mean_type}}</div>
              <div class="badge badge-pill badge-warning" ng-if="group.due_date == '' &amp;&amp; group.mean_type_id == 'LETTRE_DE_CHANGE' ">Aucune &#xE9;ch&#xE9;ance choisie</div>
              <div class="badge badge-pill badge-success" ng-if="!(group.due_date == '') &amp;&amp; group.mean_type_id == 'LETTRE_DE_CHANGE' ">Date d'&#xE9;ch&#xE9;ance: {{group.due_date_view}}</div>
            </div>
          </div>
        </div>
        <div class="d-block p-4 card-footer">
          <div ng-if="vm.total_amount &gt; 0" class="badge badge-pill badge-success">Total : {{vm.total_amount_in_human}}</div>
          <div ng-if="!(vm.total_amount &gt; 0)" class="badge badge-pill badge-danger">Total : {{vm.total_amount_in_human}}</div>
        </div>
      </div>
    </div>
    <script type="text/ng-template" id="accountListDialog.html">
      <div class="modal-header">
        <h4 class="modal-title">Choisir mode de r&#xE8;glement</h4>
      </div>
      <div class="modal-body">
        <div class="col-md-12">
          <div class="position-relative form-group">
            <label for="account_id" class="">
              <xsl:text>Compte bancaire</xsl:text>
            </label>
            <select class="col-md-6 custom-select form-control form-control-sm" aria-controls="example" ng-model="vm.data.account_id" ng-options="item.id as item.name for item in vm.accounts">
               	  </select>
          </div>
        </div>
        <div class="col-md-12">
          <div class="position-relative form-group">
            <label for="mean_type_id" class="">
              <xsl:text>Moyen de paiement</xsl:text>
            </label>
            <select class="col-md-6 custom-select form-control form-control-sm" aria-controls="example" ng-model="vm.data.mean_type_id">
              <option value="NONE"> -- Choisir un moyen de paiement -- </option>
              <option ng-repeat="item in vm.data.payment_mean_types_accepted" value="{{{{item.id}}}}">{{item.name}}</option>
            </select>
          </div>
        </div>
        <div class="col-md-12" ng-if="vm.data.mean_type_id == 'LETTRE_DE_CHANGE'">
          <div class="position-relative form-group">
            <label for="due_date">
              <xsl:text>Date d'&#xE9;ch&#xE9;ance</xsl:text>
            </label>
            <input ng-model="vm.data.due_date" placeholder="Entrez une date..." type="date" class="col-md-6 form-control"/>
            <span>
              <b>{{vm.data.due_date_expected}}</b>
            </span>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <div class="row">
          <button type="button" class="btn btn-sm btn-primary mr-1" ng-click="vm.choose()">Enregistrer</button>
          <button type="button" class="btn btn-danger mr-2" ng-click="vm.cancel()">Annuler</button>
        </div>
      </div>
    </script>
    <script type="text/ng-template" id="scinderOrderDialog.html">
      <div class="modal-header">
        <h4 class="modal-title">Scinder le montant de l'ordre en deux (2)</h4>
      </div>
      <div class="modal-body">
        <div class="col-md-12">
          <div class="position-relative form-group">
            <label>
              <xsl:text>Montant de d&#xE9;part : </xsl:text>
            </label>
            <span class="col-md-6">
              <b>{{vm.data.amount_to_pay_in_human}}</b>
            </span>
          </div>
        </div>
        <div class="col-md-12">
          <div class="position-relative form-group">
            <label>
              <xsl:text>Montant du premier ordre</xsl:text>
            </label>
            <input ng-model="vm.firstAmount" placeholder="Entrez un montant..." type="number" class="col-md-6 form-control"/>
            <span class="mt-3" ng-if="vm.isInValid()" style="color: red">{{vm.erreurMessage()}}</span>
          </div>
        </div>
        <div class="col-md-12">
          <div class="position-relative form-group">
            <label>
              <xsl:text>Montant du deuxi&#xE8;me ordre : </xsl:text>
            </label>
            <span class="col-md-6">
              <b>{{vm.data.amount_to_pay - vm.firstAmount | number}}</b>
            </span>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <div class="row">
          <button type="button" class="btn btn-sm btn-primary mr-1" ng-click="vm.scinder()" ng-disabled="vm.loading || vm.firstAmount == 0 || vm.isInValid()"><i class="fa fa-spinner fa-spin" ng-if="vm.loading"/> Scinder
		      </button>
          <button type="button" class="btn btn-danger mr-2" ng-click="vm.cancel()">Annuler</button>
        </div>
      </div>
    </script>
    <script type="text/ng-template" id="otherBeneficiaryDialog.html">
      <div class="modal-header">
        <h4 class="modal-title">Choisir un b&#xE9;n&#xE9;ficiaire</h4>
      </div>
      <div class="modal-body">
        <div class="col-md-12">
          <div class="position-relative form-group">
            <label for="third_party_id" class="">
              <xsl:text>B&#xE9;n&#xE9;ficiaire</xsl:text>
            </label>
            <select id="third_party_id" name="third_party_id" data-placeholder="Rechercher b&#xE9;n&#xE9;ficiaire par nom, abr&#xE9;g&#xE9;..." class="form-control form-control-sm" ng-model="vm.beneficiaryId" ng-options="item.id as item.name for item in vm.items">
              <option value=""/>
            </select>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <div class="row">
          <button type="button" class="btn btn-sm btn-primary mr-1" ng-click="vm.choose()" ng-disabled="vm.loading"><i class="fa fa-spinner fa-spin" ng-if="vm.loading"/> Continuer
		      </button>
          <button type="button" class="btn btn-danger mr-2" ng-click="vm.cancel()">Annuler</button>
        </div>
      </div>
      <script type="text/javascript"><![CDATA[
    	  $('select').select2({
		    theme: 'bootstrap4',
		});			
        ]]></script>
    </script>
  </xsl:template>
  <xsl:template match="page" mode="custom-script">
    <script src="/io/surati/gap/web/base/js/select2.js"/>
    <script src="/io/surati/gap/web/base/js/jquery.scrollTo.min.js"/>
    <script type="text/javascript"><![CDATA[
        		$(window).scroll(function() {
				  if ($(this).scrollTop() > 200) {
				    $("#toolbar").css("visibility", "visible");
				  } else {
				    $("#toolbar").css("visibility", "hidden");
				  }
				});
				var app = angular.module("app", ['ui.bootstrap'])
				                 .config(["$provide", function($provide){
							        $provide.decorator('$uibModal', function ($delegate) {
							            var open = $delegate.open;
							
							            $delegate.open = function (options) { // avoid closing by backdrop click
							                options = angular.extend(options || {}, {
							                    backdrop: 'static',
							                    keyboard: false,
							                    size: 'lg'
							                });
							
							                return open(options);
							            };
							            return $delegate;
							        });
				                 }]);		
	
	            app.controller("scinderOrderDialogCtrl", ["data", '$uibModalInstance', "$scope", "$http", "$httpParamSerializerJQLike", function (data, $uibModalInstance, $scope, $http, $httpParamSerializerJQLike) {
				    var vm = this;	             		              
	               
	                vm.data = Object.assign({}, data.order);
	                vm.groupId = data.group_id;
	                vm.cancel = cancel;
					vm.scinder = scinder;				
          			vm.erreurMessage = erreurMessage;
          			vm.isInValid = isInValid;
          			
          			function erreurMessage() {
          			     var message;
          			     
          			     if(vm.firstAmount < 0 && vm.data.amount_to_pay > 0) {
          			     	message = "Le montant doit être positif.";
          			     } else if(vm.firstAmount >= vm.data.amount_to_pay && vm.data.amount_to_pay > 0) {
          			        message = "Le montant doit être inférieur au montant de départ.";
          			     } else if(vm.firstAmount > 0 && vm.data.amount_to_pay < 0) {
          			        message = "Le montant doit être négatif.";
          			     } else if(vm.firstAmount <= vm.data.amount_to_pay && vm.data.amount_to_pay < 0) {
          			     	message = "Le montant doit être compris entre 0 et le montant de départ.";
          			     } else {
          			     	message = "Montant correct";
          			     }
          			     return message;
          			}
          			
          			function isInValid() {
          			   return ((vm.firstAmount < 0 || vm.firstAmount >= vm.data.amount_to_pay) && vm.data.amount_to_pay > 0) || ((vm.firstAmount > 0 || vm.firstAmount <= vm.data.amount_to_pay) && vm.data.amount_to_pay < 0);
          			}
          			
					function scinder(){
					        vm.loading = true;
							var data = {
							   id: vm.data.id,
							   group_id: vm.groupId,
							   first_amount: vm.firstAmount
							};						
							$http({
							    url: '/api/payment-order/split',
							    method: 'POST',
							    data: $httpParamSerializerJQLike(data),
							    headers: {
							      'Content-Type': 'application/x-www-form-urlencoded'
							    }
							}).then(
					            function(response){
					                vm.loading = false;
					                var group = response.data;
					                $uibModalInstance.close(group);
					            },
					            function(error){
					                vm.loading = false;
					                toastr.info(error.data.message);
					            }
				            );	    
					}
					
					function cancel(){
						$uibModalInstance.dismiss();
					}
					   
					this.$onInit = function(){
						vm.firstAmount = 0;
					}
			    }]);
			    
			    app.controller("accountListDialogCtrl", ["data", '$uibModalInstance', "$scope", "$http", function (data, $uibModalInstance, $scope, $http) {
				    var vm = this;	             		              
	               
	                vm.data = Object.assign({}, data);
	                if(vm.data.due_date) {
	                	vm.data.due_date = new Date(vm.data.due_date);
	                }
	                vm.cancel = cancel;
					vm.choose = choose;	
					vm.accounts = [
					{
						id: 0,
						name : " -- Choisir un compte -- "
					},
						]]><xsl:for-each select="bank_accounts/bank_account">
						{
						   id: <xsl:value-of select="id"/>,
						   name: "<xsl:value-of select="full_name"/>"
						},
						</xsl:for-each><![CDATA[
					];	
					
					vm.meantypes = [
						]]><xsl:for-each select="payment_mean_types/payment_mean_type">
						{
						   id: '<xsl:value-of select="id"/>',
						   name: "<xsl:value-of select="name"/>"
						},
						</xsl:for-each><![CDATA[
					];				
          			
					function choose(){
						$uibModalInstance.close(vm.data);		    
					}
					
					function cancel(){
						$uibModalInstance.dismiss();
					}
					   
					this.$onInit = function(){
		
					}
			    }]);
			    
			    app.controller("otherBeneficiaryDialogCtrl", ["data", '$uibModalInstance', "$scope", "$http", "$httpParamSerializerJQLike", function (data, $uibModalInstance, $scope, $http, $httpParamSerializerJQLike) {
				    var vm = this;	             		              
	               
	                vm.poids = data.poids;
	                group = data.group;
	                vm.cancel = cancel;
					vm.choose = choose;				
          			
					function choose(){
							if(group)	{
							    vm.loading = true;
								var data = {
								   group_id: group.id,
								   beneficiary_id: vm.beneficiaryId
								};						
								$http({
								    url: '/api/payment-order-group/change-beneficiary',
								    method: 'POST',
								    data: $httpParamSerializerJQLike(data),
								    headers: {
								      'Content-Type': 'application/x-www-form-urlencoded'
								    }
								}).then(
						            function(response){
						                vm.loading = false;
						                setTimeout(
						                	function(){
						                		$.scrollTo($('#' + group.id).offset().top - 120, 500);
						                	},
						                	500
						                );
						                var groups = response.data.items;
						                toastr.success("Changement du bénéficiaire du groupe avec succès !");	
						                $uibModalInstance.close(groups);
						            },
						            function(error){
						                vm.loading = false;
						                toastr.info(error.data.message);
						            }
					            );
							} else {
							    vm.loading = true;
								var data = {
								   poids: vm.poids,
								   beneficiary_id: vm.beneficiaryId
								};						
								$http({
								    url: '/api/payment-order/merge-across',
								    method: 'POST',
								    data: $httpParamSerializerJQLike(data),
								    headers: {
								      'Content-Type': 'application/x-www-form-urlencoded'
								    }
								}).then(
						            function(response){
						                vm.loading = false;
						                setTimeout(
						                	function(){
						                		$.scrollTo($('#' + response.data.target_id).offset().top - 120, 500);
						                	},
						                	500
						                );
						                var groups = response.data.items;
						                toastr.success("Regroupement effectuée avec succès !");	
						                $uibModalInstance.close(groups);
						            },
						            function(error){
						                vm.loading = false;
						                toastr.info(error.data.message);
						            }
					            );
							}    
					}
					
					function cancel(){
						$uibModalInstance.dismiss();
					}
					   
					vm.items = [
						]]><xsl:for-each select="third_parties/third_party">
						{
						   id: '<xsl:value-of select="id"/>',
						   name: "<xsl:value-of select="name"/>"
						},
						</xsl:for-each><![CDATA[
					];
					
					this.$onInit = function(){
						vm.beneficiaryId = 0;
					}
			    }]);
			    
				app.controller("AppCtrl", ['$uibModal', "$scope", "$http", "$httpParamSerializerJQLike", function ($uibModal, $scope, $http, $httpParamSerializerJQLike) {
					   var vm = this;
		               vm.openAccountDialog = openAccountDialog;
		               vm.openScinderDialog = openScinderDialog;
		               vm.mergeSelectedOrders = mergeSelectedOrders;
		               vm.poSelectChanged = poSelectChanged;
		               vm.mergeInNewGroup = mergeInNewGroup;
		               vm.openOtherBeneficiaryDialog = openOtherBeneficiaryDialog;
		               vm.amountSelected = amountSelected;
		               
		               vm.selectAllChanged = function(items, selected){
	       			   	   items.forEach(function(item, index) {
	       			   	   		item.selected = selected;
	       			   	   });
	         			}
         			
         			    function mergeInNewGroup() {
         			        if(!vm.isAnyPaymentOrderSelected()) {
		                      toastr.info("Vous devez sélectionner au moins un élément !");
		                      return;
		                   }
         			        vm.loadingMerge = true;
         			    	mergeSelectedOrders({}, function(){
         			    		vm.loadingMerge = false;
         			    	});
         			    }
         			    
         				function poSelectChanged(group, selected){
         				   if(!selected) {
         				      group.selectAll = false;
         				   }
	         			}
	         			
         			   vm.accounts = [
			                    ]]><xsl:for-each select="bank_accounts/bank_account">
			                    {
			                    	id: '<xsl:value-of select="id"/>',
			                    	name: '<xsl:value-of select="name"/>'
			                    },
			                    </xsl:for-each><![CDATA[
			           ];
			           
			           vm.isAnyPaymentOrderSelected = function() {
			           	  var poids = [];
		                   vm.groups.forEach(function(group, index1) {
		                      group.orders.forEach(function(order, index2) {
		                      	 if(order.selected) {
		                      	    poids.push(order.id);
		                      	 }
		                      });
		                   });
		                   return poids.length > 0;
			           }
			           
			           function amountSelected() {
			             var amount = 0;
			             if(!vm.groups) return 0;
		                   vm.groups.forEach(function(group, index1) {
		                      group.orders.forEach(function(order, index2) {
		                      	 if(order.selected) {
		                      	    amount += order.amount_to_pay;
		                      	 }
		                      });
		                   });
		                   return amount;
			           }
			           
		               function mergeSelectedOrders(targetgroup, callback) {
		                   var poids = [];
		                   vm.groups.forEach(function(group, index1) {
		                      group.orders.forEach(function(order, index2) {
		                      	 if(order.selected) {
		                      	    poids.push(order.id);
		                      	 }
		                      });
		                   });
		                   if(poids.length == 0) {
		                      toastr.info("Il n'y a pas d'éléments sélectionnés à ajouter au groupe");
		                      return;
		                   }
		                   merge(targetgroup, poids, callback);
		               }
		               
		               vm.validateMultiple = function() {
		                  vm.loading = true;
		                  validate(0, function(validated) {
		                  	vm.loading = false;
		                  	if (validated) {
		                  		toastr.success("Validation de tous les ordres effectuée avec succès !");
		                  	}
		                  });
		               }
		               
		               vm.validateSingle = function(item) {
		                  item.loadingValidate = true;
		                  validate(item.id, function(validated){
		                  	item.loadingValidate = false;
		                  	if (validated) {
		                  		toastr.success("Validation du groupe d'ordres effectuée avec succès !");
		                  	}
		                  });
		               }
		            
		               vm.load = function() {							
				            var config = {
				                params: { }
				            };
				            vm.loadingData = true;
				            return $http.get('/api/payment-order-group/to-prepare', config).then(
						            function(response){
						            	vm.loadingData = false;					            
							            vm.groups = response.data.items;
							            vm.groups.forEach(function(item, index) {
							               item.due_date = item.due_date;
							            });
							            vm.total_amount = response.data.total_amount;
					            		vm.total_amount_in_human = response.data.total_amount_in_human;
						            },
						            function(error){
						            	vm.loadingData = false;
						            }
				            );
				       }           		              
		               
		               function openAccountDialog(group){	       
					       $uibModal.open({
				                templateUrl: 'accountListDialog.html',
				                controller: 'accountListDialogCtrl as vm',
				                resolve: {
				                    data: group
				                }
				            }).result.then(
				                function (data) {
				                    useAccount(group, data);					                		               		
					            }, function () {
									
					            }
				            );
						}
						
					   function openScinderDialog(group, order){	       
					       $uibModal.open({
				                templateUrl: 'scinderOrderDialog.html',
				                controller: 'scinderOrderDialogCtrl as vm',
				                resolve: {
				                    data: {
				                    	order: order,
				                    	group_id: group.id
				                    }
				                }
				            }).result.then(
				                function (groupupdated) {
				                   group.orders = groupupdated.orders;
				                   toastr.success("Scission de l'ordre effectuée avec succès !");			                		               		
					            }, function () {
									
					            }
				            );
						}
					
					    function openOtherBeneficiaryDialog(group){	
					       var poids = [];
		                   vm.groups.forEach(function(group, index1) {
		                      group.orders.forEach(function(order, index2) {
		                      	 if(order.selected) {
		                      	    poids.push(order.id);
		                      	 }
		                      });
		                   });
		                   if(!group && poids.length == 0) {
		                      toastr.info("Vous devez sélectionner au moins un élément !");
		                      return;
		                   }   
					       $uibModal.open({
				                templateUrl: 'otherBeneficiaryDialog.html',
				                controller: 'otherBeneficiaryDialogCtrl as vm',
				                resolve: {
				                    data: {
				                    	poids: poids,
				                    	group: group
				                    }
				                }
				            }).result.then(
				                function (groups) {
				                   vm.groups = groups;	                		               		
					            }, function () {
									
					            }
				            );
						}
						
					   function useAccount(group, data) {
					   		data.id = group.id;						
							$http({
							    url: '/api/payment-order-group/bank-account/choose',
							    method: 'POST',
							    data: $httpParamSerializerJQLike(data),
							    headers: {
							      'Content-Type': 'application/x-www-form-urlencoded'
							    }
							}).then(
					            function(response){
					                var newgroup = response.data;
					            	group.account_id = newgroup.account_id;
					            	group.account = newgroup.account;
					            	group.mean_type_id = newgroup.mean_type_id;
					            	group.mean_type = newgroup.mean_type;
					                group.due_date = newgroup.due_date;
					                group.due_date_view = newgroup.due_date_view;
					                toastr.success("Choix du mode de règlement effectué avec succès !");
					            },
					            function(error){
					                toastr.info(error.data.message);
					            }
				            );
				       }
				       
				       function merge(targetgroup, poids, callback) {
					        var data = {
					            target_id: targetgroup.id,
			                    poids: poids
			                };
			                targetgroup.loading = true;			
							$http({
							    url: '/api/payment-order/to-prepare/merge',
							    method: 'POST',
							    data: $httpParamSerializerJQLike(data),
							    headers: {
							      'Content-Type': 'application/x-www-form-urlencoded'
							    }
							}).then(
					            function(response){
					                if(callback) callback();
					                targetgroup.loading = false;
					            	vm.groups = response.data.items;
					            	vm.total_amount = response.data.total_amount;
					            	vm.total_amount_in_human = response.data.total_amount_in_human;
					            	toastr.success("Regroupement des ordres effectué avec succès !");
					            	setTimeout(
					                	function(){
					                		$.scrollTo($('#' + response.data.target_id).offset().top - 120, 500);
					                	},
					                	500
					                );
					            },
					            function(error){
					                if(callback) callback();
					            	targetgroup.loading = false;
					                toastr.info(error.data.message);
					            }
				            );
				       }
				       
				       function validate(id, callback) {
					        var data = {
			                    id: id
			                };		
							$http({
							    url: '/api/payment-order-group/to-prepare/validate',
							    method: 'POST',
							    data: $httpParamSerializerJQLike(data),
							    headers: {
							      'Content-Type': 'application/x-www-form-urlencoded'
							    }
							}).then(
					            function(response){
					            	callback(true);
					            	vm.groups = response.data.items;
					            	vm.total_amount = response.data.total_amount;
					            	vm.total_amount_in_human = response.data.total_amount_in_human;
					            },
					            function(error){
					            	callback(false);
					                toastr.info(error.data.message);
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
