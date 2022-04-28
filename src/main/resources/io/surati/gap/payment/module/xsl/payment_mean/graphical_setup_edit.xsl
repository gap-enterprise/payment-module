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
  <xsl:include href="/io/surati/gap/web/base/xsl/layout.xsl"/>
  <xsl:template match="page" mode="head">
    <title>
      <xsl:text>GAP - Moyen de paiement</xsl:text>
    </title>
  </xsl:template>
  <xsl:template match="page" mode="header">
    <style type="text/css">
    	.canvas {
		    border: 1px solid black;
		}
    </style>
    <div class="app-page-title app-page-title-simple">
      <div class="page-title-wrapper">
        <div class="page-title-heading">
          <div class="page-title-icon">
            <i class="lnr-inbox icon-gradient bg-night-fade"/>
          </div>
          <div>
            <xsl:text>Moyen de paiement</xsl:text>
            <xsl:choose>
              <xsl:when test="holder_id">
                <div class="page-title-subheading opacity-10">
                  <nav class="" aria-label="breadcrumb">
                    <ol class="breadcrumb">
                      <li class="breadcrumb-item">
                        <a href="/home">
                          <i aria-hidden="true" class="fa fa-home"/>
                        </a>
                      </li>
                      <li class="breadcrumb-item">
                        <a href="/third-party">Tiers</a>
                      </li>
                      <li class="breadcrumb-item">
                        <a href="/third-party/view?id={holder_id}">
                          <xsl:value-of select="holder_name"/>
                        </a>
                      </li>
                      <li class="active breadcrumb-item" aria-current="page">
                        <xsl:text>Configurer manuellement</xsl:text>
                      </li>
                    </ol>
                  </nav>
                </div>
              </xsl:when>
              <xsl:otherwise>
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
                      <li class="breadcrumb-item">
                        <a href="/bank/payment-mean/view?id={item/id}&amp;bank={item/bank_id}">
                          <xsl:value-of select="item/name"/>
                        </a>
                      </li>
                      <li class="active breadcrumb-item" aria-current="page">
                        <xsl:text>Configurer graphiquement</xsl:text>
                      </li>
                    </ol>
                  </nav>
                </div>
              </xsl:otherwise>
            </xsl:choose>
          </div>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="body">
    <div class="main-card mb-3 card">
      <div class="card">
        <div class="card-body">
          <form action="/bank/payment-mean/graphical-setup/save" method="post">
            <input name="id" type="hidden" value="{item/id}"/>
            <input name="bank_id" type="hidden" value="{item/bank_id}"/>
            <xsl:for-each select="payment_mean_fields/payment_mean_field">
              <input id="{type_id}_x" name="{type_id}_x" value="{x_px}" type="number" class="form-control-xs" step="any" hidden=""/>
              <input id="{type_id}_y" name="{type_id}_y" value="{y_px}" type="number" class="form-control-xs" step="any" hidden=""/>
              <input id="{type_id}_width" name="{type_id}_width" value="{width_px}" type="number" class="form-control-xs" step="any" hidden=""/>
            </xsl:for-each>
            <canvas id="canvas-bank-note" class="canvas" width="{item/width_px}" height="{item/height_px}"/>
            <div class="divider"/>
            <div class="clearfix">
              <button type="submit" class="btn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-primary">
                <xsl:text>Enregistrer </xsl:text>
                <i class="fa fa-check"/>
              </button>
              <button type="button" class="btn-shadow float-right btn-wide btn-pill mr-1 btn btn-outline-secondary" onclick="location.href='/bank/payment-mean/view?id={item/id}&amp;bank={item/bank_id}'">
                <xsl:text>Annuler </xsl:text>
                <i class="fa fa-undo"/>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="page" mode="custom-script">
    <script src="/io/surati/gap/web/base/js/fabric.min.js"/>
    <script type="text/javascript"><![CDATA[		
			(function() {
			  	var canvas = this.__canvas = new fabric.Canvas('canvas-bank-note');
			  
			  	// Create a new instance of the Image class
			  	var img = new Image();

			  	// When the image loads, set it as background image
				img.onload = function() {
				    var f_img = new fabric.Image(img);
				
				    canvas.setBackgroundImage(f_img);
				
				    canvas.renderAll();
				};

				// Define the Data URI
				var myDataURL = "]]><xsl:value-of select="image"/><![CDATA[";
				
				// Set the src of the image with the base64 string
				img.src = myDataURL;
				var fields = [
					]]><xsl:for-each select="payment_mean_fields/payment_mean_field">
					    {
					        x : <xsl:value-of select="x_px"/>,
					        y : <xsl:value-of select="y_px"/>,
					    	width: <xsl:value-of select="width_px"/>,
					    	name: "<xsl:value-of select="name"/>",
					    	id: "<xsl:value-of select="type_id"/>",
					    	isVisible: <xsl:value-of select="visible"/>
					    },
		            </xsl:for-each><![CDATA[
				];
				fields.forEach(function(item, index) {
				  if(item.isVisible) {
				  	  var text = new fabric.Text(item.name, { fontSize: 30});
					  var rect = new fabric.Rect({
					    originX: 'left',
					    originY: 'top',
					    width: item.width,
					    height: 35,
					    fill: 'rgba(255,0,0,0.5)',
					    transparentCorners: false
					  });
					  var group = new fabric.Group([rect, text], {
					      left: item.x,
					      top: item.y - 35
					  });
					  group.name = item.id;
					  canvas.add(group);
					  group['setControlVisible']('tr', false);
					  group['setControlVisible']('br', false);
					  group['setControlVisible']('mt', false);
					  group['setControlVisible']('mb', false);
					  group['setControlVisible']('tl', false);
					  group['setControlVisible']('bl', false);
					  group['setControlVisible']('mtr', false);
				  }
				});
			    canvas.on('object:modified', function (options) {
		          var coords = canvas.getActiveObject().oCoords;
		          console.log(options.target.name);
		          var input_x = document.getElementById(options.target.name + "_x");
		          input_x.setAttribute("value", coords.bl.x);
		          var input_y = document.getElementById(options.target.name + "_y");
		          input_y.setAttribute("value", coords.bl.y);
		          var input_width = document.getElementById(options.target.name + "_width");
		          input_width.setAttribute("value", coords.br.x - coords.bl.x);
		        });
		        canvas.renderAll();
			})();			
        ]]></script>
  </xsl:template>
</xsl:stylesheet>
