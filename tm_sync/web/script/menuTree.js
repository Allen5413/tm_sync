$.extend($.fn.tree.methods, {
    /**
     * 激活复选框
     * @param {Object} jq
     * @param {Object} target
     */
    enableCheck : function(jq, target) {
        return jq.each(function(){
            var realTarget;
            if(typeof target == "string" || typeof target == "number"){
                realTarget = $(this).tree("find",target).target;
            }else{
                realTarget = target;
            }
            var ckSpan = $(realTarget).find(">span.tree-checkbox");
            if(ckSpan.hasClass('tree-checkbox-disabled0')){
                ckSpan.removeClass('tree-checkbox-disabled0');
            }else if(ckSpan.hasClass('tree-checkbox-disabled1')){
                ckSpan.removeClass('tree-checkbox-disabled1');
            }else if(ckSpan.hasClass('tree-checkbox-disabled2')){
                ckSpan.removeClass('tree-checkbox-disabled2');
            }
        });
    },
    /**
     * 禁用复选框
     * @param {Object} jq
     * @param {Object} target
     */
    disableCheck : function(jq, target) {
        return jq.each(function() {
            var realTarget;
            var that = this;
            var state = $.data(this,'tree');
            var opts = state.options;
            if(typeof target == "string" || typeof target == "number"){
                realTarget = $(this).tree("find",target).target;
            }else{
                realTarget = target;
            }

            var ckSpan = $(realTarget).find(">span.tree-checkbox");
            ckSpan.removeClass("tree-checkbox-disabled0").removeClass("tree-checkbox-disabled1").removeClass("tree-checkbox-disabled2");
            if(ckSpan.hasClass('tree-checkbox0')){
                ckSpan.addClass('tree-checkbox-disabled0');
            }else if(ckSpan.hasClass('tree-checkbox1')){
                ckSpan.addClass('tree-checkbox-disabled1');
            }else{
                ckSpan.addClass('tree-checkbox-disabled2')
            }
            if(!state.resetClick){
                $(this).unbind('click').bind('click', function(e) {
                    var tt = $(e.target);
                    var node = tt.closest('div.tree-node');
                    if (!node.length){return;}
                    if (tt.hasClass('tree-hit')){
                        $(this).tree("toggle",node[0]);
                        return false;
                    } else if (tt.hasClass('tree-checkbox')){
                        if(tt.hasClass('tree-checkbox-disabled0') || tt.hasClass('tree-checkbox-disabled1') || tt.hasClass('tree-checkbox-disabled2')){
                            $(this).tree("select",node[0]);
                        }else{
                            if(tt.hasClass('tree-checkbox1')){
                                $(this).tree('uncheck',node[0]);
                            }else{
                                $(this).tree('check',node[0]);
                            }
                            return false;
                        }
                    } else {
                        $(this).tree("select",node[0]);
                        opts.onClick.call(this, $(this).tree("getNode",node[0]));
                    }
                    e.stopPropagation();
                });
            }
        });
    }
});

