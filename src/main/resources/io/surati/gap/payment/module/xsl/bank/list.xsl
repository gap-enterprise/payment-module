<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sec="http://www.surati.io/Security/User/Profile" version="2.0">
  <xsl:output method="html" include-content-type="no" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
  <xsl:include href="/io/surati/gap/web/base/xsl/layout.xsl"/>
  <xsl:template match="page" mode="head">
    <title>
      <xsl:text>GAP - Banques</xsl:text>
    </title>
  </xsl:template>
  <xsl:template match="page" mode="header">
    <div class="app-page-title app-page-title-simple">
      <div class="page-title-wrapper">
        <div class="page-title-heading">
          <div class="page-title-icon">
            <i class="lnr-apartment icon-gradient bg-night-fade"/>
          </div>
          <div>
            <xsl:text>Banques</xsl:text>
            <div class="page-title-subheading opacity-10">
              <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                  <li class="breadcrumb-item">
                    <a href="/home">
                      <i aria-hidden="true" class="fa fa-home"/>
                    </a>
                  </li>
                  <li class="active breadcrumb-item">
                    <xsl:text>Banques</xsl:text>
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
          <xsl:text>Liste des banques</xsl:text>
        </div>
        <xsl:if test="sec:hasAccess(.,'CONFIGURER_BANQUES')">
          <div class="btn-actions-pane-right">
            <a href="/bank/edit" type="button" class="btn-icon btn-wide btn-outline-2x btn btn-outline-focus btn-sm d-flex">
              <xsl:text>Nouveau</xsl:text>
              <span class="pl-2 align-middle opacity-7">
                <i class="fa fa-plus"/>
              </span>
            </a>
          </div>
        </xsl:if>
      </div>
      <div class="card-body">
        <div class="row" ng-if="vm.loadingData">
          <div class="col-sm-12 text-center">
            <h4 class="text-muted">Chargement des donn&#xE9;es... <small>Veuillez patienter</small></h4>
            <img src="/io/surati/gap/web/base/img/loader.gif" width="250"/>
          </div>
        </div>
        <div ng-if="!vm.loadingData">
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
                <input type="search" class="form-control form-control-sm" placeholder="Saisir Code banque, Abr&#xE9;g&#xE9;, Intitul&#xE9;" aria-controls="example" ng-model="vm.filter" ng-model-options="{{ debounce: 1000 }}" ng-change="vm.filterChanged(vm.filter)" aria-describedby="search-addon"/>
                <div class="input-group-append">
                  <span class="input-group-text" id="search-addon">
                    <i class="fa fa-search"/>
                  </span>
                </div>
              </div>
            </div>
          </div>
          <h6 class="text-center pb-1 pt-1" ng-if="vm.items.length == 0">
            <xsl:text>Il n'y a aucune banque trouv&#xE9;e.</xsl:text>
          </h6>
          <div class="row" ng-if="vm.items.length &gt; 0">
            <div class="col-sm-12 col-md-12">
              <div class="table-responsive">
                <table class="table table-hover table-striped table-bordered table-sm dataTable dtr-inline">
                  <thead>
                    <tr>
                      <th>N&#xB0;</th>
                      <th>Code banque</th>
                      <th>Abr&#xE9;g&#xE9;</th>
                      <th>Intitul&#xE9;</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr ng-repeat="item in vm.items">
                      <td>
			                    {{ vm.firstPosition + $index }}
			                  </td>
                      <td>
			                    {{ item.code }}
			                  </td>
                      <td>
			                    {{ item.abbreviated }}
			                  </td>
                      <td>
			                    {{ item.name }}
			                  </td>
                      <td>
                        <div role="group">
                          <a href="/bank/view?id={{{{item.id}}}}" class="mb-2 mr-2 btn btn-sm btn-outline-primary">
                            <i class="fa fa-eye"/>
                          </a>
                          <xsl:if test="sec:hasAccess(.,'CONFIGURER_BANQUES')">
                            <a href="/bank/edit?id={{{{item.id}}}}" class="mb-2 mr-2 btn btn-sm btn-outline-success">
                              <i class="fa fa-edit"/>
                            </a>
                            <a href="/bank/delete?id={{{{item.id}}}}" class="mb-2 mr-2 btn btn-sm btn-outline-danger" onclick="return confirm('Voulez-vous supprimer cette banque ?');">
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
	
				app.controller("AppCtrl", ["$scope", "$http", function ($scope, $http) {
					   var vm = this;
		                   
		               vm.totalCount = 0;
		               
		               vm.firstPosition = 0;
		               vm.lastPosition = 0;
		               
		               vm.nbperpageoptions = [10, 25, 50, 100];
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
				                    filter: vm.filter
				                }
				            };
				
				            vm.loadingData = true;
				            return $http.get('/bank/search', config).then(
						            function(response){
						            	vm.loadingData = false;
						            	
						            	vm.totalCount = response.data.count;						            
							            vm.items = response.data.items;
							            vm.firstPosition = vm.nbItemsPerPage * (vm.currentPage - 1) + 1;
							            vm.lastPosition = vm.firstPosition + vm.items.length - 1;
						            },
						            function(error){
						            	vm.loadingData = false;
						            }
				            );
				        }
         
		               vm.pageChanged = function(){
		               		vm.search();
		               };		             		              
		               
					   this.$onInit = function(){
					   					   	    
					   	    vm.nbItemsPerPage = 10;
					   	    vm.pageSize = 5;
					   	    vm.currentPage = 1;
					   	    
					   	    vm.search();
					   };
			    }]);	
				
				angular.bootstrap(document, ['app']);			
        ]]></script>
  </xsl:template>
</xsl:stylesheet>
