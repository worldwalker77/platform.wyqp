<!DOCTYPE html>
<html>
  <head>
    <title>嘻哈纸牌</title>
    <meta charset="utf-8">
	<#include "ftl/common/common.ftl"/>
     
  </head>
    
  <body  style="overflow:auto">
		<div id="crash-container" class="container-fluid" style="padding-left:0;padding-right:0;padding-buttom:0">
		<div class="panel panel-default">
			<div class="panel-body">
			
					<div class="row" style="margin-top:5px"> 
							
						<div class="col-xs-3">
							<span>开始时间 </span>
						</div>
						<div class="col-xs-4">
							<input size="16" type="text" id="startDate" readonly class="form_datetime" value="${startDate}">
						</div>
						<div class="pull-right">
							<div class="col-xs-1">
								<button type="reset" class="btn btn-sm btn-primary" id="resetBtn">
									<i></i>清空
								</button>
							</div>
						</div>
						
					</div>
					
					<div class="row" style="margin-top:5px"> 
						<div class="col-xs-3">
							<span>结束时间</span>
						</div>
						<div class="col-xs-4">
							<input size="16" type="text" id="endDate" readonly class="form_datetime" value="${endDate}">
						</div>
						<div class="pull-right">
							<div class="col-xs-1">
								<button type="button" class="btn btn-sm btn-primary " id="searchBtn">
									<i></i>查询
								</button>
							</div>
						</div>
					</div>
					
					
				</div>
			</div>
		</div>

    	<!-- table 插件 -->
		<#include "ftl/common/table.ftl"/>
	
	<script type="text/javascript" src="/resources/js/wechat/billingDetails.js?version=${version}"></script>
  </body>
</html>