var MenuTree = new _menutree();
function _menutree(){
    var contextPath;

    var treeId;

    var rowNum;

    /**
     * 初始化菜单树
     */
    this.initMenuTree = function(data){
        $(MenuTree.treeId).tree({
            lines: true,
            onBeforeExpand: function(node){
                //如果该节点没打开过，就去打开。
                if(typeof(node.attributes) != "undefined" && !node.attributes.isOpen){
                    node.attributes.isOpen = true;
                    MenuTree.getMenuData(node);
                }
            },
            onCheck : function(node, checked){
                $(MenuTree.treeId).tree("expand", node.target);
                //设置值
                MenuTree.setMenuValue();
            }
        }).tree("loadData", data);
    }

    /**
     * 点击菜单节点，获取他下面的资源节点
     */
    this.getMenuData = function(selected){
        var random = Math.ceil(Math.random()*10000);
        $.ajax({
            url: MenuTree.contextPath+"/findResourceByMenuId/doFindResourceByMenuId.htm?random="+random+"&menuId="+selected.id,
            async : false,
            success : function(data){
                var resources = [];
                for(var i=0; i<data.resourceList.length; i++){
                    var resource = {
                        'id' : data.resourceList[i].id,
                        'text' : data.resourceList[i].name
                    };
                    resources.push(resource);
                }
                MenuTree.addTreeNode(selected, resources);
            }
        });
    }

    /**
     * 打开编辑时，勾选复选框
     */
    this.setMenuTreeCheckbox = function(selectResources){
        //debugger;
        var nodes = $(MenuTree.treeId).tree("getRoots");
        if(nodes.length > 0){
            for(var i=0; i<nodes.length; i++){
                var node = nodes[i];
                var id = node.id;
                if(selectResources.length > 0){
                    for(var j=0; j<selectResources.length; j++){
                        var selectResource = selectResources[j];
                        //如果资源关联得菜单ID和当前节点的菜单id相同，说明选中的资源就是该菜单下的
                        if(id == selectResource.menuId){
                            //展开菜单节点
                            $(MenuTree.treeId).tree("expand", node.target);
                            //遍历该菜单下的资源节点，如果和选中的资源匹配就勾选复选框
                            var childNodes = $(MenuTree.treeId).tree("getChildren", node.target);
                            if(childNodes.length > 0){
                                for(var k=0; k < childNodes.length; k++){
                                    var childNode = childNodes[k];
                                    if(childNode.id == selectResource.id){
                                        $(MenuTree.treeId).tree("check", childNode.target);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 禁止复选框，如果其他运费选择了的区域，后面运费就不能在选择相同的区域
     */
    this.disableAreaTreeCheckbox = function(node){
        //得到当前运费选中的区域
        var nowAreaId = $("#areaIds"+AreaTree.rowNum).val();
        //得到当前模板所有的区域
        var areaIdAll = AreaTree.getCheckAreaIdAll();
        //除去当前选中的区域后剩下的所选择的区域
        var noNowAreaId = areaIdAll.replace(nowAreaId, "");
        //判断除去当前选中的区域中的一级区域，如果有，就禁止这些一级区域的checkbox
        var noNowAreaIds = noNowAreaId.split(",");
        if(noNowAreaIds.length > 0){
            for(var j=0; j<noNowAreaIds.length; j++){
                if(null == node){
                    //得到所有一级节点
                    var rootNodes = $(AreaTree.treeId).tree("getRoots");
                    for(var k=0; k<rootNodes.length; k++){
                        var rootNode = rootNodes[k];
                        if(rootNode.id.substring(0,2) == noNowAreaIds[j].substring(0,2)){
                            $(AreaTree.treeId).tree('disableCheck',rootNode.target);//禁用
                        }
                    }
                }
                if(null != node){
                    //得到所有一级节点
                    var childrenNodes = $(AreaTree.treeId).tree("getChildren", node.target);
                    for(var k=0; k<childrenNodes.length; k++){
                        var childrenNode = childrenNodes[k];
                        if(childrenNode.id == noNowAreaIds[j]){
                            $(AreaTree.treeId).tree('disableCheck',childrenNode.target);//禁用
                        }
                    }
                }
            }
        }
    }


    /**
     * 增加树节点
     * @param selected
     * @param dataJSON
     */
    this.addTreeNode = function (selected, dataJSON){
        $(MenuTree.treeId).tree('append', {
            parent: selected.target,
            data: dataJSON
        });
        MenuTree.setMenuValue();
    }

    /**
     * 选择菜单时，把选中的资源id赋值到文本域
     */
    this.setMenuValue = function (){
        var nodes = $(MenuTree.treeId).tree('getChecked');
        var resourceIds = "";
        if(nodes.length > 0){
            for(var i=0; i < nodes.length; i++){
                var node = nodes[i];
                //菜单不用记录，只记录资源
                if(typeof(node.attributes) == "undefined"){
                    var resourceId = node.id;
                    resourceIds += resourceId+",";
                }
            }
        }
        $("#resourceIds").val(resourceIds.substring(0, resourceIds.length-1));
    }


    /**
     * 得到模板选择的所有区域
     */
    this.getCheckAreaIdAll = function (){
        var areaIdAll = "";
        $("[name=areaIds]").each(function(){
            areaIdAll += $(this).val()+",";
        });
        return areaIdAll.substring(0, areaIdAll.length-1);
    }
}
