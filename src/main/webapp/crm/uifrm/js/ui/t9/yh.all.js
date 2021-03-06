(function($) {
  window.YH = {
    version: "2.1",
    /**
     * 注册组件类型,为了能够使用xtype快速初始化组件
     */
    register: {},
    /**
     * 是否为手持设备
     */
    isTouchDevice: 'ontouchstart' in window,
    fitTouchDevice: function() {
      function simMouseEvt(type, target, pos) {
        var evt = document.createEvent('MouseEvents');
        evt.initMouseEvent(type, true, true, document, 
            0, pos.screenX, pos.screenY, pos.clientX, pos.clientY, 
            false, false, false, false, 
            0, target);
        return target.dispatchEvent(evt);
      }
      document.addEventListener("touchstart", function(e) {
        if (!simMouseEvt("mousedown", e.target, e.changedTouches[0])) {
          e.preventDefault();
          setTimeout(function() {
            if (e.target instanceof HTMLElement && !e.target.getAttribute("yhmovement")) {
              //simMouseEvt("mouseup", e.target, e.changedTouches[0]);
              simMouseEvt("click", e.target, e.changedTouches[0]);
            }
          }, 300);
        }
      });
      document.addEventListener("touchmove", function(e) {
        e.target instanceof HTMLElement && e.target.setAttribute("yhmovement", "move");
        simMouseEvt("mousemove", e.target, e.changedTouches[0]) || e.preventDefault();
      });
      document.addEventListener("touchend", function(e) {
        var timer;
        if (e.target instanceof HTMLElement) {
          if (e.target.getAttribute("yhdbclick")) {
            clearTimeout(timer);
            e.target.removeAttribute("yhdbclick");
            simMouseEvt("dblclick", e.target, e.changedTouches[0]);
          }
          else {
            e.target.setAttribute("yhdbclick", "dbclick");
            timer = setTimeout(function() {
              e.target.removeAttribute("yhdbclick");
            }, 1000);
          }
          e.target.removeAttribute("yhmovement");
        }
        simMouseEvt("mouseup", e.target, e.changedTouches[0]) || e.preventDefault();
      });
    },
    /**
     * 控制台
     * 对浏览器控制台进行容错包装
     * 替代出错时的alert,提升用户体验
     */
    console: {
      log: window.console && window.console.log || $.noop,
      info: window.console && window.console.info || $.noop,
      warn: window.console && window.console.warn || $.noop,
      error: window.console && window.console.error || $.noop,
      assert: window.console && window.console.assert || $.noop,
      dir: window.console && window.console.dir || $.noop,
      clear: window.console && window.console.clear || $.noop,
      profile: window.console && window.console.profile || $.noop,
      profileEnd: window.console && window.console.profileEnd || $.noop
    },
    /**
     * 解析JSON,支持畸形的JSON字符串
     * @param text        支持畸形的JSON字符串
     * @return            JSON对象
     */
    parseJson: function (text) {
      try {
        return eval("(" + text+ ")");
      } catch (e) {
        return {};
      }
    },
    /**
     * 浏览器信息
     */
    browser: {
      isIE6: $.browser.msie && /^6./.test($.browser.version),
      isIE7: $.browser.msie && /^7./.test($.browser.version)
    },
    /**
     * 设置所有的动画效果开关
     * @param open          boolean
     */
    fx: function(open) {
      $.fx.off = !open;
    },
    /**
     * 规格化JSON
     * @param t             规格化的模板JSON
     *                      支持正则表达式!
     * @param o             需要被规格化的JSON
     * @return              规格化后的数据
     */
    formatJson: function (t, o) {
      if (!o || !t) {
        return;
      }
      var r = {};
      for (var k in t) {
        var v = t[k];
        //当属性是数组的时候递归调用规格化函数
        if (v && v instanceof RegExp) {
          if (o[k] && v.test(o[k])) {
            o[k] && (r[k] = o[k]);
          }
          else {
            return;
          }
        }
        else if (v && v instanceof Array) {
          if (!o[k]) {
            continue;
          }
          r[k] = [];
          $.each(o[k], function(i, e) {
            r[k].push(YH.formatJson(t, e));
          });
          r[k] = $.grep(r[k], function(e, i) {
            return !!e;
          });
        }
        else if (v && typeof v === "object") {
          o[k] && (r[k] = YH.formatJson(v, o[k]));
        }
        else if (v === "*") {
          r[k] = o[k];
        }
        else if (v === "not") {
          
        }
        else {
          o[k] && (r[k] = o[k]);
        }
      }
      return r;
    },
    /**
     * 注册组件
     */
    components: {},
    /**
     * 布局
     */
    layouts: {
      register: {}
    },
    /**
     * 通过id获取组件
     * @param id
     * @return            组件
     */
    getCmp: function(id) {
      return YH.components[id];
    },
    /**
     * 创建组件时自动调用的方法
     */
    addCmp: function(id, cmp) {
      if (typeof id == "string" || typeof id == "number"){
        YH.components[id] = cmp;
      }
    },
    /**
     * 销毁组件时自动调用的方法
     */
    removeCmp: function(id) {
      delete YH.components[id];
    },
    /**
     * 自动生成的ID,当用户没有定义控件的ID时使用自动生成的ID
     * @return autoID
     */
    getAutoID: function(prefix) {
      YH.autoID = YH.autoID || 0;
      YH.autoID++;
      return (prefix || "yhAuto") + YH.autoID;
    },
    /**
     * json对象转化为String
     * @param o          JSON对象,对象属性可以是函数
     * @return             JSON字符串
     */
    jsonToString: function(o){
      var self = this;
      switch(typeof(o)){
        case "string":
            return '"' + o.replace(/(["\\])/g, "\\$1") + '"';
        case "function":
            return o;
        case "array":
            return "[" + o.map(self.jsonToString).join(",") + "]";
        case "object":
            if(o instanceof Array){
              var strArr = [];
              var len = o.length;
              for(var i = 0; i < len; i++){
                  strArr.push(self.jsonToString(o[i]));
              }
              return "[" + strArr.join(",") + "]";
            }
            else if(o == null){
              return "null";
            }
            else{
              var string = [];
              for (var k in o) {
                string.push(self.jsonToString(k) + ":" + self.jsonToString(o[k]));
              }
              return "{" + string.join(",") + "}";
            }
        case "number":
            return o;
        case false:
            return o;
        default:
            return o;
      }
    },
    /**
     * 向元素中添加属性
     * @param el          [html元素,jquery元素,css选择器]
     * @param pros        属性的哈希表{"height": "100px", "src": "http://..."}
     */
    addPros: function(el, pros) {
      var el = $(el);
      if (!el[0] || !pros) {
        return false;
      }
      var tagName = el[0].tagName;
      
      //定义dom元素合法的属性
      var elPros = {
        "BODY": ["style"],
        "DIV": ["style", "onclick"],
        "SPAN": ["style"],
        "A": ["href", "style", "target", "onclick"],
        "IMG": ["style", "src"]
      };
      
      $.each(pros, function(k, v) {
        if ($.inArray(k, elPros[tagName] || []) >= 0) {
          el.attr(k, v);
        }
      });
    },
    /**
     * 组合函数
     */
    compose: function(cmp, subcmp) {
      var o = {};
      subcmp.compose() && $.each(subcmp.compose(), function(k, v) {
        if (typeof v === "function") {
          o[k] = function() {
            v.apply(subcmp, arguments);
          }
        }
      });
      $.extend(cmp, o);
    },
    /**
     * 判断是否为YH组件
     */
    isCmp: function(o) {
      return !!(YH.register[o && o.xtype] && o.status && o.render);
    },
    /**
     * 创建组件的函数[Panel, Window, Grid, ImgBox, Container...]
     * *****************重要方法*******************************
     * @arguments[0]          类型["Panel", "Window"...]
     * @arguments[1]          默认构造参数
     * @arguments[2~N]        组件的方法或公用数据 
     */
    createComponent: function() {
      var type = arguments[0], opts = arguments[1] || {}, i = 2, length = arguments.length;
      if (length > 0) {
        if (typeof name === "string") {
          this[type] = function(cfg) {
            cfg = cfg || {};
            cfg.status = cfg.status || {};
            //设置true参数,值传递
            //如果不设置true会导致多个控件的items引用同一个数组!!!
            $.extend(true, this, opts);
            //不设置true参数,地址传递
            //当Panel的items传递给Container再传递给layout,这样引用都是同一个数组,方便操作!
            $.extend(this, cfg);
            this.initialize();
            return (this.members && this.members()) || this;
          }
          for (;i < length; i++) {
            $.extend(this[type].prototype, arguments[i]);
          }
        }
        var xtype = type.toLowerCase();
        this.register[xtype] = this[type];
        this[type].prototype.xtype = xtype;
      }
    },
    /**
     * 包装方法,将传入的DOM元素/jQuery元素/选择器包装成组件
     * 可以直接通过items的方式加入容器组件中
     */
    packCmp: function(cfg) {
      cfg = cfg || {};
      cfg.el = $(cfg.el || cfg.html || "<div></div>")
      cfg.cls && cfg.el.addClass(cfg.cls);
      cfg.style && cfg.el.css(cfg.style);
      cfg.renderTo && cfg.el.appendTo(cfg.renderTo);
      
      return {
        el: cfg.el,
        render: function(el) {
          if (el) {
            cfg.el.appendTo(el);
          }
        }
      };
    },
    zIndex: 1,
    maxZIndex: 9999,
    /**
     * 获取某一层的更大的z-index值(z-index的管理分为三个层)
     */
    topZIndex: function(layer) {
      YH.zIndex++;
      return {
        "upper": YH.zIndex + 5000,
        "middle": YH.zIndex + 3000,
        "lower": YH.zIndex + 1000
      } [layer] || YH.zIndex;
    },
    /**
     * 关闭/打开js错误提示函数
     * @param type        close: 关闭错误
     *                    其他: 开启错误
     */
    error: function(type) {
      if (type == "close") {
        YH.error.originalEvent = YH.error.originalEvent || window.onerror;
        window.onerror = function(e, url, line) {
          YH.console.error(url + "  " + line + "  " + e);
          return true;
        };
      }
      else {
        window.onerror = YH.error.originalEvent;
      }
    }
  };
  
  /**
   * 创建Component的超类,包括组件共有的方法
   */
  YH.createComponent("Component", null, {
    /**
     * 默认调用的方法,统一由Component超类调用
     */
    "initialize": function() {
    
      this.status.initialized = false;
      //初始化组件容器
      this.el = $(this.el || "<div></div>")
      this.id = this.id || YH.getAutoID(this.autoIdPrefix);
      this.el.attr("id", this.id);
      //注册组件
      YH.addCmp(this.id, this);
      
      //调用初始化事件方法
      this.doListeners();
      this.doLoader();
      
      //统一调用render方法
      this.renderTo && this.render(this.renderTo);
      this.cls && this.el.addClass(this.cls);
      this.parentCmp && this.parentCmp.addItems(this);
      
      //调用组件子类的初始化方法
      this.initComponent();
      this.style && this.el.css(this.style);
      
      this.doDraggable();
      this.doResizable();
      this.doInitStatus();
      this.status.initialized = true;
    },
    /**
     * 初始化组件状态
     * 初始化函数的最后!!
     * (比如窗口默认最大化之类的问题)
     */
    doInitStatus: function() {
      
    },
    /**
     * 组件子类的初始化方法
     */
    "initComponent": $.noop,
    /**
     * 重新加载组件(处理的不够全面)
     */
    "reload": function() {
      this.doLoader();
      this.refresh();
    },
    /**
     * 刷新组件(不重新加载数据)
     */
    "refresh": function() {
      this.empty();
      this.initComponent();
    },
    /**
     * 处理拖动
     */
    "doDraggable": function() {
      if (this.draggable) {
        if (!this.modal) {
          var mask = $(".draggable-mask");
          if (!mask.length) {
            mask = $("<div class=\"draggable-mask\"></div>")
            $("body").append(mask);
          }
          this.draggable.start = function(e, ui) {
            mask.css({
              "z-index": self.zIndex
            });
            mask.show();
          }
        }
        
        this.el.draggable(this.draggable);
        var self = this;
        this.el.bind("dragstop", function() {
          self.left = self.el.css("left");
          self.top = self.el.css("top");
          if (!self.modal) {
            mask.hide();
          }
        });
        this.drag = {
          "destroy": function() {
            self.el.draggable("destroy");
          },
          "disable": function() {
            self.el.draggable("disable");
          },
          "enable": function() {
            self.el.draggable("enable");
          },
          /**
           * 设置/获取拖拽的选项
           * @param name          选项名称
           * @param value         选项值,当不传递值时方法为获取拖拽选项
           */
          "option": function(name, value) {
            return self.el.draggable("option", name, value); 
          }
        };
        this.status.draggable = true;
      }
    },
    /**
     * 处理resize
     */
    "doResizable": function() {
      if (this.resizable) {
        this.el.resizable(this.resizable);
        var self = this;
        this.resize = {
          "destroy": function() {
            self.el.resizable("destroy");
          },
          "disable": function() {
            self.el.resizable("disable");
          },
          "enable": function() {
            self.el.resizable("enable");
          },
          /**
           * 设置/获取拖拽的选项
           * @param name          选项名称
           * @param value         选项值,当不传递值时方法为获取拖拽选项
           */
          "option": function(name, value) {
            return self.el.resizable("option", name, value); 
          }
        };
        this.status.resizable = true;
      }
    },
    /**
     * 初始化事件
     */
    "doListeners": function() {
      if (this.listeners) {
        var self = this;
        $.each(this.listeners, function(k, v) {
          if (typeof v === "function") {
            YH.Event.add(self, "bind", k, false, v);
          }
          else if (typeof v === "object"){
            $.each(v, function(key, value) {
              YH.Event.add(self, "bind", k, key, value);
            });
          }
        });
      }
    },
    /**
     * 清空组件dom元素
     */
    "empty": function() {
      this.el.empty();
    },
    /**
     * 销毁组件
     */
    "destroy": function() {
      if (this.items) {
        var items = this.items.slice();
        //这里不直接用this.items循环
        //因为this.items在this.parentCmp.removeItem(this, true)
        //后长度发生变化,$.each找到的项就不正确了

        $.each(items, function(i, e) {
          e && e.destroy && e.destroy();
        });
      }
      if (this.parentCmp) {
        //第二个参数true表示不再次调用组件的destroy
        this.parentCmp.removeItem(this, true);
      }
      this.el.empty();
      this.el.remove();
      YH.removeCmp(this.id);
      
    },
    /**
     * 隐藏组件
     */
    "hide": function(speed, callback) {
      if (this.status.hidden) {
        return;
      }
      this.hideEffect.call(this, speed, callback);
      this.status.hidden = true;
    },
    /**
     * 隐藏的效果(提供给用户自定义效果的借口)
     */
    "hideEffect": function(speed, callback) {
      this.el.hide(speed);
      callback && callback();
    },
    "addClass": function() {
      this.el.addClass.apply(this.el, arguments)
    },
    "removeClass": function() {
      this.el.removeClass.apply(this.el, arguments)
    },
    /**
     * 待进一步处理
     */
    "changeStyle": function(cls) {
      this.addClass(cls);
    },
    /**
     * 显示组件
     */
    "show": function() {
      if (this.status.hidden) {
        this.status.hidden = false;
        this.showEffect.apply(this, arguments);
      }
    },
    /**
     * 用户可以自定义显示的特效
     */
    showEffect: function() {
      this.el.show.apply(this.el, arguments);
    },
    "setPosition": function(pos) {
      this.el.offset(pos);
    },
    /**
     * 组件间组合的借口函数
     * @param el          将组件的DOM元素添加到节点el上
     *                    [DOM元素,JQUERY元素,JQUERY选择器]
     */
    "fadeIn": function() {
      this.el.fadeIn.apply(this.el, arguments)
    },
    "fadeOut": function(speed) {
      this.el.fadeOut.apply(this.el, arguments);
    },
    "render": function(el) {
      if (el) {
        this.el.appendTo(el);
        this.parentEl = el;
      }
    },
    /**
     * 获取组件ID
     */
    "getId": function() {
      return this.id;
    },
    /**
     * 修改组件ID,同时修改注册的组件ID
     */
    "setId": function(id) {
      YH.removeCmp(this.id);
      this.id = id;
      YH.addCmp(this.id, this);
    },
    /**
     * 获取配置参数中的宽度
     */
    "getWidth": function() {
      return this.width;
    },
    /**
     * 获取配置参数中的高度
     */
    "getHeight": function() {
      return this.height;
    },
    /**
     * 获取组件实际高度
     */
    "getAbsHeight": function() {
      return this.el.height() + "px";
    },
    /**
     * 获取组件实际宽度
     */
    "getAbsWidth": function() {
      return this.el.width() + "px";
    },
    "getPosX": function() {
      return this.el.position().left;
    },
    "getPosY": function() {
      return this.el.position().top;
    },
    "setStyle": function(style) {
      this.el.css(style);
    },
    /**
     * 配置数据Loader
     * 兼容YH.Loader类型和{url:""...}类型
     */
    "doLoader": function() {
      if (!this.loader || typeof this.loader !== "object") {
        return;
      }
      var o;
      this.loader.param = {
        totalRecords: this.maxRecords || 0
      }
      if (this.loader.xtype === "loader") {
        o = this.loader.load();
      }
      else {
        var loader = new YH.Loader(this.loader);
        o = loader.load();
      }
      
      this.data = o && o.data;
      
      this.pageInfo = o && o.pageInfo;
    },
    /**
     * 获取控件的父元素(jquery元素)
     */
    "getParentEl": function(flag) {
      if (flag) {
        return this.el.parent();
      }
      return this.parentEl || this.el.parent()[0] ? this.el.parent() : $("body");
    },
    /**
     * 控件事件的实际载体
     * @return              jQuery对象
     */
    "getEvtActor": function() {
      return this.evtActor || this.el;
    },
    "setEvtActor": function(e) {
      this.evtActor = e;
    },
    /**
     * 设置控件的公有函数和只读属性
     */
    "members": $.noop,
    /*
    "members": function() {
      var self = this;
      return {
        render: self.render,
        reload: self.reload
      }
    },
    */
    /**
     * 控件组合函数,定义组合控件时子控件对父控件透明的函数或者只读属性
     */
    "compose": $.noop
  });
  
  
  /**
   * Loader组件,目前只支持JSON格式数据
   */
  YH.Loader = function(cfg) {
    var opts = {
      "url": "",
      "dataType": "json",
      "type": "POST",
      "param": {},
      "dataRender": null,
      "success": null,
      async: false,
      "error": null
    };
    $.extend(true, this, opts, cfg);
    this.initialize();
  };
  
  $.extend(YH.Loader.prototype, {
    initialize: function() {
    },
    load: function() {
      var t = this;
      var dataType = this.dataType;
      if (this.dataType.toLowerCase() == "json") {
        dataType = "text";
      }
      var data;
      $.ajax({
        type: t.type,
        url: t.url,
        async: t.async,
        dataType: dataType,
        data: t.param,
        success: function(text) {
          if (t.dataType.toLowerCase() == "json") {
            data = (t.dataRender || (function(data) {
              if (data.rtState == "0") {
                data = data.rtData;
              }
              else {
                console.info(data.rtMsrg);
              }
              return data;
            })) (YH.parseJson(text));
          }
          t.success && t.success(data);
        },
        error: t.error
      });
      return {
        data: data
      };
    }
  });
  
  /**
   * 适应各种控件的插件,是一种方便形式
   */
  YH.Plugins = {
  };
  
  /**
   * 使用jQuery对象装载事件,对jQuery事件机制简单封装

   */
  YH.Event = {
    /**
     * 向控件添加事件的方法
     * @param cmp                 控件的引用
     * @param name                事件名称
     * @param moment              触发的时刻[before, after, ""]
     *                            当moment为空时,代表事件不是由控件的标准函数触发
     * @param event               事件的函数,参数为(event, 控件的引用, 设置事件时自定义的参数)
     * @param scope               事件函数执行时的this
     * @param params              自定义的事件函数的参数

     */
    add: function(cmp, type, name, moment, event, scope, params) {
      if (cmp.getEvtActor && ((cmp[name] && (moment === "before" || moment === "after")) || !moment)) {
          var evtName = name + (moment || "");
          if (type == "bind") {
            cmp.getEvtActor().bind(evtName, {
              cmp: cmp,
              params: params
            }, function(e) {
              var args = Array.prototype.slice.call(arguments, 0);
              args.push(e.data.cmp, e.data.params);
              event.apply(scope || cmp, args);
            });
          }
          else if (type == "one"){
            cmp.getEvtActor().one(evtName, {
              cmp: cmp,
              params: params
            }, function(e) {
              event.call(scope || cmp, args);
            });
          }
        if (moment === "before" || moment === "after") {
          if (cmp[name] && !cmp[name].original) {
            var tmp = cmp[name];
            cmp[name] = function() {
              cmp.getEvtActor().trigger(name + "before");
              cmp[name].original.apply(cmp, arguments);
              cmp.getEvtActor().trigger(name + "after");
            }
            cmp[name].original = tmp;
          }
        }
      }
    },
    /**
     * 删除事件,目前是删除按name和moment匹配的所有事件
     * @param cmp                 控件的引用
     * @param name                事件名称
     * @param moment              触发的时刻[before, after, ""]
     */
    del: function(cmp, name, moment) {
      //当没传递moment时,删除dom事件;当传递moment时,判断是否有自定义事件,如果有则删除
      if (cmp.getEvtActor && (cmp[name] && (moment === "before" || moment === "after") || !moment)) {
        var evtName = name + (moment || "");
        cmp.getEvtActor().unbind(evtName);
      }
    }
  };
  
  YH.Tools = {
    "toggle": {
      defaultHandler: function(event, toolEl, panel, params) {
        panel.toggle();
      }
    },
    "close": {
      title: "关闭",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.close();
      }
    },
    "minimize": {
      title: "最小化",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.minimize();
      }
    },
    "maximize": {
      title: "全屏最大化",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.tools["restore"] && panel.tools["restore"].show();
        panel.tools["plus"] && panel.tools["plus"].show();
        toolEl.hide();
        panel.maximize();
      }
    },
    "restore": {
      title: "还原",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.tools["maximize"] && panel.tools["maximize"].show();
        panel.tools["plus"] && panel.tools["plus"].show();
        toolEl.hide();
        panel.restore();
      }
    },
    "gear": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "pin": {
      title: "置顶",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.tools["pin"] && panel.tools["pin"].hide();
        panel.tools["unpin"] && panel.tools["unpin"].show();
        panel.pin();
      }
    },
    "unpin": {
      title: "浮动",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.tools["pin"] && panel.tools["pin"].show();
        panel.tools["unpin"] && panel.tools["unpin"].hide();
        panel.unpin();
      }
    },
    "right": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "left": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "up": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "down": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "refresh": {
      title: "刷新",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.refresh();
        panel.status.hidden && panel.show();
      }
    },
    "minus": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    }, 
    "plus": {
      title: "工作区最大化",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.tools["restore"] && panel.tools["restore"].show();
        panel.tools["maximize"] && panel.tools["maximize"].show();
        toolEl.hide();
        panel.maximize("parent");
      }
    },
    "help": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "search": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "save": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "more": {
      title: "更多",
      defaultHandler: function(event, toolEl, panel, params) {
      }
    }
  };
  
  if (YH.isTouchDevice) {
    YH.fitTouchDevice();
  }
  
  function parseFeatures(features) {
    if (!features) {
      return;
    }
    var cfg = {};
    var pros = features.split(/[,;]/);
    for (var i = 0; i < pros.length; i++) {
      if (pros[i]) {
        var kv = pros[i].split(/[=:]/);
        if (kv.length > 1) {
          var key = {
            dialogheight: "height",
            dialogwidth: "width",
            //dialogleft: "left",
            //dialogtop: "top",
            height: "height",
            width: "width",
            resizable: "resizable"
          } [kv[0].toLowerCase()];
          if (key) {
            cfg[key] = kv[1];
          }
        }
      }
    }
    return cfg;
  }
  
  if (top == window && YH.isTouchDevice) {
    window.showModalDialog = function(url, args, features) {
      var win;
      var cfg = {
        src: url,
        cls: "",
        layer: "upper",
        modal: true,
        draggable: {
          containment: false
        },
        listeners: {
          doContainer: {
            after: function(e, t) {
              try {
                var i = t.container.iframeDom;
                var w = i.contentWindow;
                w.dialogArguments = args;
                var id = t.getId();
                i.onload = function() {
                  w.dialogArguments = w.dialogArguments || args;
                  try {
                    var s = w.document.createElement("script");
                    s.innerHTML = "function close() {parent.YH.getCmp('" + id + "').close();}";
                    var head = w.document.getElementsByTagName("head").item(0)
                    s.setAttribute("type", "text/javascript"); 
                    head.appendChild(s);
                  } catch (e) {
                    w.close = function() {
                      t.close();
                    }
                  } finally {
                    
                  }
                }
              } catch (e) {
                
              } finally {
                
              }
            }
          }
        }
      };
      
      $.extend(true, cfg, parseFeatures(features));
      win = new YH.Window(cfg);
      win.show();
    }
    window.open = function (url, name, features, replace) {
      var self = this;
      var cfg = {
        src: url,
        title: name,
        width: 800,
        height: 600,
        layer: "upper",
        draggable: {
          containment: false
        },
        resizable: {},
        listeners: {
          doContainer: {
            after: function(e, t) {
              try {
                var i = t.container.iframeDom;
                var w = i.contentWindow;
                w.opener = self;
                var id = t.getId();
                i.onload = function() {
                  try {
                    w.opener = w.opener || self;
                    var s = w.document.createElement("script");
                    s.innerHTML = "function close() {parent.YH.getCmp('" + id + "').close();}";
                    var head = w.document.getElementsByTagName("head").item(0)
                    s.setAttribute("type", "text/javascript");
                    head.appendChild(s);
                  } catch (e) {
                    try {
                      w.close = function() {
                        t.close();
                      }
                    } catch (e) {
                      d.d
                      YH.console.info(e);
                    } finally {
                      
                    }
                  } finally {
                    
                  }
                }
              } catch (e) {
                
              } finally {
                
              }
            }
          }
        }
      };
      $.extend(true, cfg, parseFeatures(features));
      var win = new YH.Window(cfg);
      win.show();
    }
    
  }
  else if (YH.isTouchDevice){
    window.showModalDialog = function () {
      top.YH && top.showModalDialog.apply(this, arguments);
    }

    window.open = function () {
      top.YH && top.open.apply(this, arguments);
    }
  }
})(jQuery);
(function($) {
  var autoOpts = {
    el: $('<div></div>'),
    items: [],
    cmpCls: 'jq-autolayout'
  };
  
  YH.createComponent.call(YH.layouts, "AutoLayout", autoOpts, {
    initialize: function() {
      this.el.addClass(this.cmpCls);
      this.initComponent();
      this.style && this.el.css(this.style);
      //this.doDroppable();
    },
    initComponent: function() {
      this.renderItems();
      var self = this;
      $.each(this.items, function(i, e) {
        self.doItem(e);
      });
    },
    doDroppable: function(item) {
      if (this.droppable) {
        if (item) {
          var self = this;
          item.contentEl.droppable(this.droppable);
          item.contentEl.addClass(this.droppableCls);
          item.setEvtActor(item.contentEl);
          
          YH.Event.del(item, "drop");
          YH.Event.add(item, 'bind', 'drop', null, function(evt, ui, t) {
            if (ui.draggable.filter(self.droppable.accept)[0]) {
              self.droppable.dropStop && self.droppable.dropStop(evt, ui, t);
            }
          });
        }
        /*暂时不用
        var dropCtn = this.el.find('.' + this.droppableCls);
        if (!dropCtn[0] && this.el.filter('.' + this.droppableCls)) {
          dropCtn = this.el;
        }
        $(dropCtn).droppable(this.droppable);
        */
        
        //droppable的drop停止事件
        var drop = this.droppable.drop;
        this.droppable.drop = null;
        var self = this;
        $.each(this.droppableCmps, function(i, e) {
          e.contentEl.droppable(self.droppable);
          e.contentEl.addClass(self.droppableCls);
          e.setEvtActor(e.contentEl);
          YH.Event.add(e, 'bind', 'drop', null, function(evt, ui, t) {
            if (ui.draggable.filter(self.droppable.accept)[0]) {
              drop && drop(evt, ui, t);
            }
          });
        });
        /*
        //扩展droppable中的drop事件,增加控件的参数传递

        $(dropCtn).bind('drop', function(e, ui) {
          if (!cmp) {
            for(var e in self.items) {
              if (YH.isCmp(e) && e.contentEl == ui.draggable) {
                cmp = e;
                break;
              }
            }
          }
          alert(cmp)
          self.droppable.drop && self.droppable.drop(e, ui, cmp);
        });*/
      }
    },
    doSortable: function() {
      if (!this.sortable) {
        return;
      }
      var self = this;
      var cfg = {
        /**
         * 处理拖拽内容宽度为%的情况         */
        start: function(e, ui) {
          if (/%/.test(ui.item.css('width'))) {
            ui.item.data('sortWidth', ui.item.css('width'));
            ui.item.css({
              'width': ui.placeholder.innerWidth()
            });
          
            ui.placeholder.css({
              'height': ui.item.innerHeight()
            });
            ui.item.addClass('ui-draggable-dragging');
          }
        },
        stop: function(e, ui) {
          if (ui.item.data('sortWidth')) {
            ui.item.removeData('sortWidth');
            ui.item.css({
              'width': ui.item.data('sortWidth')
            });
            ui.item.removeClass('ui-draggable-dragging');
          }
          //排序结束的触发事件
          self.listeners && self.listeners.sortStop && self.listeners.sortStop(e, ui);
        },
        containment: self.el,
        handle: '.drag-handle'
      };
      $.extend(cfg, self.sortable);
      
      $.each(this.sortableCmps, function(i, e) {
        e.contentEl.addClass(self.sortableCls);
      });
      
      if (self.sortConnect) {
        cfg.connectWith = self.el.find('.' + self.sortableCls);
      }
      
      this.el.find('.' + this.sortableCls).sortable(cfg);
      
      $.each(this.sortableCmps, function(i, e) {
        e.setEvtActor(e.contentEl);
        YH.Event.add(e, 'bind', 'sortremove', null, function(e, ui, cmp) {
          cmp.removeItem(YH.getCmp(ui.item.attr('id')), true);
        });
        
        YH.Event.add(e, 'bind', 'sortreceive', null, function(e, ui, cmp) {
          cmp.receiveItem(YH.getCmp(ui.item.attr('id')));
        });
        
        e.el.css({
          'overflow': 'visible'
        });
      });
      self.el.css({
        'overflow-y': 'hidden',
        'overflow-x': 'hidden'
      });
    },
    doItem: function(e) {
      if (YH.isCmp(e)) {
        e.setStyle({
          width: '100%'
        });
      }
    },
    /**
     * 添加内部组件
     */
    addItems: function(items) {
      if (items instanceof Array) {
        var self = this;
        $.each(items, function(i, e) {
          self.addItems(e);
        });
      }
      else {
        items = this.renderItem(items);
        this.doItem(items);
        this.items.push(items);
      }
    },
    /**
     * 移除所有内部组件
     */
    removeItems: function() {
      var temp = this.items.slice();
      $.each(temp, function(i, e) {
        if (YH.isCmp(e)) {
          e.destroy();
        }
        else if (e.el) {
          e.el.remove();
        }
      });
      this.items.splice(0);
    },
    /**
     * 载入内部组件
     */
    renderItems: function(items) {
      var self = this;
      if (items instanceof Array) {
        this.removeItems();
      }
      else {
        items = this.items;
      }
      
      
      if ($.isArray(this.items) && !this.status.renderItems) {
        $.each(items, function(i, e) {
          self.items[i] = self.renderItem(e);
        });
        this.status.renderItems = true;
      }
    },
    renderItem: function(item, renderTo) {
      if (!item) {
        return;
      }
      if (!YH.isCmp(item) && !item.render) {
        if (!item.xtype && item.constructor == Object ) {
          item.xtype = 'container';
        }
        if (typeof item.xtype === 'string') {
          item = new YH.register[item.xtype](item);
        }
      }
      
      if (YH.isCmp(item)) {
        item.render(renderTo || this.el);
        item.parentCmp = this.owner;
      }
      else if (item.render){
        item.render(renderTo || this.el);
      }
      else {
        item.parentCmp = this.owner;
      }
      this.doItem(item);
      return item;
    },
    /**
     * 接收组件,组件已经存在并且已经在dom节点中插入到this.el
     */
    receiveItem: function(item) {
      item.parentCmp = this.owner;
      item.parentEl = this.el;
      this.items.push(item);
    },
    /**
     * 移除组件
     * @param item          [number/cmp]
     * @param flag          false  调用item的destroy方法
     *                      true   只在this.items中移除item
     */
    removeItem: function(item, flag) {
      var self = this;
      if (typeof item === 'number') {
        item = this.items[item];
        this.items.splice(item, 1);
      }
      else {
        $.each(this.items, function(i, e) {
          if (e == item) {
            self.items.splice(i, 1);
          }
        });
      }
      
      if (!flag) {
        if (YH.isCmp(item)) {
          item.destroy();
        }
        else if (item.el) {
          item.el.remove();
        }
      }
    },
    /**
     * 子控件按照dom中的顺序排序(仅在autolayout中排序才有意义)
     * @param deep        是否深度排序(是否子控件迭代排序)
     */
    sortItems: function(deep) {
      $.each(this.items, function(i, e) {
        e.sort = e.parentEl && e.parentEl.children().index(this.el) || 0;
        deep && e.items && e.sortItems && e.sortItems(deep);
      });
      this.items.sort(function(a, b) {
        return a.sort - b.sort;
      });
    },
    /**
     * 打开/关闭设计模式
     * @param flag        true或者不传递:打开设计模式
     *                    false:关闭设计模式
     */
    designLayout: function(flag) {
      if (flag === false) {
        //关闭设计模式,待实现
      }
      else {
        this.designMode = true;
        this.doDesign();
      }
    },
    doDesign: $.noop,
    /**
     * 把container的items处理移交到layout中     */
    compose: function() {
      var self = this;
      return {
        addItems: self.addItems,
        renderItems: self.renderItems,
        renderItem: self.renderItem,
        removeItem: self.removeItem,
        receiveItem: self.receiveItem,
        sortItems: self.sortItems,
        removeItems: self.removeItems,
        designLayout: self.designLayout
      }
    }
  });
}) (jQuery);(function($) {
  var opts = {
    el: $('<div></div>'),
    activeItem: 0,
    tabs: false,
    lazyLoad: false,
    fit: true,
    loadHandler: [],
    sp: '&nbsp;|&nbsp;'
  };
  YH.createComponent.call(YH.layouts, "CardLayout", opts, YH.layouts.AutoLayout.prototype, {
    initComponent: function() {
      var self = this;
      var activeItem = this.activeItem || 0;
      this.lastActive = this.activeItem;
      var lazyLoad = this.lazyLoad;
      var reload = this.reload;
      this.loadHandler = this.loadHandler || [];
      var loadHandler = this.loadHandler;
      
      if (this.items && this.items.length) {
        $.each(this.items, function(i, e) {
          if (activeItem != i && (e.src || e.iframeSrc)) {
            e.lazySrc = e.src || e.iframeSrc;
            e.src = e.iframeSrc = "about:blank";
          }
        });
        this.renderItems();
        $.each(this.items, function(i, e) {
          e.setStyle({
            'width': '100%'
          });
          if (self.fit) {
            e.setStyle({
              'height': '100%'
            });
          }
          if (activeItem != i) {
            e.hide();
          }
          else {
            if (lazyLoad) {
              if (loadHandler[i]) {
                loadHandler[i](e, i);
                eloaded = true;
              }
            }
          }
          
          if (!lazyLoad) {
            if (loadHandler[i]) {
              loadHandler[i](e, i);
            }
          }
        });
      }
      //兼容以前的cardlayout
      else {
        $.each(this.el.children(), function(i, e) {
          $(e).css({
            'width': '100%',
            'height': '100%'
          });
          if (activeItem != i) {
            $(e).hide();
          }
          else {
            if (lazyLoad) {
              if (loadHandler[i]) {
                loadHandler[i]($(e), i);
                $(e).data('loaded', true);
              }
            }
          }
          
          if (!lazyLoad) {
            if (loadHandler[i]) {
              loadHandler[i]($(e), i);
            }
          }
        });
      }
      this.doTabs();
    },
    /**
     * 处理cardlayout的标签问题


     */
    doTabs: function() {
      if (this.tabs) {
        var self = this;
        var tabHeader = new YH.Container({
          layout: 'floatlayout',
          height: 'auto',
          cls: 'tab-header',
          items: []
        });
        
        //判断是否是panel类的容器
        if (self.owner.owner) {
          //解决了tabpanel的内容高度显示不全的问题!!!
          self.owner.owner.tabHeader = tabHeader;
          self.owner.owner.contentEl.before(tabHeader.el);
        }
        else {
          self.owner.el.prepend(tabHeader.el);
        }
        if (this.items && this.items.length) {
          $.each(this.items, function(i, e) {
            if (e.tabBtn) {
              if (self.activeItem == i) {
                e.tabBtn.status = e.tabBtn.status || {};
                e.tabBtn.status.isPressed = true;
              }
              e.tabBtn.style = e.tabBtn.style || {};
              e.tabBtn.style["float"] = 'left';
              e.tabBtn.style.width = 'auto';
              e.tabBtn.handler = function() {
                if (self.items[self.lastActive].tabBtn) {
                  tabHeader.items[self.lastActive].setStatus('default');
                }
                if (self.items[i].tabBtn) {
                  tabHeader.items[i].setStatus('active');
                }
                self.setActiveItem(i);
              }
              tabHeader.addItems(self.items[i].tabBtn);
            }
            else {
              var a = $('<a href="javascript:void(0)"></a>');
              a.html(e.title || ('tab' + i));
              a.click(function() {
                self.setActiveItem(i)
              });
              tabHeader.el.append(a);
            }
          });
        }
      }
    },
    setActiveItem: function(i) {
      if (this.items[i].src == "about:blank" && this.items[i].src != this.items[i].lazySrc) {
        this.items[i].iframeDom.src = this.items[i].src = this.items[i].iframeSrc = this.items[i].lazySrc;
      }
      this.el.children().eq(this.lastActive).hide();
      this.el.children().eq(i).show();
      this.lastActive = i;
      
      var el = this.el.children().eq(i);
      if (this.lazyLoad && this.reload) {
        if (this.loadHandler[i]) {
          this.loadHandler[i](el, i);
        }
      }
      else if (this.lazyLoad){
        if (this.loadHandler[i] && !el.data('loaded')) {
          this.loadHandler[i](el, i);
          el.data('loaded', true)
        }
      }
    }
  });
})(jQuery);(function($) {
  var opts = {
    el: $('<div></div>'),
    items: [],
    draggable: {
      containment: 'parent'
    },
    resizable: {},
    position: 'center',
    droppableCls: 'free-droppable',
    cmpCls: 'jq-freelayout',
    droppableCmps: []
  };
  
  YH.createComponent.call(YH.layouts, "FitLayout", opts, YH.layouts.AutoLayout.prototype, {
    doItem: function(e) {
      if (YH.isCmp(e)) {
        e.setStyle({
          width: '100%',
          zoom: 1
        });
        if (e.setHeight) {
          e.setHeight(this.el.height() - (e.weltHeight || 75));
        }
      }
    },
    receiveItem: function(e) {
      YH.layouts.AutoLayout.prototype.receiveItem.apply(this, arguments);
      if (e.setHeight) {
        e.setHeight(this.el.height() - (e.weltHeight || 75));
      }
    }
  });
})(jQuery);(function($) {
  var opts = {
    el: $('<div></div>'),
    border: '5px solid transparent',
    items: [],
    sortable: {
      revert: true,
      'tolerance': 'pointer'        //通过鼠标的位置计算拖动的位置*重要属性*
    },
    pixel: false,
    designMode: false,
    cellpadding: 0,
    cellspacing: 0,
    cmpCls: 'jq-columnlayout',
    sortableCls: 'column-sortable',
    droppableCls: 'column-droppable',
    droppableCmps: [],
    sortableCmps: [],
    style: {
      'position': 'relative',//重要!
      'overflow-y': 'auto',
      'overflow-x': 'hidden'
    }
  };
  YH.createComponent.call(YH.layouts, "ColumnLayout", opts, YH.layouts.AutoLayout.prototype, {
    initComponent: function() {
    
      this.table = $('<table></table>');
      this.table.attr('cellpadding', this.cellpadding);
      this.table.attr('cellspacing', this.cellspacing);
      this.el.append(this.table);
      this.renderCells();
      this.doSortable();
    },
    renderCells: function() {
      var self = this;
      var tr = $('<tr></tr>');
      this.table.append(tr);
      $.each(this.items, function(i, e) {
        var td = $('<td valign="top"></td>');
        
        var width;
        if (self.pixel) {
          if (e.columnWidth) {
            width = parseInt(e.columnWidth) +  'px';
          }
          else {
            width = 'auto'
          }
        }
        else {
          width = Math.floor((e.columnWidth || 0.5) * 100) + '%';
        }
        
        td.css({
          width: width
        });
        self.items[i] = e = self.renderItem(e, td);
        tr.append(td);
      });
      this.doDesign();
    },
    doDesign: function() {
      if (this.designMode) {
        
        var self = this;
        var dragColumns  = this.table[0].rows[0].cells; // first row columns, used for changing of width
        if (!dragColumns){
          return; // return if no table exists or no one row exists
        }
      
        var dragColumnNo; // current dragging column
        var dragX;        // last event X mouse coordinate
        
        var saveOnmouseup;   // save document onmouseup event handler
        var saveOnmousemove; // save document onmousemove event handler
        var saveBodyCursor;  // save body cursor property
        
        /**
        * 取消事件的默认动作,阻止事件的冒泡传递
        */
        function preventEvent(e) {
          var ev = e || window.event;
          if (ev.preventDefault){
           ev.preventDefault();
          }
          else {
           ev.returnValue = false;
          }
          if (ev.stopPropagation){
           ev.stopPropagation();
          }
          return false;
        }
      
        this.changeColumnWidth = function(no, w) {
          if (!dragColumns){
            return false;
          }
          if (no < 0){ 
            return false;
          }
           
          if (dragColumns.length < no){
            return false;
          }
          
          if (parseInt(dragColumns[no].style.width) <= -w){ 
            return false;
          }
          if (dragColumns[no+1] && parseInt(dragColumns[no+1].style.width) <= w){
            return false;
          }
          var width =parseInt(dragColumns[no].style.width) + w +'px';
          dragColumns[no].style.width = width;
          
          if (dragColumns[no+1]){
            dragColumns[no+1].style.width = parseInt(dragColumns[no+1].style.width) - w + 'px';
          }
          return true;
        }
        
        // ============================================================
        // do drag column width
        this.columnDrag = function(e) {
          var e = e || window.event;
          var X = e.clientX || e.pageX;
          if (!self.changeColumnWidth(dragColumnNo, X-dragX)) {
           // stop drag!
           self.stopColumnDrag(e);
          }
        
          dragX = X;
          // prevent other event handling
          preventEvent(e);
          return false;
        }
        
        // ============================================================
        // stops column dragging
        this.stopColumnDrag = function(e) {
          var e = e || window.event;
          if (!dragColumns) return;
        
          // restore handlers & cursor
          document.onmouseup  = saveOnmouseup;
          document.onmousemove = saveOnmousemove;
          document.body.style.cursor = saveBodyCursor;
        
          // remember columns widths in cookies for server side
          var colWidth = '';
          var separator = '';
          for (var i=0; i<dragColumns.length; i++) {
            colWidth += separator + parseInt( $(dragColumns[i]).width() );
            separator = '+';
            
            self.items[i].columnWidth = Math.floor( $(dragColumns[i]).width() * 100 / self.table.width()) / 100;
            
          }
          preventEvent(e);
        }
        
        // ============================================================
        // init data and start dragging
        this.startColumnDrag = function(e) {
          var e = e || window.event;
        
          // if not first button was clicked
          //if (e.button != 0) return;
        
          // remember dragging object
          dragColumnNo = (e.target || e.srcElement).parentNode.cellIndex;
          dragX = e.clientX || e.pageX;
        
          // set up current columns widths in their particular attributes
          // do it in two steps to avoid jumps on page!
          var colWidth = new Array();
          for (var i=0; i<dragColumns.length; i++) {
            colWidth[i] = parseInt( $(dragColumns[i]).width() );
          }
          for (var i=0; i<dragColumns.length; i++) {
            dragColumns[i].width = ""; // for sure
            dragColumns[i].style.width = colWidth[i] + "px";
          }
        
          saveOnmouseup = document.onmouseup;
          document.onmouseup = self.stopColumnDrag;
        
          saveBodyCursor = document.body.style.cursor;
          document.body.style.cursor = 'w-resize';
        
          // fire!
          saveOnmousemove = document.onmousemove;
          document.onmousemove = self.columnDrag;
        
          preventEvent(e);
        }
      
        for (var i=0; i<dragColumns.length; i++) {
          var evt = $('<div></div>');
          evt.css({
            'position': 'relative',
            'height': '20px',
            'background-color': 'gray',
            'width': '5px',
            'left': '100%',
            'z-index': YH.topZIndex(),
            'cursor': 'w-resize'
          });
          $(dragColumns[i]).prepend(evt);
          evt.mousedown(self.startColumnDrag);
        }
      }
    },
    doItem: function(e) {
      /**
       * 向子元素增加设置列宽度的函数
       */
      e.setColumnWidth = function(width) {
        e.parentEl.css({
          width: Math.floor((width || 0.5) * 100) + '%'
        });
      }
      e.leaf = true;
      //通过YH库的事件处理,避免绑定到jquery元素的事件找不到YH组件
      if (YH.isCmp(e)) {
        
        //只有可排序的列布局才会有下边距
        if (this.sortable) {
          e.contentEl.css({
            "padding-bottom": "100px"
          });
        }
        
        this.doDroppable(e);
        this.sortable && this.sortableCmps.push(e);
      }
    }
  });
  
}) (jQuery);(function($) {
  var opts = {
    el: $('<div></div>'),
    items: [],
    "float": 'left',
    droppableCls: 'float-droppable',
    cmpCls: 'jq-floatlayout'
  };
  
  YH.createComponent.call(YH.layouts, "FloatLayout", opts, YH.layouts.AutoLayout.prototype, {
    initComponent: function() {
      this.el = $(this.el);
      this.el.parent().css({
        width: 'auto'
      });
      this.doFloat();
    },
    doFloat: function() {
      var self = this;
      
      if (this.items instanceof Array && this.items.length) {
        $.each(this.items, function(i, e) {
          self.items[i] = e = self.renderItem(e);
        });
      }
      else {
        this.el.children().css({
          "float": self["float"]
        });
      }
    },
    doItem: function(e) {
      if (YH.isCmp(e)) {
        e.setStyle({
          "float": this["float"]
        });
        this.droppable && this.droppableCmps.push(e);
      }
      else {
        e.el.css({
          "float": this["float"]
        })
      }
    }
  });
})(jQuery);(function($) {
  var opts = {
    el: $('<div></div>'),
    items: [],
    draggable: {
      containment: 'parent'
    },
    resizable: {},
    position: 'center',
    droppableCls: 'free-droppable',
    cmpCls: 'jq-freelayout',
    droppableCmps: []
  };
  
  YH.createComponent.call(YH.layouts, "FreeLayout", opts, YH.layouts.AutoLayout.prototype, {
    initComponent: function() {
      this.el = $(this.el);
      if (this.droppable) {
        this.el.data('cmp', this.owner);
        this.el.addClass(this.droppableCls);
      }
      //this.doDraggable();
      //this.doResizable();
      var self = this;
      $.each(this.items, function(i, e) {
        self.items[i] = e = self.renderItem(e, i);
        self.doItem(e);
      });
      this.doDroppable(this.owner);
    },
    doDraggable: function(draggable) {
      $.extend(true, this.draggable, draggable);
      var self = this;
      if (!this.draggable) {
        return;
      }
      if (this.items instanceof Array && this.items.length) {
        $.each(this.items, function(i, e) {
          self.doItemDrag(e);
        });
      }
      else {
        this.el.children().draggable(this.draggable);
      }
    },
    doResizable: function(resizable) {
      $.extend(true, this.resizable, resizable);
      var self = this;
      if (!this.resizable) {
        return;
      }
      if (this.items instanceof Array && this.items.length) {
        $.each(this.items, function(i, e) {
          self.doItemResize(e);
        });
      }
      else {
        this.el.children().resizable(this.resizable);
      }
    },
    doItemResize: function(e) {
      if (YH.isCmp(e)) {
        if (!e.status.resizable) {
          e.resizable = {};
          $.extend(true, e.resizable, this.resizable);
          e.doResizable && e.doResizable();
        }
      }
    },
    doItemDrag: function(e) {
      if (YH.isCmp(e)) {
        if (!e.draggable) {
          e.draggable = {};
          $.extend(true, e.draggable, this.draggable);
          e.doDraggable && e.doDraggable();
        }
        else if (e.drag){
          e.drag.option('containment', 'parent');
        }
        
        if (!e.left && !e.top) {
          if (self.position == 'center') {
            e.setPosCenter();
          }
        }
        
        if (!e.status.clickActive) {
          e.clickActive && e.clickActive();
        }
        e.setStyle({
          'left': e.left,
          'top': e.top
        });
      }
    },
    doItem: function(e) {
      this.doItemDrag(e);
      this.doItemResize(e);
      if (YH.isCmp(e)) {
        e.setStyle({
          'position': 'absolute'
        });
      }
    }
  });
})(jQuery);(function($) {
  var opts = {
    el: $('<div></div>'),
    border: '5px solid transparent',
    items: [],
    sortable: {
      revert: true,
      'tolerance': 'pointer'        //通过鼠标的位置计算拖动的位置*重要属性*
    },
    cols: 3,
    cellpadding: 0,
    cellspacing: 0,
    designMode: false,
    selectable: {
      cancel: '.gridlayout-dragColumn,.extend-container,.' + this.designCls
    },
    designCls: 'gridlayout-dragColumn',
    cmpCls: 'jq-gridlayout',
    selectableCls: 'ui-selected',
    sortableCls: 'grid-sortable',
    droppableCls: 'grid-droppable',
    droppableCmps: [],
    sortableCmps: [],
    colsWidth: []
  };
  YH.createComponent.call(YH.layouts, "GridLayout", opts, YH.layouts.ColumnLayout.prototype, {
    initComponent: function() {
      YH.layouts.ColumnLayout.prototype.initComponent.call(this);
    },
    initTable: function() {
      this.tr = [];
      for (var i = 0; i < this.cols; i++ ) {
        var tr = $('<tr></tr>');
        this.tr.push(tr);
        this.table.append(tr);
        for (var j = 0; j < this.rows; j++ ) {
          var td = $('<td></td>');
          tr.append(td);
        }
      }
    },
    renderCells: function() {
      //this.initTable();
      var self = this;
      this.tr = [];
      var cols = 0;
      
      var colIndex = 0;
      var rowIndex = 0;
      
      this.tr.push({cols: this.cols, td: []});
      $.each(this.items, function(i, e) {
        e.colSpan = e.colSpan || 1;
        e.rowSpan = e.rowSpan || 1;
        e.index = i;
        e.colIndex = colIndex % self.cols;
        
        colIndex += e.colSpan
        while (colIndex > self.tr[rowIndex].cols) {
          rowIndex++;
          if (!self.tr[rowIndex]) {
            self.tr.push({cols: self.cols, td: []});
          }
          colIndex = e.colSpan;
        }
        
        e.rowIndex = rowIndex;
        if (e.rowSpan > 1) {
          for (var j = 1; j < e.rowSpan; j++) {
            if (!self.tr[rowIndex + j]) {
              self.tr.push({cols: self.cols - e.colSpan, td: []});
            }
            else {
              self.tr[rowIndex + j].cols -= e.colSpan;
            }
          }
        }
        
        self.tr[rowIndex].td.push(e);
        
        self.doItem(e);
      });
      
      var tr = $('<tr></tr>');
      tr.addClass(this.designCls);
      for (var i = 0; i < this.cols; i++) {
        var td = $('<td></td>');
        td.attr("width", self.colsWidth[i] || '');
        tr.append(td);
      }
      this.table.prepend(tr);
      
      this.renderTable();
      
      this.doDesign();
      
    },
    
    renderTable: function() {
      var self = this;
      
      $.each(this.tr, function(i, e) {
        var tr = $('<tr></tr>');
        self.table.append(tr);
        $.each(e.td, function(j, el) {
          var td = $('<td></td>');
          td.attr('valign', 'top');
          td.attr('colSpan', el.colSpan);
          td.attr('rowSpan', el.rowSpan);
          tr.append(td);
          el.layout = "fitlayout";
          el.height = el.rowSpan * 100;
          self.items[el.index] = el = self.renderItem(el, td);
          td.data('cmpId', el.id);
        });
      });
    },
    doItem: function(e) {
      if (YH.isCmp(e)) {
        e.leaf = true;
        this.sortable && this.sortableCmps.push(e);
        this.doDroppable(e);
      }
    },
    doDesign: function() {
      if (this.designMode) {
        //打开设计模式的时候也开启排序
        this.doSortable();
        this.table.selectable(this.selectable);
        this.el.addClass('gridlayout-designMode');
        this.doTipMenu();
        
        var self = this;
        var dragColumns  = this.table[0].rows[0].cells; // first row columns, used for changing of width
        if (!dragColumns){
          return; // return if no table exists or no one row exists
        }
      
        var dragColumnNo; // current dragging column
        var dragX;        // last event X mouse coordinate
        
        var saveOnmouseup;   // save document onmouseup event handler
        var saveOnmousemove; // save document onmousemove event handler
        var saveBodyCursor;  // save body cursor property
        
        /**
        * 取消事件的默认动作,阻止事件的冒泡传递

        */
        function preventEvent(e) {
          var ev = e || window.event;
          if (ev.preventDefault){
           ev.preventDefault();
          }
          else {
           ev.returnValue = false;
          }
          if (ev.stopPropagation){
           ev.stopPropagation();
          }
          return false;
        }
      
        this.changeColumnWidth = function(no, w) {
          if (!dragColumns){
            return false;
          }
          if (no < 0){ 
            return false;
          }
           
          if (dragColumns.length < no){
            return false;
          }
          
          if (parseInt(dragColumns[no].style.width) <= -w){ 
            return false;
          }
          if (dragColumns[no+1] && parseInt(dragColumns[no+1].style.width) <= w){
            return false;
          }
          var width =parseInt(dragColumns[no].style.width) + w +'px';
          dragColumns[no].style.width = width;
          
          //
          self.colsWidth[no] = width;
          
          if (dragColumns[no+1]){
            self.colsWidth[no + 1] = parseInt(dragColumns[no+1].style.width) - w + 'px';
            dragColumns[no+1].style.width = parseInt(dragColumns[no+1].style.width) - w + 'px';
          }
          return true;
        }
        
        // ============================================================
        // do drag column width
        this.columnDrag = function(e) {
          var e = e || window.event;
          var X = e.clientX || e.pageX;
          if (!self.changeColumnWidth(dragColumnNo, X-dragX)) {
           // stop drag!
           self.stopColumnDrag(e);
          }
        
          dragX = X;
          // prevent other event handling
          preventEvent(e);
          return false;
        }
        
        // ============================================================
        // stops column dragging
        this.stopColumnDrag = function(e) {
          var e = e || window.event;
          if (!dragColumns) return;
        
          // restore handlers & cursor
          document.onmouseup  = saveOnmouseup;
          document.onmousemove = saveOnmousemove;
          document.body.style.cursor = saveBodyCursor;
        
          // remember columns widths in cookies for server side
          var colWidth = '';
          var separator = '';
          for (var i=0; i<dragColumns.length; i++) {
            colWidth += separator + parseInt( $(dragColumns[i]).width() );
            separator = '+';
          }
          preventEvent(e);
        }
        
        // ============================================================
        // init data and start dragging
        this.startColumnDrag = function(e) {
          var e = e || window.event;
        
          // if not first button was clicked
          //if (e.button != 0) return;
        
          // remember dragging object
          dragColumnNo = (e.target || e.srcElement).parentNode.cellIndex;
          dragX = e.clientX || e.pageX;
        
          // set up current columns widths in their particular attributes
          // do it in two steps to avoid jumps on page!
          var colWidth = new Array();
          for (var i=0; i<dragColumns.length; i++) {
            colWidth[i] = parseInt( $(dragColumns[i]).width() );
          }
          for (var i=0; i<dragColumns.length; i++) {
            dragColumns[i].width = ""; // for sure
            dragColumns[i].style.width = colWidth[i] + "px";
          }
        
          saveOnmouseup = document.onmouseup;
          document.onmouseup = self.stopColumnDrag;
        
          saveBodyCursor = document.body.style.cursor;
          document.body.style.cursor = 'w-resize';
        
          // fire!
          saveOnmousemove = document.onmousemove;
          document.onmousemove = self.columnDrag;
        
          preventEvent(e);
        }
      
        for (var i=0; i<dragColumns.length; i++) {
          var evt = $('<div></div>');
          evt.css({
            'position': 'relative',
            'height': '20px',
            'background-color': 'gray',
            'width': '5px',
            'left': '100%',
            'z-index': YH.topZIndex(),
            'cursor': 'w-resize'
          });
          $(dragColumns[i]).empty().append(evt);
          evt.mousedown(self.startColumnDrag);
        }
      }
    },
    verifyMerge: function() {
      var self = this;
      var cls = '.' + this.selectableCls;
      var cells = 0;
      var tds = self.table.find('td' + cls);
      if (tds.length < 2) {
        return false;
      }
      var td = tds.first();
      /*
      var left = td.attr('cellIndex');
      var right = td.attr('cellIndex') + td.attr('colSpan');
      var top = td.parent().attr('rowIndex');
      var bottom = td.parent().attr('rowIndex') + td.attr('rowSpan');
      */
      var cmp = YH.getCmp(td.data("cmpId"));
      
      var top = cmp.rowIndex, left = cmp.colIndex, bottom = cmp.rowIndex + cmp.rowSpan, right = cmp.colIndex + cmp.colSpan;
      self.table.find('td' + cls).each(function(i, e) {
        var cmp = YH.getCmp($(e).data("cmpId"));
        
        left = Math.min(left, cmp.colIndex);
        
        top = Math.min(top, cmp.rowIndex);
        
        right = Math.max(right, (cmp.colIndex + cmp.colSpan));
        
        bottom = Math.max(bottom, (cmp.rowIndex + cmp.rowSpan));
        var col = e.colSpan || 0;
        var row = e.rowSpan || 0
        cells += col * row; 
        return;
        
        var rowIndex = e.parentNode.rowIndex;
        var cellIndex = e.cellIndex;
        
        if (rowIndex < top) {
          top = rowIndex;
        }
        if (rowIndex + row > bottom) {
          bottom = rowIndex + row;
        }
        if (cellIndex < left) {
          left = cellIndex;
        }
        if (cellIndex + col > right) {
          right = cellIndex + col;
        }
      });
      
      if (cells != (right - left) * (bottom - top)) {
        return false;
      }
      return true;
    },
    mergeCells: function() {
      if (!this.verifyMerge()) {
        return;
      }
      var self = this;
      var cls = '.' + this.selectableCls;
      var cols = 0, rows = 0, total = 0;
      self.table.find('td' + cls).eq(0).parent().children(cls).each(function(i, e) {
         cols += $(e).attr('colspan') || 0;
      });
      
      self.table.find('td' + cls).each(function(i, e) {
         var col = $(e).attr('colspan') || 0;
         var row = $(e).attr('rowspan') || 0;
         total += col * row;
      });
      rows = total / cols;
      var td = $('td' + cls).first();
      self.table.find('td' + cls).each(function(i, e) {
         if (i == 0) {
           $(e).attr('colspan', cols);
           $(e).attr('rowspan', rows);
           var cmp = YH.getCmp($(e).find('div:first').attr('id'));
           if (cmp) {
             cmp.colSpan = cols;
             cmp.rowSpan = rows;
             cmp.setStyle({height: rows * (100 + self.cellpadding + self.cellspacing)});
           }
         }
         else {
           var cmp = YH.getCmp($(e).find('div:first').attr('id'));
           cmp && cmp.destroy();
           $(e).remove();
         }
      });
    },
    addItem: function(renderTo) {
      renderTo = $(renderTo);
      var item = new YH.Container({
        renderTo: renderTo,
        autoIdPrefix: 'portal',
        layout: "fitlayout",
        //指定父组件,为了合并单元格的时候销毁没有用的容器
        parentCmp: this,
        style: {
          height: '100px'
        }
      });
      this.items.push(item);
      this.doItem(item);
    },
    splitCells: function() {
      var cls = '.' + this.selectableCls;
      var cols = 0, rows = 0, total = 0;
      var self = this;
      
      $('td' + cls).eq(0).parent().children(cls).each(function(i, e) {
        cols += $(e).attr('colspan') || 0;
      });
      
      $('td' + cls).each(function(i, e) {
        var col = $(e).attr('colspan') || 0;
        $(e).attr('colspan', 1);
        var cmp = YH.getCmp($(e).find('div:first').attr('id'));
        if (cmp) {
          cmp.colSpan = 1;
        }
        for (;col > 1; col--) {
          var copy = $(e).clone(true).empty();
          $(e).after(copy);
          self.addItem(copy);
        }
      });
      
      $('td' + cls).each(function(i, e) {
        var row = $(e).attr('rowspan') || 0;
        $(e).attr('rowspan', 1);
        var cmp = YH.getCmp($(e).find('div:first').attr('id'));
        if (cmp) {
          cmp.rowSpan = 1;
        }
        var tr = $(e).parent();
        for (; row > 1; row--) {
          var copy = $(e).clone().empty();
          tr = tr.next().append(copy);
          self.addItem(copy);
        }
      });
      if (self.sortable) {
        self.table.find('td').sortable(this.sortable);
      }
      this.sortItems();
      this.refresh();
    },
    unselect: function() {
      this.table.find('.' + this.selectableCls).removeClass(this.selectableCls);
    },
    sortItems: function(deep) {
      var self = this;
      $.each(this.items, function(i, e) {
        var r = e.parentEl.parent().index();
        var c = e.parentEl.index();
        e.sort = (r + 1) * self.cols + c;
        //e.sort = self.table.find('td').index(e.parentEl);
        deep && e.items && e.sortItems && e.sortItems(deep);
      });
      this.items.sort(function(a, b) {
        return a.sort - b.sort;
      });
    },
    doSortable: function() {
      //暂时gird布局的排序只在设计模式下启用
      if (!this.sortable || !this.designMode) {
        return;
      }
      var self = this;
      var cfg = {
        /**
         * 处理拖拽内容宽度为%的情况         */
        start: function(e, ui) {
          if (/%/.test(ui.item.css('width'))) {
            ui.item.data('sortWidth', ui.item.css('width'));
            ui.item.css({
              'width': ui.placeholder.innerWidth()
            });
          
            ui.placeholder.css({
              'height': ui.item.innerHeight()
            });
            ui.item.addClass('ui-draggable-dragging');
          }
        },
        stop: function(e, ui) {
          if (ui.item.data('sortWidth')) {
            ui.item.removeData('sortWidth');
            ui.item.css({
              'width': ui.item.data('sortWidth')
            });
            ui.item.removeClass('ui-draggable-dragging');
          }
          //排序结束的触发事件
          self.listeners && self.listeners.sortStop && self.listeners.sortStop(e, ui);
        },
        handle: '.drag-handle'
      };
      $.extend(cfg, self.sortable);
      
      $.each(this.sortableCmps, function(i, e) {
        e.contentEl.addClass(self.sortableCls);
      });
      
      if (self.sortConnect) {
        cfg.connectWith = self.el.find('.' + self.sortableCls);
      }
      this.el.find('.' + this.sortableCls).sortable("destroy");
      this.el.find('.' + this.sortableCls).sortable(cfg);
      
      $.each(this.sortableCmps, function(i, e) {
        e.setEvtActor(e.contentEl);
        YH.Event.add(e, 'bind', 'sortremove', null, function(e, ui, cmp) {
          cmp.removeItem(YH.getCmp(ui.item.attr('id')), true);
        });
        
        YH.Event.add(e, 'bind', 'sortreceive', null, function(e, ui, cmp) {
          cmp.receiveItem(YH.getCmp(ui.item.attr('id')));
        });
        
        e.el.css({
          'overflow': 'visible'
        });
      });
    },
    addRow: function(position) {
      var cls = '.' + this.selectableCls;
      var self = this;
      position = position || 'after';
      var clone;
      if (typeof position === 'string') {
        cls = cls || '.ui-selected';
        var td = $('td' + cls).first();
        if (!td[0]) {
          return;
        }
        var tr = td.parent();
        //clone = tr.clone();
        //clone.children().empty();
        
        clone = $('<tr></tr>');
        for (var i = 0; i < this.cols; i++ ) {
          var td = $('<td></td>');
          clone.append(td);
        }
        if (position == 'before') {
          tr.before(clone);
        }
        else {
          tr.after(clone);
        }
        
        this.doDesign();
      }
      clone.children().each(function(i, e) {
        self.addItem(e);
      });
      this.sortItems();
      this.refresh();
    },
    addColumn: function(pos) {
      var self = this;
      this.table.find('tr').each(function(i, e) {
        var td = $('<td valign="top"></td>');
        if (pos == 'right') {
          td.appendTo(e);
        }
        else {
          td.prependTo(e);
        }
        
        //调整列宽的tr不加item
        if (!$(this).is("." + self.designCls)) {
          self.addItem(td);
        }
      });
      
      this.cols++;
      //同步cfg和layout的列数, 方便存储用      this.owner.layoutCfg.cols = this.cols;
      this.sortItems();
      this.refresh();
    },
    doTipMenu: function() {
      if (this.tip) {
        return;
      }
      var self = this;
      var menu = new YH.Menu({
        classes: ['menu-lv3'],
        data: [{
          text: "合并单元格",
          handleClick: function(node, t) {
            self.mergeCells();
            self.tip.hide();
          }
        }/*,{
          text: "拆分单元格",
          handleClick: function(node, t) {
            self.splitCells();
            self.tip.hide();
          }
        }*/,{
          text: "之前加入行",
          handleClick: function(node, t) {
            self.addRow('before');
            self.tip.hide();
          }
        },{
          text: "之后加入行",
          handleClick: function(node, t) {
            self.addRow('after');
            self.tip.hide();
          }
        },{
          text: "左边插入列",
          handleClick: function(node, t) {
            self.addColumn();
            self.tip.hide();
          }
        },{
          text: "右边插入列",
          handleClick: function(node, t) {
            self.addColumn('right');
            self.tip.hide();
          }
        },{
          text: "取消选择",
          handleClick: function(node, t) {
            self.unselect();
            self.tip.hide();
          }
        }],
        selClass: 'menu-selected',
        renderTo: 'body'
      });

      this.tip = new YH.Tip({
        event: 'rightClick',
        delay: 2,
        style: {
          width: '190px'
        },
        items: [menu],
        listeners: {
          'show': {
            'before': function(e, t) {
              if (!self.verifyMerge()) {
                t.items[0] && t.items[0].items[0].hide();
              }
              else {
                t.items[0] && t.items[0].items[0].show();
              }
            }
          }
        },
        target: self.el
      });
    },
    refresh: function() {
      this.el.empty();
      this.initComponent();
    }
  });
  
}) (jQuery);(function($) {
  var opts = {
    layout: 'autolayout',
    //通过left/right/top/bottom定位(解决兼容性问题!),相对于offsetParent
    location: false,
    /*
    location: {
      left: 0,
      right: 0,
      top: 100,
      buttom: 100
    },
    */
    layoutCfg: {},
    items: [],
    height: '100%',
    width: '100%',
    iframeSrc: null,
    plugins: [],
    contentEl: null
  };
  
  YH.createComponent('Container', opts, YH.Component.prototype, {
    initComponent: function() {
      this.locate();
      this.el.css({
        width: this.width,
        height: this.height,
        overflow: 'hidden'
      });
      
      if (this.contentEl) {
        this.contentEl = $(this.contentEl).show();
      }
      else {
        this.contentEl = $('<div class="jq-container"></div>');
      }
      this.el.append(this.contentEl);
      if (this.html) {
        this.contentEl.html(this.html);
      }
      else if (this.url) {
        this.autoLoad();
      }
      else if (this.iframeSrc) {
        this.loadIframe();
      }
      else if (this.items) {
        this.doLayout();
      }
      this.doPlugins();
    },
    /**
     * 以AJAX的方式将目标页面载入到容器中
     */
    autoLoad: function() {
      if (this.contentEl[0]) {
        new Ajax.Updater(this.contentEl[0], this.url, {
          parameters: this.param,
          evalScripts: true
        });
      }
    },
    /**
     * 根据设置的top/bottom/left/right控制控件高宽
     */
    locate: function() {
      if (this.location && $.browser.msie && /[7,6]/.test($.browser.version)) {
        //ff中局部函数要在调用前声明,否则会出错
        function parseNumber(n) {
          n = parseInt(n);
          return isNaN(n) ? false : n;
        }
        
        this.el.css({
          'position': 'absolute'
        });
        
        //this.location忘记了this导致页面呢跳转了!!
        (typeof this.location == 'object') || (this.location = {});
        var op = this.el.offsetParent();
        this.location.top = parseNumber(this.location.top || this.el.css('top'));
        this.location.bottom = parseNumber(this.location.bottom || this.el.css('bottom'));
        this.location.left = parseNumber(this.location.left || this.el.css('left'));
        this.location.right = parseNumber(this.location.right || this.el.css('right'));
        
        if (this.location.top !== 'false' && this.location.bottom !== 'false') {
          var height = op.height() - this.location.top - this.location.bottom;
          this.height = isNaN(height) ? this.height : height;
        }
        if (this.location.left !== 'false' && this.location.right !== 'false') {
          var width = op.width() - this.location.left - this.location.right;
          this.width = isNaN(width) ? this.width : width;
        }
        this.el.css($.grep(this.location, function(k, v) {
          return v === 'false'
        }));
        var self = this;
        //当窗口resize时重新计算高宽
        function resizeEvt() {
          if (self.location.top !== 'false' && self.location.bottom !== 'false') {
            self.height = op.height() - self.location.top - self.location.bottom;
          }
          if (self.location.left !== 'false' && self.location.right !== 'false') {
            self.width = op.width() - self.location.left - self.location.right;
          }
          
          var css = {};
          isNaN(self.height) || (css.height = self.height);
          isNaN(self.width) || (css.width = self.width);
          
          self.el.css(css);
        }
        $(window).resize(resizeEvt);
      }
    },
    /**
     * 加载iframe
     */
    loadIframe: function(iframeSrc) {
      this.iframeSrc = iframeSrc || this.iframeSrc;
      var contentEl = this.contentEl;
      this.iframe = $('<iframe border="0" frameborder="0" cellspacing="0" src="about:blank" style="overflow:auto;width:100%;height:100%;border:none;background-color:transparent;" allowTransparency="true"></iframe>');
      this.iframe.attr('id', 'iframe' + this.id);
      this.iframe.attr('name', 'iframe' + this.id);
      this.contentEl.addClass('jq-container-iframe');
      this.iframeDom = this.iframe[0];
      this.iframeDom.src = this.iframeSrc;
      this.contentEl.append(this.iframe);
    },
    reloadIframe: function() {
      this.iframeDom.src = this.iframeSrc;
    },
    setSrc: function(src) {
      this.iframeSrc = src;
      this.iframeDom.src = src;
    },
    setHtml: function (html) {
      this.html = html;
      this.contentEl.html(this.html);
    },
    doLayout: function() {
      var self = this;
      if (this.layout) {
        //暂时的容错处理        if (YH.layouts.register[this.layout]) {
          if (!YH.isCmp(this.layoutMgr)) {
            this.layoutCfg.el = this.contentEl;
            this.layoutCfg.items = this.items;
            this.layoutCfg.owner = this;
            this.layoutMgr = new YH.layouts.register[this.layout](this.layoutCfg);
          }
 
          YH.compose(this, this.layoutMgr);
        }
      }
    },
    /**
     * 组件的插件(没用到)
     */
    doPlugins: function() {
      var t = this;
      $.each(this.plugins, function(i, e){
        if (YH.Plugins[e]) {
          YH.Plugins[e].apply(t, arguments);
        }
      });
    },
    compose: function() {
      var self = this;
      return {
        addItems: self.addItems,
        doSortable: self.doSortable,
        doLayout: self.doLayout,
        reloadIframe: self.reloadIframe,
        loadIframe: self.loadIframe,
        setSrc: self.setSrc,
        autoLoad: self.autoLoad,
        sortItems: self.sortItems,
        setHtml: self.setHtml
      }
    },
    destroy: function() {
      if (this.iframeDom) {
        this.iframeDom.src = 'about: blank';
      }
      YH.Component.prototype.destroy.call(this);
    }
  });
}) (jQuery);(function($) {
  var btnOpts = {
    adaptive : false,
    cls : "",
    toggle : false,
    toggleHandler : [],
    icon : "",
    title : "",
    btnText : "",
    event : "click",
    handler : $.noop,
    normalCls : "jq-button-normal",
    activeCls : "jq-button-active",
    title : ""
  };
  YH.createComponent("Button", btnOpts, YH.Component.prototype, {
    initComponent : function() {
      var self = this;
      this.btn = $('<button class="jq-button"></button>');
      this.btn.attr("title", this.title);
      this.btn.addClass(this.btnCls);
      if (this.status.isPressed) {
        this.btn.addClass(this.activeCls)
      } else {
        this.btn.addClass(this.normalCls)
      }
      this.el.append(this.btn).addClass("jq-cmp-button");
      if (this.icon) {
        var img = $('<img align="absmiddle"></img>');
        img.attr("src", this.icon);
        this.btn.append(img)
      }
      if (this.btnText) {
        var span = $("<span></span>").text(this.btnText);
        this.btn.append(span)
      }
      this.toggleHandler[0] = this.toggleHandler[0] || $.noop;
      this.toggleHandler[1] = this.toggleHandler[1] || $.noop;
      if (this.toggle) {
        this.btn.click(function(e) {
          var btn = this;
          if (!self.status.isPressed) {
            self.status.isPressed = true;
            self.btn.removeClass(self.normalCls).addClass(self.activeCls);
            self.toggleHandler[0](e, self)
          } else {
            self.status.isPressed = false;
            self.btn.addClass(self.normalCls).removeClass(self.activeCls);
            self.toggleHandler[1](e, self)
          }
        })
      } else {
        this.btn.bind(this.event, function(e) {
          self.handler(e, self)
        })
      }
    },
    disable : function() {
      this.btn[0].setAttribute("disabled", true)
    },
    enable : function() {
      this.btn[0].removeAttribute("disabled")
    },
    setStatus : function(status, disabled) {
      this.status.isPressed = !(status == "default");
      if (this.status.isPressed) {
        this.btn.removeClass(this.normalCls).addClass(this.activeCls)
      } else {
        this.btn.addClass(this.normalCls).removeClass(this.activeCls)
      }
      disabled || this.btn[0] && this.btn[0].removeAttribute("disabled")
    }
  })
})(jQuery);(function($) {
  var menuOpts = {
    data: [],
    items: [],
    el: null,
    icon: false,
    activeMenu: 0,
    openUrl: $.noop,
    expandType: 0,
    isLazyLoad: false,
    lazyLoadData: $.noop,
    animate: true,
    classes: [],
    liClass: [],
    selClass: '',
    expClass: []
  };
  
  YH.createComponent("Menu", menuOpts, YH.Component.prototype, {
    initComponent: function() {
      this.items = this.data;
      this.createUL(this.items, 0, this.el);
    },
    /**
     * 创建UL,并初始化A标签的点击事件     * @param lv            目录的等级     * @param container     目录的容器     */
    createUL: function(items, lv, container) {
      var ul = $('<ul></ul>');
      var self = this;
      container.append(ul);
      if (this.classes[lv]) {
        ul.attr('class', this.classes[lv]);
      }
      
      $.each(items, function(i, e) {
        var a = $('<a href="javascript:void(0)"></a>');
        var li = $('<li></li>').append(a);
        
        //在li中记录id信息,暂时为快捷菜单的排序服务
        li.data('id', e.id);
        e.li = li;
        ul.append(li);
        
        self.addMethod(e);
        
        if (!e.leaf) {
          li.attr('class', self.liClass[lv]);
          if (!self.isLazyLoad && e.children) {
            var temp = $('<li style="height:auto;display:none;"></li>');
            li.after(temp);
            self.createUL.call(self, e.children, lv + 1, temp);
          }
        }
        if (e.icon && self.icon) {
          var img = $('<img></img>');
          img.attr('src', e.icon);
          a.append(img);
        }
        
        var span = $('<span></span>').html(e.text);
        a.append(span);
        
        //处理默认展开菜单
        if (e.id == self.activeMenu || e.active) {
          self.menuClick(e, lv, li, false);
        }
        
        a[0].onclick = function() {
          self.menuClick(e, lv, li);
          return false;
        };
      });
    },
    /**
     * 为每个菜单项添加方法
     */
    addMethod: function(e) {
      e.show = function() {
        e.li.show();
      }
      e.hide = function() {
        e.li.hide();
      }
      e.setText = function(text) {
        e.text = text;
        a.html(text);
      }
    },
    /**
     * 菜单点击事件
     * @param menu              菜单节点
     * @param lv                菜单节点的级次     * @param li                菜单节点对应的LI标签
     * @param animate           点击菜单是否需要动画效果
     */
    menuClick: function(menu, lv, li, animate) {
      if (animate == undefined) {
        animate = this.animate;
      }
      if (menu.handleClick) {
        menu.handleClick(menu, this);
      }
      else if (!!menu.leaf) {
        this.solveLastClick(li);
        this.openUrl(menu, this);
      }
      else{
        if (this.isLazyLoad){
          if (!li.data('expend')){
            li.data('expend', true);
            if (!li.data('opened')) {
              var data = this.lazyLoadData(menu, this);
              li.after('<li style="height:auto;display:none;"></li>');
              var container = li.next();
              this.createUL(data, lv + 1, container);
              container.show(animate ? this.speed || 500 : 0);
              li.data('opened', true);
            }
            else {
              li.next().show(animate ? this.speed || 500 : 0);
            }
            this.solveLastExpand(li, lv);
          }
          else {
            li.next().hide(this.speed || 500);
            li.removeClass(this.expClass[lv]);
            li.data('expend', false);
          }
        }
        else {
          if (!li.data('expend')){
            li.data('expend', true);
            li.next().show(animate ? this.speed || 500 : 0);
            this.solveLastExpand(li, lv);
          }
          else {
            li.next().hide(this.speed || 500);
            li.removeClass(this.expClass[lv]);
            li.data('expend', false);
          }
        }
      }
    },
    solveLastClick: function (li) {
      if (this.lastClick) {
        this.lastClick.removeClass(this.selClass);
      }
      this.lastClick = li;
      li.addClass(this.selClass);
    },
    solveLastExpand: function (li, lv) {
      if (!this.lastExpand) {
        this.lastExpand = [null, null, null];
      }
      li.addClass(this.expClass[lv]);
      if (!this.expandType){
        if (this.lastExpand[lv] && this.lastExpand[lv] != li) {
          this.lastExpand[lv].removeClass(this.expClass[lv])
          this.lastExpand[lv].next().hide(this.speed || 500);
          this.lastExpand[lv].data('expend', false)
        }
        this.lastExpand[lv] = li;
      }
    },
    /**
     * 用户自定义函数,点击的事件
     * @param node             对应的目录节点
     * @param self                YH.Menu的实例引用
     */
    openUrl: $.noop
  });
})(jQuery);(function($) {
  var gridOpts = {
    'title': '',
    'data': '',
    'cls': '',
    'url': '',
    'params': {},
    'maxRecords': 5,
    lineHeight: 25,
    listeners: {
      initRows: {
        before: function(e, t) {
          if (!t.data || !t.data.length) {
            t.el.append('<div class="nodata"><span>暂无数据...</span></div>');
            var h = t.maxRecords * t.lineHeight + "px";
            t.el.css({
              height: h,
              'line-height': h,
              'text-align': 'center',
              'font-size': '18px'
            });
          }
        }
      }
    },
    'renderTo': null
  };
  YH.createComponent('Grid', gridOpts, YH.Component.prototype, {
    initComponent: function() {
      var t = this;
      this.initRows();
      this.el.css({
        height: this.maxRecords * this.lineHeight + "px"
      });
    },
    rowRender: function(i, e) {
      var content = $('<div></div>');
      $.each(e.cells || [], function(index, el) {
        var element;
        if (el.type == '' || el.type.toLowerCase() == 'text') {
          element = $('<span></span>');
          element.html(el.text);
        }
        else if (el.type.toLowerCase() == 'link' || el.type.toLowerCase() == 'img'){
          element = $('<a></a>');
          element.html(el.text);
        }
        else {
          elment = $('<span></span>');
        }
        YH.addPros(element, el);
        content.append(element);
      });
      return content;
    },
    initRows: function() {
      var ul = $('<ul></ul>');
      ul.appendTo(this.el);
      var self = this;
      $.each(this.data || [], function(i, e) {
        if (i >= self.maxRecords) {
          return;
        }
        var li = $('<li></li>');
        var liContent = self.rowRender(i, e);
        li.append(liContent);
        li.appendTo(ul);
      });
    }
  });
  
  YH.Image = function(cfg) {
    var opts = {
      'title': '',
      'path': '',
      'cls': '',
      'listeners': {
        'click': $.noop
      },
      'describe': '',
      'listeners': {}
    };
    
    $.extend(true, this, opts, cfg);
    this.initialize();
  };
  $.extend(YH.Image.prototype, new YH.Component(), {
    initialize: function() {
      this.el.css({
        'text-align': 'center'
      });
      this.el.attr('class', this.cls);
      var a = $('<a href="javascript:void(0)" style="display:block;"></a>');
      a.click(this.listeners.click);
      
      var img = $('<img></img>');
      if (this.path) {
        img.attr('src', this.path);
        a.append(img);
      }
      var describe = '';
      if (this.describe) {
        describe = this.describe;
      }
      
      this.el.append(a).append(describe);
      
      if (this.renderTo) {
        this.renderTo.append(el);
      }
    },
    render: function(el) {
      if (el) {
        this.el.appendTo(el);
      }
    }
  });
  YH.register['image'] = YH.Image;
  
  var customOpts = {
    title: '',
    url: '',
    params: {
    },
    data: null,
    renderCmp: $.noop
  };
    
  YH.createComponent('Custom', customOpts, YH.Component.prototype, {
    initComponent: function() {
    }
  });
    
  var imgBoxOpts = {
    'title': '',
    'cls': '',
    'imgSize': {
      'height': 125,
      'width': 225
    },
    'maxRecords': 5,
    'delay': 3000,
    'loader': null,
    'data': null,
    'params': {},
    'renderTo': null,
    listeners: {
      initContent: {
        before: function(e, t) {
          if (!t.data || !t.data.length) {
            t.el.append('<div class="nodata" style="line-height: 125px"><span>暂无数据...</span></div>');
            var h =  "125px";
            t.el.css({
              'height': h,
              'line-height': h,
              'text-align': 'center',
              'font-size': '18px'
            });
          }
        }
      }
    }
  };
  
  YH.createComponent('ImgBox', imgBoxOpts, YH.Component.prototype, {
    'initComponent': function() {
      this.el.addClass("yh-imgbox");
      this.container = $('<div class="yh-imgbox-container"></div>');
      
      this.imgs = $('<div class="yh-imgbox-imgs"></div>');
      var div = $('<div class="yh-imgbox-texts"></div>');
      this.texts = $("<ul></ul>")
      this.el.append(this.container.append(this.imgs).append(div.append(this.texts)));
      
      this.initContent();
      this.initScroll();
      this.initMouseEvent();
    },
    'initScroll': function() {
      this.doScroll();
      this.imgsHeight = (this.data.length > 5 ? this.maxRecords : this.data.length) * this.imgSize.height;
      this.scrollHeight = 0;
    },
    'scroll': function() {
      this.scrollHeight += this.imgSize.height;
      if (this.scrollHeight > this.imgsHeight - this.imgSize.height) {
        this.scrollHeight = 0;
      }
      
      this.imgs.stop().animate({
        top: - this.scrollHeight
      }, "slow");
      this.doScroll();
    },
    'doScroll': function() {
      var t = this;
      
      this.task = setTimeout(function() {
        t.scroll.call(t);
      }, t.delay);
    },
    'initMouseEvent': function() {
      var imgs = this.texts.children();
      var t = this;
      $.each(imgs || [], function(i, e) {
        $(e).mouseover(function() {
          clearTimeout(t.task);
          var top = i * t.imgSize.height;
          t.imgs.stop().animate({
            top: - top
          }, "normal");
        });
        $(e).mouseout(function() {
          t.doScroll();
        });
      });
    },
    'initContent': function() {
      if (this.data) {
        var t = this;
        $.each(this.data, function(i, el){
          if (i >= t.maxRecords) {
            return;
          }
          var e = el.cells[0];
          var a4Img = $('<a></a>');
          var img = $('<img></img>');
          a4Img.append(img);
          t.imgs.append(a4Img);
          
          var li = $('<li></li>');
          var a4Text = $('<a></a>');
          var span = $('<span></span>');
          span.html(e.text);
          a4Text.append(span);
          t.texts.append(li.append(a4Text));
          
          YH.addPros(img, e);
          YH.addPros(a4Text, e);
          YH.addPros(a4Img, e);
        });
      }
    }
  });
})(jQuery);(function($) {
  var panelOpts = {
    'extendContainer': true,
    'width': 300,
    'height': 300,
    'layout': 'autolayout',
    'layoutCfg': {},
    'icon': '',
    'tbar': [{id: 'close'}],
    'cls': '',
    'left': 0,
    containment: 'parent',
    'top': 0,
    autoWidth: false,
    'cmpCls': 'jq-panel',
    'renderTo': null,
    borderHeight: 50,
    'style': {},
    //float属性代表是否时浮动在别的元素只上,window/tip都是浮动的    'float': false,
    'tools': [],
    activeCls: 'window-active',
    lazyContainer: false,
    'autoHideBorders': false,
    'autoHideToolbar': false,
    'isMaximize': false,
    'listeners': {
      resize: $.noop
    }
  };
  
  /**
   * 当窗口resize时触发设置最大化组件的大小的函数
   */
  $(window).resize(function() {
    $.each(YH.components, function(k, v) {
      if (v.status && v.status.fillCtn || v.status.maximize) {
        v.maxEvt && v.maxEvt();
      }
    });
  });
  
  YH.createComponent("Panel", panelOpts, YH.Component.prototype, {
    'initComponent': function() {
      this.el.addClass('extend-container');
      
      if (typeof this.height === "string" && /^[0-9]{1,}$/.test(this.height)) {
        this.height *= 1;
      }
      
      if (typeof this.width === "string" && /^[0-9]{1,}$/.test(this.width)) {
        this.width *= 1;
      }
      
      this.doRender();
      
      //解决ie6的select框问题
      if (YH.browser.isIE6 && this["float"]) {
        this.ie6Mask = $('<iframe border="0" frameborder="0" cellspacing="10" class="ie6mask" src="about:blank"></iframe>');
        var self = this;
        self.el.append(self.ie6Mask);
        setTimeout(function() {
          self.ie6Mask.css({
            height: self.el.innerHeight() - 12,
            width: self.el.innerWidth() - 12
          });
        }, 100);
      }
    },
    "refresh": function() {
      this.status.container = false;
      YH.Component.prototype.refresh.apply(this, arguments);
    },
    'doRender': function() {
      var css = {'height': 'auto'};
      
      if (!this.autoWidth) {
        css.width = this.width;
      }
      
      this.el.css(css);
      this.createHeader();
      this.createBody();
      this.lazyContainer || this.doContainer();
      this.createFooter();
      
      if (this.draggable) {
        this.clickActive();
      }
    },
    doInitStatus: function() {
      if (this.autoHideBorders) {
        this.setAutoHideBorders();
      }
      
      if (this.autoHideTools) {
        this.setAutoHideTools();
      }
      
      if (this.isPin) {
        this.pin();
        this.tools.pin && this.tools.pin.hide();
      }
      else {
        this.tools.unpin && this.tools.unpin.hide();
      }
      
      if (this.top || this.left || this.right || this.bottom) {
        this.posCenter = false;
      }
      
      if (this.posCenter) {
        var ctn;
        if (!this.containment || this.containment == 'parent') {
          ctn = (this.parentCmp || this.parentEl) ? 'parent' : null;
        }
        else {
          ctn = $(this.containment);
        }
        this.setPosCenter(ctn);
      }
      else {
        this.el.css({
          left: this.left,
          top: this.top,
          bottom: this.bottom,
          right: this.right
        });
      }
     
      //初始化最大化最小化填充满父元素和还原图标的显示状态
      if (!this.isFillCtn && !this.isMaximize) {
        this.tools.restore && this.tools.restore.hide();
      }
      else if (this.isFillCtn) {
        this.tools.plus && this.tools.plus.hide();
        this.maximize(this.containment);
      }
      else if (this.isMaximize) {
        this.tools.maximize && this.tools.maximize.hide();
        this.maximize();
      }
      
      this.doDbclick();
    },
    /**
     * 双击窗口标题栏的最大化的处理

     */
    doDbclick: function() {
      var self = this;
      if (this.tools.plus && this.tools.restore) {
        this.headerEl.dblclick(function() {
          if (self.status.maximize) {
            return;
          }
          if (self.status.fillCtn) {
            self.restore();
            self.tools.restore && self.tools.restore.hide();
            self.tools.plus && self.tools.plus.show();
          }
          else {
            self.maximize(self.containment);
            self.tools.restore && self.tools.restore.show();
            self.tools.plus && self.tools.plus.hide();
          }
        });
      }
      else if (this.tools.maximize) {
        this.headerEl.dblclick(function() {
          if (self.status.maximize) {
            self.restore();
          }
          else {
            self.maximize();
          }
        });
      }
    },
    'doDraggable': function() {
      if (this.draggable) {
        if (typeof this.draggable == 'boolean') {
          this.draggable = {};
        }
        //this.draggable.handle = this.draggable.handle || '.' + this.cmpCls + '-header';
        if (typeof this.draggable.containment === "undefined") {
          this.draggable.containment = 'body';
        }
      }
      YH.Component.prototype.doDraggable.call(this);
    },
    'clickActive': function() {
      this.status.clickActive = true;
      //当点击的时候,窗口变为激活状态

      var self = this;
      this.el.mousedown(function() {
        self.active();
      });
    },
    'doResizable': function() {
      var self = this;
      if (this.resizable) {
        if (!this.modal) {
          var mask = $(".draggable-mask");
          if (!mask.length) {
            mask = $("<div class=\"draggable-mask\"></div>")
            $("body").append(mask);
          }
          (typeof this.resizable === "object") || (this.resizable = {});
          this.resizable.start = function(e, ui) {
            mask.css({
              "z-index": self.zIndex
            });
            mask.show();
          }
        }
        this.resizable.alsoResize = this.resizable.alsoResize || this.contentEl;
        this.resizable.minWidth = this.resizable.minWidth || this.minWidth || 200;
        this.resizable.minHeight = this.resizable.minHeight || this.minHeight || 100;
        
        YH.Component.prototype.doResizable.call(this);
        
        //当resize后设置contentEl宽度,防止最大化还原后contentEl宽度超出
        this.el.resizable('option', {stop: function(e, ui) {
          self.contentEl.css({
            width: '100%'
          });
          self.height = self.contentEl.height();
          self.width =self.el.width();
          self.el.css({height: 'auto'});
          if (!self.modal) {
            $(".draggable-mask").hide();
          }
        }});
        
        this.el.css({
          'position': 'absolute'
        });
      }
    },
    'setPosCenter': function(ctn) {
      ctn = ctn || $('body');
      if (ctn == 'parent') {
        ctn = this.getParentEl();
      }
      ctn = $(ctn);
      
      //窗口居中暂时处理办法
      if (ctn[0] && ctn[0].tagName == 'BODY') {
        ctn = $('body');
      }
      YH.Panel.lastLeft = YH.Panel.lastLeft || 0;
      YH.Panel.lastTop = YH.Panel.lastTop || 0;
      var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
      //暂时不处理窗口打开位置叠加的问题
      this.top = (ctn.height() - this.el.height() - 100) / 2 + scrollTop + (YH.Panel.lastTop += 0);
      this.left = (ctn.width() - this.el.width()) / 2 + (YH.Panel.lastLeft += 0);
      this.top = this.top < 0 ? 0 : this.top;
      var pos = ctn.position();
      this.top += pos.top;
      this.left += pos.left;
      this.el.css({
        'top': this.top,
        'left': this.left
      });
    },
    'toggle': function(speed) {
      this.el.toggleClass(this.cmpCls + '-collapsed');
      this.contentEl.toggle(speed);
    },
    'createHeader': function() {
      this.headerEl = $('<div class="' + this.cmpCls + '-header"></div>');
      //拖动的class
      this.headerEl.addClass('drag-handle');
      this.initTools();
      var title = $('<span></span>');
      if (this.icon) {
        var img = $('<img></img>');
        img.attr('src', this.icon);
        img.attr('align', 'absmiddle');
        this.headerEl.append(img);
      }
      else if (this.iconCls) {
        var div = $('<div style="float: left;"></div>');
        div.addClass(this.iconCls);
        this.headerEl.append(div);
      }
      title.attr('class', this.cmpCls + '-header-text');
      title.append(this.title);
      this.headerEl.append(title);
      var tl = $('<div class="' + this.cmpCls + '-tl"></div>');
      var tr = $('<div class="' + this.cmpCls + '-tr"></div>');
      var tc = $('<div class="' + this.cmpCls + '-tc"></div>');
      tl.append(tr);
      tr.append(tc);
      tc.append(this.headerEl);
      this.el.append(tl);
    },
    'createBody': function() {
      this.contentEl = $('<div class="' + this.cmpCls + '-content"></div>');
      this.contentEl.css({
        height: this.height
      });
      
      var mc = $('<div class="' + this.cmpCls + '-mc"></div>');
      var ml = $('<div class="' + this.cmpCls + '-ml"></div>');
      var mr = $('<div class="' + this.cmpCls + '-mr"></div>');
      
      this.el.append(ml.append(mr.append(mc)));
      mc.append(this.contentEl);
    },
    doContainer: function(reload) {
      if (!this.status.container || reload) {
        var t = this;
        this.container = new YH.Container({
          renderTo: t.contentEl,
          items: t.items,
          url: t.url,
          html: t.html,
          loadCallback: t.loadCallback,
          iframeSrc: t.iframeSrc || t.src,
          layout: t.layout,
          param: t.param,
          layout: t.layout,
          owner: t,
          layoutCfg: t.layoutCfg,
          contentEl: t.content
        });
        YH.compose(this, this.container);
        this.status.container = true;
      }
    },
    'createFooter': function() {
      var bl = $('<div class="' + this.cmpCls + '-bl"></div>');
      var br = $('<div class="' + this.cmpCls + '-br"></div>');
      var bc = $('<div class="' + this.cmpCls + '-bc"></div>');
      var bcP = $('<div class="' + this.cmpCls + '-pointer-bc"></div>');
      bl.append(br);
      br.append(bc);
      this.el.append(bl);
      this.pointerBc && this.el.append(bcP);
    },
    'maximize': function(ctn) {
      var type = !!ctn ? 'fillCtn' : 'maximize';
      ctn = ctn || $('body');
      if (ctn == 'parent') {
        ctn = this.parentEl;
      }
      ctn = $(ctn);
      //从正常窗口到最大化时,记录正常位置,绑定window的resize事件
      if (!this.status.fillCtn && !this.status.maximize) {
        //在初始化完毕的状态下才记录模块原来的位置
        if (this.status.initialized) {
          this.left = this.el.position().left;
          this.top = this.el.position().top;
        }
        this.replacer = {};
        this.replacer.offset = this.el.offset();
        this.replacer.css = {
          height: this.contentEl.css('height'),
          width: this.el.css('width'),
          position: this.el.css('position')
        };
      }
      
      this.drag && this.drag.disable();
      this.resize && this.resize.disable();
      
      if (type == 'maximize') {
        this.layer = 'middle';
      }
      else {
        this.layer = 'lower';
      }
      //因为显示和隐藏需要修改status,不能直接改变show函数,需要专门增加效果函数
      !this.status.hidden && this.active();
      
      //当ctn是父元素的时候不用append
      //(this.el.parent()[0] != ctn[0]) && this.el.appendTo(ctn);
      this.maximize.height = ctn.height() - this.borderHeight || 50
      var css = ctn.position();
      
      var h;
      if (this.layout == "cardlayout" && this.layoutCfg.tabs) {
        //todo-处理tabpanel在fillCtn时的高度问题
        h = this.maximize.height;
      }
      else {
        h = this.maximize.height;
      }
      if (this.animate && !this.status.hidden) {
        var self = this;
        this.contentEl.find('.jq-container-iframe').hide();
        this.el.animate({
          width: ctn.width(),
          left: css.left
        }, 'fast', function() {
          self.contentEl.animate({
            height: h
          }, 'fast');
          self.el.animate({
            top: css.top
          }, 'fast', function() {
            self.contentEl.find('.jq-container-iframe').show();
          });
        });
      }
      else {
        this.contentEl.height(h);
        css.width = '100%';
        this.el.css(css);
      }
      this.status.fillCtn = this.status.maximize = false;
      this.status[type] = true;
    },
    getCrtHeight: function() {
      if (this.status.fillCtn || this.status.maximize) {
        return this.maximize.height;
      }
      return this.height;
    },
    'restore': function() {
      this.status.fillCtn = this.status.maximize = false;
      if (this.replacer) {
        this.el.css({
          height: 'auto',
          position: this.replacer.css.position
        });
        if (this.animate && !this.status.hidden) {
          var self = this;
          this.contentEl.find('.jq-container-iframe').hide();
          this.el.animate({
            top: this.top
          }, 'fast');
          this.contentEl.animate({
            height: this.replacer.css.height
          }, 'fast', function() {
            self.el.animate({
              left: self.left,
              width: self.replacer.css.width
            }, 'fast', function() {
              self.contentEl.find('.jq-container-iframe').show();
            });
          });
        }
        else {
          this.contentEl.css({
            height: this.replacer.css.height
          });
          this.el.css({left: this.left, top: this.top, width: this.replacer.css.width});
        }
        //当ctn是父元素的时候不用append
        (this.el.parent()[0] != $(this.parentEl)[0]) && this.el.appendTo(this.parentEl);
      }
      this.drag && this.drag.enable();
      this.drag && this.resize.enable();
      this.active('lower');
    },
    /**
     * 模块最大化后
     */
    maxEvt: function() {
      var ctn;
      if (this.status.fillCtn) {
        var ctn;
        if (!this.containment || this.containment == 'parent') {
          ctn = this.parentEl;
        }
        else {
          ctn = $(this.containment);
        }
      }
      else if (this.status.maximize) {
        ctn = $('body')
      }
      else {
        return;
      }
      this.el.width($(ctn).width());
      this.maximize.height = $(ctn).height() - this.borderHeight || 50;
      this.contentEl.height(this.maximize.height);
    },
    'minimize': function() {
      this.hide();
    },
    'pin': function() {
      this.active('upper');
    },
    'unpin': function() {
      this.active('lower');
    },
    /**
     * 为了兼容目前桌面模块的实现方法,以后再用更好的处理
     */
    'autoLoad': function() {
    },
    'getAbsContentHeight': function() {
      return this.contentEl.height() + 'px';
    },
    'getTitle': function() {
      return this.title || '';
    },
    'setTitle': function(title) {
      this.title = title || '';
      this.headerEl.find('span').html(this.title);
    },
    'setWidth': function(width) {
      this.width = width;
      this.el.css({
        'width': this.width
      });
    },
    'setHeight': function(height) {
      this.height = height;
      this.contentEl.css({
        'height': this.height
      });
    },
    'scrollIn': function(speed, callback) {
      var top = (this.status.fillCtn || this.status.maximize) ? 0 : this.top;
      this.el.css({
        top: (document.documentElement || document.body).clientHeight + 'px'
      });
      this.el.show();
      this.el.animate({
        top: top
      }, speed || 'slow', function() {
        callback && callback();
      });
    },
    'scrollOut': function(speed, callback) {
      var self = this;
      var top = (this.status.fillCtn || this.status.maximize) ? 0 : this.top;
      this.el.animate({
        top: (document.documentElement || document.body).clientHeight + 'px'
      }, speed || 'slow', function() {
        self.el.hide();
        self.el.css({
          top: top
        });
        callback && callback();
      });
    },
    collapse: function(speed, callback) {
      var self = this;
      var top = this.getPosY();
      var height = this.getCrtHeight();
      //这里的stop很重要,避免了collapse和enpand同时运行产生显示不正常的问题
      this.el.stop().animate({
        top: parseInt(top + this.getCrtHeight()) / 2
      }, speed);
      this.contentEl.stop().animate({
        height: 0
      }, speed, function() {
        self.el.css({
          display: 'none'
        });
        self.contentEl.css({
          height: height
        });
        self.el.css({top: top});
        callback && callback();
      });
      this.contentEl.find('.jq-container-iframe').hide();
    },
    expand: function(speed, callback) {
      var self = this;
      var height = this.getCrtHeight();
      this.contentEl.css({
        height: 0
      });
      this.el.show();
      var top = this.getPosY();
      this.el.css({top: top + parseInt(this.getCrtHeight()) / 2});
      this.el.stop().animate({
        top: top
      }, speed, function() {
      });
      this.contentEl.stop().animate({
        height: height
      }, speed, function() {
        self.doContainer();
        self.contentEl.find('.jq-container-iframe').show();
        callback && callback()
      });
    },
    /**
     * 关闭函数先hide再destroy
     */
    'close': function(speed) {
      var self = this;
      
      //为了处理iframe中的页面存在询问是否确定离开的情况      if (this.container && this.container.iframeDom) {
        var button = document.getElementById("buttonId");
        function unload() {
          //避免ie9下出现问题          setTimeout(function() {
            self.hide(speed, function() {
              self.destroy();
            });
          }, 0);
        }
        //使用try,解决跨域的问题
        try {
          var conWin = this.container.iframeDom.contentWindow;
          if (conWin && this.iframeUnload) {
            //再一次点关闭的时候不重复注册事件
          }
          else if (conWin.addEventListener) {
            conWin.addEventListener("unload", unload, false);
          }
          else if (conWin.attachEvent) {
            conWin.attachEvent("onunload", unload);
          }
          else {
            //当页面不能注册unload事件时,直接关闭页面
            this.hide(speed, function() {
              self.destroy();
            });
          }
        } catch (e) {
          //跨域的情况
          unload();
        } finally {
          
        }
        this.container.iframeDom.src = 'about:blank';
        this.iframeUnload = true;
      }
      else {
        this.hide(speed, function() {
          self.destroy();
        });
      }
    },
    'initTools': function() {
      var tools = this.tbar || [];
      var t = this;
      t.tools = {};
      $.each(tools, function(i, e){
        if (YH.Tools[e.id]) {
          t.tools[e.id] = {
            el: $('<a href="javascript:void(0)">&nbsp;</a>'),
            show: function() {
              this.el.show();
            },
            hide: function() {
              this.el.hide();
            },
            initTool: function() {
              t.headerEl.append(this.el);
              this.el.attr('class', 'jq-tool jq-tool-' + e.id);
              this.el.attr('title', YH.Tools[e.id].title);
              if (e.hidden) {
                this.hide();
              }
              this.el.bind('click', function(){
                if (!e.preventDefault) {
                  YH.Tools[e.id].defaultHandler(e, t.tools[e.id].el, t, e.params);
                }
                if (e.handler){
                  e.handler(e, t.tools[e.id].el, t, e.params);
                }
                //阻止默认的动作                return false;
              });
            }
          };
          t.tools[e.id].initTool();
        }
      });
    },
    'active': function(layer) {
      this.layer = layer || this.layer;
      this.zIndex = YH.topZIndex(this.layer);
      this.el.css({
        'z-index': this.zIndex
      });
      this.addClass(this.activeCls);
      this.status.hidden && this.show();
    },
    deactive: function() {
      this.removeClass(this.activeCls);
    },
    'hide': function(speed, callback) {
      if (this.status.hidden) {
        callback && callback();
        return;
      }
      var self = this;
      this.trayBtn && this.trayBtn.disable();
      this.hideEffect(speed, function() {
        self.trayBtn && self.trayBtn.enable();
        callback && callback();
      });
      this.status.hidden = true;
    },
    'show': function(speed, callback) {
      if (this.status.hidden) {
        this.status.hidden = false;
        var self = this;
        this.trayBtn && this.trayBtn.disable();
        this.showEffect(speed, function() {
          self.trayBtn && self.trayBtn.enable();
          callback && callback()
        });
      }
    },
    /**
     * 设置自动隐藏工具栏
     */
    'setAutoHideTools': function() {
      var self = this;
      this.headerEl.hide();
      this.el.hover(function() {
        self.headerEl.show();
      }, function() {
        self.headerEl.hide();
      });
    },
    /**
     * 设置自动隐藏边框
     */
    'setAutoHideBorders': function() {
      this.el.addClass('window-tsp');
      this.el.hover(function() {
        $(this).removeClass('window-tsp');
      }, function() {
        $(this).addClass('window-tsp');
      });
    },
    /**
     * 重写容器的销毁函数
     */
    destroy: function() {
      //如果存在repalcer就先销毁replacer
      //(this.status.fillCtn || this.status.maximize) && $(window).unbind('resize', this.maxEvt);
      //调用通用的销毁函数      if (this.container && this.container.iframeDom) {
        this.container.iframeDom.src = 'about:blank';
      }
      YH.Component.prototype.destroy.call(this);
    }
  });
})(jQuery);
(function ($) {
  var tipOpts = {
    'event': 'mouseOver',
    'html': '',
    'content': null,
    'delay': 5,
    relative: {
      x: 10,
      y: 10
    },
    'float': true,
    'items': null,
    'cmpCls': 'jq-tip',
    'extendContainer': true,
    'layout': 'autolayout',
    'layoutCfg': {},
    'icon': '',
    'cls': '',
    'renderTo': 'body',
    'style': {},
    'tools': []
  };
  
  YH.createComponent('Tip', tipOpts, YH.Panel.prototype, {
    'initComponent': function() {
      if (!this.target) {
        this.destroy();
        return;
      }
      this.target = $(this.target);

      YH.Panel.prototype.initComponent.apply(this, arguments);
      
      this.events[this.event].call(this);
    },
    doInitStatus: function() {
      this.el.css({
        position: 'absolute',
        'z-index': YH.topZIndex('upper'),
        display: 'none'
      });
      this.status.hidden = true;
    },
    /**
     * TIP触发显示的事件

     */
    'events': {
      /**
       * 鼠标移过时显示

       */
      'mouseOver': function() {
        var self = this;
        this.target.bind('mousemove', function(evt) {
          self.setPosition({
            top: evt.clientY + self.relative.y,
            left: evt.clientX + self.relative.x
          });
          self.el.css({
            display: 'block'
          });
        });
        
        this.target.bind('mouseout', function() {
          self.el.css({
            'display': 'none'
          });
        });
      },
      /**
       * 鼠标单击显示
       */
      'leftClick': function() {
        var self = this;
        this.target.bind('click', function(evt) {
          self.setPosition({
            'top': self.target.offset().top  + self.relative.y,
            'left': self.target.offset().left + self.target.width() + self.relative.x
          });
          self.show();
          clearTimeout(self.timer);
          self.timer = setTimeout(function() {
            self.hide();
          }, self.delay * 1000);
        });
        
        self.el.bind('mouseover', function() {
          clearTimeout(self.timer);
          return false;
        }).bind('mouseout', function() {
          clearTimeout(self.timer);
          self.timer = setTimeout(function() {
            self.hide();
          }, self.delay * 1000);
        });
      },
      /**
       * 鼠标右键单击显示
       */
      'rightClick': function() {
        var self = this;
        this.target.bind('contextmenu', function(e) {
          self.show();
          self.setPosition({
            left: e.clientX,
            top: e.clientY
          });
          clearTimeout(self.timer);
          self.timer = setTimeout(function() {
            self.hide();
          }, self.delay * 1000);
          return false;
        });

        self.el.bind('mouseover', function() {
          clearTimeout(self.timer);
        }).bind('mouseout', function() {
          clearTimeout(self.timer);
          self.timer = setTimeout(function() {
            self.hide();
          }, self.delay * 1000);
        });
      }
    },
    /**
     * 重写父类的设置位置方法, 解决了组件被部分遮挡的问题

     * @param pos         位置信息
     */
    setPosition: function(pos) {
      var h = parseInt(this.getAbsHeight());
      var w = parseInt(this.getAbsWidth());
      var cw = parseInt((document.documentElement || document.body).clientWidth);
      var ch = parseInt((document.documentElement || document.body).clientHeight);
      var l = parseInt(pos.left);
      var t = parseInt(pos.top);
      if (l + w > cw - 5) {
        pos.left = l + (cw - 5 - l - w);
      }
      if (t + h > ch - 5) {
        pos.top = t - h;
      }
      this.el.css(pos);
    }
  });
})(jQuery);(function($){
  var opts = {
    'modal': false,
    'draggable': {},
    'width': 300,
    'id': '',
    'tbar': [{
      id: 'close'
    }],
    'float': true,
    borderHeight: 40,
    modalCls: '',
    containment: 'parent',
    activeCls: 'window-active',
    'renderTo': 'body',
    'cmpCls': 'jq-window',
    'height': 300,
    'maskOpacity': '0.5',
    'closeAction': 'close',
    'src': '',
    'posCenter': true,
    'iframeSrc': '',
    'icon': '',
    layer: 'lower',
    'layout': 'autolayout',
    'layoutCfg': {},
    'plugins': [],
    'cls': '',
    'style': {},
    'listeners': {
      resize: $.noop
    }
  };
  
  YH.createComponent('Window', opts, YH.Panel.prototype, {
    initComponent: function() {
      if (this.modal) {
        this.layer = 'upper';
        this.createModalBg();
      }
      this.initContainer();
      //调用panel的initComponent函数
      YH.Panel.prototype.initComponent.call(this);
    },
    show: function() {
      YH.Panel.prototype.show.apply(this, arguments);
      this.modalBg && this.modalBg.show();
    },
    hide: function() {
      YH.Panel.prototype.hide.apply(this, arguments);
      this.modalBg && this.modalBg.hide();
    },
    close: function() {
      YH.Panel.prototype.close.apply(this, arguments);
      //close调用的是hide和destroy 不需要hidemask
      //this.modalBg && this.modalBg.hide();
    },
    initContainer: function() {
      this.el.addClass(this.cmpCls + '-container');
      this.el.css({
        'width': this.width,
        'z-index': YH.topZIndex(this.layer)
      });
      
      var t = this;

      this.el.hide();
      this.status.hidden = true;
      
      //添加控件显示隐藏销毁时对应的遮罩层处理的事件
      if (t.modalBg) {
        YH.Event.add(this, 'bind', 'destroy', 'before', function(e, t) {
          t.modalBg.remove();
        });
        
        $(window).resize(function() {
          t.modalBg.css({
            'width': (document.documentElement || document.body).scrollWidth,
            'height': (document.documentElement || document.body).scrollHeight
          });
        });
      }
    },
    createModalBg: function() {
      this.modalBg = $('<div class="' + this.cmpCls + '-mask"></div>');
      $('body').append(this.modalBg);
      var CSS = {
        'width': (document.documentElement || document.body).scrollWidth,
        'height': (document.documentElement || document.body).scrollHeight,
        'top': '0px',
        'z-index': YH.topZIndex(this.layer),
        'left': '0px',
        'display': 'none',
        'opacity': this.maskOpacity,
        'filter': 'alpha(opacity=' + this.maskOpacity * 100 + ');WIDTH: 100%'
      };
      this.modalBg.css(CSS);
      this.modalBg.addClass(this.modalCls);
      if (this.modalStyle) {
        this.modalBg.css(this.modalStyle);
      }
    }
  });
  
  var windowMrgOpts = {
    tray: null,
    container: null,
    items: [],
    activeWindow: null,
    activeBtn: null
  };
  
  /**
   * 窗口管理器
   */
  YH.createComponent('WindowMgr', windowMrgOpts, {
    initialize: function() {
      var self = this;
      
      $.each(this.items, function(i, e) {
        self.addTrayBtn(e);
      });
    },
    addTrayBtn: function(win) {
      var self = this;
      if (this.tray && !win.trayBtn) {
        var btn = new YH.Button({
          status: {
            isPressed: !win.status.hidden
          },
          normalCls:'btn-icon',
          activeCls: 'btn-icon-active',
          icon: win.icon,
          text: win.title,
          toggle: true,
          title: win.title,
          toggleHandler: [function(e, t, callback) {
            win.show('normal', callback)
            win.active();
          },function(e, t, callback){
            win.hide('normal', callback);
          }]
        });
        
        //删除Tray中的btn引用
        YH.Event.add(btn, 'bind', 'destroy', 'before', function(e, t) {
          self.tray.items = $.grep(self.items, function(n, i){
            return n.id != t.id;
          });
        });
        
        win.trayBtn = btn;
        this.tray.addItems(btn);
        
      }
      
      function doActive(e, t) {
        //第一次创建窗口时,设置这个窗口的托盘图标为激活状态

        if (!self.activeWindow) {
          self.activeWindow = t;
          t.trayBtn.setStatus('active', true);
        }
        else if (self.activeWindow.id != t.id) {
          self.activeWindow.trayBtn && self.activeWindow.trayBtn.setStatus('default', true);
          self.activeWindow.deactive();
          self.activeWindow = t;
          t.trayBtn.setStatus('active', true);
        }
      }
      
      function doHide(e, t) {
        var id = '', max = 0;
        $.each(self.items, function(i, e){
          if(!e.status.hidden && e.id != t.id) {
            if ((e.zIndex || 0) > max) {
              max = e.zIndex || 0;
              id = e.id;
            }
          }
        });
        if (id) {
          YH.getCmp(id).active();
        }
        else {
          t.trayBtn && t.trayBtn.setStatus('default', true);
        }
      }
      
      YH.Event.add(win, 'bind', 'active', 'after', doActive);
      YH.Event.add(win, 'bind', 'hide', 'after', doHide);
      //删除WindowMgr中的window引用
      YH.Event.add(win, 'bind', 'destroy', 'before', function(e, t) {
        self.items = $.grep(self.items, function(n, i){
          return n.id != t.id;
        });
        t.trayBtn && t.trayBtn.destroy();
      });
    },
    addItems: function(windows) {
      var self = this;
      if (windows instanceof Array) {
        $.each(windows, function(i, e) {
          self.items.push(e);
          self.addTrayBtn(e);
        });
      }
      else {
        this.items.push(windows);
        this.addTrayBtn(windows);
      }
    },
    createWindow: function(cfg) {
      var win = YH.getCmp(cfg && cfg.id);
      if (!win) {
        win = new YH.Window(cfg);
        win.el.addClass(this.cls);
        this.addItems(win);
      }
      win.active();
    },
    createPanel: function(cfg) {
      if (!YH.isCmp(cfg.parentCmp)) {
        return;
      }
      var panel = new YH.Panel(cfg);
      this.addItems(panel);
      panel.active();
    },
    hideAll: function() {
      $.each(this.items, function(i, e) {
        e.hide();
      });
    },
    removeAll: function() {
      $.each(this.items, function(i, e) {
        YH.isCmp(e) && e.destroy();
      });
    },
    showAll: function() {
      $.each(this.items, function(i, e) {
        e.show();
      });
    },
    getActive: function() {
      return this.activeWindow;
    }
  });
})(jQuery);
