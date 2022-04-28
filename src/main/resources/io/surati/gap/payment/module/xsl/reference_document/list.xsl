<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sec="http://www.surati.io/Security/User/Profile" version="2.0">
  <xsl:output method="html" include-content-type="no" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
  <xsl:include href="/io/surati/gap/web/base/xsl/layout.xsl"/>
  <xsl:template match="page" mode="head">
    <title><xsl:text>GAP</xsl:text> - <xsl:value-of select="root_page/title"/> - <xsl:value-of select="root_page/subtitle"/>
    </title>
  </xsl:template>
  <xsl:template match="page" mode="header">
    <div class="app-page-title app-page-title-simple">
      <div class="page-title-wrapper">
        <div class="page-title-heading">
          <div class="page-title-icon">
            <i class="lnr-briefcase icon-gradient bg-night-fade"/>
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
    <div class="main-card mb-3 card card-body" app="app" ng-controller="AppCtrl as vm">
      <div class="card-header">
        <div class="card-header-title font-size-lg text-capitalize font-weight-normal">
          <xsl:value-of select="root_page/subtitle"/>
        </div>
        <div class="btn-actions-pane-right">
          <div class="row">
            <xsl:if test="sec:hasAccess(.,'EDITER_DOCUMENT_REF')">
              <a href="/reference-document/selected?{root_page/full}" class="btn-icon btn-wide btn-outline-2x btn btn-primary btn-sm d-flex mr-1"><xsl:text>Voir ma s&#xE9;lection</xsl:text> ({{vm.amount_selected_in_human}})
	            <span class="pl-2 align-middle opacity-7"><i class="fa fa-eye"/></span>
	          </a>
              <a href="/reference-document/edit?{root_page/full}" class="btn-icon btn-wide btn-outline-2x btn btn-outline-focus btn-sm d-flex mr-1">
                <xsl:text>Nouveau</xsl:text>
                <span class="pl-2 align-middle opacity-7">
                  <i class="fa fa-plus"/>
                </span>
              </a>
              <a href="/reference-document/import/edit?{root_page/full}" class="btn-icon btn-wide btn-outline-2x btn btn-outline-focus btn-sm d-flex mr-1">
                <xsl:text>Importer</xsl:text>
                <span class="pl-2 align-middle opacity-7">
                  <i class="fa fa-download"/>
                </span>
              </a>
            </xsl:if>
          </div>
        </div>
      </div>
      <div class="card-body">
        <div class="row dataTables_wrapper dt-bootstrap4">
          <div class="col-sm-12 col-md-3">
            <div class="dataTables_length">
              <label>Afficher 
	      				<select name="example_length" aria-controls="example" class="custom-select custom-select-sm form-control form-control-sm" ng-model="vm.nbItemsPerPage" ng-options="option for option in vm.nbperpageoptions" ng-change="vm.nbItemsPerPageChanged(vm.nbItemsPerPage)"/> &#xE9;l&#xE9;ments
     				</label>
            </div>
          </div>
          <div class="col-sm-12 col-md-9">
            <div class="input-group input-group-sm">
              <div class="input-group-prepend">
                <span class="input-group-text">Contient</span>
              </div>
              <input type="search" class="form-control form-control-sm" placeholder="N&#xB0; document, B&#xE9;n&#xE9;ficiaire, Objet, Lieu d'&#xE9;dition" aria-controls="example" ng-model="vm.filterContains" ng-model-options="{{ debounce: 1500 }}" ng-change="vm.search()" aria-describedby="search-addon"/>
              <div class="input-group-append">
                <span class="input-group-text" id="search-addon1">
                  <i class="fa fa-search"/>
                </span>
              </div>
            </div>
          </div>
        </div>
        <div class="row mt-2">
          <div class="col-sm-12 col-md-9 offset-3">
            <div class="input-group input-group-sm">
              <div class="input-group-prepend">
                <span class="input-group-text">Ne contient pas</span>
              </div>
              <input type="search" class="form-control form-control-sm" placeholder="N&#xB0; document, B&#xE9;n&#xE9;ficiaire, Objet, Lieu d'&#xE9;dition" aria-controls="example" ng-model="vm.filterNotContains" ng-model-options="{{ debounce: 1500 }}" ng-change="vm.search()" aria-describedby="search-addon"/>
              <div class="input-group-append">
                <span class="input-group-text" id="search-addon2">
                  <i class="fa fa-search"/>
                </span>
              </div>
            </div>
          </div>
        </div>
        <div class="row mt-2">
          <div class="col-sm-12 col-md-6">
            <div class="d-flex align-items-center">
              <label class="col-md-5">Date du document :</label>
              <div class="input-group input-group-sm">
                <input type="date" class="form-control" aria-controls="example" ng-model="vm.editbegindate" ng-change="vm.search()"/>
                <div class="input-group-append">
                  <button ng-click="vm.editbegindate=''; vm.search()" class="btn btn-outline-secondary" type="button">X</button>
                </div>
              </div>
              <div class="col-md-1">
                <span>&#xE0;</span>
              </div>
              <div class="input-group input-group-sm">
                <input type="date" class="form-control" aria-controls="example" ng-model="vm.editenddate" ng-change="vm.search()"/>
                <div class="input-group-append">
                  <button ng-click="vm.editenddate=''; vm.search()" class="btn btn-outline-secondary" type="button">X</button>
                </div>
              </div>
            </div>
          </div>
          <div class="col-sm-12 col-md-5 offset-md-1">
            <div class="d-flex align-items-center">
              <label class="col-md-4">Statut:</label>
              <select class="col-md-8 custom-select custom-select-sm form-control form-control-sm" aria-controls="example" ng-model="vm.statusId" ng-model-options="{{ debounce: 500 }}" ng-change="vm.search()">
                <option value="NONE"> -- Choisir un statut -- </option>
                <option ng-repeat="item in vm.status" value="{{{{item.id}}}}">{{item.name}}</option>
              </select>
            </div>
          </div>
        </div>
        <div class="row mt-2">
          <div class="col-sm-12 col-md-5 offset-md-7">
            <div class="d-flex align-items-center">
              <label class="col-md-4">Ordonner par :</label>
              <div class="input-group input-group-sm">
                <select class="input-group-text custom-select custom-select-sm" aria-controls="example" ng-model="vm.sorterFieldId" ng-model-options="{{ debounce: 500 }}" ng-change="vm.search()">
                  <option ng-repeat="item in vm.fields" value="{{{{item.id}}}}">{{item.name}}</option>
                </select>
                <div class="input-group-append">
                  <select class="input-group-text custom-select custom-select-sm" aria-controls="example" ng-model="vm.sorterDirectionId" ng-model-options="{{ debounce: 500 }}" ng-change="vm.search()">
                    <option value="ASC">Croissant</option>
                    <option value="DESC">D&#xE9;croissant</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="row mt-2" ng-if="vm.loadingData">
          <div class="col-sm-12 text-center">
            <h4 class="text-muted">Chargement des donn&#xE9;es... <small>Veuillez patienter</small></h4>
            <img src="/io/surati/gap/web/base/img/loader.gif" width="250"/>
          </div>
        </div>
        <div class="mt-2" ng-if="!vm.loadingData">
          <h6 class="text-center pb-1 pt-5" ng-if="vm.items.length == 0">
            <xsl:text>Il n'y a aucun document de r&#xE9;f&#xE9;rence trouv&#xE9;.</xsl:text>
          </h6>
          <div class="row" ng-if="vm.items.length &gt; 0">
            <div class="col-sm-12 col-md-12">
              <button type="button" class="mb-1 mr-1 btn btn-sm btn-success" ng-click="vm.selectMultiple()" ng-disabled="vm.loadingSelectMultiple"><i class="fa fa-spinner fa-spin" ng-if="vm.loadingSelectMultiple"/><i class="fa fa-plus" ng-if="!vm.loadingSelectMultiple"/> Ajouter &#xE9;l&#xE9;ments s&#xE9;lectionn&#xE9;s
                    </button>
              <button type="button" class="mb-1 btn btn-sm btn-success" ng-click="vm.selectSelection()" ng-disabled="vm.loadingSelectSelection"><i class="fa fa-spinner fa-spin" ng-if="vm.loadingSelectSelection"/><i class="fa fa-plus" ng-if="!vm.loadingSelectSelection"/> Ajouter les r&#xE9;sultats de la recherche
                    </button>
            </div>
            <div class="col-sm-12 col-md-12">
              <div class="table-responsive">
                <table class="table table-hover table-striped table-bordered table-sm dataTable dtr-inline">
                  <thead>
                    <tr>
                      <th>
                        <input type="checkbox" ng-model="vm.selectAll" ng-change="vm.selectAllChanged(vm.selectAll)"/>
                      </th>
                      <th>N&#xB0;</th>
                      <th>Date</th>
                      <th>Document</th>
                      <th>B&#xE9;n&#xE9;ficiaire</th>
                      <th>Montant total</th>
                      <th>Reste &#xE0; payer</th>
                      <th>Statut</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr ng-repeat="item in vm.items">
                      <td>
                        <input type="checkbox" ng-model="item.selected"/>
                      </td>
                      <td>
			                    {{ vm.firstPosition + $index }}
			                  </td>
                      <td>
			                    {{ item.date_view }}
			                  </td>
                      <td>
			                    {{ item.name }}
			                  </td>
                      <td>
			                    {{ item.beneficiary }}
			                  </td>
                      <td>
			                    {{ item.amount_in_human }}
			                  </td>
                      <td>
                        <div ng-if="item.amount_left &gt; 0" class="badge badge-pill badge-success">{{item.amount_left_in_human}}</div>
                        <div ng-if="!(item.amount_left &gt; 0)" class="badge badge-pill badge-danger">{{item.amount_left_in_human}}</div>
                      </td>
                      <td>
			                    {{ item.status }}
			                  </td>
                      <td>
                        <div role="group">
                          <xsl:if test="sec:hasAccess(.,'EDITER_DOCUMENT_REF')">
                            <button type="button" class="mb-1 mr-1 btn btn-xs btn-success" ng-click="vm.selectOne(item)" ng-disabled="item.loadingSelectOne">
                              <i class="fa fa-spinner fa-spin" ng-if="item.loadingSelectOne"/>
                              <i class="fa fa-plus" ng-if="!item.loadingSelectOne"/>
                            </button>
                          </xsl:if>
                          <xsl:if test="sec:hasAccess(.,'VISUALISER_DOCUMENT_REF')">
                            <a href="/reference-document/view?id={{{{item.id}}}}&amp;{root_page/full}" class="mb-1 mr-1 btn btn-xs btn-outline-primary">
                              <i class="fa fa-eye"/>
                            </a>
                          </xsl:if>
                          <xsl:if test="sec:hasAccess(.,'EDITER_DOCUMENT_REF')">
                            <a href="/reference-document/edit?id={{{{item.id}}}}&amp;{root_page/full}" class="mb-1 mr-1 btn btn-xs btn-outline-success">
                              <i class="fa fa-edit"/>
                            </a>
                            <a href="/reference-document/delete?id={{{{item.id}}}}&amp;{root_page/full}" class="mb-1 mr-1 btn btn-xs btn-outline-danger" onclick="return confirm('Voulez-vous supprimer ce document de r&#xE9;f&#xE9;rence ?');">
                              <i class="fa fa-trash"/>
                            </a>
                          </xsl:if>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div class="d-block p-4 card-footer">
              <div ng-if="vm.amount_left &gt; 0" class="badge badge-pill badge-success">Total : {{vm.amount_left_in_human}}</div>
              <div ng-if="!(vm.amount_left &gt; 0)" class="badge badge-pill badge-danger">Total : {{vm.amount_left_in_human}}</div>
            </div>
          </div>
          <div class="row mt-3" ng-if="vm.items.length &gt; 0">
            <div class="col-sm-12 col-md-5">
              <div class="dataTables_info" id="example_info" role="status" aria-live="polite">Affichant de {{vm.firstPosition}} &#xE0; {{vm.lastPosition}} - {{vm.totalCount}} &#xE9;l&#xE9;ments</div>
            </div>
            <div class="col-md-7">
              <ul uib-pagination="" first-text="Premier" last-text="Dernier" previous-text="Pr&#xE9;c&#xE9;dent" next-text="Suivant" total-items="vm.totalCount" ng-model="vm.currentPage" items-per-page="vm.nbItemsPerPage" max-size="vm.pageSize" num-pages="vm.pagesCount" class="pagination-md float-right" rotate="false" boundary-links="true" force-ellipses="true" ng-change="vm.pageChanged()"/>
            </div>
          </div>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="custom-script">
    <script type="text/javascript"><![CDATA[
				var app = angular.module("app", ['ui.bootstrap']);			
	
				app.controller("AppCtrl", ["$scope", "$http", "$httpParamSerializerJQLike", function ($scope, $http, $httpParamSerializerJQLike) {
					   var vm = this;

		               vm.nbItemsPerPageChanged = function(newnb) {
		               		vm.search();
		               }
		               
		               vm.filterChanged = function(filter) {
		               		vm.search();
		               }
		               
		               vm.search = function() {							
				            var config = {
				                params: {
				                    page: vm.currentPage,
				                    nbperpage: vm.nbItemsPerPage,
				                    filtercontains: vm.filterContains,
				                    filternotcontains: vm.filterNotContains,
				                    editbegindate: vm.editbegindate,
				                    editenddate: vm.editenddate,
				                    statusid: vm.statusId,
				                    sorterfieldid: vm.sorterFieldId,
				                    sorterdirectionid: vm.sorterDirectionId
				                }
				            };
				
				            vm.loadingData = true;
				            return $http.get('/reference-document/to-pay/search', config).then(
						            function(response){
						            	vm.loadingData = false;
						            	
						            	vm.totalCount = response.data.count;						            
							            vm.items = response.data.items;
							            vm.total_amount = response.data.total_amount;
						                vm.total_amount_in_human = response.data.total_amount_in_human;
						                vm.amount_selected = response.data.amount_selected;
						                vm.amount_selected_in_human = response.data.amount_selected_in_human;
						                vm.amount_left = response.data.amount_left;
						                vm.amount_left_in_human = response.data.amount_left_in_human;
							            vm.firstPosition = vm.nbItemsPerPage * (vm.currentPage - 1) + 1;
							            vm.lastPosition = vm.firstPosition + vm.items.length - 1;
						            },
						            function(error){
						            	vm.loadingData = false;
						            }
				            );
				       }
				       
				       vm.selectSelection = function() {
					      vm.loadingSelectSelection = true;
					   	  select([], function() {
					   	  	vm.loadingSelectSelection = false;
					   	  });
					   }
					   
					   vm.selectMultiple = function() {
					      var docids = [];
					      vm.items.forEach(function(item, index) {
					      	if(item.selected) {
					      	   docids.push(item.id);
					      	}
					      });
					      if(docids.length == 0) {
					      	return;
					      }
					      vm.loadingSelectMultiple = true;
					   	  select(docids, function() {
					   	  	vm.loadingSelectMultiple = false;
					   	  });
					   }
					   
					   vm.selectOne = function(item) {
					   	   item.loadingSelectOne = true;
					   	   select([item.id], function() {
					   	   	 item.loadingSelectOne = false;
					   	   });
					   }
					   
         			   function select(docids, callback) {							
				            var data = {
			                    page: vm.currentPage,
			                    nbperpage: vm.nbItemsPerPage,
			                    filtercontains: vm.filterContains,
			                    filternotcontains: vm.filterNotContains,
			                    editbegindate: vm.editbegindate,
			                    editenddate: vm.editenddate,
			                    statusid: vm.statusId,
			                    sorterfieldid: vm.sorterFieldId,
			                    sorterdirectionid: vm.sorterDirectionId,
			                    refdocids: docids
			                };
			                
				            return $http(
					            {
								    url: '/reference-document/select',
								    method: 'POST',
								    data: $httpParamSerializerJQLike(data),
								    headers: {
								      'Content-Type': 'application/x-www-form-urlencoded'
								    }
								}
							).then(
					            function(response){
					            	vm.totalCount = response.data.count;						            
						            vm.items = response.data.items;
						            vm.total_amount = response.data.total_amount;
						            vm.total_amount_in_human = response.data.total_amount_in_human;
						            vm.amount_selected = response.data.amount_selected;
						            vm.amount_selected_in_human = response.data.amount_selected_in_human;
						            vm.amount_left = response.data.amount_left;
						            vm.amount_left_in_human = response.data.amount_left_in_human;
						            vm.selectAll = false;
						            vm.firstPosition = vm.nbItemsPerPage * (vm.currentPage - 1) + 1;
						            vm.lastPosition = vm.firstPosition + vm.items.length - 1;
						            callback();
					            },
					            function(error){
					                callback();
					                toastr.error(error.data.message);
					            }
				            );
				       }
				        
          			   vm.selectAllChanged = function(selected){
          			   	   vm.items.forEach(function(item, index) {
          			   	   		item.selected = selected;
          			   	   });
          			   }
          			   
		               vm.pageChanged = function(){
		               		vm.search();
		               };	
		               
		               vm.status = [
			                    ]]><xsl:for-each select="reference_document_statuss/reference_document_status">
			                    {
			                    	id: '<xsl:value-of select="id"/>',
			                    	name: '<xsl:value-of select="name"/>'
			                    },
			                    </xsl:for-each><![CDATA[
			           ];
			           
			           vm.fields = [
			                    ]]><xsl:for-each select="fields/field">
			                    {
			                    	id: '<xsl:value-of select="id"/>',
			                    	name: '<xsl:value-of select="name"/>'
			                    },
			                    </xsl:for-each><![CDATA[
			           ];             		              
		               
					   this.$onInit = function(){
						    vm.totalCount = 0;
			                vm.firstPosition = 0;
			                vm.lastPosition = 0;
			                vm.nbperpageoptions = [10, 25, 50, 100];
					   	    vm.nbItemsPerPage = 25;
					   	    vm.currentPage = 1;
					   	    vm.pageSize = 5;
					   	    vm.sorterFieldId = "DATE";
					   	    vm.sorterDirectionId = "DESC";
					   	    vm.statusId = "NONE";
					   	    vm.amountSelected = "O FCFA";
					   	    vm.search();
					   };
			    }]);	
				
				angular.bootstrap(document, ['app']);			
        ]]></script>
  </xsl:template>
</xsl:stylesheet>
