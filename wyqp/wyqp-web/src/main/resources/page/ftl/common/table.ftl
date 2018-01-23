<#assign contextPath = request.contextPath />
<!-- table 插件  CSS 文件 -->
<!-- table 插件 js 文件 -->
<script src="${contextPath}/resources/bootstrap-3.3.5/js/bootstrap-table.min.js?v=b4666afe4e"></script>
<!-- table 插件 locale 国际化 js 文件 -->
<script src="${contextPath}/resources/bootstrap-3.3.5/js/bootstrap-table-zh-CN.min.js?v=7426a14aa4"></script>
<table class="table table-hover table-condensed table-striped"
	data-locale="zh-CN"
	data-pagination="true" 
	data-side-pagination="server" 
	data-click-to-select="true" 
	data-smart-display="true" 
	data-pagination-first-text="首页" 
	data-pagination-pre-text="上一页" 
	data-pagination-next-text="下一页" 
	data-pagination-last-text="尾页" 
	data-page-number="1" 
	data-page-size="10" 
	data-page-list="[5,10,20,50,100]">
</table>
<script type="text/javascript">
	/*
	 * onSort，Fires when user sort a column
	 * name: the sort column field name
	 * order: the sort column order
	 */
	$('.table.table-striped').on('sort.bs.table', function(e, name, order) {
		//设置从起始页开始
		$(this).bootstrapTable('getOptions').pageNumber = 1;
	});
</script>
