<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<#include "ftl/common/common.ftl"/>

<style>
	.form{margin-left:50px;margin-top:50px}
	.customer-select-active{background-color:#B8CAD6}
	.line-spacing{margin-top:3px}
	#editor {overflow:scroll; max-height:1000px;height:200px;margin:0}
</style>

<title>版本控制</title>
</head>
<body>
		<!-- 定义表单结构   
		<div id="version-upload" class="form">  
		    <form name="versionUploadForm" action="${contextPath}/backend/versioncontroll/" method="post" enctype="multipart/form-data"> 
		    		<div>
		    			<label for="multipartFiles">选择要上传的im客户端文件:</label>
		            	<input id="multipart-files" type="file" name="multipartFiles" />
		    		</div>
		            <div>
		            	<label for="clientVersion">版本号:</label>
		           		<input id="client-version" type="text" name="clientVersion" />
		            </div>  
		            <div>
		            	<label for="changeLog">增加的特性:</label>
		            	<input id="change-log" type="text" name="changeLog" style="width:1000px" />
		            </div>
		            <input id="submit-button" type="button" value="提交" /> 
		    </form>  
		</div>--><!-- /多文件上传 --> 
		
		
		
		<div id="version-container" class="container-fluid" style="padding-left:0;padding-right:0">
			<div class="panel panel-default">
				<div class="panel-body">
					<form name="versionUploadForm" enctype="multipart/form-data">
						<div class="row">
							<div class="col-xs-3">
								<div class="input-group">
									<span class="input-group-addon input-sm">版本号</span> <input
										type="text" class="form-control" id="client-version"  name="clientVersion"
										maxlength="15" />
								</div>
								<!-- /input-group -->
							</div>
							<div class="col-xs-3">
								<div class="input-group">
									<span class="input-group-addon input-sm">上传客户端文件</span> <input
										type="file" class="form-control" id="multipart-files"  name="multipartFiles"
										maxlength="15" />
								</div>
								<!-- /input-group -->
							</div>
						</div>	
						<div class="row" style="margin-top:5px"> 
							<div class="col-xs-9">
								<div class="input-group">
									<span class="input-group-addon input-sm">新特性</span> <input
										type="text" class="form-control" id="change-log"  name="changeLog"
										/>
								</div>
								<!-- /input-group -->
							</div>
							<div class="pull-right">
								<div class="col-xs-1">
									<button type="reset" class="btn btn-sm btn-primary" id="resetBtn">
										<i class="glyphicon glyphicon-remove"></i>清空
									</button>
								</div>
								<!-- /.col-xs-1 -->
							</div>
							<div class="pull-right">
								<div class="col-xs-1">
									<button type="button" class="btn btn-sm btn-primary " id="submit-button">
										<i class="glyphicon glyphicon-search"></i>提交
									</button>
								</div>
								<!-- /.col-xs-1 -->
							</div>
						</div>
					</form>
				</div>
			</div>
			<!-- /.panel-body -->
			<!-- table 插件 -->
			<#include "ftl/common/table.ftl"/>
		</div>
		
		<script src="${contextPath}/resources/bootstrap-3.3.5/js/bootstrap-wysiwyg.js"></script>
		<script src="${contextPath}/resources/bootstrap-3.3.5/js/jquery.hotkeys.js"></script>
		<script src="${contextPath}/resources/js/utils/qiao.js"></script>
		<script src="${contextPath}/resources/js/utils/common.js"></script>
		<script src="${contextPath}/resources/bootstrap-3.3.5/js/bootstrap-typeahead2.js"></script>
		<script src="${contextPath}/resources/js/utils/commonUtil.js"></script>
		<script src="${contextPath}/resources/js/backend/versioncontroll.js"></script>
		
</body>
</html>
